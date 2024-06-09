package com.example.followvehicle.ui.home.children;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.followvehicle.R;

public class StatusFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);;
//        Button showBottomSheetButton = view.findViewById(R.id.showBottomSheetButton);
//        showBottomSheetButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAlertDialog();
////                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
////                bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
//            }
//        });

//        getParentFragmentManager().setFragmentResultListener("bottomSheetRequestKey", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                String data = result.getString("dataKey");
//                // Xử lý dữ liệu nhận được
//                Log.d("SlideshowFragment", "Data received: " + data);
//                Toast.makeText(getContext(), "Data received: " + data, Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;

    }
    private void showAlertDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        builder.setTitle("Thông Báo");
//        builder.setMessage("Đây là một thông báo!");
//        builder.setPositiveButton("OK", null);
//        builder.show();
//        Toast.makeText(requireContext(), "You Clicked ", Toast.LENGTH_SHORT).show();
    }

}