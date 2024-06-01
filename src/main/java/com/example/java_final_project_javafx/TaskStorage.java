package com.example.java_final_project_javafx;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class TaskStorage {

    private static final String TASKS_FILE = "Tasks.csv";
    //    private static final String FINISH_TASKS_FILE = "FinishedTasks.csv";
    public final int TASK = 1;
    public final int FINISHED = 2;

    public void saveTasksToFile(List<Task> tasks) {
//       儲存任務資料
        File file;
//        int taskType = TASK;
        file = new File(TASKS_FILE);
//        if (taskType == FINISHED)
//            file = new File(FINISH_TASKS_FILE);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Task task : tasks) {
                writer.println(task.getName() + "," + task.getStartDate() + "," + task.getEndDate() + "," + task.getCompleted());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void loadTasksFromFile(List<Task> tasks) {
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
                Task task = new Task(name, startDate, endDate, completed);
                tasks.add(task);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        public void moveTask(Task task){
//
//        }


    }
}
