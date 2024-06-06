package com.example.followvehicle.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<String>> selectedItem = new MutableLiveData<>();

    public void selectItem(List<String> item) {
        selectedItem.setValue(item);
    }

    public LiveData<List<String>> getSelectedItem() {
        return selectedItem;
    }
}
