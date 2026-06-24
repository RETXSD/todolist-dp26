package com.todolist.pattern.behavioral.command;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;

/**
 * COMMAND PATTERN — Concrete Command: Delete Task
 *
 * Role: CONCRETE COMMAND
 *
 * Encapsulates the "delete task" action.
 * Saves a copy of the Task object before deleting,
 * so that undo() can re-save it to the database.
 *
 * Receiver: TaskRepository (performs the actual DB operation)
 */
public class DeleteTaskCommand implements TaskCommand {

    // RECEIVER — the object that actually performs the DB work
    private final TaskRepository taskRepository;

    private final Task task;

    // Full task snapshot saved before deletion (used for undo)
    private Task deletedTaskSnapshot;

    public DeleteTaskCommand(Task task, TaskRepository taskRepository) {
        this.task           = task;
        this.taskRepository = taskRepository;
    }

    /**
     * Executes the command: saves a snapshot of the task, then deletes it.
     */
    @Override
    public void execute() {
        // Save full task object snapshot before deleting
        // (ID will be lost after delete, but the snapshot preserves all fields)
        this.deletedTaskSnapshot = Task.builder()
                .user(task.getUser())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskDate(task.getTaskDate())
                .taskTime(task.getTaskTime())
                .category(task.getCategory())
                .urgency(task.getUrgency())
                .recurrencePattern(task.getRecurrencePattern())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();

        taskRepository.delete(task);
    }

    /**
     * Undoes the command: re-saves the previously deleted task.
     * Note: a new ID will be assigned by the database.
     */
    @Override
    public void undo() {
        taskRepository.save(deletedTaskSnapshot);
    }
}
