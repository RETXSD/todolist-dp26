package com.todolist.pattern.behavioral.observer;

import com.todolist.model.Task;


public interface TaskObserver {
    void onTaskCreated(Task task);
    void onTaskEdited(Task task);
    void onTaskCompleted(Task task);
    void onTaskDeleted(Task task);
}
