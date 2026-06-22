package com.todolist.pattern.behavioral.state;

/**
 * STATE PATTERN - Concrete state for unfinished tasks past their date.
 */
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
