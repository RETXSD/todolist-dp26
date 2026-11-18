package com.todolist.pattern.behavioral.command;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;


public class CreateTaskCommand implements TaskCommand {

    private final TaskRepository taskRepository;
    private Task task;

    public CreateTaskCommand(Task task, TaskRepository taskRepository) {
        this.task = task;
        this.taskRepository = taskRepository;
    }

    @Override
    public void execute() {
        this.task = taskRepository.save(task);
    }

    @Override
    public void undo() {
        if (task.getId() != null) {
            taskRepository.delete(task);
        }
    }

    public Task getTask() {
        return task;
    }
}
