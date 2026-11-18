package com.todolist.pattern.behavioral.strategy;

import java.util.List;

import com.todolist.model.Task;


public interface SortStrategy {


    List<Task> sort(List<Task> tasks);
}
