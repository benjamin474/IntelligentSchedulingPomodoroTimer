package com.example.java_final_project_javafx;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;

public class TaskStorage {

    private static final String TASKS_FILE = "Tasks.txt";

    public void saveTasksToFile(List<Task> tasks) {
//       儲存任務資料
//       try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))){
//           for(Task task : tasks) {
//               writer.write(task);
//               writer.newLine();
//           }
//       }catch(IOException e){
//           e.printStackTrace();
//       }
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TASKS_FILE))) {
//            out.writeObject(tasks);
//            System.out.println("Saving tasks to file: " + new File(TASKS_FILE).getAbsolutePath());
//        } catch (IOException e) {
//            System.err.println("Error saving tasks to file: " + e.getMessage());
////            e.printStackTrace();
//        }

    }

    public void loadTasksFromFile(ObservableList<Task> tasks) {
        File file = new File(TASKS_FILE);
        System.out.println("Loading tasks from file: " + file.getAbsolutePath());

        if (file.exists()) {
            if(file.length() == 0) {
                System.out.println("The File is empty");
                return;
            }
//            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
//                List<Task> loadedTasks = (List<Task>) in.readObject();
//                tasks.addAll(loadedTasks);
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            try(BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))){
//                String line;
//
//
//            }
        }
    }
}
