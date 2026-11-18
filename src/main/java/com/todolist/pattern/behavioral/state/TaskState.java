package com.todolist.pattern.behavioral.state;


public interface TaskState {

    String getStatusLabel();

    String getCssClass();

    boolean isActionable();
}
