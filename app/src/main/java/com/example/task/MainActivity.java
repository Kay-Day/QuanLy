package com.example.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task.adapter.TaskAdapter;
import com.example.task.db.DatabaseHelper;
import com.example.task.model.Task;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        Button addTaskButton = findViewById(R.id.addTaskButton);
        Button notificationsButton = findViewById(R.id.notificationsButton);
        Button documentsButton = findViewById(R.id.documentsButton);
        Button reportButton = findViewById(R.id.reportButton);
        Button logoutButton = findViewById(R.id.logoutButton);

        db = new DatabaseHelper(this);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        loadTasks();

        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, 1);
        });

        notificationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });

        documentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DocumentsActivity.class);
            startActivity(intent);
        });

        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            // Xóa trạng thái đăng nhập
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", false);
            editor.remove("username");
            editor.apply();

            // Chuyển về màn hình LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadTasks() {
        taskList.clear();
        taskList.addAll(db.getAllTasks());
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == RESULT_OK) {
                loadTasks();
            }
        }
    }

    @Override
    public void onEditClick(Task task) {
        Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
        intent.putExtra("task_id", task.getId());
        startActivityForResult(intent, 2);
    }

    @Override
    public void onDeleteClick(Task task) {
        db.deleteTask(task.getId());
        loadTasks();
    }
}