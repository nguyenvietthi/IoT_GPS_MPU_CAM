package com.example.followvehicle.ui.VehicleManage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VehicleManageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VehicleManageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Empty!!!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}