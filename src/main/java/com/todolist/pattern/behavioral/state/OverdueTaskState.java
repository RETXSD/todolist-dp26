package com.todolist.pattern.behavioral.state;


public class OverdueTaskState implements TaskState {

    @Override
    public String getStatusLabel() {
        return "Overdue";
    }

    @Override
    public String getCssClass() {
        return "overdue";
    }

    @Override
    public boolean isActionable() {
        return true;
    }
}
