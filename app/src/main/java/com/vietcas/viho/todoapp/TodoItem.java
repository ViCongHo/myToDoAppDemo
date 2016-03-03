package com.vietcas.viho.todoapp;

public class TodoItem {
    private int id;
    private String task;
    public TodoItem(String task) {
        this.task = task;
    }
    public TodoItem(int id, String task) {
        this.id = id;
        this.task = task;
    }
    public int getID() {
        return this.id;
    }
    public String getTask() {
        return this.task;
    }
    @Override
    public String toString() {
        return this.task;
    }
}
