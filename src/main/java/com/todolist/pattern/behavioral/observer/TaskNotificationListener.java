package com.todolist.pattern.behavioral.observer;

import com.todolist.model.Task;
import com.todolist.service.PatternActivityLog;
import com.todolist.service.NotificationContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * OBSERVER PATTERN — Concrete Observer (Listener)
 *
 * Role: CONCRETE OBSERVER — reacts directly to event callbacks from the Subject.
 * Writes logs to session-scoped PatternActivityLog and user notifications to NotificationContainer.
 */
@Component
public class TaskNotificationListener implements TaskObserver {

    private static final Logger log = LoggerFactory.getLogger(TaskNotificationListener.class);
    
    private final PatternActivityLog activityLog;
    private final NotificationContainer notificationContainer;

    public TaskNotificationListener(PatternActivityLog activityLog, NotificationContainer notificationContainer) {
        this.activityLog = activityLog;
        this.notificationContainer = notificationContainer;
    }

    @Override
    public void onTaskCompleted(Task task) {
        String displayMsg = "[OBSERVER] TaskCompletedEvent: Toggle complete for '" + task.getTitle() + "' -> " + (task.isCompleted() ? "COMPLETED" : "INCOMPLETE");
        log.info(displayMsg);
        activityLog.addLog(displayMsg);
        notificationContainer.addNotification("Task '" + task.getTitle() + "' marked as " + (task.isCompleted() ? "completed" : "incomplete") + "!");
    }

    @Override
    public void onTaskDeleted(Task task) {
        String displayMsg = "[OBSERVER] TaskDeletedEvent: Deleted task '" + task.getTitle() + "'";
        log.info(displayMsg);
        activityLog.addLog(displayMsg);
        notificationContainer.addNotification("Task '" + task.getTitle() + "' deleted successfully.");
    }

    @Override
    public void onTaskCreated(Task task) {
        String displayMsg = "[OBSERVER] TaskCreatedEvent: Created task '" + task.getTitle() + "'";
        log.info(displayMsg);
        activityLog.addLog(displayMsg);
        notificationContainer.addNotification("Task '" + task.getTitle() + "' created successfully!");
    }

    @Override
    public void onTaskEdited(Task task) {
        String displayMsg = "[OBSERVER] TaskEditedEvent: Edited task '" + task.getTitle() + "'";
        log.info(displayMsg);
        activityLog.addLog(displayMsg);
        notificationContainer.addNotification("Task '" + task.getTitle() + "' updated successfully!");
    }
}
