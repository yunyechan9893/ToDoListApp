package com.example.todolist.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface DAO {
    @Insert
    void insert(ToDoList entity);

    @Update
    void update(ToDoList entity);

    @Delete
    void delete(ToDoList entity);

    @Query("SELECT * FROM to_do_list WHERE strftime('%Y', datetime(upload_date / 1000, 'unixepoch')) = :year " +
            "AND strftime('%m', datetime(upload_date / 1000, 'unixepoch')) = :month " +
            "AND strftime('%d', datetime(upload_date / 1000, 'unixepoch')) = :day")
    List<ToDoList> getEntityByDate(String year, String month, String day);

    @Query("SELECT * FROM to_do_list")
    List<ToDoList> getTestEntityByDate();
}
