package com.example.java_final_project_javafx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

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

    @FXML
    private ListView<Task> nearingExpirationListView;

    @FXML
    private Button chooseTaskButton;

    @FXML
    private Button getAdviceButton;

    @FXML
    TextArea adviceTextArea;

    @FXML
    Label adviceLabel;

    private Task selectedTask;

    private Timer timer;
    private int timeRemaining;
    public final int TASK = 1;
    public final int FINISH = 2;
    private boolean isPaused = false;
    private int initialTime;

    public TaskStorage taskStorage = new TaskStorage();

    // 初始化，設置初始狀態和從文件加載任務
    @FXML
    public void initialize() {
        setChooseTaskButton(true);
        taskListView.setItems(FXCollections.observableArrayList());
        taskListView.setCellFactory(param -> new TaskListCell());
        taskStorage.loadTasksFromFile(taskListView.getItems(), finishedListView.getItems());
        durationField.setText("25:00");

        nearingExpirationListView.setItems(FXCollections.observableArrayList());
        nearingExpirationListView.setCellFactory(param -> new TaskListCell("Near"));

        checkNearingExpirationTasks();
    }


    // 開始計時器
    @FXML
    protected void startTimer() {
        System.out.println("Starting timer");
        if (timer == null) {
            try {
                // set the initial time
                String[] time = durationField.getText().split(":");
                try {
                    int minutes = Integer.parseInt(time[0]);
                    int seconds = 0;
                    if (time.length > 1) {
                        seconds = Integer.parseInt(time[1]);
                    }
                    timeRemaining = minutes * 60 + seconds;
                } catch (NumberFormatException e) {
                    timeRemaining = initialTime;
                }

                // store the initial time
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
                                    if (selectedTask != null) {
                                        new MessageDialog("Time's up!", "Now edit your progress!", selectedTask, MainController.this);
                                    } else {
                                        new MessageDialog("Time's up!", "Time's up!");
                                    }
                                    resetTimer();
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

    // 暫停計時器
    @FXML
    protected void pauseTimer() {
        System.out.println("Pause timer");
        if (timer != null)
            isPaused = true;
    }

    // 重置計時器
    @FXML
    protected void resetTimer() {
        setChooseTaskButton(true);
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

    // 顯示添加任務對話框
    @FXML
    protected void showAddTaskDialog() {
        TaskDialog taskDialog = new TaskDialog(this);
        taskDialog.show();
    }

    // 顯示編輯任務對話框
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

    // 將新任務添加到任務列表中
    public void storeToListView(Task newTask) {
        taskListView.getItems().add(newTask);
        saveTasksToFile();
    }

    // 刪除選中任務
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
        checkNearingExpirationTasks();
    }

    // 刪除已完成任務
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

    // 標記選中任務為完成
    @FXML
    protected void finishTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            new MessageDialog("Selection Error", "No task selected");
            return;
        }
        selectedTask.setCompleted(100);
        getFinishedList();
        checkNearingExpirationTasks();
    }

    // 查看任務詳情
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

    // 更新完成的任務列表
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

    // 保存任務到文件
    public void saveTasksToFile() {
        taskStorage.saveTasksToFile(taskListView.getItems(), finishedListView.getItems());
        getFinishedList();
        listRefresh();
        checkNearingExpirationTasks();
    }

    // 獲取任務列表的大小
    public int getListViewSize(int type) {
        if (type == TASK) {
            return taskListView.getItems().size();
        } else if (type == FINISH) {
            return finishedListView.getItems().size();
        }
        return 0;
    }

    // 刷新任務列表
    public void listRefresh() {
        taskListView.refresh();
        finishedListView.refresh();
    }

    // 顯示選擇任務對話框
    @FXML
    protected void chooseTask() {
        ChooseTaskDialog chooseTaskDialog = new ChooseTaskDialog(this);
        chooseTaskDialog.show();
    }

    // 獲取任務列表視圖
    public ListView<Task> getTaskListView() {
        return taskListView;
    }

    // 設置當前選中任務
    public void setTask(Task task) {
        this.selectedTask = task;
        System.out.println("Selected task: " + task);
    }

    // 設置選擇任務按鈕
    public void setChooseTaskButton(Boolean b) {
        if (b) {
            chooseTaskButton.setText("Choose Task");
            chooseTaskButton.setDisable(false); // Enable the button
            selectedTask = null;
        } else {
            chooseTaskButton.setText(selectedTask.toString());
            chooseTaskButton.setDisable(true); // Disable the button
        }
    }

    private void checkNearingExpirationTasks() {
        List<Task> nearingExpirationTasks = taskListView.getItems().stream()
                .filter(task -> {
                    LocalDate endDate = task.getEndDate();
                    LocalDate today = LocalDate.now();
                    // 檢查相差天數小於兩天 並且尚未逾期
                    return endDate != null && ChronoUnit.DAYS.between(today, endDate) <= 2 && !(endDate.isBefore(today));
                }).collect(Collectors.toList());
        nearingExpirationListView.getItems().setAll(nearingExpirationTasks);
    }

    @FXML
    public void getAdvice() {
        getAdviceButton.setVisible(false);
        adviceTextArea.setText("Generating advice...");
        adviceLabel.setText("QwO");
        new Thread(() -> {
            ChatBot chatBot = new ChatBot();
            String advice = chatBot.getMessage(taskStorage.getFileStr());
            System.out.println(advice);
            
            Platform.runLater(() -> {
                adviceTextArea.setText(advice);
                adviceLabel.setText("Advice");
                getAdviceButton.setVisible(true);
            });
        }).start();
    }
}
