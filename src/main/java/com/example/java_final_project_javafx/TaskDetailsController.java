package com.example.java_final_project_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.collections.FXCollections;

public class TaskDetailsController {

    @FXML
    private Label taskNameLabel; // Label to display the task name

    @FXML
    private Label startDateLabel; // Label to display the task start date

    @FXML
    private Label endDateLabel; // Label to display the task end date

    @FXML
    private Label progressLabel; // Label to display the task progress

    @FXML
    private Label commentLabel; // Label to display the task comment

    @FXML
    private ListView<SubTask> subTaskListView; // ListView to display the subtasks

    private Task task; // The task associated with this controller

    // Method to set the task and update the view with the task details
    public void setTask(Task task) {
        this.task = task;
        updateView();
    }

    // Method to update the view with the task details
    private void updateView() {
        if (task != null) {
            taskNameLabel.setText(task.getName()); // Set the task name label
            startDateLabel.setText(task.getStartDate().toString()); // Set the start date label
            endDateLabel.setText(task.getEndDate().toString()); // Set the end date label
            progressLabel.setText(task.calculateTotalProgress() + "%"); // Set the progress label
            commentLabel.setText(task.getComment()); // Set the comment label

//            subTaskListView.setItems(FXCollections.observableArrayList(task.getSubTasks()));
        }
    }



//    @FXML
//    protected void markSubTaskCompleted() {
//        SubTask selectedSubTask = subTaskListView.getSelectionModel().getSelectedItem();
//        if (selectedSubTask != null) {
//            selectedSubTask.setCompleted(true);
//            updateView();
//        }
//    }

//    @FXML
//    protected void addSubTask() {
////        用於增加子任務
//        TextInputDialog dialog = new TextInputDialog();
//        dialog.setTitle("Add SubTask");
//        dialog.setHeaderText(null);
//        dialog.setContentText("Enter subtask name:");
//
//        dialog.showAndWait().ifPresent(subTaskName -> {
//            SubTask newSubTask = new SubTask(subTaskName, 0);
//            task.addSubTask(newSubTask);
//            updateView();
//        });
//    }
}
