package com.todolist.pattern.creational;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.todolist.config.AppConfig;
import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.model.User;


@Component
public class TaskFactory {

    private final AppConfig.ConfigHolder config = AppConfig.ConfigHolder.getInstance();


    public Task createTask(User user,
                           String title,
                           String description,
                           LocalDate taskDate,
                           String category,
                           String urgency) {
        return createTask(user, title, description, taskDate, null, category, urgency, null);
    }

    public Task createTask(User user,
                           String title,
                           String description,
                           LocalDate taskDate,
                           LocalTime taskTime,
                           String category,
                           String urgency) {
        return createTask(user, title, description, taskDate, taskTime, category, urgency, null);
    }

    public Task createTask(User user,
                           String title,
                           String description,
                           LocalDate taskDate,
                           LocalTime taskTime,
                           String category,
                           String urgency,
                           String recurrencePattern) {

        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                "Unrecognized category: '" + category + "'. Valid values: PERSONAL, WORK, SHOPPING.");
        }


        Urgency urg = resolveUrgency(urgency, cat);


        LocalDate resolvedDate = resolveDate(taskDate, cat);


        return Task.builder()
                .user(user)
                .title(title)
                .description(description)
                .taskDate(resolvedDate)
                .taskTime(taskTime)
                .category(cat)
                .urgency(urg)
                .completed(false)
                .recurrencePattern(recurrencePattern != null && !recurrencePattern.isBlank() ? recurrencePattern.toUpperCase() : null)
                .build();
    }


    private Urgency resolveUrgency(String urgency, Category category) {
        if (urgency != null && !urgency.isBlank()) {
            try {
                return Urgency.valueOf(urgency.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "Unrecognized urgency: '" + urgency + "'. Valid values: URGENT, PRIORITY, OPTIONAL.");
            }
        }


        return config.getDefaultUrgency(category);
    }


    private LocalDate resolveDate(LocalDate taskDate, Category category) {
        if (taskDate != null) return taskDate;

        return config.getDefaultTaskDate(category);
    }
}
