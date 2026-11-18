package com.todolist.pattern.behavioral.command;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;


public class CompleteTaskCommand implements TaskCommand {


    private final TaskRepository taskRepository;

    private final Task task;


    private boolean previousCompletedState;

    public CompleteTaskCommand(Task task, TaskRepository taskRepository) {
        this.task           = task;
        this.taskRepository = taskRepository;
    }


    @Override
    public void execute() {

        this.previousCompletedState = task.isCompleted();

        task.setCompleted(!previousCompletedState);
        taskRepository.save(task);
    }


    @Override
    public void undo() {
        task.setCompleted(previousCompletedState);
        taskRepository.save(task);
    }
}
