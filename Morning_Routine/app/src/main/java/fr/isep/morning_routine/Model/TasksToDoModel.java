package fr.isep.morning_routine.Model;

//Define the structure of individual tasks
public class TasksToDoModel {

    private int id;
    private int status;
    private String task;
    //private String duration;

    //public String getDuration() {return duration;}

    //public void setDuration(String duration) { this.duration = duration;}

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
}
