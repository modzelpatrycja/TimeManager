package com.example.timemanager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SummaryActivity extends AppCompatActivity implements TaskHolder {

    private RecyclerView recView;
    private SummaryAdapter summaryAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        db = new DatabaseHelper(SummaryActivity.this);
        db.addToTaskArray(SummaryActivity.this);
        summaryView();

    }

    private void summaryView() {
        recView = findViewById(R.id.summRec);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recView.getContext(), layoutManager.getOrientation());
        recView.addItemDecoration(dividerItemDecoration);
        summaryAdapter = new SummaryAdapter(tasksWithTimer);
        recView.setAdapter(summaryAdapter);

    }
}
