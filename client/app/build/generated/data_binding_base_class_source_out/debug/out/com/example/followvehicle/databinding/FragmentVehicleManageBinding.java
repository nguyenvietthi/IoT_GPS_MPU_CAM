// Generated by view binder compiler. Do not edit!
package com.example.followvehicle.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.followvehicle.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentVehicleManageBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton addVehicle;

  @NonNull
  public final ListView listView;

  @NonNull
  public final TextView textGallery;

  private FragmentVehicleManageBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton addVehicle, @NonNull ListView listView,
      @NonNull TextView textGallery) {
    this.rootView = rootView;
    this.addVehicle = addVehicle;
    this.listView = listView;
    this.textGallery = textGallery;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentVehicleManageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentVehicleManageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_vehicle_manage, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentVehicleManageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.add_vehicle;
      FloatingActionButton addVehicle = ViewBindings.findChildViewById(rootView, id);
      if (addVehicle == null) {
        break missingId;
      }

      id = R.id.listView;
      ListView listView = ViewBindings.findChildViewById(rootView, id);
      if (listView == null) {
        break missingId;
      }

      id = R.id.text_gallery;
      TextView textGallery = ViewBindings.findChildViewById(rootView, id);
      if (textGallery == null) {
        break missingId;
      }

      return new FragmentVehicleManageBinding((ConstraintLayout) rootView, addVehicle, listView,
          textGallery);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
