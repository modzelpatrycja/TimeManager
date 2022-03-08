package com.example.timemanager;

import org.junit.Test;

import static com.example.timemanager.TaskHolder.tasksWithTimer;
import static org.junit.Assert.assertEquals;

public class ListOfTasksTest {
    @Test
    public void should_returnTrue_whenTaskIsNotInTheList() {
        tasksWithTimer.add(new Task("1", "task", 20, 1000, 2000, "24-05-2021"));
        ListOfTasks listOfTasks = new ListOfTasks();
        boolean expected = true;
        String name = "task 2";

        boolean actualVal = listOfTasks.checkIfUnique(name);
        assertEquals(expected, actualVal);
    }

    @Test
    public void should_returnFalse_whenTaskIsInTheList() {
        tasksWithTimer.add(new Task("1", "task", 20, 1000, 2000, "24-05-2021"));
        ListOfTasks listOfTasks = new ListOfTasks();
        boolean expected = false;
        String name = "task";

        boolean actualVal = listOfTasks.checkIfUnique(name);
        assertEquals(expected, actualVal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfUnique_nullArgumentThrowsIAException() {
        tasksWithTimer.add(new Task("1", "task", 20, 1000, 2000, "24-05-2021"));
        ListOfTasks listOfTasks = new ListOfTasks();
        String name = null;
        listOfTasks.checkIfUnique(name);
    }

}
