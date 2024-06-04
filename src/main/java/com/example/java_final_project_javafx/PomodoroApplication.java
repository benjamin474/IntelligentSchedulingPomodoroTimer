package com.example.java_final_project_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PomodoroApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the main view
        FXMLLoader fxmlLoader = new FXMLLoader(PomodoroApplication.class.getResource("main-view.fxml"));
        BorderPane root = fxmlLoader.load();
        // Create a new scene with the loaded layout
        Scene scene = new Scene(root, 800, 600);
        // Set the title of the stage (window)
        stage.setTitle("Pomodoro Timer");
        // Set the scene to the stage
        stage.setScene(scene);
        // Show the stage
        stage.show();

        // 儲存目前計畫表
        stage.setOnCloseRequest(event -> {
            // Get the controller from the FXMLLoader
            MainController controller = fxmlLoader.getController();
            // Call the saveTasksToFile method in the controller
            controller.saveTasksToFile();
        });
    }

    public static void main(String[] args) {
        // Launch the application
        launch();
    }
}
