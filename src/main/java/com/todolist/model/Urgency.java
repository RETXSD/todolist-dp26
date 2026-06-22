package com.todolist.model;

/**
 * Enum representing task urgency levels.
 * Order matters: URGENT > PRIORITY > OPTIONAL (used in Strategy Pattern sorting).
 */
public enum Urgency {
    URGENT,
    PRIORITY,
    OPTIONAL
}
