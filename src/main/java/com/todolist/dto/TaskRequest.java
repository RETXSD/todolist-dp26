package com.todolist.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for creating/editing a task.
 */
public class TaskRequest {

    private String title;
    private String description;
    private LocalDate taskDate;
    private LocalTime taskTime;
    private String category;
    private String urgency;
    private String recurrencePattern;

    public TaskRequest() {
    }

    public TaskRequest(String title, String description, LocalDate taskDate, LocalTime taskTime,
                       String category, String urgency, String recurrencePattern) {
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.category = category;
        this.urgency = urgency;
        this.recurrencePattern = recurrencePattern;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public LocalTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(LocalTime taskTime) {
        this.taskTime = taskTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }
}
