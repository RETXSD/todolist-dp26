package com.todolist.pattern.behavioral.strategy.notification;

import com.todolist.dto.ReminderNotification;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * STRATEGY PATTERN - concrete notification rule for urgent tasks.
 *
 * Urgent tasks produce two reminders: one hour before and one hour after the
 * task time. Completed tasks are ignored by supports().
 */
@Component
public class UrgentNotificationStrategy implements NotificationStrategy {

    @Override
    public boolean supports(Task task) {
        return task != null && !task.isCompleted() && task.getUrgency() == Urgency.URGENT;
    }

    @Override
    public List<ReminderNotification> buildNotifications(Task task, LocalDateTime now) {
        List<ReminderNotification> notifications = new ArrayList<>();

        if (task.getTaskTime() == null) {
            notifications.add(new ReminderNotification(
                    task.getId(),
                    task.getTitle(),
                    task.getTaskDate(),
                    null,
                    null,
                    "Needs Time",
                    "Set a task time so urgent reminders can be scheduled.",
                    "Needs Time",
                    "badge-priority",
                    false
            ));
            return notifications;
        }

        LocalDateTime dueAt = LocalDateTime.of(task.getTaskDate(), task.getTaskTime());
        LocalDateTime beforeAt = dueAt.minusHours(1);
        LocalDateTime afterAt = dueAt.plusHours(1);

        notifications.add(buildReminder(
                task,
                beforeAt,
                "Before",
                "Urgent task starts in one hour.",
                isInWindow(now, beforeAt, dueAt),
                now
        ));
        notifications.add(buildReminder(
                task,
                afterAt,
                "After",
                "Urgent task passed one hour ago. Check if it is done.",
                isInWindow(now, dueAt, afterAt),
                now
        ));

        return notifications;
    }

    private ReminderNotification buildReminder(Task task, LocalDateTime triggerAt, String type,
                                               String message, boolean active, LocalDateTime now) {
        String statusLabel;
        String statusCssClass;

        if (active) {
            statusLabel = "Active";
            statusCssClass = "badge-urgent";
        } else if (now.isBefore(triggerAt)) {
            statusLabel = "Scheduled";
            statusCssClass = "badge-optional";
        } else {
            statusLabel = "Passed";
            statusCssClass = "badge-priority";
        }

        return new ReminderNotification(
                task.getId(),
                task.getTitle(),
                task.getTaskDate(),
                task.getTaskTime(),
                triggerAt,
                type,
                message,
                statusLabel,
                statusCssClass,
                active
        );
    }

    private boolean isInWindow(LocalDateTime now, LocalDateTime start, LocalDateTime end) {
        return !now.isBefore(start) && !now.isAfter(end);
    }
}
