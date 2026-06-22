package com.todolist.pattern.behavioral.observer;

import com.todolist.model.Task;

/**
 * OBSERVER PATTERN — Observer Interface
 *
 * Role: OBSERVER (Listener)
 *
 * Defines the contract that all concrete observers must implement.
 * This decouples the subject (TaskEventPublisher) from concrete listener implementations.
 */
public interface TaskObserver {
    void onTaskCreated(Task task);
    void onTaskEdited(Task task);
    void onTaskCompleted(Task task);
    void onTaskDeleted(Task task);
}
