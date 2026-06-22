package com.todolist.pattern.behavioral.state;

import com.todolist.model.Task;

import java.time.LocalDate;

/**
 * STATE PATTERN - Chooses the current state object for a task.
 */
public class TaskStateResolver {

    private TaskStateResolver() {
    }

    public static TaskState resolve(Task task, LocalDate today) {
        if (task.isCompleted()) {
            return new CompletedTaskState();
        }
        if (task.getTaskDate().isBefore(today)) {
            return new OverdueTaskState();
        }
        return new PendingTaskState();
    }
}
