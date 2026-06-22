package com.todolist.pattern.structural;

import com.todolist.model.Task;

/**
 * DECORATOR PATTERN — Concrete Component
 *
 * Role: CONCRETE COMPONENT (the base, non-decorated implementation)
 *
 * Wraps a real JPA Task entity and provides the base behavior
 * that decorators will then extend.
 */
public class BaseTask implements TaskComponent {

    // The actual Task entity being wrapped
    private final Task task;

    public BaseTask(Task task) {
        this.task = task;
    }

    @Override
    public String getTitle() {
        return task.getTitle();
    }

    @Override
    public String getDescription() {
        return task.getDescription();
    }

    @Override
    public String getUrgency() {
        return task.getUrgency().name();
    }

    /**
     * Base label format: "[CATEGORY] title"
     * Example: "[WORK] Submit design pattern report"
     */
    @Override
    public String getDisplayLabel() {
        return "[" + task.getCategory().name() + "] " + task.getTitle();
    }
}
