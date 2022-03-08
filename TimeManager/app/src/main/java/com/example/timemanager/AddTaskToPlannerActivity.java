package com.example.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskToPlannerActivity extends AppCompatActivity implements TaskHolder {
    private Button addTaskToPlanner;
    private EditText newPlannerTaskName;
    private String day;
    private String dayNumber;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtasktoplanner);
        newPlannerTaskName = (EditText) findViewById(R.id.newTaskInPlanner);
        addTaskToPlanner = findViewById(R.id.addTaskToPlanner);
        Intent intent = getIntent();
        day = intent.getStringExtra("day");
        dayNumber = intent.getStringExtra("dayNumber");

        addTaskToPlanner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!newPlannerTaskName.getText().toString().trim().isEmpty()) {
                    myDB = new DatabaseHelper(AddTaskToPlannerActivity.this);
                    myDB.addTaskToDatabase(newPlannerTaskName.getText().toString().trim(), 0, 0, day, Integer.parseInt(dayNumber), "false", 0, "-");
                    finish();
                }
            }
        });
    }

}
