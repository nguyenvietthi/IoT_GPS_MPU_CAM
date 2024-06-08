// Generated by view binder compiler. Do not edit!
package com.example.followvehicle.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.followvehicle.R;
import com.mapbox.maps.MapView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentLocationBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final MapView CurrentLocationMap;

  private FragmentLocationBinding(@NonNull RelativeLayout rootView,
      @NonNull MapView CurrentLocationMap) {
    this.rootView = rootView;
    this.CurrentLocationMap = CurrentLocationMap;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentLocationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentLocationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_location, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentLocationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.CurrentLocationMap;
      MapView CurrentLocationMap = ViewBindings.findChildViewById(rootView, id);
      if (CurrentLocationMap == null) {
        break missingId;
      }

      return new FragmentLocationBinding((RelativeLayout) rootView, CurrentLocationMap);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
