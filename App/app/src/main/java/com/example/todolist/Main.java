package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todolist.adapters.RecyclerviewAdapter;
import com.example.todolist.databinding.ActivityMainBinding;
import com.example.todolist.fragments.DatePickerListener;
import com.example.todolist.fragments.MenuListener;
import com.example.todolist.fragments.MyDatePicker;
import com.example.todolist.fragments.MyMenuPicker;
import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListDatabase;
import com.example.todolist.models.ToDoListSingleTone;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Main extends AppCompatActivity implements DatePickerListener, MenuListener {
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
    ActivityMainBinding binding;
    ToDoListDatabase database;
    RecyclerviewAdapter adapter;

    String[] months = {"", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    String[] daysOfWeek = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    String selectDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        database = ToDoListSingleTone.getInstance(getApplicationContext());

        String time = getTime();
        String[] curruntDate = time.split("-");
        String year = curruntDate[0];
        String month = curruntDate[1];
        String day = curruntDate[2];

        selectDate = time;

        String monthText;
        int monthNumber = Integer.parseInt(month);

        if (monthNumber >= 1 && monthNumber <= 12) {
            monthText = months[monthNumber];
        } else {
            monthText = "Invalid Month";
        }

        // 현재 날짜를 가져옵니다.
        LocalDate currentDate = LocalDate.now();

        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // 요일을 영어로 문자열로 변환합니다.
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        getEntityByDate(year, month, day);

        binding.dayTextview.setText(day);
        binding.yearTextview.setText(year);
        binding.monthTextTextview.setText(monthText);
        binding.weekTextview.setText(dayOfWeekString);

        binding.toDoListAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ToDoListAdd.class);
            intent.putExtra("selectDate", selectDate);
            startActivity(intent);
        });


        binding.dateLayout.setOnClickListener(v -> {
            MyDatePicker datePicker = MyDatePicker.getInstance(selectDate);
            datePicker.setDatePickerListener(this);
            datePicker.show(getSupportFragmentManager(), "add");
        });
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        return mFormat.format(mDate);
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        String stringYear = year + "";
        String stringMonth = month+"";
        String stringDay = day+"";

        if (month < 10){
            stringMonth = "0" + month;
        }

        if (day < 10){
            stringDay = "0" + day;
        }

        String monthText;

        if (month >= 1 && month <= 12) {
            monthText = months[month];
        } else {
            monthText = "Invalid Month";
        }

        String date = year + "-" + stringMonth + "-" + stringDay;
        selectDate = date;

        DayOfWeek dayOfWeek = LocalDate.parse(date).getDayOfWeek();
        // 요일을 영어로 문자열로 변환합니다.
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        getEntityByDate(stringYear, stringMonth, stringDay);


        binding.dayTextview.setText(stringDay);
        binding.monthTextTextview.setText(monthText);
        binding.yearTextview.setText(year+"");
        binding.weekTextview.setText(dayOfWeekString);
    }

    private void getEntityByDate(String year, String month, String day){
        Single.create(
                emitter -> {
                    List<ToDoList> toDoList =database.myDao().getEntityByDate(year,month,day);

                    emitter.onSuccess(toDoList);
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(objectList -> {
                            List<ToDoList> toDoList = (List<ToDoList>) objectList;

                            binding.toDoListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                            RecyclerviewAdapter adapter = new RecyclerviewAdapter(toDoList, database, this);
                            binding.toDoListRecyclerview.setAdapter(adapter);
                        },
                        throwable -> {
                            Log.e("error",throwable.toString());
                        }
        );
    }


    @Override
    public void onItemLongClick(ToDoList toDoList) {
        MyMenuPicker menuPicker = MyMenuPicker.getInstance(toDoList);
        menuPicker.setMenuListener(this);
        menuPicker.show(getSupportFragmentManager(), "main");
    }

    @Override
    public void onDestroyDialog() {
        String[] date = selectDate.split("-");
        String year = date[0];
        String month = date[1];
        String day = date[2];

        getEntityByDate(year, month, day);
    }
}
