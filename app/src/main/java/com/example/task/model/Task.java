package com.example.task.model;

public class Task {
    private int id;
    private String title;
    private String description;
    private String assignedTo;
    private String status;
    private String deadline;
    private String priority;

    public Task() {}

    public Task(String title, String description, String assignedTo, String status, String deadline, String priority) {
        this.title = title;
        this.description = description;
        this.assignedTo = assignedTo;
        this.status = status;
        this.deadline = deadline;
        this.priority = priority;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}