package com.example.followvehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.followvehicle.api.StoreUserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ShowNotification extends Fragment {


    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<String> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_notification, container, false);

        ListView listView =view.findViewById(R.id.notification_list);

        notificationList = StoreUserData.getInstance(requireContext()).getNotifications();
        Collections.reverse(notificationList);
        List<Item> items = new ArrayList<>();

        for(String str:notificationList){
            items.add(new Item(StoreUserData.getInstance(requireContext()).getKeyDeviceCode(), str));
        }




        CustomAdapter adapter = new CustomAdapter(requireContext(), R.layout.list_item, items);
        listView.setAdapter(adapter);

//        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
//
//        // Dữ liệu mẫu cho thông báo
////        notificationList = new ArrayList<>();
////        notificationList.add("Thông báo 1");
////        notificationList.add("Thông báo 2");
////        notificationList.add("Thông báo 3");
//        notificationList = StoreUserData.getInstance(requireContext()).getNotifications();
//        Collections.reverse(notificationList);
//        // Khởi tạo adapter và gán cho RecyclerView
//        adapter = new NotificationAdapter(notificationList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);

        return view;
    }

    // Phương thức để thêm thông báo mới
    public void addNotification(String notification) {
        notificationList.add(notification);
        adapter.notifyItemInserted(notificationList.size() - 1);
    }
}