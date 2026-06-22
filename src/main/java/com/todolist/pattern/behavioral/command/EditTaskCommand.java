package com.todolist.pattern.behavioral.command;

import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.repository.TaskRepository;

import java.time.LocalDate;

/**
 * COMMAND PATTERN — Concrete Command: Edit Task
 *
 * Role: CONCRETE COMMAND
 *
 * Encapsulates the "edit task" action.
 * Saves the previous state of the task properties before executing,
 * so that undo() can revert to the previous state.
 *
 * Receiver: TaskRepository (persists changes to DB)
 */
public class EditTaskCommand implements TaskCommand {

    private final TaskRepository taskRepository;
    private final Task task;

    // Previous state
    private final String oldTitle;
    private final String oldDescription;
    private final LocalDate oldTaskDate;
    private final Category oldCategory;
    private final Urgency oldUrgency;
    private final String oldRecurrencePattern;

    // New state
    private final String newTitle;
    private final String newDescription;
    private final LocalDate newTaskDate;
    private final Category newCategory;
    private final Urgency newUrgency;
    private final String newRecurrencePattern;

    public EditTaskCommand(Task task,
                           String title,
                           String description,
                           LocalDate taskDate,
                           Category category,
                           Urgency urgency,
                           String recurrencePattern,
                           TaskRepository taskRepository) {
        this.task           = task;
        this.taskRepository = taskRepository;

        // Capture snapshot of old values
        this.oldTitle               = task.getTitle();
        this.oldDescription         = task.getDescription();
        this.oldTaskDate            = task.getTaskDate();
        this.oldCategory            = task.getCategory();
        this.oldUrgency             = task.getUrgency();
        this.oldRecurrencePattern   = task.getRecurrencePattern();

        // Capture new values
        this.newTitle               = title;
        this.newDescription         = description;
        this.newTaskDate            = taskDate;
        this.newCategory            = category;
        this.newUrgency             = urgency;
        this.newRecurrencePattern   = recurrencePattern;
    }

    @Override
    public void execute() {
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        task.setTaskDate(newTaskDate);
        task.setCategory(newCategory);
        task.setUrgency(newUrgency);
        task.setRecurrencePattern(newRecurrencePattern);
        taskRepository.save(task);
    }

    @Override
    public void undo() {
        task.setTitle(oldTitle);
        task.setDescription(oldDescription);
        task.setTaskDate(oldTaskDate);
        task.setCategory(oldCategory);
        task.setUrgency(oldUrgency);
        task.setRecurrencePattern(oldRecurrencePattern);
        taskRepository.save(task);
    }
}
