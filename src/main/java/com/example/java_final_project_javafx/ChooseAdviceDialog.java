package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.NumberFormat;

public class ChooseAdviceDialog {
    // create the dialog
    Stage dialog = new Stage();

    VBox dialogVbox = new VBox(18);

    HBox buttonBox = new HBox(15); // create a new HBox with 10px spacing

    private ListView<Task> taskListView;
    private ListView<Task> adviceListView = new ListView<Task>();

    Button cancelButton = new Button("Cancel");
    Button addButton = new Button("Add");

    Label taskLabel = new Label("Advice task");

    NumberFormat format = NumberFormat.getInstance();

    MainController mainController;

    ChooseAdviceDialog(MainController mainController) {
        // store the task list view
        this.mainController = mainController;
        this.taskListView = new ListView<Task>(mainController.getTaskListView().getItems()); // Pass the ObservableList<Task> instead of ListView<Task>

        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Preview Task");

        addButton.setOnAction(e -> handleTask());
        cancelButton.setOnAction(e -> dialog.close());

        taskLabel.setFont(Font.font("System", 14)); // set the font of the label to bold and size 20
    }

    private void addToList(String str) {
        String[] fields = str.split(",");
        String name = fields[0];
        String startDate = fields[1];
        String endDate = fields[2];
        String completed = fields[3];
        String comment = fields[4];
        Task task = new Task(name, startDate, endDate, completed, comment);
        adviceListView.getItems().add(task);
    }

    public void show(String str) {
        for (String s : str.split("\n")) {
            try {
                addToList(s);
            } catch (Exception e) {
                // new MessageDialog("Error", "Invalid task format");
                continue;
            }
        }
        // add the task list view to the dialog
        buttonBox.getChildren().addAll(addButton, cancelButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        // add the task list view to the dialog
        dialogVbox.getChildren().addAll(taskLabel, adviceListView, buttonBox, new Label(""));
        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 250);
        dialog.setScene(dialogScene);
        dialog.show();
        mainController.listRefresh();
    }

    private void handleTask() {
        for(Task task : adviceListView.getItems()) {
            Task newTask = new Task(task);
            mainController.storeToListView(newTask);
        }
        
        
        dialog.close();
    }
}
