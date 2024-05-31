package com.example.java_final_project_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.util.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TaskDialog {
    Stage dialog = new Stage();
    VBox dialogVbox = new VBox(20);
    TextField newTaskField = new TextField();
    DatePicker startDatePicker = new DatePicker();
    DatePicker endDatePicker = new DatePicker();
    TextField progressField = new TextField();
    Slider slider = new Slider();
    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    Button addButton = new Button("Add");
    NumberFormat format = NumberFormat.getInstance();
    MainController mainController;
    TaskDialog(MainController mainController) {
        // store the task list view
        this.mainController = mainController;
        
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Task");

        // set the default values for the new task
        newTaskField.setText("Task " + (mainController.getListViewSize(mainController.TASK) + 1));
        newTaskField.setPromptText("Enter new task");

        // set the default values for the new task
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setPromptText("Start date");

        // set the default values for the new task
        endDatePicker.setValue(LocalDate.now().plusDays(1));
        endDatePicker.setPromptText("End date");

        // set the default values for the new task
        progressField.setText("0");
        progressField.setPromptText("Completion (0 - 100%)");

        // set the slider's min, max, and initial value
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(0);
        slider.setBlockIncrement(1);
                
        // 將文字框的 textProperty 綁定到拉桿的 valueProperty，並使用 NumberFormat 來格式化數字
        IntegerProperty intSliderValue = new SimpleIntegerProperty(0);

        // 將滑桿的值綁定到整數屬性
        slider.valueProperty().addListener((obs, oldval, newVal) -> 
            intSliderValue.set(newVal.intValue())
        );

        // 將文字框的 textProperty 綁定到整數屬性，並使用 NumberFormat 來格式化數字
        progressField.textProperty().bind(Bindings.createStringBinding(() -> 
            format.format(intSliderValue.get()), intSliderValue)
        );

        // set the action for the save button
        addButton.setOnAction(event -> handleTask());
        cancelButton.setOnAction(event -> dialog.close());

        // set the action for the add button
        newTaskField.setOnAction(event -> handleTask());
        
        dialogVbox.getChildren().addAll(newTaskField, startDatePicker, endDatePicker, progressField, slider, addButton, cancelButton);
        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();

    }
    TaskDialog(Task task) {
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Task");
        newTaskField.setText(task.getName());
        startDatePicker.setValue(task.getStartDate());
        endDatePicker.setValue(task.getEndDate());
        // progressField.setText(NumberFormat.getInstance().format(task.getProgress()));
        // slider.setValue(task.getProgress());
        dialogVbox.getChildren().add(new Label("Task Name"));
        dialogVbox.getChildren().add(newTaskField);
        dialogVbox.getChildren().add(new Label("Start Date"));
        dialogVbox.getChildren().add(startDatePicker);
        dialogVbox.getChildren().add(new Label("End Date"));
        dialogVbox.getChildren().add(endDatePicker);
        dialogVbox.getChildren().add(new Label("Progress"));
        dialogVbox.getChildren().add(progressField);
        dialogVbox.getChildren().add(slider);
        dialogVbox.getChildren().add(saveButton);
        dialogVbox.getChildren().add(cancelButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void handleTask() {
        try {
            String newTaskName = newTaskField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer progress = progressField.getText().isEmpty() ? null : Integer.parseInt(progressField.getText());

            checkLegality(newTaskName, startDate, endDate, progress);

            Task newTask = new Task(newTaskName, startDate, endDate, progress);

            // 增加並儲存至文件
            mainController.storeToListView(newTask);

            mainController.taskStorage.saveTasksToFile(mainController.taskListView.getItems());
            dialog.close();
        } catch (IllegalArgumentException e) {
            new ErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            new ErrorDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }

    private void checkLegality(String taskName, LocalDate startDate, LocalDate endDate, Integer progress) {
        if (taskName.isEmpty()) {
            throw new IllegalArgumentException("Task cannot be empty");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot earlier than start date");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Please select start and end dates");
        }
        if (progress != null && (progress < 0 || progress > 100)) {
            throw new IllegalArgumentException("Completion must be between 0 and 100");
        }
    }
}
