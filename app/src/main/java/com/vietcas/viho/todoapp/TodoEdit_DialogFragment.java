package com.vietcas.viho.todoapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class TodoEdit_DialogFragment extends DialogFragment {
    private Button cancelBtn, okBtn;
    private EditText etEditText;
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }
    public TodoEdit_DialogFragment() {

    }
    public static TodoEdit_DialogFragment newInstance(String title, String task) {
        TodoEdit_DialogFragment frag = new TodoEdit_DialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("task", task);
        frag.setArguments(bundle);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.todo_edit_dialog, container);
    }
    @Override
    public void  onViewCreated(View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        etEditText = (EditText) view.findViewById(R.id._etEditText);
        String title = getArguments().getString("title", "edit_todo_title");
        String task = getArguments().getString("task");
        getDialog().setTitle(title);
        etEditText.setText(task);
        etEditText.requestFocus();
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        okBtn = (Button) view.findViewById(R.id.btn_ok);
        onDismissDialog();
        onAllow();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    private void onDismissDialog() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void onAllow() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etEditText.getText().length() > 0) {
                    EditNameDialogListener dialogListener = (EditNameDialogListener) getActivity();
                    dialogListener.onFinishEditDialog(etEditText.getText().toString());
                    dismiss();
                }
            }
        });
    }
}
