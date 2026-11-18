package com.todolist.pattern.behavioral.state;


public class CompletedTaskState implements TaskState {

    @Override
    public String getStatusLabel() {
        return "Completed";
    }

    @Override
    public String getCssClass() {
        return "completed";
    }

    @Override
    public boolean isActionable() {
        return false;
    }
}
