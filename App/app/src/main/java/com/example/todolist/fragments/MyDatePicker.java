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
import androidx.fragment.app.Fragment;

import com.example.todolist.R;
import com.example.todolist.databinding.FragmentDatePickerDialogBinding;

public class MyDatePicker extends DialogFragment {
    private DatePickerListener datePickerListener;
    private int year;
    private int month ;
    private int day;
    public static MyDatePicker getInstance(String stringDate){
        String[] dateList = stringDate.split("-");
        String year = dateList[0];
        String month = dateList[1];
        String day = dateList[2];

        MyDatePicker myDatePicker = new MyDatePicker();
        Bundle args = new Bundle();
        args.putString("year", year);
        args.putString("month", month);
        args.putString("day", day);
        myDatePicker.setArguments(args);
        return myDatePicker;
    }

    FragmentDatePickerDialogBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_picker_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String stringYear = args.getString("year");
        String stringMonth = args.getString("month");
        String stringDay = args.getString("day");

        year = Integer.parseInt(stringYear);
        month = Integer.parseInt(stringMonth) - 2;
        day = Integer.parseInt(stringDay);

        int monthOfYear = month + 1;

        binding.datepicker.init(year, monthOfYear, day, (view1, year1, monthOfYear1, dayOfMonth) -> {
            // 또는 원하는 다른 동작을 여기에 수행
            year = year1;
            month = monthOfYear1 - 1;
            day = dayOfMonth;
        });

        binding.addButton.setOnClickListener(v -> {
            Log.d("DatePicker", "Year: " + year + ", Month: " + month + ", Day: " + day);

            datePickerListener.onDateSelected(year, month + 2, day);

            this.dismiss();
        });

        binding.cancelButton.setOnClickListener(v ->
                this.dismiss()
        );
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.datePickerListener = listener;
    }

}
