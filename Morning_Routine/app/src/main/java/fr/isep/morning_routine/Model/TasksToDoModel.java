package fr.isep.morning_routine.Model;

import java.io.Serializable;

//Define the structure of individual tasks
public class TasksToDoModel implements Serializable {

    private int id;
    private int status;
    private String task;
    private String startingTime;
    private String endingTime;
    private String applicationName;

    public TasksToDoModel(int id, int status, String task, String startingTime, String endingTime, String applicationName) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.applicationName = applicationName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
