package com.todolist.pattern.structural;


public class RecurringTaskDecorator implements TaskComponent {


    private final TaskComponent wrapped;


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


    @Override
    public String getDisplayLabel() {
        return wrapped.getDisplayLabel() + " (Recurring: " + recurrencePattern + ")";
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }
}
