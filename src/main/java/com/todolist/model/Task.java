package com.todolist.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Plain Java model for tasks.
 * Supports Builder Pattern via a manual TaskBuilder for clean object construction.
 */
public class Task {

    private Long id;
    private User user;
    private String title;
    private String description;
    private LocalDate taskDate;
    private Category category;
    private Urgency urgency;
    private String recurrencePattern;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task() {
    }

    public Task(Long id, User user, String title, String description, LocalDate taskDate,
                Category category, Urgency urgency, String recurrencePattern, boolean completed,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.category = category;
        this.urgency = urgency;
        this.recurrencePattern = recurrencePattern;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private Long id;
        private User user;
        private String title;
        private String description;
        private LocalDate taskDate;
        private Category category;
        private Urgency urgency;
        private String recurrencePattern;
        private boolean completed;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder user(User user) {
            this.user = user;
            return this;
        }

        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder taskDate(LocalDate taskDate) {
            this.taskDate = taskDate;
            return this;
        }

        public TaskBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public TaskBuilder urgency(Urgency urgency) {
            this.urgency = urgency;
            return this;
        }

        public TaskBuilder recurrencePattern(String recurrencePattern) {
            this.recurrencePattern = recurrencePattern;
            return this;
        }

        public TaskBuilder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public TaskBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TaskBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Task build() {
            return new Task(id, user, title, description, taskDate, category, urgency,
                    recurrencePattern, completed, createdAt, updatedAt);
        }
    }
}
