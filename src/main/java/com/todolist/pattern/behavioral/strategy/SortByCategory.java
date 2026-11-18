package com.todolist.pattern.behavioral.strategy;

import com.todolist.model.Task;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;


@Component("sortByCategory")
public class SortByCategory implements SortStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(t -> t.getCategory().name()))
                .toList();
    }
}
