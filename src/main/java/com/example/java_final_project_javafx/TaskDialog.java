package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.text.NumberFormat;
import java.time.LocalDate;

public class TaskDialog {
    // create the dialog
    Stage dialog = new Stage();

    VBox dialogVbox = new VBox(20);

    TextField newTaskField = new TextField();
    TextField progressField = new TextField();

    DatePicker startDatePicker = new DatePicker();
    DatePicker endDatePicker = new DatePicker();
    
    Slider slider = new Slider();

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
                
        // tie the slider's value to an integer property
        IntegerProperty intSliderValue = new SimpleIntegerProperty(0);

        // let the slider value be updated by the text field
        slider.valueProperty().addListener((obs, oldval, newVal) -> 
            intSliderValue.set(newVal.intValue())
        );

        // let the text field be updated by the slider value
        progressField.textProperty().bind(Bindings.createStringBinding(() -> 
            format.format(intSliderValue.get()), intSliderValue)
        );

        // set the action for the save button
        addButton.setOnAction(event -> handleTask());
        cancelButton.setOnAction(event -> dialog.close());

        // set the action for the add button
        newTaskField.setOnAction(event -> handleTask());
    }
    TaskDialog(MainController mainController, Task task) {
        // store the task list view
        this(mainController);
        
        // initialize the dialog
        dialog.setTitle("Edit Task");
        newTaskField.setText(task.getName());
        startDatePicker.setValue(task.getStartDate());
        endDatePicker.setValue(task.getEndDate());
        slider.setValue(task.getCompleted());

        // set the action for the save button
        addButton.setText("Save");

        addButton.setOnAction(event -> handleEditTask(task));

    }

    public void show() {
        dialogVbox.getChildren().addAll(newTaskField, startDatePicker, endDatePicker, progressField, slider, addButton, cancelButton);
        // set the scene
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

            // add the new task to the list view
            mainController.storeToListView(newTask);

            dialog.close();
        } catch (IllegalArgumentException e) {
            new ErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            new ErrorDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }

    private void handleEditTask(Task selectedTask) {
        try {
            String editedTaskName = newTaskField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer progress = progressField.getText().isEmpty() ? null : Integer.parseInt(progressField.getText());

            checkLegality(editedTaskName, startDate, endDate, progress);

            selectedTask.setName(editedTaskName);
            selectedTask.setStartDate(startDate);
            selectedTask.setEndDate(endDate);
            selectedTask.setCompleted(progress);
            
            mainController.saveTasksToFile();

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
