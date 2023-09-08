package com.example.todolist.models;

import android.content.Context;

import androidx.room.Room;

public class ToDoListSingleTone {
    private static ToDoListDatabase INSTANCE;

    public static synchronized ToDoListDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDoListDatabase.class, "ToDoListDatabase")
                    .build();
        }
        return INSTANCE;
    }
}
