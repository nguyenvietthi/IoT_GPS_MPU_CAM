package com.example.followvehicle.ui.slideshow;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.followvehicle.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectDateTime extends BottomSheetDialogFragment {
    private SharedViewModel sharedViewModel;
    private EditText dateTimeEditText1;
    private EditText dateTimeEditText2;

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        List<String> item = new ArrayList<>();
        item.add(dateTimeEditText1.getText().toString().replace("Start: ", "").replace("/", "-"));
        item.add(dateTimeEditText2.getText().toString().replace("Start: ", "").replace("/", "-"));
        sharedViewModel.selectItem(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);;
        dateTimeEditText1 = view.findViewById(R.id.dateTimeEditText1);
        dateTimeEditText2 = view.findViewById(R.id.dateTimeEditText2);

        dateTimeEditText1.setOnClickListener(v -> showDateTimePickerDialog(dateTimeEditText1, "Start"));
        dateTimeEditText2.setOnClickListener(v -> showDateTimePickerDialog(dateTimeEditText2, "Finish"));

        return view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            bottomSheetDialog.getBehavior().setHideable(true);
            bottomSheetDialog.getBehavior().setSkipCollapsed(true);
        });
        dialog.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }


    private void showDateTimePickerDialog(EditText dateTimeEditText, String value) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                String dateTime = String.format("%d/%02d/%02d %02d:%02d",
                                        year, month + 1, dayOfMonth, hourOfDay, minute);
                                dateTimeEditText.setText(value + ": " + dateTime);
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}