package com.example.java_final_project_javafx;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TaskListCell extends ListCell<Task> {
    private HBox content;
    private Label nameLabel;
    private Label dateLabel;
    private Label commentLabel;
    private Label brLabel = new Label("Comment:\n");
    private ProgressBar progressBar;

    public TaskListCell() {
        super();
        nameLabel = new Label();
        dateLabel = new Label();
        commentLabel = new Label();
        progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        VBox vBox = new VBox(nameLabel, dateLabel, brLabel, commentLabel);

        HBox.setHgrow(vBox, Priority.ALWAYS);

        content = new HBox(vBox, progressBar);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty || task == null) {
            setGraphic(null);
        } else {
            nameLabel.setText(task.getName());
            dateLabel.setText(task.getStartDate() + " - " + task.getEndDate());
            commentLabel.setText(task.getComment());
            progressBar.setProgress(task.calculateTotalProgress() / 100.0);
            setGraphic(content);
        }
    }
}
