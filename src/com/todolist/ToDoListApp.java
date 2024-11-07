package com.todolist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ToDoListApp extends JFrame {
    private TaskManager taskManager;
    private LocalDate selectedDate;
    private JLabel dateLabel;
    private JList<Task> taskList;
    private DefaultListModel<Task> taskListModel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton completeButton;
    private JButton historyButton;

    public ToDoListApp() {
        taskManager = new TaskManager();
        selectedDate = LocalDate.now();

        setTitle("To-Do List Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        dateLabel = new JLabel("Date: " + selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                JLabel.CENTER);
        JButton previousDayButton = new JButton("<");
        JButton nextDayButton = new JButton(">");

        previousDayButton.addActionListener(e -> changeDate(selectedDate.minusDays(1)));
        nextDayButton.addActionListener(e -> changeDate(selectedDate.plusDays(1)));

        JPanel datePanel = new JPanel();
        datePanel.add(previousDayButton);
        datePanel.add(dateLabel);
        datePanel.add(nextDayButton);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");
        completeButton = new JButton("Mark as Completed");
        historyButton = new JButton("View History");

        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        completeButton.addActionListener(e -> markTaskCompleted());
        historyButton.addActionListener(e -> viewHistory());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(historyButton);

        add(datePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadTasksForDate();
        updateButtonState();

        setVisible(true);
    }

    private void changeDate(LocalDate newDate) {
        selectedDate = newDate;
        dateLabel.setText("Date: " + selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        loadTasksForDate();
        updateButtonState();
    }

    private void loadTasksForDate() {
        taskListModel.clear();
        taskManager.loadTasks(selectedDate);
        for (Task task : taskManager.getTasksForDate(selectedDate)) {
            taskListModel.addElement(task);
        }
    }

    private void addTask() {
        String description = JOptionPane.showInputDialog("Enter task description:");
        if (description != null && !description.trim().isEmpty()) {
            String priorityStr = JOptionPane.showInputDialog("Enter task priority (1 and above):");
            int priority;
            try {
                priority = Integer.parseInt(priorityStr);
                if (priority < 1)
                    throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid priority. Please enter a number 1 and above.");
                return;
            }
            taskManager.addTask(description, priority, selectedDate);
            loadTasksForDate();
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskManager.deleteTask(selectedIndex, selectedDate);
            loadTasksForDate();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to delete.");
        }
    }

    private void markTaskCompleted() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskManager.markTaskCompleted(selectedIndex, selectedDate);
            loadTasksForDate();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to mark as completed.");
        }
    }

    private void viewHistory() {
        LocalDate historyDate = LocalDate.parse(JOptionPane.showInputDialog("Enter date (yyyy-MM-dd):"));
        if (historyDate.isBefore(LocalDate.now())) {
            taskManager.loadTasks(historyDate);
            StringBuilder history = new StringBuilder(
                    "Tasks for " + historyDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ":\n");
            for (Task task : taskManager.getTasksForDate(historyDate)) {
                history.append(task.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, history.toString(), "Task History", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Cannot view future tasks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateButtonState() {
        boolean isPastDate = selectedDate.isBefore(LocalDate.now());
        addButton.setEnabled(!isPastDate);
        deleteButton.setEnabled(!isPastDate);
        completeButton.setEnabled(!isPastDate);
    }

    public static void main(String[] args) {
        new ToDoListApp();
    }
}
