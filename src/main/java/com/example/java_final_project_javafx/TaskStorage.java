package com.example.java_final_project_javafx;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    private static final String TASKS_FILE = "Tasks.csv";
    // private static final String FINISH_TASKS_FILE = "FinishedTasks.csv";
    public final int TASK = 1;
    public final int FINISHED = 2;

    public void saveTasksToFile(List<Task> tasks, List<Task> finishedTasks) {
//       儲存任務資料
        File file;
        file = new File(TASKS_FILE);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (Task task : tasks) {
                writer.print(task.getName() + "," + task.getStartDate() + "," + task.getEndDate() + "," + task.getCompleted() + "," + task.getComment());
                for (SubTask subTask : task.getSubTasks()) {
                    writer.print(subTask.getName() + ",");
                }
                writer.println();
            }
            for (Task task : finishedTasks) {
                writer.println(task.getName() + "," + task.getStartDate() + "," + task.getEndDate() + "," + task.getCompleted() + "," + task.getComment());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasksFromFile(List<Task> tasks, List<Task> finishedTasks) {
//        加載任務資料
        File file = new File(TASKS_FILE);
        System.out.println("Loading tasks from file: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            tasks.clear();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                String name = fields[0];
                LocalDate startDate = LocalDate.parse(fields[1]);
                LocalDate endDate = LocalDate.parse(fields[2]);
                Integer completed = Integer.parseInt(fields[3]);
                String comment = "fields[4]";

                Task task = new Task(name, startDate, endDate, completed, comment);
                if (completed == 100) {
                    finishedTasks.add(task);
                } else {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
