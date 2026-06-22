package com.todolist.pattern.behavioral.state;

/**
 * STATE PATTERN - State interface.
 *
 * Encapsulates how a task behaves and appears based on its current status.
 */
public interface TaskState {

    String getStatusLabel();

    String getCssClass();

    boolean isActionable();
}
