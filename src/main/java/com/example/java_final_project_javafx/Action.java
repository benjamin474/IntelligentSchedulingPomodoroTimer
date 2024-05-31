//package com.example.java_final_project_javafx;
//
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//public abstract class Action {
//
//    protected void showErrorDialog(String title, String message) {
//        Stage errorDialog = new Stage();
//        errorDialog.initModality(Modality.APPLICATION_MODAL);
//        errorDialog.setTitle(title);
//        VBox dialogVbox = new VBox(20);
//        dialogVbox.getChildren().add(new Label(message));
//        Button closeButton = new Button("Close");
//        closeButton.setOnAction(event -> errorDialog.close());
//        dialogVbox.getChildren().add(closeButton);
//        Scene dialogScene = new Scene(dialogVbox, 300, 150);
//        errorDialog.setScene(dialogScene);
//        errorDialog.show();
//    }
//
//}
