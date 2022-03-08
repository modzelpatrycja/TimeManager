package com.example.timemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.lang.Long.parseLong;


public class MainActivity extends AppCompatActivity implements TaskHolder {

    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private List<DayModel> dayModelList = new ArrayList<>();
    private TextView timer;
    private ImageButton startButton;
    private ImageButton pauseButton;
    private ImageButton stopButton;
    private CountDownTimer countDownTimer;
    private long timeLeft;
    private ImageButton addTaskWithTimerButton;
    private ImageButton listOfTaskButton;
    private ImageButton summaryButton;
    private TextView task;
    private TextView startTask;
    private String name;
    private String time;
    private long timeInMillis;
    private TextView timeLeftText;
    private long timeSpent;
    private boolean isTimerRunning;
    private DatabaseHelper db;
    private String currMonday;
    private Calculations calc;
    private RecyclerView parentRecyclerViewItem;
    private DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(MainActivity.this);
        calc = new Calculations();
        addToTaskArray();
        createPlannerView();

        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currMonday = df.format(c.getTime());

        if (tasksWithTimer.size() > 0) {
            if (tasksWithTimer.get(0).getCurrentMonday() == null || !tasksWithTimer.get(0).getCurrentMonday().equals(currMonday)) {
                for (int i = 0; i < tasksWithTimer.size(); i++) {
                    DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                    db.updateData(tasksWithTimer.get(i).getId(), tasksWithTimer.get(i).getName(), tasksWithTimer.get(i).getTimeGoal(), 0, "-", -1, "", tasksWithTimer.get(i).getTotalTime(), currMonday);
                    tasksWithTimer.get(i).setCurrentMonday(currMonday);
                }
            }
        }
        changeActivity();
        addInfoFromNewTask();


        timer = findViewById(R.id.timer);
        timeLeftText = findViewById(R.id.timeLeftText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        pauseButton = findViewById(R.id.pauseButton);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        time = intent.getStringExtra("time");
        addInfoFromNewTask();
        timerButtonsSettings();

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DatabaseHelper(MainActivity.this);
        addToTaskArray();
        parentRecyclerViewItem.removeItemDecoration(dividerItemDecoration);
        createPlannerView();

    }

    private void timerButtonsSettings() {

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(name == null || name == "")) {
                    updateCountDownText();
                    startTimer();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning == true) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertDialogBuilder.setTitle("Confirm stop");
                    alertDialogBuilder.setMessage("Are you sure you want to stop task?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopTimer();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountDownText();
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                addTaskWithTimerButton.setVisibility(View.INVISIBLE);
                isTimerRunning = true;
            }

            @Override
            public void onFinish() {
                stopTimer();
            }
        }.start();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        pauseButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
    }

    private void stopTimer() {
        calculateTimeSpent();
        countDownTimer.cancel();
        timeLeft = 0;
        updateCountDownText();
        timeLeftText.setVisibility(View.INVISIBLE);
        task.setVisibility(View.INVISIBLE);
        startTask.setVisibility(View.VISIBLE);
        task.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        addTaskWithTimerButton.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);

    }

    private void updateCountDownText() {
        String timeLeftFormatted = calc.countDownText(timeLeft);
        timer.setText(timeLeftFormatted);
    }

    private void createPlannerView() {

        parentRecyclerViewItem = findViewById(R.id.planner);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        RecAdapter RecAdapter = new RecAdapter(DayModelList());
        parentRecyclerViewItem.setAdapter(RecAdapter);
        parentRecyclerViewItem.setLayoutManager(layoutManager);
        dividerItemDecoration = new DividerItemDecoration(parentRecyclerViewItem.getContext(), layoutManager.getOrientation());
        parentRecyclerViewItem.addItemDecoration(dividerItemDecoration);

    }

    private List<DayModel> DayModelList() {
        List<DayModel> itemList = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            DayModel dayModel = new DayModel(days[i], DayTasksList(i));
            itemList.add(dayModel);
        }

        return itemList;
    }


    private List<DayTasks> DayTasksList(int i) {
        List<DayTasks> DayTasksList = new ArrayList<>();
        ArrayList<checkBoxTask> tasksCheckBox = new ArrayList<checkBoxTask>();
        tasksCheckBox = tasksInPlanner.get(i).getCheckBoxTaskList();


        for (int j = 0; j < tasksCheckBox.size(); j++) {
            String key = tasksCheckBox.get(j).getName();
            boolean value = tasksCheckBox.get(j).getChecked();
            String id = tasksCheckBox.get(j).getId();
            String dayNumber = tasksCheckBox.get(j).getDayNumber();
            DayTasksList.add(new DayTasks(key, value, Integer.parseInt(id), dayNumber));
        }


        return DayTasksList;
    }

    private void changeActivity() {
        addTaskWithTimerButton = findViewById(R.id.addTaskWithTimer);
        addTaskWithTimerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TasksWithTimerActivity.class);
                startActivity(intent);
            }
        });

        listOfTaskButton = findViewById(R.id.addToList);
        listOfTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfTasksActivity.class);
                startActivity(intent);
            }
        });

        summaryButton = findViewById(R.id.summary);
        summaryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addInfoFromNewTask() {

        task = (TextView) findViewById(R.id.currentTaskName);
        startTask = (TextView) findViewById(R.id.startNewTask);
        if (name == null) {
            task.setText("Start new Task");
            task.setVisibility(View.INVISIBLE);
            startTask.setVisibility(View.VISIBLE);
        } else {
            task.setVisibility(View.VISIBLE);
            startTask.setVisibility(View.INVISIBLE);
            task.setText(getIntent().getStringExtra("name"));
            task.setTypeface(Typeface.DEFAULT_BOLD);
        }

        if (time != null) {
            timeLeftText.setVisibility(View.VISIBLE);
            time = time.replace(" mins", "");
            timeInMillis = Long.parseLong(time) * 60000;
            timeLeft = timeInMillis;
            updateCountDownText();
        }
    }

    private void calculateTimeSpent() {
        if (isTimerRunning) {
            timeSpent = timeInMillis - timeLeft + 1000;
            String convertedTimeSpent = calc.timeConverter(timeSpent);
            String convertedFullTimeSpent = null;

            for (int i = 0; i < tasksWithTimer.size(); i++) {
                if (tasksWithTimer.get(i).getName().equals(name)) {
                    tasksWithTimer.get(i).setTimeSpent(tasksWithTimer.get(i).getTimeSpent() + timeSpent);
                    tasksWithTimer.get(i).setTotalTime(tasksWithTimer.get(i).getTotalTime() + timeSpent);
                    DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                    db.updateData(tasksWithTimer.get(i).getId(), tasksWithTimer.get(i).getName(), tasksWithTimer.get(i).getTimeGoal(), tasksWithTimer.get(i).getTimeSpent(), "-", -1, "", tasksWithTimer.get(i).getTotalTime(), currMonday);
                    convertedFullTimeSpent = calc.timeConverter(tasksWithTimer.get(i).getTimeSpent());
                    break;
                }
            }

            Toast.makeText(this, "Time spent now:\n " + convertedTimeSpent + "\nTime spent during the week:\n " + convertedFullTimeSpent, Toast.LENGTH_LONG).show();
            isTimerRunning = false;
        }
    }

    private void addToTaskArray() {
        Cursor cursor = db.readAllData();
        tasksWithTimer.clear();
        tasksInPlanner.clear();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int i = 0; i < days.length; i++) {
            tasksInPlanner.add(new DayTasks(days[i], false, 1, Integer.toString(i)));
        }
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {
                if (cursor.getString(4).equals("-")) {
                    tasksWithTimer.add(new Task(cursor.getString(0), cursor.getString(1), parseLong(cursor.getString(2)), parseLong(cursor.getString(3)), parseLong(cursor.getString(7)), cursor.getString(8)));
                } else {
                    tasksInPlanner.get(Integer.parseInt(cursor.getString(5))).addToPlannerWithStateCheckBox(cursor.getString(1), Boolean.parseBoolean(cursor.getString(6)), cursor.getString(0), cursor.getString(5));
                }
            }
        }

    }

}