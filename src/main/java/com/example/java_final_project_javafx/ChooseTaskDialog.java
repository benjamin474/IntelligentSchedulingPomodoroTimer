package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.text.NumberFormat;

public class ChooseTaskDialog {
    // create the dialog
    Stage dialog = new Stage();

    VBox dialogVbox = new VBox(15);

    HBox buttonBox = new HBox(15); // create a new HBox with 10px spacing

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
        // add the task list view to the dialog
        buttonBox.getChildren().addAll(addButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        // add the task list view to the dialog
        dialogVbox.getChildren().addAll(taskListView, buttonBox);
        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 350);
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
