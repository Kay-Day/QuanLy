package com.example.task;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task.db.DatabaseHelper;

public class ReportActivity extends AppCompatActivity {
    private TextView pendingTasksTextView, inProgressTasksTextView, completedTasksTextView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        pendingTasksTextView = findViewById(R.id.pendingTasksTextView);
        inProgressTasksTextView = findViewById(R.id.inProgressTasksTextView);
        completedTasksTextView = findViewById(R.id.completedTasksTextView);
        db = new DatabaseHelper(this);

        int pendingCount = db.getTaskCountByStatus("Pending");
        int inProgressCount = db.getTaskCountByStatus("In Progress");
        int completedCount = db.getTaskCountByStatus("Completed");

        pendingTasksTextView.setText("Pending Tasks: " + pendingCount);
        inProgressTasksTextView.setText("In Progress Tasks: " + inProgressCount);
        completedTasksTextView.setText("Completed Tasks: " + completedCount);
    }
}