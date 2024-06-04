package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;

import java.text.NumberFormat;
import java.time.LocalDate;

public class TaskDialog {

    // create the dialog
    Stage dialog = new Stage();

    // Layout containers
    VBox dialogVbox = new VBox(20);

    HBox buttonBox = new HBox(15); // create a new HBox with 10px spacing
    HBox startBox = new HBox(10);
    HBox endBox = new HBox(10);

    // Input fields for task details
    TextField newTaskField = new TextField();
    TextField progressField = new TextField();
    TextField commentField = new TextField();

    // Date pickers for start and end dates
    DatePicker startDatePicker = new DatePicker();
    DatePicker endDatePicker = new DatePicker();

    // Slider for progress
    Slider slider = new Slider();

    // Buttons for canceling and adding the task
    Button cancelButton = new Button("Cancel");
    Button addButton = new Button("Add");

    // Number format for the slider
    NumberFormat format = NumberFormat.getInstance();

    // Reference to the main controller
    MainController mainController;

    // Labels for task details
    private Label taskLabel = new Label("Task Name");
    private Label startLabel = new Label("Start Date");
    private Label endLabel = new Label("End Date");
    private Label progressLabel = new Label("Progress");
    private Label commentLabel = new Label("Comment");

    // Constructor for adding a new task
    TaskDialog(MainController mainController) {
        // store the task list view
        this.mainController = mainController;

        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Task");

        // Set default values and prompts for input fields
        newTaskField.setText("Task " + (mainController.getListViewSize(mainController.TASK) + 1));
        newTaskField.setPromptText("Enter new task");


        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setPromptText("Start date");


        endDatePicker.setValue(LocalDate.now().plusDays(1));
        endDatePicker.setPromptText("End date");


        progressField.setText("0");
        progressField.setPromptText("Completion (0 - 100%)");

        //set the default values for the
        commentField.setText("");
        commentField.setPromptText("Enter comment");

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

        buttonBox.setAlignment(Pos.CENTER); // align the buttons to the center of the HBox
    }

    // Constructor for editing an existing task
    TaskDialog(MainController mainController, Task task) {
        // store the task list view
        this(mainController);

        // initialize the dialog
        dialog.setTitle("Edit Task");
        newTaskField.setText(task.getName());
        startDatePicker.setValue(task.getStartDate());
        endDatePicker.setValue(task.getEndDate());
        commentField.setText(task.getComment());
        slider.setValue(task.getCompleted());

        // set the action for the save button
        addButton.setText("Save");

        addButton.setOnAction(event -> handleEditTask(task));

    }

    // Show the dialog
    public void show() {
        buttonBox.getChildren().addAll(addButton, cancelButton);
        dialogVbox.getChildren().addAll(
                new VBox(5, taskLabel, newTaskField),
                new VBox(5, startLabel, startDatePicker),
                new VBox(5, endLabel, endDatePicker),
                new VBox(10, progressLabel, progressField, slider),
                new VBox(5, commentLabel, commentField),
                buttonBox
        );
        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    // Handle adding a new task
    private void handleTask() {
        try {
            String newTaskName = newTaskField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer progress = progressField.getText().isEmpty() ? null : Integer.parseInt(progressField.getText());
            String comment = commentField.getText();

            checkLegality(newTaskName, startDate, endDate, progress);

            Task newTask = new Task(newTaskName, startDate, endDate, progress, comment);

            // add the new task to the list view
            mainController.storeToListView(newTask);

            dialog.close();
        } catch (IllegalArgumentException e) {
            new MessageDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            new MessageDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handle editing an existing task
    private void handleEditTask(Task selectedTask) {
        try {
            String editedTaskName = newTaskField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer progress = progressField.getText().isEmpty() ? null : Integer.parseInt(progressField.getText());
            String comment = commentField.getText();

            checkLegality(editedTaskName, startDate, endDate, progress);

            // Update the selected task
            selectedTask.setName(editedTaskName);
            selectedTask.setStartDate(startDate);
            selectedTask.setEndDate(endDate);
            selectedTask.setCompleted(progress);
            selectedTask.setComment(comment);

            mainController.saveTasksToFile();

            dialog.close();
        } catch (IllegalArgumentException e) {
            new MessageDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            new MessageDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Validate the input data
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
