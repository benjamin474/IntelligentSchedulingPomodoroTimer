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

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
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
    private ListView<Task> taskListView;

    //    @FXML
//    private ListView<String> workflowListView;
    @FXML
    private ListView<Task> finishedListView;

    private Timer timer;
    private int timeRemaining;
    public final int TASK = 1;
    public final int FINISH = 2;

    private TaskStorage taskStorage = new TaskStorage();

    @FXML
    public void initialize() {
        taskListView.setItems(FXCollections.observableArrayList());
        taskListView.setCellFactory(param -> new TaskListCell());
        taskStorage.loadTasksFromFile(taskListView.getItems());
        durationField.setText("25");
    }

    @FXML
    protected void startTimer() {
        String durationText = durationField.getText();

        try {
            int duration = Integer.parseInt(durationText) * 60;
            if (duration <= 0)
                showErrorDialog("Input Error", "Please enter a positive number");

            timeRemaining = duration;
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // JavaFX UI操作需要在JavaFX Application Thread中執行
                    javafx.application.Platform.runLater(() -> {
                        timeRemaining--;
                        int minutes = timeRemaining / 60;
                        int seconds = timeRemaining % 60;
                        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                        if (timeRemaining <= 0) {
                            timer.cancel();
                        }
                    });
                }
            }, 0, 1000);
        } catch (NumberFormatException e) {
            showErrorDialog("Input Error", "Please enter a valid number");
        }
    }

    @FXML
    protected void showAddTaskDialog() {
        // 新增頁面初始化
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);

        // 輸入任務名稱
        TextField newTaskField = new TextField();
        newTaskField.setText("Task " + (taskListView.getItems().size() + 1));
        newTaskField.setPromptText("Enter new task");

        // 開始日期
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setPromptText("Start date");

        // 結束日期
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setValue(LocalDate.now());
        endDatePicker.setPromptText("End date");

        // 結束日期早於開始日期會反紅
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // 輸入完成度
        TextField progressField = new TextField();
        progressField.setText("0");
        progressField.setPromptText("Completion (0 - 100%)");


        Button addButton = new Button("Add");
        addButton.setOnAction(event -> handleAddTask(newTaskField, startDatePicker, endDatePicker, progressField, dialog));

        newTaskField.setOnAction(event -> handleAddTask(newTaskField, startDatePicker, endDatePicker, progressField, dialog));

        dialogVbox.getChildren().addAll(newTaskField, startDatePicker, endDatePicker, progressField, addButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void handleAddTask(TextField newTaskField, DatePicker startDatePicker, DatePicker endDatePicker, TextField progressField, Stage dialog) {
        try {
            String newTaskName = newTaskField.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer progress = progressField.getText().isEmpty() ? null : Integer.parseInt(progressField.getText());

            checkLegality(newTaskName, startDate, endDate, progress);

            Task newTask = new Task(newTaskName, startDate, endDate, progress);

            // 增加並儲存至文件
            storeToListView(newTask);

            taskStorage.saveTasksToFile(taskListView.getItems());
            dialog.close();
        } catch (IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }


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
            }
            deleteTaskElement();
            storeToListView(selectedTask);
            taskStorage.saveTasksToFile(taskListView.getItems());
            dialog.close();
        } catch (IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Unexpected Error", "An unexpected error occurred. Please try again.");
        }
    }

    private void storeToListView(Task newTask) {
        if (newTask.getCompleted() < 100) {
            taskListView.getItems().add(newTask);
        } else {
            System.out.println("Task already completed");
            finishedListView.getItems().add(newTask);
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

    @FXML
    protected void deleteTaskElement() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // 刪除該任務並保存進度
            taskListView.getItems().remove(selectedIndex);
            taskStorage.saveTasksToFile(taskListView.getItems());
        } else {
            showErrorDialog("Selection Error", "No task selected");
        }
    }

    @FXML
    protected void deleteFinishedTaskElement() {
        int selectedIndex = finishedListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            finishedListView.getItems().remove(selectedIndex);
            taskStorage.saveTasksToFile(taskListView.getItems());
        }
    }

    @FXML
    protected void finishTask() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
//        if()
    }

    @FXML
    protected void viewTaskDetails() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showErrorDialog("Selection Error", "No task selected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task-details-view.fxml"));
            VBox root = loader.load();
            TaskDetailsController controller = loader.getController();
            controller.setTask(selectedTask);

            Stage stage = new Stage();
            stage.setTitle("Task Details");
            stage.setScene(new Scene(root, 400, 300));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String title, String message) {
        Stage errorDialog = new Stage();
        errorDialog.initModality(Modality.APPLICATION_MODAL);
        errorDialog.setTitle(title);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Label(message));
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> errorDialog.close());
        dialogVbox.getChildren().add(closeButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 150);
        errorDialog.setScene(dialogScene);
        errorDialog.show();
    }

    public void saveTasksToFile() {
        taskStorage.saveTasksToFile(taskListView.getItems());
    }
}
