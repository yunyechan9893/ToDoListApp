package com.example.todolist.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ToDoList.class}, version = 1)
@TypeConverters({RoomTypeConverter.class}) // type converter를 사용하려면 포함
public abstract class ToDoListDatabase extends RoomDatabase {
    public abstract DAO myDao();
}
