package com.todolist.pattern.behavioral.strategy.notification;

import com.todolist.dto.ReminderNotification;
import com.todolist.model.Task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * STRATEGY PATTERN - notification rule interface.
 *
 * Each implementation decides whether it supports a task and what reminder
 * notifications should be generated for that task.
 */
public interface NotificationStrategy {

    boolean supports(Task task);

    List<ReminderNotification> buildNotifications(Task task, LocalDateTime now);
}
