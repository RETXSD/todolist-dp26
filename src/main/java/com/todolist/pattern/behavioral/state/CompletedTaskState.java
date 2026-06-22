package com.todolist.pattern.behavioral.state;

/**
 * STATE PATTERN - Concrete state for completed tasks.
 */
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
