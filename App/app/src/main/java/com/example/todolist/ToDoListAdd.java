package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.databinding.ActivityToDoListAddBinding;
import com.example.todolist.fragments.DatePickerListener;
import com.example.todolist.fragments.MyDatePicker;
import com.example.todolist.fragments.MyTimePicker;
import com.example.todolist.fragments.TimePickerListener;
import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListDatabase;
import com.example.todolist.models.ToDoListSingleTone;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ToDoListAdd extends AppCompatActivity implements DatePickerListener, TimePickerListener {

    ActivityToDoListAddBinding binding;
    ToDoListDatabase database;
    String selectDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_to_do_list_add);
        database = ToDoListSingleTone.getInstance(getApplicationContext());
        Intent intent = getIntent();
        selectDate = intent.getStringExtra("selectDate");
        binding.dateButton.setText(selectDate);


        //데이트 버튼 클릭시
        binding.dateButton.setOnClickListener(v -> {
            MyDatePicker datePicker = MyDatePicker.getInstance(selectDate);
            datePicker.setDatePickerListener(this);
            datePicker.show(getSupportFragmentManager(), "add");
        });

        //데이트 버튼 클릭시
        binding.timeButton.setOnClickListener(v -> {
            MyTimePicker timePicker = MyTimePicker.getInstance("10:00:00");
            timePicker.setTimePickerListener(this);
            timePicker.show(getSupportFragmentManager(), "add");
        });

        //Add 버튼 클릭 시
        binding.addButton.setOnClickListener(v -> {
            String title = binding.titleEdittext.getText().toString();
            String stringDate = binding.dateButton.getText().toString();
            String contents = binding.contentsEdittext.getText().toString();

            //시간 구현 후 다시 추가
            stringDate += " 00:00:00";

            if (title.equals("") && title.isEmpty()){
                Toast.makeText(getApplicationContext(), "Title을 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stringDate.equals("") && stringDate.isEmpty()){
                Toast.makeText(getApplicationContext(), "Date를 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (contents.equals("") && contents.isEmpty()){
                Toast.makeText(getApplicationContext(), "Contents를 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }



            Date date = convertTODate(stringDate);

            ToDoList toDoList = new ToDoList();
            toDoList.setTitle(title);
            toDoList.setContents(contents);
            toDoList.setUploadDate(date);
            toDoList.setIsChecked(false);

            Completable.create(emitter -> {
                database.myDao().insert(toDoList);
                emitter.onComplete();
            }).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe();

            Toast.makeText(getApplicationContext(), "Register Complete", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(this, Main.class);
            startActivity(intent2);
        });

        //Cancel 버튼 클릭 시
        binding.cancelButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, Main.class);
            startActivity(intent2);
        });
    }


    public Date convertTODate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = dateFormat.parse(dateString);
            Log.i("1", date.toString());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        String stringMonth = month+"";
        if (month < 10){
            stringMonth = "0" + month;
        }

        String stringDay = day+"";
        if (day < 10){
            stringDay = "0" + day;
        }

        selectDate = year + "-" + stringMonth + "-" + stringDay;
        binding.dateButton.setText(selectDate);
    }

    @Override
    public void onTimeSelected(int hour, int min) {
        Log.d("date", hour+":"+min);

        String amPm = "AM";
        String stringHour = hour+"";

        if (hour >= 12){
            amPm = "PM";

            if ( hour != 12 )
            {
                hour -= 12;
                stringHour = hour+"";
            }
        }

        if (hour < 10){
            stringHour = "0" + hour;
        }

        String stringMin = min+"";
        if (min < 10){
            stringMin = "0" + min;
        }

        String time = stringHour + ":" + stringMin + ":00";
        String timeFormat = amPm + " " + time;
        binding.timeButton.setText(timeFormat);
    }
}
