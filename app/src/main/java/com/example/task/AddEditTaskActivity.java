package com.example.task;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task.db.DatabaseHelper;
import com.example.task.model.Notification;
import com.example.task.model.Task;
import com.example.task.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, statusEditText, deadlineEditText, priorityEditText;
    private Spinner assignedToSpinner;
    private Button saveButton;
    private DatabaseHelper db;
    private Task task;
    private boolean isEditMode = false;
    private List<User> userList;
    private List<String> usernameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        assignedToSpinner = findViewById(R.id.assignedToSpinner);
        statusEditText = findViewById(R.id.statusEditText);
        deadlineEditText = findViewById(R.id.deadlineEditText);
        priorityEditText = findViewById(R.id.priorityEditText);
        saveButton = findViewById(R.id.saveButton);

        db = new DatabaseHelper(this);

        // Lấy danh sách người dùng
        userList = db.getAllUsers();
        usernameList = new ArrayList<>();
        for (User user : userList) {
            usernameList.add(user.getUsername());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usernameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assignedToSpinner.setAdapter(adapter);

        if (getIntent().hasExtra("task_id")) {
            isEditMode = true;
            int taskId = getIntent().getIntExtra("task_id", -1);
            task = db.getTaskById(taskId);
            if (task != null) {
                titleEditText.setText(task.getTitle());
                descriptionEditText.setText(task.getDescription());
                int position = usernameList.indexOf(task.getAssignedTo());
                if (position >= 0) {
                    assignedToSpinner.setSelection(position);
                }
                statusEditText.setText(task.getStatus());
                deadlineEditText.setText(task.getDeadline());
                priorityEditText.setText(task.getPriority());
            }
        }

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String assignedTo = assignedToSpinner.getSelectedItem().toString();
            String status = statusEditText.getText().toString();
            String deadline = deadlineEditText.getText().toString();
            String priority = priorityEditText.getText().toString();

            if (title.isEmpty() || assignedTo.isEmpty() || status.isEmpty() || deadline.isEmpty() || priority.isEmpty()) {
                return;
            }

            if (isEditMode) {
                task.setTitle(title);
                task.setDescription(description);
                task.setAssignedTo(assignedTo);
                task.setStatus(status);
                task.setDeadline(deadline);
                task.setPriority(priority);
                db.updateTask(task);

                // Tạo thông báo khi chỉnh sửa công việc
                Notification notification = new Notification();
                notification.setMessage("Task updated: " + title);
                notification.setTimestamp(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                db.addNotification(notification);
            } else {
                Task newTask = new Task(title, description, assignedTo, status, deadline, priority);
                db.addTask(newTask);

                // Tạo thông báo khi thêm công việc mới
                Notification notification = new Notification();
                notification.setMessage("New task added: " + title);
                notification.setTimestamp(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                db.addNotification(notification);
            }

            setResult(RESULT_OK);
            finish();
        });
    }
}