package com.example.java_final_project_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.collections.FXCollections;

public class TaskDetailsController {

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label startDateLabel;

    @FXML
    private Label endDateLabel;

    @FXML
    private Label progressLabel;

    @FXML
    private Label commentLabel;

    @FXML
    private ListView<SubTask> subTaskListView;

    private Task task;

    public void setTask(Task task) {
        this.task = task;
        updateView();
    }

    private void updateView() {
        if (task != null) {
            taskNameLabel.setText(task.getName());
            startDateLabel.setText(task.getStartDate().toString());
            endDateLabel.setText(task.getEndDate().toString());
            progressLabel.setText(task.calculateTotalProgress() + "%");
            commentLabel.setText(task.getComment());

            subTaskListView.setItems(FXCollections.observableArrayList(task.getSubTasks()));
        }
    }

    @FXML
    protected void markSubTaskCompleted() {
        SubTask selectedSubTask = subTaskListView.getSelectionModel().getSelectedItem();
        if (selectedSubTask != null) {
            selectedSubTask.setCompleted(true);
            updateView();
        }
    }

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
