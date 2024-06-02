package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageDialog {
    Stage dialog = new Stage();
    VBox dialogVbox = new VBox(20);
    Label messageLabel = new Label();
    Button okButton = new Button("OK");

//    因為程式中許多地方是用於Error 所以預設標題為Error
    MessageDialog(String message) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Error");

        // set the error message
        messageLabel.setText(message);

        // add the error label to the dialog
        dialogVbox.getChildren().add(messageLabel);

        // add the ok button to the dialog
        dialogVbox.getChildren().add(okButton);

        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);

        // show the dialog
        dialog.show();

        // set the ok button action
        okButton.setOnAction(e -> dialog.close());
    }

    MessageDialog(String title, String message) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);

        // set the error message
        messageLabel.setText(message);

        // add the error label to the dialog
        dialogVbox.getChildren().add(messageLabel);

        // add the ok button to the dialog
        dialogVbox.getChildren().add(okButton);

        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);

        // show the dialog
        dialog.show();

        // set the ok button action
        okButton.setOnAction(e -> dialog.close());
    }

    MessageDialog(String title, String message, Task task, MainController mainController) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);

        // set the error message
        messageLabel.setText(message);

        // add the error label to the dialog
        dialogVbox.getChildren().add(messageLabel);

        // add the ok button to the dialog
        dialogVbox.getChildren().add(okButton);

        // set the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);

        // show the dialog
        dialog.show();

        // set the ok button action
        okButton.setOnAction(event -> {
            dialog.close();
            TaskDialog taskDialog = new TaskDialog(mainController, task);
            taskDialog.show();
        });
    }

    public void close() {
        dialog.close();
    }
}