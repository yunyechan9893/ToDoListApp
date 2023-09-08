package com.example.todolist.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.R;
import com.example.todolist.databinding.FragmentDatePickerDialogBinding;
import com.example.todolist.databinding.FragmentTimePickerDialogBinding;

public class MyTimePicker extends DialogFragment {
    private TimePickerListener timePickerListener;
    private FragmentTimePickerDialogBinding binding;
    private int hour;
    private int min;


    public static MyTimePicker getInstance(String stringTime){
        String[] timeList = stringTime.split(":");
        String hour = timeList[0];
        String min = timeList[1];


        MyTimePicker myTimePicker = new MyTimePicker();
        Bundle args = new Bundle();
        args.putString("hour",hour);
        args.putString("minute",min);
        myTimePicker.setArguments(args);

        return myTimePicker;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_picker_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String stringHour = getArguments().getString("hour");
        String stringMin  = getArguments().getString("minute");

        hour = Integer.parseInt(stringHour);
        min = Integer.parseInt(stringMin);

        binding.timepicker.setHour(hour);
        binding.timepicker.setMinute(min);

        binding.timepicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            hour=hourOfDay;
            min=minute;
        });


        binding.addButton.setOnClickListener(v -> {
            timePickerListener.onTimeSelected(hour, min);

            this.dismiss();
        });

        binding.cancelButton.setOnClickListener(v ->
                this.dismiss()
        );
    }

    public void setTimePickerListener(TimePickerListener listener) {
        this.timePickerListener = listener;
    }

}
