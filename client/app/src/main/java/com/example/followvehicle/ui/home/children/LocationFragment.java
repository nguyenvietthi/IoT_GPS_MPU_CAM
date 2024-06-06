package com.example.followvehicle.ui.home.children;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.followvehicle.R;
import com.example.followvehicle.api.api;
import com.example.followvehicle.databinding.FragmentSlideshowBinding;


import com.example.followvehicle.ui.slideshow.SlideshowViewModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LocationFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private MapView mapView;
    private Handler handler;
    private CircleAnnotationManager circleAnnotationManager;
    private CircleAnnotation circleAnnotation;
    PointAnnotationManager pointAnnotationManager;
    PointAnnotationOptions pointAnnotationOptions;
    Bitmap bitmap;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);;

        mapView = view.findViewById(R.id.CurrentLocationMap);

        pointAnnotationOptions = new PointAnnotationOptions();

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {


                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loacation3);
                AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
                pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

                updateLocation(true);

                handler = new Handler();
                startLocationUpdates();
            }
        });
        return view;
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
                        // Cập nhật vị trí của hình tròn
                        if(focusCamera) {
                            CameraOptions cameraOptions = new CameraOptions.Builder()
                                    .center(Point.fromLngLat(lng, lat))
                                    .zoom(15.5)
                                    .build();
                            mapView.getMapboxMap().setCamera(cameraOptions);
                        }

                        pointAnnotationManager.deleteAll();
                        pointAnnotationOptions.withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap).withPoint(Point.fromLngLat(lng , lat));

                        pointAnnotationManager.create(pointAnnotationOptions);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }

    public void onStart() {
        super.onStart();
        mapView.onStart();
    }


    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
//
//import android.graphics.Color;
//import android.location.Location;
//import android.os.Bundle;
//
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import com.example.followvehicle.R;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class LocationFragment extends Fragment implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private Circle currentCircle;
//    private Handler handler;
//    private int index = 0;
//    private List<LatLng> locations = new ArrayList<>();
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_location, container, false);
//
//        // Thêm tọa độ vào danh sách
//        locations.add(new LatLng(21.0075904, 105.8133696)); // Vị trí 1
//        locations.add(new LatLng(10.762700, 106.660300)); // Vị trí 2
//        locations.add(new LatLng(10.762800, 106.660400)); // Vị trí 3
//        // Thêm các tọa độ khác vào đây...
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment == null) {
//            mapFragment = SupportMapFragment.newInstance();
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            transaction.replace(R.id.map, mapFragment).commit();
//        }
//        mapFragment.getMapAsync(this);
//
//        return view;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//
//        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                float currentZoom = mMap.getCameraPosition().zoom;
//                float newRadius = calculateRadius(currentZoom);
//                float strokeWidth = calculateStrokeWidth(currentZoom);
//                currentCircle.setRadius(newRadius);
//                currentCircle.setStrokeWidth(strokeWidth);
//            }
//        });
//
//        LatLng initialLatLng = locations.get(0);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLatLng, 16));
//
//        currentCircle = mMap.addCircle(new CircleOptions()
//                .center(initialLatLng)
//                .strokeColor(Color.WHITE) // Màu viền là màu trắng
//                .fillColor(Color.BLUE)); // Đặt màu nền trong suốt)); // Màu của hình tròn
//
//        handler = new Handler();
//        startLocationUpdates();
//    }
//
//    private void startLocationUpdates() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Cập nhật vị trí của hình tròn
//                if (index < locations.size()) {
//                    LatLng nextLatLng = locations.get(index);
//                    if (currentCircle != null) {
//                        currentCircle.setCenter(nextLatLng);
//                    } else {
//                        currentCircle = mMap.addCircle(new CircleOptions()
//                                .center(nextLatLng)
//                                .strokeColor(Color.WHITE) // Màu viền là màu trắng
//                                .fillColor(Color.BLUE)); // Đặt màu nền trong suốt));
//                    }
////                    mMap.moveCamera(CameraUpdateFactory.newLatLng(nextLatLng));
//                    index++;
//                } else {
//                    index = 0; // Reset index nếu muốn lặp lại từ đầu
//                }
//                handler.postDelayed(this, 500); // Cập nhật sau mỗi 0.5 giây
//            }
//        }, 500);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        handler.removeCallbacksAndMessages(null); // Ngừng cập nhật khi fragment bị pause
//    }
//    private float calculateRadius(float zoom) {
//        // Tính toán độ lớn mới của hình tròn dựa trên cấp độ zoom
//        // Bạn có thể tùy chỉnh logic tính toán dựa trên yêu cầu của bạn
//        // Ví dụ: độ lớn của hình tròn có thể tăng khi zoom in và giảm khi zoom out
//        // Ở đây, chúng ta đang giữ độ lớn cố định
//        return 30; // Độ lớn mặc định
//    }
//    private float calculateStrokeWidth(float zoom) {
//        // Tính toán độ lớn mới của hình tròn dựa trên cấp độ zoom
//        // Bạn có thể tùy chỉnh logic tính toán dựa trên yêu cầu của bạn
//        // Ví dụ: độ lớn của hình tròn có thể tăng khi zoom in và giảm khi zoom out
//        // Ở đây, chúng ta đang giữ độ lớn cố định
//        return 15; // Độ lớn mặc định
//    }
//
//}