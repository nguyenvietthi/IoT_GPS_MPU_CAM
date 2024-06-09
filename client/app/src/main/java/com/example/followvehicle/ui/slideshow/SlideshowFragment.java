package com.example.followvehicle.ui.slideshow;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.followvehicle.MainActivity;
import com.example.followvehicle.R;
import com.example.followvehicle.api.StoreUserData;
import com.example.followvehicle.api.api;
import com.example.followvehicle.databinding.FragmentSlideshowBinding;
//import com.mapbox.maps.plugin.annotation.generated.Cir;

import com.example.followvehicle.model.Location;
import com.example.followvehicle.ui.login.LoginActivity;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraBoundsOptions;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotation;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SlideshowFragment extends Fragment{

    private FragmentSlideshowBinding binding;
    private MapView mapView;
    private SeekBar seekBarTime;
    private int process_value;
    private Handler handler;
    private CircleAnnotationManager circleAnnotationManager;
    private CircleAnnotation circleAnnotation;
    PointAnnotationManager pointAnnotationManager;
    PointAnnotationOptions pointAnnotationOptions;
    Bitmap startFinishBitmap;
    Bitmap normalBitmap;

    List<Location> locationList = new ArrayList<>();

    private SharedViewModel sharedViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SelectDateTime bottomSheetFragment = new SelectDateTime();
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());

        mapView = binding.mapView;
        pointAnnotationOptions = new PointAnnotationOptions();
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
                pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);
                GesturesUtils.addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener(){
                    @Override
                    public boolean onMapClick(@NonNull Point point){
                        SelectDateTime bottomSheetFragment = new SelectDateTime();
                        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
                        return false;
                    }
                });
            }

        });

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getSelectedItem().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> s) {
                if(!s.get(0).equals("") && !s.get(0).equals("")){
                    seekBarTime.setVisibility(View.VISIBLE);
                    String cookie = StoreUserData.getInstance(requireContext()).getCookie();
                    String email = StoreUserData.getInstance(requireContext()).getEmail();
                    String deviceID = StoreUserData.getInstance(requireContext()).getKeyDeviceCode();

                    api.getLocationHistoryList(email, cookie, s.get(0), s.get(1), deviceID, new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (!response.isSuccessful()) {
                            } else {
                                String responseData = response.body().string();
                                String status = "";
                                JSONArray jasonArray;
                                List list = new ArrayList();
                                try {
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    status = jsonObject.getString("message");
                                    jasonArray = jsonObject.getJSONArray("location");
                                    int size = jasonArray.length();
                                    int i = 0;
                                    while (i < size) {
                                        locationList.add(new Location(jasonArray.get(i).toString()));
                                        i++;
                                    }
                                    System.out.println(locationList);
                                    showLocationPoint(locationList);




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (status.equals("OK")) {

                                } else {

                                }
                            }
                        }
                    });
                }

            }
        });

        seekBarTime = binding.seekBarTime;
        seekBarTime.setVisibility(View.GONE);
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                process_value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                List<Location> subLocationList = splitListByPercentage(locationList, process_value);

                showLocationPoint(subLocationList);


            }
        });

        return root;
    }

    private void showLocationPoint(List<Location> locationListIn){

        List<Point> points =new ArrayList<>();

        for (Location location: locationListIn){
            points.add(Point.fromLngLat(location.getLng(), location.getLat()));
        }

//        if (pointAnnotationManager != null) {
            pointAnnotationManager.deleteAll();
//        }

        EdgeInsets edgeInsets = new EdgeInsets(100.0, 100.0, 100.0, 100.0);

        CameraOptions cameraOptions = mapView.getMapboxMap().cameraForCoordinates(points, edgeInsets, null, null);

        mapView.getMapboxMap().setCamera(cameraOptions);

        normalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lc25);
        PointAnnotationOptions normalPointAnnotationOptions = new PointAnnotationOptions();

        for(int i = 1 ; i < locationListIn.size() - 1; i++){
            normalPointAnnotationOptions.withTextAnchor(TextAnchor.CENTER)
                    .withPoint(Point.fromLngLat(locationListIn.get(i).getLng(),locationListIn.get(i).getLat()))
                    .withTextSize(12.0)
                    .withIconImage(normalBitmap)
                    .withTextColor("#000000")
//                    .withTextField(locationList.get(i).getDatetime() + "\n\n\n\n")
            ;

            pointAnnotationManager.create(normalPointAnnotationOptions);
        }

        startFinishBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.specialdot);
        PointAnnotationOptions speacialPointAnnotationOptions = new PointAnnotationOptions();
        speacialPointAnnotationOptions.withTextAnchor(TextAnchor.CENTER)
                .withPoint(Point.fromLngLat(locationListIn.get(locationListIn.size() - 1).getLng(),locationListIn.get(locationListIn.size() - 1).getLat()))
                .withTextSize(14.0)
                .withIconImage(startFinishBitmap)
                .withTextColor("#8B4513")
                .withTextHaloBlur(50.00)
                .withTextField(locationListIn.get(locationListIn.size() - 1).getDatetime().replace("\"", "") + "\n\n\n\n")
        ;

        pointAnnotationManager.create(speacialPointAnnotationOptions);

        speacialPointAnnotationOptions.withTextAnchor(TextAnchor.CENTER)
                .withPoint(Point.fromLngLat(locationListIn.get(0).getLng(),locationListIn.get(0).getLat()))
                .withTextSize(14.0)
                .withIconImage(startFinishBitmap)
                .withTextColor("#8B4513")
                .withTextField(locationListIn.get(0).getDatetime().replace("\"", "") + "\n\n\n\n")
        ;

        pointAnnotationManager.create(speacialPointAnnotationOptions);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
//        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        handler.removeCallbacksAndMessages(null);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static <T> List<T> splitListByPercentage(List<T> list, int percentage) {
        int size = list.size();
        int subListSize = size * percentage / 100;
        return list.subList(0, Math.min(size, subListSize));
    }


}