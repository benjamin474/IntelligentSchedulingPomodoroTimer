package com.example.java_final_project_javafx;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;

public class TaskStorage {

    private static final String TASKS_FILE = "tasks.txt";

    public void saveTasksToFile(List<Task> tasks) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TASKS_FILE))) {
            out.writeObject(tasks);
            System.out.println("Saving tasks to file: " + new File(TASKS_FILE).getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
//            e.printStackTrace();
        }
    }

    public void loadTasksFromFile(ObservableList<Task> tasks) {
        File file = new File(TASKS_FILE);
        System.out.println("Loading tasks from file: " + file.getAbsolutePath());
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                List<Task> loadedTasks = (List<Task>) in.readObject();
                tasks.addAll(loadedTasks);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
