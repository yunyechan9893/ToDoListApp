package com.example.todolist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.R;
import com.example.todolist.databinding.FragmentMenuDialogBinding;
import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListDatabase;
import com.example.todolist.models.ToDoListSingleTone;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MyMenuPicker extends DialogFragment {
    FragmentMenuDialogBinding binding;
    private ToDoListDatabase database;
    MenuListener menuListener;
    String[] menuItem = {"DELETE","CANCEL"};

    public static MyMenuPicker getInstance(ToDoList toDoList){

        Bundle args = new Bundle();
        args.putSerializable("toDoList", toDoList);

        MyMenuPicker menuPicker = new MyMenuPicker();
        menuPicker.setArguments(args);

        return menuPicker;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = ToDoListSingleTone.getInstance(getContext());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_dialog, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ToDoList toDoList = (ToDoList) getArguments().getSerializable("toDoList");

        // ArrayAdapter를 사용하여 데이터와 레이아웃을 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, menuItem);
        binding.menuListview.setAdapter(adapter);

        binding.menuListview.setOnItemClickListener((parent, view1, position1, id) -> {

            if (position1==0) {
                onClickDelete(toDoList).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(() ->
                                menuListener.onDestroyDialog()
                        );

            }

            this.dismiss();
        });

    }

    private Completable onClickDelete(ToDoList toDoList){
        return Completable.create(emitter ->{
                    database.myDao().delete(toDoList);
                    emitter.onComplete();
        });

    }

    public void setMenuListener(MenuListener menuListener){
        this.menuListener = menuListener;
    }

}
