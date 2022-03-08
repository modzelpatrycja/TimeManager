package com.example.timemanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ListOfTasksActivity extends AppCompatActivity implements TaskHolder, AdapterView.OnItemSelectedListener {

    private EditText newTaskName;
    private Spinner selectTimeGoal;
    private ArrayList<String> time;
    private ArrayList<String> displayedTime;
    private Button createNewTask;
    private Button removeTask;
    private long timeSelected;
    private Button removeTaskButton;
    private Spinner tasks;
    private ArrayList<String> spinnerTaskInfo;
    private String name;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private DatabaseHelper db;
    private ListOfTasks listOfTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listoftasks);
        newTaskName = findViewById(R.id.nameOfTask);
        listOfTasks = new ListOfTasks();

        selectTimeGoal = (Spinner) findViewById(R.id.timeOfTheTask);
        selectTimeGoal.setOnItemSelectedListener(this);

        time = new ArrayList<String>();
        displayedTime = new ArrayList<String>();
        for (int i = 2; i < 6; i++) {
            time.add(i * 10 + " mins");
            displayedTime.add(i * 10 + " mins");
        }

        for (int i = 1; i < 16; i++) {
            time.add(i * 60 + " mins");
            displayedTime.add(i + " h");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, displayedTime);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTimeGoal.setAdapter(spinnerAdapter);

        createNewTask = findViewById(R.id.addTaskToListButton);
        createNewTask.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (!newTaskName.getText().toString().trim().isEmpty()) {
                    if (!listOfTasks.checkIfUnique(newTaskName.getText().toString().trim())) {
                        Toast.makeText(v.getContext(), " Task " + newTaskName.getText().toString().trim() + " already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        db = new DatabaseHelper(ListOfTasksActivity.this);
                        Calendar c = GregorianCalendar.getInstance();
                        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String currMonday = df.format(c.getTime());

                        db.addTaskToDatabase(newTaskName.getText().toString().trim(), timeSelected, 0, "-", -1, "-", 0, currMonday);
                        newTaskName.setText("");
                    }
                }

            }
        });


        removeTask = findViewById(R.id.removeFromList);
        removeTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopupWindowClick();
            }
        });
    }

   /* private boolean checkIfUnique() {
        for (int i = 0; i < tasksWithTimer.size(); i++) {
            if (tasksWithTimer.get(i).getName().equals(newTaskName.getText().toString().trim())) {
                return false;
            }
        }
        return true;
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timeSelected = Long.parseLong(time.get(position).replace(" mins", ""));


        Spinner spin = (Spinner) parent;
        Spinner spin2 = (Spinner) parent;
        if (spin.getId() == R.id.spinner) {
            timeSelected = Long.parseLong(time.get(position).replace(" mins", ""));
        }
        if (spin2.getId() == R.id.listOfTasks) {
            name = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showPopupWindowClick() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.activity_removetask, null);

        db = new DatabaseHelper(ListOfTasksActivity.this);
        db.addToTaskArray(ListOfTasksActivity.this);

        tasks = (Spinner) popup.findViewById(R.id.listOfTasks);
        tasks.setOnItemSelectedListener(this);
        removeTaskButton = popup.findViewById(R.id.removeTaskButton);

        spinnerTaskInfo = new ArrayList<String>();
        for (int i = 0; i < tasksWithTimer.size(); i++) {
            spinnerTaskInfo.add(tasksWithTimer.get(i).getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTaskInfo);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tasks.setAdapter(spinnerAdapter);


        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 1500);

        removeTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.deleteRow(name);
                dialog.dismiss();
            }
        });

    }

}
