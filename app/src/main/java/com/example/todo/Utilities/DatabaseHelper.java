package com.example.todo.Utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todo.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
private static final int DATABASE_VERSION = 1;
private static final String DATABASE_NAME = "toDoListDatabase";
private static final String TODO_TABLE = "todo";
private static final String TODO_ID = "id";
private static final String TASK = "task";
private static final String STATUS = "status";
private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "("+ TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER)";
private SQLiteDatabase db;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);

    }
    public void openDatabase() {
        db = this.getWritableDatabase();
    }
    public void insertTask(TodoModel task) {
        ContentValues cv= new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }
    @SuppressLint("Range")
    public List<TodoModel> getAllTasks() {
        List<TodoModel> tasks = new ArrayList<>();
        Cursor cr=null;
        db.beginTransaction();
        try {
            cr=db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cr != null) {
                if (cr.moveToFirst()) {
                    do {
                        TodoModel task = new TodoModel();
                        task.setId(cr.getInt(cr.getColumnIndex(TODO_ID)));
                        task.setTask(cr.getString(cr.getColumnIndex(TASK)));
                        task.setStatus(Boolean.parseBoolean(String.valueOf(cr.getInt(cr.getColumnIndex(STATUS)))));
                        tasks.add(task);
                    } while (cr.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            cr.close();
        }
        return tasks;
    }
    public void updateStatus(int id,int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, TODO_ID + "=?", new String[]{String.valueOf(id)});
    }
    public void updateTask(int id,String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, TODO_ID + "=?", new String[]{String.valueOf(id)});
        }
    public void deleteTask(int id) {
        db.delete(TODO_TABLE, TODO_ID + "=?", new String[]{String.valueOf(id)});
    }
}
