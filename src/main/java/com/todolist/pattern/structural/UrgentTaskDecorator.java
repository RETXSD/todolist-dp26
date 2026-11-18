package com.todolist.pattern.structural;


public class UrgentTaskDecorator implements TaskComponent {


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


    @Override
    public String getUrgency() {
        return "URGENT";
    }


    @Override
    public String getDisplayLabel() {
        return " URGENT: " + wrapped.getDisplayLabel();
    }
}
