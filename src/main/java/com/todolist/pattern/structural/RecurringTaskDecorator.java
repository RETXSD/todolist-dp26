package com.todolist.pattern.structural;

/**
 * DECORATOR PATTERN — Concrete Decorator (Recurring)
 *
 * Role: CONCRETE DECORATOR
 *
 * Wraps any TaskComponent and adds recurring-task behavior:
 * - Appends " (Recurring)" suffix to the display label.
 * - Stores the recurrence pattern (DAILY, WEEKLY, MONTHLY).
 *
 * Decorators can be stacked:
 * e.g., new RecurringTaskDecorator(new UrgentTaskDecorator(new BaseTask(task)), "DAILY")
 */
public class RecurringTaskDecorator implements TaskComponent {

    // The wrapped component (can be BaseTask or another decorator)
    private final TaskComponent wrapped;

    /**
     * Recurrence frequency: DAILY, WEEKLY, or MONTHLY.
     */
    private final String recurrencePattern;

    public RecurringTaskDecorator(TaskComponent wrapped, String recurrencePattern) {
        this.wrapped            = wrapped;
        this.recurrencePattern  = recurrencePattern;
    }

    @Override
    public String getTitle() {
        return wrapped.getTitle();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public String getUrgency() {
        return wrapped.getUrgency();
    }

    /**
     * Appends recurrence info to the wrapped display label.
     * Example: "[PERSONAL] Morning jog (Recurring: DAILY)"
     */
    @Override
    public String getDisplayLabel() {
        return wrapped.getDisplayLabel() + " (Recurring: " + recurrencePattern + ")";
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }
}
