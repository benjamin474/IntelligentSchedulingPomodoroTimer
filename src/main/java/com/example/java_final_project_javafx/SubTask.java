package com.example.java_final_project_javafx;

import java.io.Serializable;

public class SubTask implements Serializable {
    private String name; // Name of the subtask
    private int progress; // Progress percentage of the subtask
    private boolean completed; // Progress percentage of the subtask

    // Constructor to initialize the subtask with a name and initial progress
    public SubTask(String name, int progress) {
        this.name = name;
        this.progress = progress;
        this.completed = false; // By default, the subtask is not completed
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    // Override the toString method to display the subtask name and progress percentage
    @Override
    public String toString() {
        return name + ": " + progress + "%";
    }
}
