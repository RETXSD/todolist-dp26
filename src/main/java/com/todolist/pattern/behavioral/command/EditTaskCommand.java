package com.todolist.pattern.behavioral.command;

import java.time.LocalDate;
import java.time.LocalTime;

import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.repository.TaskRepository;


public class EditTaskCommand implements TaskCommand {

    private final TaskRepository taskRepository;
    private final Task task;


    private final String oldTitle;
    private final String oldDescription;
    private final LocalDate oldTaskDate;
    private final LocalTime oldTaskTime;
    private final Category oldCategory;
    private final Urgency oldUrgency;
    private final String oldRecurrencePattern;


    private final String newTitle;
    private final String newDescription;
    private final LocalDate newTaskDate;
    private final LocalTime newTaskTime;
    private final Category newCategory;
    private final Urgency newUrgency;
    private final String newRecurrencePattern;

    public EditTaskCommand(Task task,
                           String title,
                           String description,
                           LocalDate taskDate,
                           LocalTime taskTime,
                           Category category,
                           Urgency urgency,
                           String recurrencePattern,
                           TaskRepository taskRepository) {
        this.task           = task;
        this.taskRepository = taskRepository;


        this.oldTitle               = task.getTitle();
        this.oldDescription         = task.getDescription();
        this.oldTaskDate            = task.getTaskDate();
        this.oldTaskTime            = task.getTaskTime();
        this.oldCategory            = task.getCategory();
        this.oldUrgency             = task.getUrgency();
        this.oldRecurrencePattern   = task.getRecurrencePattern();


        this.newTitle               = title;
        this.newDescription         = description;
        this.newTaskDate            = taskDate;
        this.newTaskTime            = taskTime;
        this.newCategory            = category;
        this.newUrgency             = urgency;
        this.newRecurrencePattern   = recurrencePattern;
    }

    @Override
    public void execute() {
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        task.setTaskDate(newTaskDate);
        task.setTaskTime(newTaskTime);
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
        task.setTaskTime(oldTaskTime);
        task.setCategory(oldCategory);
        task.setUrgency(oldUrgency);
        task.setRecurrencePattern(oldRecurrencePattern);
        taskRepository.save(task);
    }
}
