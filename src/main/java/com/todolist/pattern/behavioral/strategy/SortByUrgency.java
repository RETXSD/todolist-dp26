package com.todolist.pattern.behavioral.strategy;

import com.todolist.config.AppConfig;
import com.todolist.model.Task;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * STRATEGY PATTERN — Concrete Strategy: Sort by Urgency
 *
 * Sorts tasks in the order: URGENT → PRIORITY → OPTIONAL.
 * Tasks with the same urgency are sub-sorted by createdAt (newest first).
 */
@Component("sortByUrgency")
public class SortByUrgency implements SortStrategy {

    private final AppConfig.ConfigHolder config = AppConfig.ConfigHolder.getInstance();

    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator
                        .comparingInt((Task t) -> config.getUrgencyPriority(t.getUrgency()))
                        .thenComparing(t -> t.getCreatedAt() != null ? t.getCreatedAt() : java.time.LocalDateTime.MIN,
                                       Comparator.reverseOrder()))
                .toList();
    }
}
