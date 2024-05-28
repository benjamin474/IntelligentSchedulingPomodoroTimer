package com.example.java_final_project_javafx;

import java.io.Serializable;

public class SubTask implements Serializable {
    private String name;
    private boolean completed;
    private int progress;

    public SubTask(String name, int progress) {
        this.name = name;
        this.progress = progress;
        this.completed = false;
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

    @Override
    public String toString() {
        return name + ": " + progress + "%";
    }
}
