package com.todolist.pattern.creational;

import com.todolist.config.AppConfig;
import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * FACTORY PATTERN — TaskFactory
 *
 * WHY FACTORY:
 * - The creation logic of a Task object is non-trivial:
 *   different categories require different default urgencies and dates.
 * - Instead of scattering "new Task(...)" with all this logic across the codebase,
 *   we centralize it in one Factory class.
 * - The client (TaskFacade) only calls createTask() and gets back
 *   a fully configured Task — without knowing the internal creation details.
 *
 * BENEFIT:
 * - Single Responsibility: creation logic lives in one place.
 * - Easy to extend: adding a new category only requires changing this class.
 *
 * Also uses BUILDER PATTERN (Task.builder()) internally to construct the Task object.
 */
@Component
public class TaskFactory {

    private final AppConfig.ConfigHolder config = AppConfig.ConfigHolder.getInstance();

    /**
     * Creates a Task with category-specific default behaviors applied.
     *
     * @param user        the owner of the task
     * @param title       task title (required)
     * @param description optional description
     * @param taskDate    the date the task belongs to (may be defaulted)
     * @param category    string name of the category (PERSONAL / WORK / SHOPPING)
     * @param urgency     string name of the urgency level (may be null → default applied)
     * @return a fully configured Task object (not yet persisted)
     */
    public Task createTask(User user,
                           String title,
                           String description,
                           LocalDate taskDate,
                           String category,
                           String urgency) {
        return createTask(user, title, description, taskDate, category, urgency, null);
    }

    public Task createTask(User user,
                           String title,
                           String description,
                           LocalDate taskDate,
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

        // Resolve urgency with category-specific defaults
        Urgency urg = resolveUrgency(urgency, cat);

        // Resolve date with category-specific defaults
        LocalDate resolvedDate = resolveDate(taskDate, cat);

        // BUILDER PATTERN: construct the complex Task object step by step
        return Task.builder()
                .user(user)
                .title(title)
                .description(description)
                .taskDate(resolvedDate)
                .category(cat)
                .urgency(urg)
                .completed(false)
                .recurrencePattern(recurrencePattern != null && !recurrencePattern.isBlank() ? recurrencePattern.toUpperCase() : null)
                .build();
    }

    /**
     * Applies category-specific default urgency rules.
     * - PERSONAL  → default OPTIONAL
     * - WORK      → default PRIORITY
     * - SHOPPING  → default OPTIONAL
     */
    private Urgency resolveUrgency(String urgency, Category category) {
        if (urgency != null && !urgency.isBlank()) {
            try {
                return Urgency.valueOf(urgency.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                    "Unrecognized urgency: '" + urgency + "'. Valid values: URGENT, PRIORITY, OPTIONAL.");
            }
        }

        // Apply defaults based on category
        return config.getDefaultUrgency(category);
    }

    /**
     * Applies category-specific default date rules.
     * - SHOPPING: if date is null, default to today + 3 days
     * - Others: default to today
     */
    private LocalDate resolveDate(LocalDate taskDate, Category category) {
        if (taskDate != null) return taskDate;

        return config.getDefaultTaskDate(category);
    }
}
