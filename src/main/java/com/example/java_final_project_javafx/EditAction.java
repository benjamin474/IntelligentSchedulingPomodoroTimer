package com.example.java_final_project_javafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;

public class EditAction extends Action {
    @FXML
    protected void showEditTaskDialog() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showErrorDialog("Selection Error", "No task selected");
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        TextField editTaskField = new TextField(selectedTask.getName());
        editTaskField.setPromptText("Edit task");

//         起訖時間
        DatePicker startDatePicker = new DatePicker(selectedTask.getStartDate());
        DatePicker endDatePicker = new DatePicker(selectedTask.getEndDate());

//         判斷完成度
        TextField progressField = new TextField(selectedTask.getCompleted() != null ? String.valueOf(selectedTask.getCompleted()) : "");
        progressField.setPromptText("Completion (0-100%)");
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(0);
        slider.setBlockIncrement(1);

        NumberFormat format = NumberFormat.getIntegerInstance();

        // 雙向綁定 TextField 和 Slider
        progressField.textProperty().bindBidirectional(slider.valueProperty(), new StringConverter<Number>() {
            public String toString(Number object) {
                return format.format(object);
            }

            public Number fromString(String string) {
                try {
                    return format.parse(string);
                } catch (ParseException e) {
                    return 0;
                }
            }
        });

//        儲存修改
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> handleEditTask(editTaskField, startDatePicker, endDatePicker, progressField, dialog, selectedTask));

        editTaskField.setOnAction(event -> handleEditTask(editTaskField, startDatePicker, endDatePicker, progressField, dialog, selectedTask));

        dialogVbox.getChildren().addAll(editTaskField, startDatePicker, endDatePicker, progressField, saveButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    private void handleEditTask(TextField editTaskField, DatePicker startDatePicker, DatePicker endDatePicker, TextField progressField, Stage dialog, Task selectedTask) {
        try {
            String editedTaskName = editTaskField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer progress = progressField.getText().isEmpty() ? null : Integer.parseInt(progressField.getText());

            checkLegality(editedTaskName, startDate, endDate, progress);


            selectedTask.setName(editedTaskName);
            selectedTask.setStartDate(startDate);
            selectedTask.setEndDate(endDate);
            selectedTask.setCompleted(progress);

            taskListView.refresh();
            finishedListView.refresh();
            // 儲存任務
            if (progress == 100) {
                deleteTaskElement();
                finishedListView.getItems().add(selectedTask);
            }
            taskStorage.saveTasksToFile(taskListView.getItems());
            dialog.close();
        } catch (IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }
}
