package com.example.timemanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ParentViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<DayModel> itemList;

    RecAdapter(List<DayModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.daylayout, viewGroup, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder parentViewHolder, int position) {

        DayModel DayModel = itemList.get(position);
        parentViewHolder.DayModelTitle.setText(DayModel.getName());
        parentViewHolder.dayNumber = position;

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                parentViewHolder.ChildRecyclerView.getContext());

        layoutManager.setInitialPrefetchItemCount(
                DayModel.getChildItemList().size());

        TaskListAdapter childItemAdapter = new TaskListAdapter(
                DayModel.getChildItemList());
        parentViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        parentViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);

        parentViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    class ParentViewHolder
            extends RecyclerView.ViewHolder {

        private TextView DayModelTitle;
        private RecyclerView ChildRecyclerView;
        private int dayNumber;

        ParentViewHolder(final View itemView) {
            super(itemView);

            DayModelTitle = itemView.findViewById(R.id.dayName);
            ChildRecyclerView = itemView.findViewById(R.id.tasksRecyclerView);

            itemView.findViewById(R.id.addTaskButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddTaskToPlannerActivity.class);
                    intent.putExtra("day", DayModelTitle.getText());
                    intent.putExtra("dayNumber", Integer.toString(dayNumber));
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}