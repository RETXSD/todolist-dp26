package com.todolist.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Plain Java model for application users.
 */
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private List<Task> tasks;

    public User() {
    }

    public User(Long id, String username, String email, String password,
                LocalDateTime createdAt, List<Task> tasks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private LocalDateTime createdAt;
        private List<Task> tasks;

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilder tasks(List<Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public User build() {
            return new User(id, username, email, password, createdAt, tasks);
        }
    }
}
