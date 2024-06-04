package com.example.java_final_project_javafx;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class TaskStorage {

    private static final String TASKS_FILE = "Tasks.csv"; // File name for storing tasks
    // private static final String FINISH_TASKS_FILE = "FinishedTasks.csv";
    public final int TASK = 1; // Constant for identifying task type
    public final int FINISHED = 2; // Constant for identifying finished task type

    // Method to save tasks and finished tasks to a file
    public void saveTasksToFile(List<Task> tasks, List<Task> finishedTasks) {
        // save the tasks to a file
        File file;
        file = new File(TASKS_FILE); // Create a file object with the specified file name

        try (PrintWriter writer = new PrintWriter(file)) {
            // Write each task to the file
            for (Task task : tasks) {
                writer.println(task.getName() + "," + task.getStartDate() + "," + task.getEndDate() + "," + task.getCompleted() + "," + task.getComment());
            }
            // Write each finished task to the file
            for (Task task : finishedTasks) {
                writer.println(task.getName() + "," + task.getStartDate() + "," + task.getEndDate() + "," + task.getCompleted() + "," + task.getComment());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load tasks and finished tasks from a file
    public void loadTasksFromFile(List<Task> tasks, List<Task> finishedTasks) {
        // load the tasks from a file
        File file = new File(TASKS_FILE); // Create a file object with the specified file name
        System.out.println("Loading tasks from file: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            tasks.clear(); // Clear the tasks list before loading
            while ((line = reader.readLine()) != null) {
                try {
                    // Split the line by commas to extract task fields
                    String[] fields = line.split(",");
                    String name = fields[0];
                    LocalDate startDate = LocalDate.parse(fields[1]);
                    LocalDate endDate = LocalDate.parse(fields[2]);
                    Integer completed = Integer.parseInt(fields[3]);
                    String comment = (fields[4] == null ? "" : fields[4]);
                    // Create a new Task object with the extracted fields
                    Task task = new Task(name, startDate, endDate, completed, comment);
                    // Add the task to the appropriate list based on its completion status
                    if (completed == 100) {
                        finishedTasks.add(task);
                    } else {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Error reading task: " + line); // Print error message if a task cannot be read
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
