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
        FXMLLoader fxmlLoader = new FXMLLoader(PomodoroApplication.class.getResource("main-view.fxml"));
        BorderPane root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.show();

        // 儲存目前計畫表
        stage.setOnCloseRequest(event -> {
            MainController controller = fxmlLoader.getController();
            controller.saveTasksToFile();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
