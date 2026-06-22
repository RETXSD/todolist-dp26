package com.todolist.pattern.structural;

/**
 * DECORATOR PATTERN — Concrete Decorator (Urgency)
 *
 * Role: CONCRETE DECORATOR
 *
 * Wraps any TaskComponent and adds urgent-specific behavior:
 * - Prepends "🔴 URGENT: " to the display label.
 * - Always returns "URGENT" as the urgency, regardless of wrapped component.
 *
 * This adds behavior WITHOUT modifying the original Task class.
 */
public class UrgentTaskDecorator implements TaskComponent {

    // The wrapped component (can be BaseTask or another decorator)
    private final TaskComponent wrapped;

    public UrgentTaskDecorator(TaskComponent wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getTitle() {
        return wrapped.getTitle();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    /**
     * Always returns "URGENT" — overrides whatever the wrapped component returns.
     */
    @Override
    public String getUrgency() {
        return "URGENT";
    }

    /**
     * Adds "🔴 URGENT: " prefix to the wrapped component's display label.
     * Example: "🔴 URGENT: [WORK] Submit design pattern report"
     */
    @Override
    public String getDisplayLabel() {
        return "🔴 URGENT: " + wrapped.getDisplayLabel();
    }
}
