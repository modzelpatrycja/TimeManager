package com.example.timemanager;

import java.util.ArrayList;
import java.util.HashMap;

public class DayTasks {

    private String name;
    private int id;
    private String dayNumber;
    private HashMap<String, Boolean> taskWithState = new HashMap<String, Boolean>();
    private ArrayList<checkBoxTask> checkBoxTaskList = new ArrayList<checkBoxTask>();
    private boolean checked;


    public DayTasks(String name, boolean checked, int id, String dayNumber) {
        this.name = name;
        this.checked = checked;
        this.id = id;
        this.dayNumber = dayNumber;
    }

    public void addToPlannerWithState(String task, boolean state) {
        taskWithState.put(task, state);
    }

    public void addToPlannerWithStateCheckBox(String task, boolean state, String id, String dayName) {
        checkBoxTaskList.add(new checkBoxTask(task, state, id, dayName));
    }

    public void setText(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public int getId() {

        return id;
    }

    public ArrayList<checkBoxTask> getCheckBoxTaskList() {

        return checkBoxTaskList;
    }

    public String getDayNumber() {

        return dayNumber;
    }

    public boolean isChecked() {

        return checked;
    }
}
