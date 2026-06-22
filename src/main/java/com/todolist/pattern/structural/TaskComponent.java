package com.todolist.pattern.structural;

/**
 * DECORATOR PATTERN — Component Interface
 *
 * Role: COMPONENT (the abstract interface)
 *
 * WHY DECORATOR:
 * - We want to add extra behaviors (e.g., urgency label, recurring badge)
 *   to a Task object at runtime WITHOUT modifying the Task JPA entity class.
 * - Each decorator wraps a TaskComponent and adds its own behavior
 *   on top of what the wrapped component returns.
 *
 * This interface defines the contract that both the base task
 * and all decorators must implement.
 */
public interface TaskComponent {

    String getTitle();

    String getDescription();

    String getUrgency();

    /**
     * Returns a human-readable display label.
     * Each decorator adds its own prefix/suffix to this string.
     */
    String getDisplayLabel();
}
