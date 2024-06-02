package com.example.java_final_project_javafx;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
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

    @FXML
    private ListView<Task> finishedListView;

    private Timer timer;
    private int timeRemaining;
    public final int TASK = 1;
    public final int FINISH = 2;
    private boolean isPaused = false;
    private int initialTime;

    public TaskStorage taskStorage = new TaskStorage();

    @FXML
    public void initialize() {
        taskListView.setItems(FXCollections.observableArrayList());
        taskListView.setCellFactory(param -> new TaskListCell());
        taskStorage.loadTasksFromFile(taskListView.getItems(), finishedListView.getItems());
        durationField.setText("25");
    }

    @FXML
    protected void startTimer() {
        System.out.println("Starting timer");
        if (timer == null) {
            String durationText = durationField.getText();

            try {
                initialTime = Integer.parseInt(durationText) * 60;
                if (initialTime <= 0) {
                    new MessageDialog("Input error", "Please enter a positive number");
                }

                timeRemaining = initialTime;

                if (timer != null) {
                    timer.cancel();
                }
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        // JavaFX UI操作需要在JavaFX Application Thread中執行
                        javafx.application.Platform.runLater(() -> {
                            if (!isPaused) {
                                timeRemaining--;
                                int minutes = timeRemaining / 60;
                                int seconds = timeRemaining % 60;
                                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                                if (timeRemaining <= 0) {
                                    new MessageDialog("End time", "Times up!");
                                    timer.cancel();
                                }
                            }
                        });
                    }
                }, 0, 1000);
            } catch (NumberFormatException e) {
                new MessageDialog("Input Error", "Please enter a valid number");
            }
        } else {
            isPaused = false;
        }
    }

    @FXML
    protected void pauseTimer() {
        System.out.println("Pause timer");
        if (timer != null)
            isPaused = true;
    }

    @FXML
    protected void resetTimer() {
        System.out.println("Reset timer");
        if (timer != null) {
            timer.cancel();
            timer = null;
            isPaused = false;
            int minutes = initialTime / 60;
            int seconds = initialTime % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        } else {
            new MessageDialog("Timer hasn't been started");
        }
    }

    @FXML
    protected void showAddTaskDialog() {
        TaskDialog taskDialog = new TaskDialog(this);
        taskDialog.show();
    }

    @FXML
    protected void showEditTaskDialog() {
        // get the selected task
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            new MessageDialog("Selection Error", "No task selected");
            return;
        }

        // show the dialog
        TaskDialog taskDialog = new TaskDialog(this, selectedTask);
        taskDialog.show();
    }

    public void storeToListView(Task newTask) {
        taskListView.getItems().add(newTask);
        saveTasksToFile();
    }

    @FXML
    protected void deleteTaskElement() {
        int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // 刪除該任務並保存進度
            taskListView.getItems().remove(selectedIndex);
            taskStorage.saveTasksToFile(taskListView.getItems(), finishedListView.getItems());
        } else {
            new MessageDialog("Selection Error", "No task selected");
        }
    }

    @FXML
    protected void deleteFinishedTaskElement() {
        int selectedIndex = finishedListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            finishedListView.getItems().remove(selectedIndex);
            taskStorage.saveTasksToFile(taskListView.getItems(), finishedListView.getItems());
        } else {
            new MessageDialog("Selection Error", "No task selected");

        }
    }

    @FXML
    protected void finishTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            new MessageDialog("Selection Error", "No task selected");
            return;
        }
        selectedTask.setCompleted(100);
        getFinishedList();


    }

    @FXML
    protected void viewTaskDetails() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            new MessageDialog("Selection Error", "No task selected");
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

    private void getFinishedList() {
        for (Task task : taskListView.getItems()) {
            if (task.getCompleted() == 100) {
                System.out.println(task + " is finished");
                finishedListView.getItems().add(task);
            }
        }
        for (Task task : finishedListView.getItems()) {
            taskListView.getItems().remove(task);
        }
    }

    public void saveTasksToFile() {
        taskStorage.saveTasksToFile(taskListView.getItems(), finishedListView.getItems());
        getFinishedList();
        listRefresh();
    }

    public int getListViewSize(int type) {
        if (type == TASK) {
            return taskListView.getItems().size();
        } else if (type == FINISH) {
            return finishedListView.getItems().size();
        }
        return 0;
    }

    public void listRefresh() {
        taskListView.refresh();
        finishedListView.refresh();
    }
}
