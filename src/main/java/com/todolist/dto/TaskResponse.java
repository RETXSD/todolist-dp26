package com.todolist.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for sending task data back to the view/client.
 * Includes displayLabel from the Decorator Pattern.
 */
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDate taskDate;
    private LocalTime taskTime;
    private String category;
    private String urgency;
    private boolean completed;
    private LocalDateTime createdAt;
    private String recurrencePattern;
    private String displayLabel;
    private String statusLabel;
    private String statusCssClass;
    private boolean actionable;

    public TaskResponse() {
    }

    public TaskResponse(Long id, String title, String description, LocalDate taskDate, LocalTime taskTime,
                        String category, String urgency, boolean completed,
                        LocalDateTime createdAt, String recurrencePattern, String displayLabel,
                        String statusLabel, String statusCssClass, boolean actionable) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.category = category;
        this.urgency = urgency;
        this.completed = completed;
        this.createdAt = createdAt;
        this.recurrencePattern = recurrencePattern;
        this.displayLabel = displayLabel;
        this.statusLabel = statusLabel;
        this.statusCssClass = statusCssClass;
        this.actionable = actionable;
    }

    public static TaskResponseBuilder builder() {
        return new TaskResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getStatusCssClass() {
        return statusCssClass;
    }

    public void setStatusCssClass(String statusCssClass) {
        this.statusCssClass = statusCssClass;
    }

    public boolean isActionable() {
        return actionable;
    }

    public void setActionable(boolean actionable) {
        this.actionable = actionable;
    }

    public static class TaskResponseBuilder {
        private Long id;
        private String title;
        private String description;
        private LocalDate taskDate;
        private LocalTime taskTime;
        private String category;
        private String urgency;
        private boolean completed;
        private LocalDateTime createdAt;
        private String recurrencePattern;
        private String displayLabel;
        private String statusLabel;
        private String statusCssClass;
        private boolean actionable;

        public TaskResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TaskResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskResponseBuilder taskDate(LocalDate taskDate) {
            this.taskDate = taskDate;
            return this;
        }

        public TaskResponseBuilder taskTime(LocalTime taskTime) {
            this.taskTime = taskTime;
            return this;
        }

        public TaskResponseBuilder category(String category) {
            this.category = category;
            return this;
        }

        public TaskResponseBuilder urgency(String urgency) {
            this.urgency = urgency;
            return this;
        }

        public TaskResponseBuilder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public TaskResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TaskResponseBuilder recurrencePattern(String recurrencePattern) {
            this.recurrencePattern = recurrencePattern;
            return this;
        }

        public TaskResponseBuilder displayLabel(String displayLabel) {
            this.displayLabel = displayLabel;
            return this;
        }

        public TaskResponseBuilder statusLabel(String statusLabel) {
            this.statusLabel = statusLabel;
            return this;
        }

        public TaskResponseBuilder statusCssClass(String statusCssClass) {
            this.statusCssClass = statusCssClass;
            return this;
        }

        public TaskResponseBuilder actionable(boolean actionable) {
            this.actionable = actionable;
            return this;
        }

        public TaskResponse build() {
            return new TaskResponse(id, title, description, taskDate, taskTime, category, urgency,
                    completed, createdAt, recurrencePattern, displayLabel,
                    statusLabel, statusCssClass, actionable);
        }
    }
}
