package com.todolist.pattern.structural;

import com.todolist.model.Task;


public class BaseTask implements TaskComponent {


    private final Task task;

    public BaseTask(Task task) {
        this.task = task;
    }

    @Override
    public String getTitle() {
        return task.getTitle();
    }

    @Override
    public String getDescription() {
        return task.getDescription();
    }

    @Override
    public String getUrgency() {
        return task.getUrgency().name();
    }


    @Override
    public String getDisplayLabel() {
        return "[" + task.getCategory().name() + "] " + task.getTitle();
    }
}
