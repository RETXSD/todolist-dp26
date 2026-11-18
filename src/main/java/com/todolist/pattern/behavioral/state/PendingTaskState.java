package com.todolist.pattern.behavioral.state;


public class PendingTaskState implements TaskState {

    @Override
    public String getStatusLabel() {
        return "Pending";
    }

    @Override
    public String getCssClass() {
        return "";
    }

    @Override
    public boolean isActionable() {
        return true;
    }
}
