package com.example.timemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ChildViewHolder> {

    private List<DayTasks> DayTasksList;


    TaskListAdapter(List<DayTasks> DayTasksList) {
        this.DayTasksList = DayTasksList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.taskinplanner, viewGroup, false);

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int position) {
        DayTasks DayTasks = DayTasksList.get(position);

        if (DayTasks.isChecked()) {
            childViewHolder.checkbox.setChecked(true);
        } else {
            childViewHolder.checkbox.setChecked(false);
        }

        DatabaseHelper db;
        db = new DatabaseHelper(childViewHolder.itemView.getContext());
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        childViewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (childViewHolder.checkbox.isChecked()) {

                    childViewHolder.checkbox.setChecked(true);
                    db.updateData(Integer.toString(DayTasks.getId()), DayTasks.getName(), 0, 0, days[Integer.parseInt(DayTasks.getDayNumber())], Integer.parseInt(DayTasks.getDayNumber()), "true", 0, "-");
                } else {
                    childViewHolder.checkbox.setChecked(false);
                    db.updateData(Integer.toString(DayTasks.getId()), DayTasks.getName(), 0, 0, days[Integer.parseInt(DayTasks.getDayNumber())], Integer.parseInt(DayTasks.getDayNumber()), "false", 0, "-");
                }
            }
        });

        childViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Task \" " + DayTasks.getName().trim() + "\"  has been deleted", Toast.LENGTH_SHORT).show();
                DayTasksList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount() - position);
                db.deleteRowId(Integer.toString(DayTasks.getId()));
            }
        });


        childViewHolder.DayTasksTitle.setText(DayTasks.getName());
        childViewHolder.id = DayTasks.getId();
        childViewHolder.position = position;
        childViewHolder.name = DayTasks.getName();
        childViewHolder.dayNumber = DayTasks.getDayNumber();
        childViewHolder.state = DayTasks.isChecked();

    }

    @Override
    public int getItemCount() {

        return DayTasksList.size();
    }

    class ChildViewHolder
            extends RecyclerView.ViewHolder {

        TextView DayTasksTitle;
        CheckBox checkbox;
        int position;
        int id;
        String name;
        String dayNumber;
        boolean state;
        ImageButton deleteButton;

        ChildViewHolder(View itemView) {
            super(itemView);
            DayTasksTitle = itemView.findViewById(R.id.taskInPlannerName);
            checkbox = itemView.findViewById(R.id.checkBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
