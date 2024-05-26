package com.example.java_final_project_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    @FXML
    private Label timerLabel;

    @FXML
    private TextField taskField;

    @FXML
    private TextField durationField;

    @FXML
    private ListView<String> taskListView;

    @FXML
    private ListView<String> workflowListView;

    private Timer timer;
    private int timeRemaining;

    @FXML
    protected void startTimer() {
        String durationText = durationField.getText();
        try {
            int duration = Integer.parseInt(durationText) * 60;
            timeRemaining = duration;
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeRemaining--;
                    int minutes = timeRemaining / 60;
                    int seconds = timeRemaining % 60;
                    timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                    if (timeRemaining <= 0) {
                        timer.cancel();
                    }
                }
            }, 0, 1000);
        } catch (NumberFormatException e) {
            // Handle invalid input
        }
    }

    @FXML
    protected void showAddTaskDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        TextField newTaskField = new TextField();
        newTaskField.setPromptText("Enter new task");
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> {
            String newTask = newTaskField.getText();
            if (!newTask.isEmpty()) {
                taskListView.getItems().add(newTask);
                dialog.close();
            }
        });
        dialogVbox.getChildren().addAll(newTaskField, addButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
