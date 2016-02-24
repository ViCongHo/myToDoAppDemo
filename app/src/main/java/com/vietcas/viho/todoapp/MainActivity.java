package com.vietcas.viho.todoapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> toDoListAdapter;
    ArrayList<String> todoList;
    TextView etEditText;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    private int pos;
    SQLiteDatabase myDB = null;
    String TBName = "ToDoTbl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pupulateArrayItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(toDoListAdapter);
        etEditText = (TextView) findViewById(R.id.etEditText);

       lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               todoList.remove(position);
               writeItem();
               toDoListAdapter.notifyDataSetChanged();
               return true;
           }
       });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item =(String) lvItems.getItemAtPosition(position);
                pos = position;
                lauchEditView(item, position);
            }
        });
    }
    private  void initDB() {
        try {
            myDB = this.openOrCreateDatabase("ToDoDB", MODE_PRIVATE, null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void lauchEditView(String item, int positon) {
        Intent i = new Intent(MainActivity.this, TodoEdit.class);
        i.putExtra("todoItem", item);
        startActivityForResult(i, REQUEST_CODE);
    }
    private void pupulateArrayItems() {
        readItems();
        toDoListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoList);
    }
    public void readItems() {
        File fileDir = getFilesDir();
        File todoFile =  new File(fileDir, "todo.txt");
        try {
            todoList = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            todoList = new ArrayList<String>();
            e.printStackTrace();
        }
    }
    public void writeItem() {
        File fileDir = getFilesDir();
        File todoFile =  new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, todoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addTodoItem(View view) {
        String item = etEditText.getText().toString();
        if(item.length() > 0) {
            todoList.add(item);
            writeItem();
            etEditText.setText("");
        } else {
            Toast.makeText(this, "Please enter your task!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String todoItem = data.getExtras().getString("todoItem");
            todoList.set(pos, todoItem);
            toDoListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item has changed", Toast.LENGTH_SHORT).show();
            writeItem();
        }
    }
}
