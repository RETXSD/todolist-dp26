package com.todolist.pattern.behavioral.strategy.notification;

import java.time.LocalDateTime;
import java.util.List;

import com.todolist.dto.ReminderNotification;
import com.todolist.model.Task;


public interface NotificationStrategy {

    boolean supports(Task task);

    List<ReminderNotification> buildNotifications(Task task, LocalDateTime now);
}
