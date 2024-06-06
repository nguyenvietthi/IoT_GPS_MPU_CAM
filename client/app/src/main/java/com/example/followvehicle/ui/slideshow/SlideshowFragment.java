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
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
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
    private Handler handler;
    private CircleAnnotationManager circleAnnotationManager;
    private CircleAnnotation circleAnnotation;
    PointAnnotationManager pointAnnotationManager;
    PointAnnotationOptions pointAnnotationOptions;
    Bitmap bitmap;

    List<Location> locationList = new ArrayList<>();

    private SharedViewModel sharedViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mapView = binding.mapView;

        pointAnnotationOptions = new PointAnnotationOptions();


        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dot2);
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
//                updateLocation(true);

//                handler = new Handler();
//                startLocationUpdates();
            }

        });

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getSelectedItem().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> s) {
                if(!s.get(0).equals("") && !s.get(0).equals("")){
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

                                    CameraOptions cameraOptions = new CameraOptions.Builder()
                                            .center(Point.fromLngLat(locationList.get(0).getLng(), locationList.get(0).getLat()))
                                            .zoom(15.5)
                                            .build();
                                    mapView.getMapboxMap().setCamera(cameraOptions);

                                    for(Location location:locationList){
                                        pointAnnotationOptions.withTextAnchor(TextAnchor.CENTER).withPoint(Point.fromLngLat(location.getLng() , location.getLat())).withTextSize(12.0).withIconImage(bitmap).withTextField(location.getDatetime() + "\n\n\n\n");

                                        pointAnnotationManager.create(pointAnnotationOptions);
                                    }


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

        return root;
    }

    private void startLocationUpdates() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLocation(false);
                handler.postDelayed(this, 10000); // Cập nhật sau mỗi 0.5 giây
            }
        }, 10000);
    }

private void updateLocation(boolean focusCamera){
    api.get_current_location("a", "a", "a", new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if (!response.isSuccessful()) {
                Log.e("MainActivity", "Unexpected code " + response);
            } else {
                String responseData = response.body().string();
                double lat;
                double lng;
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    lat = Double.parseDouble(jsonObject.getString("lat"));
                    lng = Double.parseDouble(jsonObject.getString("lng"));

                    if(focusCamera) {
                        CameraOptions cameraOptions = new CameraOptions.Builder()
                                .center(Point.fromLngLat(lng, lat))
                                .zoom(15.5)
                                .build();
                        mapView.getMapboxMap().setCamera(cameraOptions);
                    }

                    pointAnnotationManager.deleteAll();
                    pointAnnotationOptions.withTextAnchor(TextAnchor.CENTER).withPoint(Point.fromLngLat(lng , lat)).withTextField("2024-06-01\n 16:04:00").withTextSize(12.0).withIconImage(bitmap);

                    pointAnnotationManager.create(pointAnnotationOptions);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    });
}

//    private void drawLine(@NonNull Style style) {
//        // Tạo danh sách các tọa độ
//        List<Point> points = new ArrayList<>();
//        points.add(Point.fromLngLat(-77.0369, 38.9072)); // Washington, D.C.
//        points.add(Point.fromLngLat(-122.4194, 37.7749)); // San Francisco
//        points.add(Point.fromLngLat(-74.0060, 40.7128)); // New York
//
//        // Tạo LineString từ danh sách các điểm
//        LineString lineString = LineString.fromLngLats(points);
//
//        // Thiết lập AnnotationManager
//        AnnotationPluginImpl annotationPlugin = (AnnotationPluginImpl) mapView.getPlugin(AnnotationPluginImpl.class);
//        AnnotationManager annotationManager = annotationPlugin.createAnnotationManager(mapView, new AnnotationConfig());
//
//        // Tạo LineAnnotationManager từ AnnotationManager
//        LineAnno lineAnnotationManager = (LineAnnotationManager) annotationManager.getLineAnnotationManager(mapView);
//
//        // Tạo LineAnnotationOptions từ LineString
//        LineAnnotationOptions lineAnnotationOptions = new LineAnnotationOptions()
//                .withLineColor(Color.RED) // Màu của đường
//                .withLineWidth(5.0) // Độ rộng của đường
//                .withGeometry(lineString);
//
//        // Thêm LineAnnotation vào LineAnnotationManager
//        LineAnnotation lineAnnotation = lineAnnotationManager.create(lineAnnotationOptions);
//    }

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


}