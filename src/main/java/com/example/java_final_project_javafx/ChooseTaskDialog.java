package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;

public class ChooseTaskDialog {
    // create the dialog
    Stage dialog = new Stage();

    VBox dialogVbox = new VBox(15);

    private ListView<Task> taskListView;

    Button cancelButton = new Button("Cancel");
    Button addButton = new Button("Add");

    NumberFormat format = NumberFormat.getInstance();

    MainController mainController;

    ChooseTaskDialog(MainController mainController) {
        // store the task list view
        this.mainController = mainController;
        this.taskListView = new ListView<Task>(mainController.getTaskListView().getItems()); // Pass the ObservableList<Task> instead of ListView<Task>

        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Choose a Task");

        addButton.setOnAction(e -> handleTask());
        cancelButton.setOnAction(e -> dialog.close());
    }

    public void show() {
        dialogVbox.getChildren().addAll(taskListView, addButton, cancelButton);
        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.show();
        mainController.listRefresh();
    }

    private void handleTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            mainController.setTask(selectedTask);
            mainController.setChooseTaskButton(false);
            dialog.close();
        } else {
            new MessageDialog("Selection Error", "No task selected");
        }
    }
}
