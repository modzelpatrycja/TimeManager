package com.example.timemanager;

public class Task {
    private String id;
    private String name;
    private long timeGoal;
    private long timeSpent;
    private long totalTime;
    private String currentMonday;

    public Task(String id, String name, long timeGoal, long timeSpent, long totalTime, String currentMonday) {
        this.id = id;
        this.name = name;
        this.timeGoal = timeGoal;
        this.timeSpent = timeSpent;
        this.totalTime = totalTime;
        this.currentMonday = currentMonday;

    }

    public String getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public long getTimeGoal() {

        return timeGoal;
    }

    public long getTimeSpent() {

        return timeSpent;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setTimeSpent(long timeSpent) {

        this.timeSpent = timeSpent;
    }

    public String getCurrentMonday() {
        return currentMonday;
    }

    public void setCurrentMonday(String currentMonday) {
        this.currentMonday = currentMonday;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

}
