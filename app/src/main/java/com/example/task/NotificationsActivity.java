package com.example.task;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task.adapter.NotificationAdapter;
import com.example.task.db.DatabaseHelper;
import com.example.task.model.Notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        db = new DatabaseHelper(this);
        notificationList = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(notificationList);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationRecyclerView.setAdapter(notificationAdapter);

        loadNotifications();
    }

    private void loadNotifications() {
        notificationList.clear();
        notificationList.addAll(db.getAllNotifications());
        notificationAdapter.notifyDataSetChanged();
    }
}