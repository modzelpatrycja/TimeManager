package com.example.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TasksWithTimerActivity extends AppCompatActivity implements OnItemSelectedListener, TaskHolder {

    private Spinner selectTask;
    private ArrayList<String> spinnerTaskInfo;
    private Spinner selectTime;
    private ArrayList<String> times;
    private Button startTask;
    private String name;
    private String time;
    private DatabaseHelper db;
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskswithtimer);

        selectTask = (Spinner) findViewById(R.id.spinner);
        selectTask.setOnItemSelectedListener(this);
        db = new DatabaseHelper(TasksWithTimerActivity.this);
        db.addToTaskArray(TasksWithTimerActivity.this);

        spinnerTaskInfo = new ArrayList<String>();
        for (int i = 0; i < tasksWithTimer.size(); i++) {
            spinnerTaskInfo.add(tasksWithTimer.get(i).getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTaskInfo);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTask.setAdapter(spinnerAdapter);

        selectTime = (Spinner) findViewById(R.id.spinner2);
        selectTime.setOnItemSelectedListener(this);

        times = new ArrayList<String>();
        for (int i = 1; i < 12; i++) {
            times.add(i * 5 + " mins");
        }

        for (int i = 0; i < 7; i++) {
            times.add(60 + i * 10 + " mins");
        }

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTime.setAdapter(spinnerAdapter2);

        startTask = findViewById(R.id.addTaskToListButton);
        startTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("time", time);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner) parent;
        Spinner spin2 = (Spinner) parent;
        if (spin.getId() == R.id.spinner) {
            name = spinnerTaskInfo.get(position);
        }
        if (spin2.getId() == R.id.spinner2) {
            time = times.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

