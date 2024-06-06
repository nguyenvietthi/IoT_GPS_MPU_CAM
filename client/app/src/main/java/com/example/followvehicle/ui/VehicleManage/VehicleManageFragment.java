package com.example.followvehicle.ui.VehicleManage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.followvehicle.CustomAdapter;
import com.example.followvehicle.Item;
import com.example.followvehicle.R;
import com.example.followvehicle.databinding.FragmentVehicleManageBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VehicleManageFragment extends Fragment {

    private FragmentVehicleManageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VehicleManageViewModel galleryViewModel =
                new ViewModelProvider(this).get(VehicleManageViewModel.class);

        binding = FragmentVehicleManageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Item> items = new ArrayList<>();
        items.add(new Item("Motor", "37M1-56632"));
        items.add(new Item("Car", "37M1-56632"));
        items.add(new Item("Motor", "37M1-56632"));
        items.add(new Item("Motor", "37M1-56632"));
        items.add(new Item("Car", "37M1-56632"));

        CustomAdapter adapter = new CustomAdapter(requireContext(), R.layout.list_item, items);
        binding.listView.setAdapter(adapter);

        final TextView textView = binding.textGallery;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}