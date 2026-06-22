package com.todolist.pattern.behavioral.strategy;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.todolist.model.Task;

/**
 * STRATEGY PATTERN — Concrete Strategy: Sort by Created Date
 *
 * Sorts tasks descending by createdAt (newest tasks first).
 */
@Component("sortByCreatedDate")
public class SortByCreatedDate implements SortStrategy {

    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(
                        (Task t) -> t.getCreatedAt() != null ? t.getCreatedAt() : LocalDateTime.MIN,
                        Comparator.reverseOrder()))
                .toList();
    }
}
