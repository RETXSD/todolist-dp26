package com.todolist.pattern.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.todolist.model.Task;


@Component
public class TaskEventPublisher {

    private final List<TaskObserver> observers = new ArrayList<>();


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


    public void publishTaskCompleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCompleted(task);
        }
    }


    public void publishTaskDeleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskDeleted(task);
        }
    }


    public void publishTaskCreated(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCreated(task);
        }
    }


    public void publishTaskEdited(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskEdited(task);
        }
    }
}
