package com.example.timemanager;

import org.junit.Test;

import static com.example.timemanager.TaskHolder.tasksWithTimer;
import static org.junit.Assert.*;

public class CalculationsTest {
    @Test
    public void should_returnTrue_whenTimeConvertedCorrectly_1_secondValue() {
        Calculations calc = new Calculations();
        String expected = "0 h 0 min 1 sec";
        String actualVal = calc.timeConverter(1000);
        assertEquals(expected, actualVal);
    }

    @Test
    public void should_returnTrue_whenTimeConvertedCorrectly_largeValue() {
        Calculations calc = new Calculations();
        String expected = "16 h 40 min 0 sec";
        String actualVal = calc.timeConverter(60000001);
        assertEquals(expected, actualVal);
    }


    @Test(expected = IllegalArgumentException.class)
    public void timeConverter_lessThanZero_TimeGoal_ArgumentThrowsIAException() {
        Calculations calc = new Calculations();
        long val = -1;
        calc.timeConverter(val);

    }

    @Test
    public void should_returnTrue_countDownTextCorrect_1_secondValue() {
        Calculations calc = new Calculations();
        String expected = "00:00:01";
        String actualVal = calc.countDownText(1000);
        assertEquals(expected, actualVal);
    }


    @Test
    public void should_returnTrue_countDownTextCorrect_1_minuteValue() {
        Calculations calc = new Calculations();
        String expected = "00:01:00";
        String actualVal = calc.countDownText(60000);
        assertEquals(expected, actualVal);
    }


    @Test
    public void should_returnTrue_countDownTextCorrect_largeValue() {
        Calculations calc = new Calculations();
        String expected = "01:58:22";
        String actualVal = calc.countDownText(7102000);
        assertEquals(expected, actualVal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void countDownText_lessThanZero_TimeGoal_ArgumentThrowsIAException() {
        Calculations calc = new Calculations();
        long val = -1;
        calc.countDownText(val);

    }

    @Test
    public void should_returnTrue_percentCorrectValue() {
        Calculations calc = new Calculations();
        tasksWithTimer.add(new Task("1", "task", 20, 120000, 2000, "24-05-2021"));
        String expected = "10";
        double perc = calc.percent(0, 1200000);

        String actualVal = String.format("%.0f", perc);
        assertEquals(expected, actualVal);
    }

    @Test
    public void should_returnTrue_percentIncorrectValue() {
        Calculations calc = new Calculations();
        tasksWithTimer.add(new Task("1", "task", 20, 120000, 2000, "24-05-2021"));
        String expected = "20";
        double perc = calc.percent(0, 1200000);
        String actualVal = String.format("%.0f", perc);
        assertNotEquals(expected, actualVal);
    }


    @Test(expected = IllegalArgumentException.class)
    public void percent_lessThanZero_TimeGoal_ArgumentThrowsIAException() {
        Calculations calc = new Calculations();
        tasksWithTimer.add(new Task("1", "task", 20, 120000, 2000, "24-05-2021"));
        long timeGoal = -1;
        calc.percent(0, timeGoal);
    }

}