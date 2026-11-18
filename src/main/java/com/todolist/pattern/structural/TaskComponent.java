package com.todolist.pattern.structural;


public interface TaskComponent {

    String getTitle();

    String getDescription();

    String getUrgency();


    String getDisplayLabel();
}
