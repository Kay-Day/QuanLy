package com.example.task;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task.db.DatabaseHelper;

public class ReportActivity extends AppCompatActivity {
    private TextView pendingTasksTextView, inProgressTasksTextView, completedTasksTextView, totalProgressTextView;
    private ProgressBar progressBar;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        pendingTasksTextView = findViewById(R.id.pendingTasksTextView);
        inProgressTasksTextView = findViewById(R.id.inProgressTasksTextView);
        completedTasksTextView = findViewById(R.id.completedTasksTextView);
        totalProgressTextView = findViewById(R.id.totalProgressTextView);
        progressBar = findViewById(R.id.progressBar);
        db = new DatabaseHelper(this);

        // Lấy số lượng task theo trạng thái
        int pendingCount = db.getTaskCountByStatus("Pending");
        int inProgressCount = db.getTaskCountByStatus("In Progress");
        int completedCount = db.getTaskCountByStatus("Completed");
        int totalCount = pendingCount + inProgressCount + completedCount;

        // Cập nhật số lượng task
        pendingTasksTextView.setText(getString(R.string.pending_tasks, pendingCount));
        inProgressTasksTextView.setText(getString(R.string.in_progress_tasks, inProgressCount));
        completedTasksTextView.setText(getString(R.string.completed_tasks, completedCount));

        // Tính toán và hiển thị tiến độ tổng quan
        if (totalCount > 0) {
            int progressPercentage = (completedCount * 100) / totalCount;
            totalProgressTextView.setText("Tiến Độ Tổng Quan: " + progressPercentage + "%");
            progressBar.setProgress(progressPercentage);
        } else {
            totalProgressTextView.setText("Tiến Độ Tổng Quan: 0%");
            progressBar.setProgress(0);
        }
    }
}