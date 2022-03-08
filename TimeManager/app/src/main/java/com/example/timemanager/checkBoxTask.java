package com.example.timemanager;

public class checkBoxTask {

    private String id;
    private String name;
    private boolean checked;
    private String dayNumber;

    public checkBoxTask(String name, boolean checked, String id, String dayNumber) {
        this.id = id;
        this.name = name;
        this.checked = checked;
        this.dayNumber = dayNumber;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public boolean getChecked() {

        return checked;
    }

    public String getId() {

        return id;
    }

    public String getDayNumber() {

        return dayNumber;
    }

}
