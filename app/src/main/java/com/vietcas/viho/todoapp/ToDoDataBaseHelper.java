package com.vietcas.viho.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoDataBaseHelper extends SQLiteOpenHelper {
    //Database Info
    private static final String DATABASE_NAME = "TODODB";
    private static final int DATABASE_VERSION = 1;

    //Database table
    private static final String TABLE_TODO = "Todo_tbl";

    //To do table field
    private static final String TODO_ID = "_id";
    private static final String  TODO_TASK = "task";
    private static final String TODO_STATUS = "status";
    private static final String TODO_CREATEDATE = "created_date";
    private static final String TODO_DUEDATE = "due_date";

    private static ToDoDataBaseHelper sIntance;

    public static synchronized ToDoDataBaseHelper getIntance(Context context) {
        if (sIntance == null) {
            sIntance = new ToDoDataBaseHelper(context.getApplicationContext());
        }
        return sIntance;
    }
    public ToDoDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = ("CREATE TABLE "+ TABLE_TODO +
                "("+
                TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODO_TASK + " TEXT" +
                ")");
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + TABLE_TODO);
            onCreate(db);
        }
    }
    public void insertData(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TODO_TASK, task);
            db.insert(TABLE_TODO, null, contentValues);
            db.setTransactionSuccessful();
            Log.d("TAG", "Successful");
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to insert data");
        } finally {
            db.endTransaction();
        }
    }
    public List<TodoItem> getTodoList() {
        List<TodoItem> todoItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_TODO, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(TODO_ID));
                    String task = cursor.getString(cursor.getColumnIndex(TODO_TASK));
                    TodoItem item = new TodoItem(id, task);
                    todoItems.add(item);
                    Log.d("TAG", "this " + task);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Cannot get items from database");
        } finally {
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            };
        }
        return todoItems;
    }
    public void deleteTodo(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODO, TODO_ID + " =?", new String[]{Integer.toString((int) id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DELETE","Cannot delete");
        } finally {
            db.endTransaction();
        }
    }
    public int updateTodo(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TODO_TASK, item.getTask());
        Log.d("UPDATE","id " + item.getID());
        return db.update(TABLE_TODO, values, TODO_ID + " =?", new String[]{Integer.toString(item.getID())});
            //db.execSQL("UPDATE "+ TABLE_TODO + " SET "+ TODO_TASK + "=" + task + " WHERE _id = " + id);
    }
    }