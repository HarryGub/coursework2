package com.example.hserun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Task extends Fragment {

    ImageButton hint;
    Button check;
    EditText ans;
    String question;
    String category;
    TextView task;

    int type;
    int number;

    public Task(String q, String c, int t, int n) {
        question = q;
        category = c;
        type = t;
        number = n;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hint = (ImageButton) getView().findViewById(R.id.hint);
        check = (Button) getView().findViewById(R.id.check);
        ans = (EditText) getView().findViewById(R.id.ans);
        task = (TextView)getView().findViewById(R.id.task);
        task.setText(question);
        return inflater.inflate(R.layout.fragment_task, container, false);
    }
}