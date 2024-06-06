package com.example.followvehicle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.followvehicle.ui.VehicleManage.VehicleManageFragment;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private int mResource;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        // Lookup view for data population
        TextView textViewLine1 = convertView.findViewById(R.id.line1);
        TextView textViewLine2 = convertView.findViewById(R.id.line2);

        // Populate the data into the template view using the data object
        if (item != null) {
            textViewLine1.setText(item.getLine1());
            textViewLine2.setText(item.getLine2());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
