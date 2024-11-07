package com.todolist;

import java.time.LocalDate;

public class Task {
    private String description;
    private int priority;
    private boolean isCompleted;
    private LocalDate date;

    public Task(String description, int priority, boolean isCompleted, LocalDate date) {
        this.description = description;
        this.priority = priority;
        this.isCompleted = isCompleted;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return String.format("Task[description=%s, priority=%d, isCompleted=%s, date=%s]",
                description, priority, isCompleted, date);
    }
}
