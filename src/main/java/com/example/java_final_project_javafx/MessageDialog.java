package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class MessageDialog {
    // create the dialog
    Stage dialog = new Stage();

    VBox dialogVbox = new VBox(20);
    VBox buttonBox = new VBox(15);

    Label messageLabel = new Label();
    Button okButton = new Button("OK");

    private final int WIDTH = 300;
    private final int HEIGHT = 100;

    MessageDialog(String message) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Error");

        // set the error message
        messageLabel.setText(message);
        messageLabel.setFont(javafx.scene.text.Font.font("System", 14));

        // set the ok button action
        okButton.setOnAction(e -> dialog.close());
        okButton.setAlignment(javafx.geometry.Pos.CENTER);

        // add the error label to the dialog
        dialogVbox.getChildren().add(messageLabel);

        // add the ok button to the dialog
        dialogVbox.getChildren().add(okButton);

        // set the scene
        Scene dialogScene = new Scene(dialogVbox, WIDTH, HEIGHT);
        dialog.setScene(dialogScene);

        // show the dialog
        dialog.show();
    }

    MessageDialog(String title, String message) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);

        // set the ok button action
        okButton.setOnAction(e -> dialog.close());

        // set the error message
        messageLabel.setText(message);
        messageLabel.setFont(javafx.scene.text.Font.font("System", 14));

        buttonBox.getChildren().addAll(messageLabel, okButton);
        buttonBox.setAlignment(Pos.CENTER);

        // add the error label to the dialog
        dialogVbox.getChildren().add(buttonBox);
        dialogVbox.setAlignment(Pos.CENTER);


        // set the scene
        Scene dialogScene = new Scene(dialogVbox, WIDTH, HEIGHT);
        dialog.setScene(dialogScene);

        // show the dialog
        dialog.show();
    }

    MessageDialog(String title, String message, Task task, MainController mainController) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);

        // set the error message
        messageLabel.setText(message);
        messageLabel.setFont(javafx.scene.text.Font.font("System", 14));

        // set the ok button action
        okButton.setOnAction(event -> {
            dialog.close();
            TaskDialog taskDialog = new TaskDialog(mainController, task);
            taskDialog.show();
        });

        buttonBox.getChildren().addAll(messageLabel, okButton);
        buttonBox.setAlignment(Pos.CENTER);

        // add the error label to the dialog
        dialogVbox.getChildren().add(buttonBox);
        dialogVbox.setAlignment(Pos.CENTER);

        // set the scene
        Scene dialogScene = new Scene(dialogVbox, WIDTH, HEIGHT);
        dialog.setScene(dialogScene);

        // show the dialog
        dialog.show();
    }

    public void close() {
        dialog.close();
    }
}