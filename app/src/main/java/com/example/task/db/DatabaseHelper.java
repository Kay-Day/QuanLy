package com.example.task.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.task.model.Document;
import com.example.task.model.Notification;
import com.example.task.model.Task;
import com.example.task.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TaskManager.db";
    private static final int DATABASE_VERSION = 2; // Tăng version để upgrade database

    // Bảng Tasks
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ASSIGNED_TO = "assigned_to";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DEADLINE = "deadline"; // Thêm trường deadline
    private static final String COLUMN_PRIORITY = "priority"; // Thêm trường priority

    // Bảng Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Bảng Notifications
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_NOTIFICATION_ID = "notification_id";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Bảng Documents
    private static final String TABLE_DOCUMENTS = "documents";
    private static final String COLUMN_DOCUMENT_ID = "document_id";
    private static final String COLUMN_DOCUMENT_NAME = "document_name";
    private static final String COLUMN_DOCUMENT_PATH = "document_path";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Tasks
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_ASSIGNED_TO + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_DEADLINE + " TEXT,"
                + COLUMN_PRIORITY + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);

        // Tạo bảng Users
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Tạo bảng Notifications
        String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                + COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MESSAGE + " TEXT,"
                + COLUMN_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_NOTIFICATIONS_TABLE);

        // Tạo bảng Documents
        String CREATE_DOCUMENTS_TABLE = "CREATE TABLE " + TABLE_DOCUMENTS + "("
                + COLUMN_DOCUMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DOCUMENT_NAME + " TEXT,"
                + COLUMN_DOCUMENT_PATH + " TEXT" + ")";
        db.execSQL(CREATE_DOCUMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Thêm các cột mới vào bảng tasks
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_DEADLINE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_PRIORITY + " TEXT");

            // Tạo các bảng mới
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT UNIQUE,"
                    + COLUMN_PASSWORD + " TEXT" + ")";
            db.execSQL(CREATE_USERS_TABLE);

            String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                    + COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_MESSAGE + " TEXT,"
                    + COLUMN_TIMESTAMP + " TEXT" + ")";
            db.execSQL(CREATE_NOTIFICATIONS_TABLE);

            String CREATE_DOCUMENTS_TABLE = "CREATE TABLE " + TABLE_DOCUMENTS + "("
                    + COLUMN_DOCUMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DOCUMENT_NAME + " TEXT,"
                    + COLUMN_DOCUMENT_PATH + " TEXT" + ")";
            db.execSQL(CREATE_DOCUMENTS_TABLE);
        }
    }

    // --- Quản lý Users ---
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
            return user;
        }
        return null;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // --- Quản lý Tasks ---
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_ASSIGNED_TO, task.getAssignedTo());
        values.put(COLUMN_STATUS, task.getStatus());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_PRIORITY, task.getPriority());
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                task.setAssignedTo(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNED_TO)));
                task.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                task.setDeadline(cursor.getString(cursor.getColumnIndex(COLUMN_DEADLINE)));
                task.setPriority(cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_ASSIGNED_TO, task.getAssignedTo());
        values.put(COLUMN_STATUS, task.getStatus());
        values.put(COLUMN_DEADLINE, task.getDeadline());
        values.put(COLUMN_PRIORITY, task.getPriority());
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    @SuppressLint("Range")
    public Task getTaskById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
                        COLUMN_ASSIGNED_TO, COLUMN_STATUS, COLUMN_DEADLINE, COLUMN_PRIORITY},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Task task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            task.setAssignedTo(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNED_TO)));
            task.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
            task.setDeadline(cursor.getString(cursor.getColumnIndex(COLUMN_DEADLINE)));
            task.setPriority(cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY)));
            cursor.close();
            return task;
        }
        return null;
    }

    // --- Quản lý Notifications ---
    public void addNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, notification.getMessage());
        values.put(COLUMN_TIMESTAMP, notification.getTimestamp());
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<Notification> getAllNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification();
                notification.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTIFICATION_ID)));
                notification.setMessage(cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE)));
                notification.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                notificationList.add(notification);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notificationList;
    }

    // --- Quản lý Documents ---
    public void addDocument(Document document) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCUMENT_NAME, document.getName());
        values.put(COLUMN_DOCUMENT_PATH, document.getPath());
        db.insert(TABLE_DOCUMENTS, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    @SuppressLint("Range")
    public List<Document> getAllDocuments() {
        List<Document> documentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DOCUMENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Document document = new Document();
                document.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_DOCUMENT_ID)));
                document.setName(cursor.getString(cursor.getColumnIndex(COLUMN_DOCUMENT_NAME)));
                document.setPath(cursor.getString(cursor.getColumnIndex(COLUMN_DOCUMENT_PATH)));
                documentList.add(document);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return documentList;
    }

    // --- Báo cáo tổng quan ---
    public int getTaskCountByStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[]{COLUMN_ID},
                COLUMN_STATUS + "=?", new String[]{status}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}