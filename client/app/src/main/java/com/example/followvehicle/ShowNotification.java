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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.followvehicle.api.StoreUserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ShowNotification extends Fragment {


    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<String> notificationList;

    private SwipeRefreshLayout swipeRefreshLayout;

    List<Item> items;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_notification, container, false);

        ListView listView =view.findViewById(R.id.notification_list);

        notificationList = StoreUserData.getInstance(requireContext()).getNotifications();
        Collections.reverse(notificationList);
        items = new ArrayList<>();

        for(String str:notificationList){
            String[] parts = str.split(": ", 2);
            String dateTime = parts[0].replace("Lúc ", "");
            String text = parts[1];

            items.add(new Item(dateTime, text));
        }

        adapter = new CustomAdapter(requireContext(), R.layout.list_item, items);
        listView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                notificationList = StoreUserData.getInstance(requireContext()).getNotifications();
                Collections.reverse(notificationList);
                items = new ArrayList<>();


                for(String str:notificationList){
                    String[] parts = str.split(": ", 2);
                    String dateTime = parts[0].replace("Lúc ", "");
                    String text = parts[1];

                    items.add(new Item(dateTime, text));
                }

                adapter.clear();
                adapter.addAll(items);
                adapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

}