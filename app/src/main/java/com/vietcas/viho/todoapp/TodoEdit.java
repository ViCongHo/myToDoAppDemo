package com.vietcas.viho.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class TodoEdit extends AppCompatActivity {
    private EditText etEditText;
    private boolean isItemEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String todoItem = getIntent().getStringExtra("todoItem");
        etEditText = (EditText) findViewById(R.id.etEditText);
        etEditText.setText(todoItem);
        etEditText.setSelection(todoItem.length());
        watchEditTextChange();
    }
    private void watchEditTextChange() {
        etEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isItemEdit = true;
            }
        });
    }

    public void editToDoItem(View view) {
        if(isItemEdit) {
            Intent data = new Intent();
            data.putExtra("todoItem", etEditText.getText().toString());
            setResult(RESULT_OK, data);
        }
        this.finish();
    }
}
