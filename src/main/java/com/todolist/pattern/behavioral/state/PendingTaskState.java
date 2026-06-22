package com.todolist.pattern.behavioral.state;

/**
 * STATE PATTERN - Concrete state for unfinished tasks that are still on time.
 */
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
