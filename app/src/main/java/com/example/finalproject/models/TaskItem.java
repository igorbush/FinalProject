package com.example.finalproject.models;

public class TaskItem {
    private String id;
    private String taskName;
    private long taskDateTime;
    private boolean complete;

    public TaskItem(String id, String taskName, long taskDateTime, boolean complete) {
        this.id = id;
        this.taskName = taskName;
        this.taskDateTime = taskDateTime;
        this.complete = complete;
    }

    public TaskItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String taskName) {
        this.id = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTaskDateTime() {
        return taskDateTime;
    }

    public void setTaskDateTime(long taskDateTime) {
        this.taskDateTime = taskDateTime;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
