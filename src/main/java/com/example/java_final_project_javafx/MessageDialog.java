package com.example.java_final_project_javafx;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class MessageDialog {
    Stage dialog = new Stage();
    VBox dialogVbox = new VBox(20);
    Label errorLabel = new Label();
    Button okButton = new Button("OK");
    
    MessageDialog(String message) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Error");

        // set the error message
        errorLabel.setText(message);

        // add the error label to the dialog
        dialogVbox.getChildren().add(errorLabel);

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
        errorLabel.setText(message);

        // add the error label to the dialog
        dialogVbox.getChildren().add(errorLabel);

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

    public void close() {
        dialog.close();
    }
}