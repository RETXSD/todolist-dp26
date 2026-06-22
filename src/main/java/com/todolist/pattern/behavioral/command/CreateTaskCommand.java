package com.todolist.pattern.behavioral.command;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;

/**
 * COMMAND PATTERN — Concrete Command: Create Task
 *
 * Role: CONCRETE COMMAND
 *
 * Encapsulates the "create task" action.
 * On execute(), saves the task to the database.
 * On undo(), deletes the task from the database.
 */
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
