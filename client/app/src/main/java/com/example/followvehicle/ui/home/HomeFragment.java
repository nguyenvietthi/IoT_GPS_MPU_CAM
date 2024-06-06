package com.example.followvehicle.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.followvehicle.R;
import com.example.followvehicle.databinding.FragmentHomeBinding;
import com.example.followvehicle.databinding.FragmentSlideshowBinding;
import com.example.followvehicle.ui.home.children.CameraFragment;
import com.example.followvehicle.ui.home.children.LocationFragment;
import com.example.followvehicle.ui.home.children.StatusFragment;
import com.example.followvehicle.ui.slideshow.SlideshowViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment{
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btnFragmentStatus = binding.btnFragmentStatus;
        Button btnFragmentLocation = binding.btnFragmentLocation;
        Button btnFragmentCamera = binding.btnFragmentCamera;

        btnFragmentStatus.setOnClickListener(v -> replaceFragment(new StatusFragment()));
        btnFragmentLocation.setOnClickListener(v -> replaceFragment(new LocationFragment()));
        btnFragmentCamera.setOnClickListener(v -> replaceFragment(new CameraFragment()));

        // Load default fragment
        if (savedInstanceState == null) {
            replaceFragment(new StatusFragment());
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}