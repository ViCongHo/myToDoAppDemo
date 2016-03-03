package com.vietcas.viho.todoapp;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoEdit_DialogFragment.EditNameDialogListener{
    ArrayAdapter<TodoItem> toDoListAdapter;
    List<TodoItem> todoList;
    TextView etEditText;
    ListView lvItems;
    Button btnAddButton;
    private final int REQUEST_CODE = 20;
    private int pos;
    String TBName = "ToDoTbl";
    ToDoDataBaseHelper myDB;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDB = new ToDoDataBaseHelper(this);
        ToDoDataBaseHelper helper = ToDoDataBaseHelper.getIntance(this);
        pupulateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(toDoListAdapter);
        etEditText = (TextView) findViewById(R.id.etEditText);
        btnAddButton = (Button) findViewById(R.id.btnAdd);
        btnAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoItem();
            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long _id = todoList.get(position).getID();
                myDB.deleteTodo(_id);
                todoList.remove(position);
                toDoListAdapter.notifyDataSetChanged();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = todoList.get(position).getTask();
                Log.d("ITEM", item);
                pos = position;
                lauchEditView(item);
            }
        });
    }

    private void lauchEditView(String item) {
      /*  Intent i = new Intent(MainActivity.this, TodoEdit.class);
        i.putExtra("todoItem", item);
        startActivityForResult(i, REQUEST_CODE);*/
        FragmentManager manager = getFragmentManager();
        TodoEdit_DialogFragment dialogFrament = TodoEdit_DialogFragment.newInstance("", item);
        dialogFrament.show(manager, "My Dialog");
    }

    private void pupulateArrayItems() {
        readItems();
        toDoListAdapter = new ArrayAdapter<TodoItem>(this, android.R.layout.simple_list_item_1, todoList);
    }

    public void readItems() {
        try {
            //todoList = new ArrayList<String>(FileUtils.readLines(todoFile));
            todoList = myDB.getTodoList();
        } catch (Exception e) {
            todoList = new ArrayList<TodoItem>();
            //e.printStackTrace();
        }
    }

   /* public void writeItem() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, todoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void addTodoItem() {
        String item = etEditText.getText().toString();
        if (item.length() > 0) {
            todoList.add(new TodoItem(item));
            myDB.insertData(item);
            toDoListAdapter.notifyDataSetChanged();
            //writeItem();
            etEditText.setText("");
        } else {
            Toast.makeText(MainActivity.this, "Please enter your task!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String todoItem = data.getExtras().getString("todoItem");
            //todoList.set(pos, todoItem);
            toDoListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item has changed", Toast.LENGTH_SHORT).show();
            //writeItem();
        }
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        int id = todoList.get(pos).getID();
        TodoItem changeTask = new TodoItem(id, inputText);
        todoList.set(pos, changeTask);
        myDB.updateTodo(changeTask);
        toDoListAdapter.notifyDataSetChanged();
    }
}
