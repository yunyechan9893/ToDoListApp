package com.example.todolist.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Main;
import com.example.todolist.R;
import com.example.todolist.fragments.MenuListener;
import com.example.todolist.models.ToDoList;
import com.example.todolist.models.ToDoListDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {

    MyViewHolder viewHolder;
    Drawable drawableTrue;
    Drawable drawableFalse;
    ToDoListDatabase database;
    MenuListener menuListener;
    List<ToDoList> toDoListItems;
    Main main;

    public RecyclerviewAdapter(List<ToDoList> toDoListItems, ToDoListDatabase database, MenuListener menuListener){

        this.toDoListItems = toDoListItems;
        this.database = database;
        this.menuListener = menuListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 뷰 홀더를 생성하고 연결할 XML 레이아웃을 설정합니다.
        drawableTrue = parent.getContext().getResources().getDrawable(R.drawable.baseline_check_circle_outline_24);
        drawableFalse = parent.getContext().getDrawable(R.drawable.radio_button_unchecked);

        View view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_recyclerview_item, parent, false).getRoot();
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // 뷰 홀더와 데이터를 연결하는 코드를 여기에 작성합니다.
        ToDoList toDoListItem = toDoListItems.get(position);
        String title = toDoListItem.getTitle();
        String contents = toDoListItem.getContents();
        boolean isChecked = toDoListItem.getIsChecked();

        viewHolder.titleTextview.setText(title);
        viewHolder.contentsTextview.setText(contents);

        CheckBox checkbox = viewHolder.checkbox;
        checkbox.setChecked(isChecked);

        if (isChecked) {
            checkbox.setBackground(drawableTrue);

            Log.d("isChecked1", isChecked + "");
            isChecked = false;
        }

        viewHolder.itemLinearLayout.setOnLongClickListener(v -> {
                    menuListener.onItemLongClick(toDoListItem);
                    return true;
                }
        );

        checkbox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            toDoListItem.setIsChecked(isChecked1);

            Completable.create(emitter -> {
                        database.myDao().update(toDoListItem);
                        emitter.onComplete();
                    }).subscribeOn(Schedulers.io())
                    .subscribe();

            if (isChecked1) {
                checkbox.setBackground(drawableTrue);

                Log.d("isChecked1", isChecked1 + "");
                isChecked1 = false;
            } else {
                checkbox.setBackground(drawableFalse);

                isChecked1 = true;
                Log.d("isChecked1", isChecked1 + "");
            }
        });


    }

    @Override
    public int getItemCount() {
        return toDoListItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // 뷰 홀더 클래스를 정의합니다.
        // 여기에 뷰 홀더의 구성 요소 (예: TextView, ImageView)를 정의하고 연결합니다.
        TextView titleTextview;
        TextView contentsTextview;
        CheckBox checkbox;
        LinearLayout itemLinearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextview = itemView.findViewById(R.id.title_textview);
            contentsTextview = itemView.findViewById(R.id.contents_textview);
            checkbox = itemView.findViewById(R.id.checkbox);
            itemLinearLayout = itemView.findViewById(R.id.item_linear_layout);
        }
    }
}
