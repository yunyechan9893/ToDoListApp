package com.example.todolist.fragments;

import com.example.todolist.models.ToDoList;

public interface MenuListener {
    void onItemLongClick(ToDoList toDoList);

    void onDestroyDialog();
}
