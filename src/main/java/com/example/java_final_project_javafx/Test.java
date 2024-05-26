package com.example.java_final_project_javafx;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Enter your name:");
        TextField textField = new TextField();
        Button button = new Button("Say Hello");

        button.setOnAction(event -> {
            String name = textField.getText();
            System.out.println("Hello, " + name + "!");
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, textField, button);

        Scene scene = new Scene(vbox, 300, 200);

        primaryStage.setTitle("Hello JavaFX!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
