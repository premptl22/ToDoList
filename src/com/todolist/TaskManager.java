package com.todolist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Map<LocalDate, List<Task>> tasksByDate;

    public TaskManager() {
        tasksByDate = new HashMap<>();
    }

    public void loadTasks(LocalDate date) {
        tasksByDate.putIfAbsent(date, new ArrayList<>());
    }

    public List<Task> getTasks() {
        LocalDate date = LocalDate.now(); // Adjust this to get the correct date from context
        return tasksByDate.getOrDefault(date, new ArrayList<>());
    }

    public List<Task> getTasksForDate(LocalDate date) {
        return tasksByDate.getOrDefault(date, new ArrayList<>());
    }

    public void addTask(String description, int priority, LocalDate date) {
        tasksByDate.putIfAbsent(date, new ArrayList<>());
        tasksByDate.get(date).add(new Task(description, priority, false, date));
    }

    public void deleteTask(int index, LocalDate date) {
        if (tasksByDate.containsKey(date)) {
            List<Task> tasks = tasksByDate.get(date);
            if (index >= 0 && index < tasks.size()) {
                tasks.remove(index);
            }
        }
    }

    public void markTaskCompleted(int index, LocalDate date) {
        if (tasksByDate.containsKey(date)) {
            List<Task> tasks = tasksByDate.get(date);
            if (index >= 0 && index < tasks.size()) {
                Task task = tasks.get(index);
                task.setCompleted(true);
            }
        }
    }
}
