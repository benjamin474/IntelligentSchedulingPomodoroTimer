package com.example.java_final_project_javafx;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TaskListCell extends ListCell<Task> {
    private HBox content; // HBox layout to hold the cell content
    private Label nameLabel; // Label to display the task name
    private Label dateLabel; // Label to display the task date range
    private Label commentLabel; // Label to display the task comment
    private Label brLabel = new Label("Comment:\n"); // Label to indicate the comment section
    private ProgressBar progressBar; // Progress bar to display the task progress

    // Constructor to initialize the cell components
    public TaskListCell() {
        super();
        nameLabel = new Label();
        dateLabel = new Label();
        commentLabel = new Label();
        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        // VBox layout to hold labels
        VBox vBox = new VBox(nameLabel, dateLabel, brLabel, commentLabel);

        // Set the VBox to grow horizontally within the HBox
        HBox.setHgrow(vBox, Priority.ALWAYS);

        // Create HBox layout with the VBox and ProgressBar
        content = new HBox(vBox, progressBar);
        content.setSpacing(10);
    }

    public TaskListCell(String near) {
        super();
        nameLabel = new Label();
        dateLabel = new Label();
        commentLabel = new Label();
        progressBar = null;
//        progressBar.setMaxWidth(Double.MAX_VALUE);
        // VBox layout to hold labels
        VBox vBox = new VBox(nameLabel, dateLabel, brLabel, commentLabel);

        // Set the VBox to grow horizontally within the HBox
        HBox.setHgrow(vBox, Priority.ALWAYS);

        // Create HBox layout with the VBox and ProgressBar
        content = new HBox(vBox);
        content.setSpacing(10);
    }

    // Update the cell content based on the task item
    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty || task == null) {
            setGraphic(null); // If the item is empty, clear the cell
        } else {
            // Update the labels and progress bar with the task information
            nameLabel.setText(task.getName());
            dateLabel.setText(task.getStartDate() + " - " + task.getEndDate());
            commentLabel.setText(task.getComment());
            if(progressBar != null)
                progressBar.setProgress(task.calculateTotalProgress() / 100.0);
            setGraphic(content); // Set the HBox layout as the cell content
        }
    }
}
