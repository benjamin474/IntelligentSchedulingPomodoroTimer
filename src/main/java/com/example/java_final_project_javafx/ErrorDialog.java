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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ErrorDialog {
    Stage dialog = new Stage();
    VBox dialogVbox = new VBox(20);
    Label errorLabel = new Label();
    Button okButton = new Button("OK");
    ErrorDialog(String errorMessage) {
        // initialize the dialog
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Error");

        // set the error message
        errorLabel.setText(errorMessage);

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

    ErrorDialog(String title, String message) {
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