package com.todolist.pattern.behavioral.strategy;

import com.todolist.model.Task;

import java.util.List;

/**
 * STRATEGY PATTERN — Strategy Interface
 *
 * WHY STRATEGY:
 * - The sorting algorithm for tasks needs to change at runtime
 *   based on user preference (sort by urgency / category / date).
 * - Instead of using if-else chains in the service layer,
 *   each algorithm is encapsulated in its own class implementing this interface.
 * - The client (TaskFacade) simply holds a reference to a SortStrategy
 *   and calls sort() — it doesn't need to know WHICH algorithm is being used.
 *
 * BENEFIT:
 * - Open/Closed Principle: add a new sort algorithm without touching existing code.
 * - Algorithms are swappable at runtime by injecting a different strategy bean.
 */
public interface SortStrategy {

    /**
     * Sorts the given list of tasks according to this strategy's algorithm.
     *
     * @param tasks the list to sort (will not modify original list)
     * @return a new sorted list
     */
    List<Task> sort(List<Task> tasks);
}
