package com.todolist.pattern.behavioral.strategy;

import com.todolist.model.Task;
import com.todolist.model.Urgency;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * STRATEGY PATTERN — Concrete Strategy: Sort by Urgency
 *
 * Sorts tasks in the order: URGENT → PRIORITY → OPTIONAL.
 * Tasks with the same urgency are sub-sorted by createdAt (newest first).
 */
@Component("sortByUrgency")
public class SortByUrgency implements SortStrategy {

    // Define urgency priority order (lower number = higher priority)
    private static final Map<Urgency, Integer> URGENCY_ORDER = Map.of(
        Urgency.URGENT,   0,
        Urgency.PRIORITY, 1,
        Urgency.OPTIONAL, 2
    );

    @Override
    public List<Task> sort(List<Task> tasks) {
        return tasks.stream()
                .sorted(Comparator
                        .comparingInt((Task t) -> URGENCY_ORDER.getOrDefault(t.getUrgency(), 99))
                        .thenComparing(t -> t.getCreatedAt() != null ? t.getCreatedAt() : java.time.LocalDateTime.MIN,
                                       Comparator.reverseOrder()))
                .toList();
    }
}
