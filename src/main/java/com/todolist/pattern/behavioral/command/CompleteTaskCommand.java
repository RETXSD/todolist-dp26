package com.todolist.pattern.behavioral.command;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;

/**
 * COMMAND PATTERN — Concrete Command: Complete Task
 *
 * Role: CONCRETE COMMAND
 *
 * Encapsulates the "mark task as complete" action.
 * Saves the previous state (completed flag) before executing,
 * so that undo() can accurately reverse it.
 *
 * Receiver: TaskRepository (performs the actual DB operation)
 */
public class CompleteTaskCommand implements TaskCommand {

    // RECEIVER — the object that actually performs the DB work
    private final TaskRepository taskRepository;

    private final Task task;

    // Store the state before execute() so undo() can revert it
    private boolean previousCompletedState;

    public CompleteTaskCommand(Task task, TaskRepository taskRepository) {
        this.task           = task;
        this.taskRepository = taskRepository;
    }

    /**
     * Executes the command: saves previous state, toggles task completed flag, persists it.
     */
    @Override
    public void execute() {
        // Save current state for accurate undo
        this.previousCompletedState = task.isCompleted();

        task.setCompleted(!previousCompletedState);
        taskRepository.save(task);
    }

    /**
     * Undoes the command: restores the task's completed flag to its previous state.
     */
    @Override
    public void undo() {
        task.setCompleted(previousCompletedState);
        taskRepository.save(task);
    }
}
