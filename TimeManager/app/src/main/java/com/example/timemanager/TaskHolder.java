package com.example.timemanager;

import java.util.ArrayList;

public interface TaskHolder {
    ArrayList<Task> tasksWithTimer = new ArrayList<Task>();
    ArrayList<DayTasks> tasksInPlanner = new ArrayList<DayTasks>();
}
