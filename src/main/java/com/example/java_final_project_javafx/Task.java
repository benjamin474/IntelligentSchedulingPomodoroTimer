package com.example.java_final_project_javafx;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer completed; // Allow null to indicate not manually set
    private String comment;
    private List<SubTask> subTasks;

    public Task(String name, LocalDate startDate, LocalDate endDate, Integer completed) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.comment = "";
    }

    public Task(String name, LocalDate startDate, LocalDate endDate, Integer completed, String comment) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.comment = comment;
//        this.subTasks = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
    }

    public int calculateTotalProgress() {
        // 檢查子任務狀況
        if (completed != null) {
            return completed;
        }
        if (subTasks.isEmpty()) {
            return 0;
        }
        int totalProgress = 0;
        for (SubTask subTask : subTasks) {
            totalProgress += subTask.isCompleted() ? subTask.getProgress() : 0;
        }
        return totalProgress / subTasks.size();
    }

    @Override
    public String toString() {
        return name;
    }
}
