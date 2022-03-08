package com.example.timemanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {
    ArrayList<Task> tasksWithTimer;

    public SummaryAdapter(ArrayList<Task> tasksWithTimer) {
        this.tasksWithTimer = tasksWithTimer;
    }

    public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View dayView = LayoutInflater.from(parent.getContext()).inflate(R.layout.summ, parent, false);
        SummaryViewHolder viewH = new SummaryViewHolder(dayView);
        return viewH;
    }

    @Override
    public void onBindViewHolder(SummaryViewHolder holder, final int position) {
        holder.taskName.setText(tasksWithTimer.get(position).getName());

        String taskGoal;
        long timeGoalInMillis;
        timeGoalInMillis = tasksWithTimer.get(position).getTimeGoal() * 60000;
        if (tasksWithTimer.get(position).getTimeGoal() > 50) {
            taskGoal = tasksWithTimer.get(position).getTimeGoal()/60 + "h";
        } else {
            taskGoal = tasksWithTimer.get(position).getTimeGoal() + "min";
        }
        holder.goal.setText(taskGoal);

        holder.timeSpent.setText(timeConverter(tasksWithTimer.get(position).getTimeSpent()));
        holder.totalTime.setText(timeConverter(tasksWithTimer.get(position).getTotalTime()));
        Calculations calc = new Calculations();
        double perc = calc.percent(position, timeGoalInMillis);
        String percS = String.format("%.0f", perc);
        holder.percentage.setText(percS + "%");
        if (perc > 100) {
            holder.percentage.setTextColor(Color.parseColor("#008000"));
        }

    }

    @Override
    public int getItemCount() {
        return tasksWithTimer.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView goal;
        TextView timeSpent;
        TextView percentage;
        TextView totalTime;

        public SummaryViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.name_of_task);
            goal = view.findViewById(R.id.goal);
            timeSpent = view.findViewById(R.id.time_spent);
            percentage = view.findViewById(R.id.percentage);
            totalTime = view.findViewById(R.id.totalTime);

        }
    }

    private String timeConverter(long time) {
        String timeSpentHours = Long.toString(time / 3600000);
        String timeSpentMin = Long.toString(time / 60000);
        String timeSpentSec = Long.toString(time / 1000 % 60);
        return timeSpentHours + "h " + timeSpentMin + "min " + timeSpentSec + "sec";
    }
}