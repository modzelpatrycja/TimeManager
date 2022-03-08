package com.example.timemanager;

public class ListOfTasks implements TaskHolder {


    public boolean checkIfUnique(String newTaskName) {

        if (newTaskName == null) {
            throw new IllegalArgumentException("Null task name");
        }
        for (int i = 0; i < tasksWithTimer.size(); i++) {
            if (tasksWithTimer.get(i).getName().equals(newTaskName)) {
                return false;
            }
        }
        return true;
    }

}
