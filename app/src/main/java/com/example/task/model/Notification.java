package com.example.task.model;

public class Notification {
    private int id;
    private String message;
    private String timestamp;

    public Notification() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}