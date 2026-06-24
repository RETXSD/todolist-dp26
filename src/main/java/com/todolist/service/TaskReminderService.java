package com.todolist.service;

import com.todolist.dto.ReminderNotification;
import com.todolist.model.Task;
import com.todolist.pattern.behavioral.strategy.notification.NotificationStrategy;
import com.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Generates task reminders by delegating rule decisions to NotificationStrategy.
 */
@Service
public class TaskReminderService {

    private final TaskRepository taskRepository;
    private final List<NotificationStrategy> notificationStrategies;

    public TaskReminderService(TaskRepository taskRepository,
                               List<NotificationStrategy> notificationStrategies) {
        this.taskRepository = taskRepository;
        this.notificationStrategies = notificationStrategies;
    }

    public List<ReminderNotification> getUrgentNotifications(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.findUrgentUncompletedByUserId(userId);

        return tasks.stream()
                .flatMap(task -> notificationStrategies.stream()
                        .filter(strategy -> strategy.supports(task))
                        .flatMap(strategy -> strategy.buildNotifications(task, now).stream()))
                .sorted(Comparator
                        .comparing(ReminderNotification::isActive).reversed()
                        .thenComparing(notification -> notification.getTriggerAt() != null
                                ? notification.getTriggerAt()
                                : LocalDateTime.MAX))
                .toList();
    }
}
