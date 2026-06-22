package com.todolist.pattern.behavioral.observer;

import com.todolist.model.Task;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * OBSERVER PATTERN — Subject (Publisher)
 *
 * Role: SUBJECT — stores a list of observers and notifies them manually when state changes occur.
 * This is a pure Java implementation of the Observer pattern.
 */
@Component
public class TaskEventPublisher {

    private final List<TaskObserver> observers = new ArrayList<>();

    /**
     * Spring automatically autowires all components implementing the TaskObserver interface
     * and injects them as a list into this constructor.
     */
    public TaskEventPublisher(List<TaskObserver> observers) {
        if (observers != null) {
            this.observers.addAll(observers);
        }
    }

    public void addObserver(TaskObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers when a task is completed.
     */
    public void publishTaskCompleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCompleted(task);
        }
    }

    /**
     * Notify all observers when a task is deleted.
     */
    public void publishTaskDeleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskDeleted(task);
        }
    }

    /**
     * Notify all observers when a task is created.
     */
    public void publishTaskCreated(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCreated(task);
        }
    }

    /**
     * Notify all observers when a task is edited.
     */
    public void publishTaskEdited(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskEdited(task);
        }
    }
}
