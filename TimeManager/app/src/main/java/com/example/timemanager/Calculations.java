package com.example.timemanager;

import java.util.Locale;

public class Calculations implements TaskHolder {

    public String timeConverter(long time) {

        if (time < 0) {
            throw new IllegalArgumentException("TimeLeft cannot be less than 0");
        }
        long hours = time / 3600000;
        String timeSpentHours = Long.toString(hours);
        long minutes = (time - (hours * 3600000)) / 60000;
        String timeSpentMin = Long.toString(minutes);
        long seconds = ((time - (hours * 3600000)) / 1000) % 60;
        String timeSpentSec = Long.toString(seconds);

        return timeSpentHours + " h " + timeSpentMin + " min " + timeSpentSec + " sec";

    }

    public String countDownText(long timeLeft) {
        if (timeLeft < 0) {
            throw new IllegalArgumentException("TimeLeft cannot be less than 0");
        }
        int hours = (int) (timeLeft / 3600000);
        int minutes = (int) ((timeLeft - (hours * 3600000)) / 1000) / 60;
        int seconds = (int) ((timeLeft - (hours * 3600000)) / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        return timeLeftFormatted;
    }

    public double percent(int position, long timeGoalInMillis) {
        if (timeGoalInMillis < 0) {
            throw new IllegalArgumentException("TimeGoal must be greater than 0");
        }
        double perc = (double) tasksWithTimer.get(position).getTimeSpent() / (double) timeGoalInMillis * 100;
        return perc;
    }

}
