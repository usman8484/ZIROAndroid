package com.usman.todoapp.Model;

public class ToDoModel {
    private String TaskName,date,key;

    public ToDoModel(String taskName, String date, String key) {
        TaskName = taskName;
        this.date = date;
        this.key = key;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
