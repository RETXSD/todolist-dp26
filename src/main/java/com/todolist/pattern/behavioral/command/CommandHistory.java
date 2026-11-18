package com.todolist.pattern.behavioral.command;

import java.io.Serializable;
import java.util.Stack;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.todolist.service.NotificationContainer;
import com.todolist.service.PatternActivityLog;


@Component
@SessionScope
public class CommandHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PatternActivityLog activityLog;
    private final NotificationContainer notificationContainer;


    private final Stack<TaskCommand> history = new Stack<>();

    public CommandHistory(PatternActivityLog activityLog, NotificationContainer notificationContainer) {
        this.activityLog = activityLog;
        this.notificationContainer = notificationContainer;
    }


    public void push(TaskCommand command) {
        command.execute();
        history.push(command);
        activityLog.addLog("[COMMAND] Push: Executed '" + command.getClass().getSimpleName() + "'");
    }


    public void undo() {
        if (!history.isEmpty()) {
            TaskCommand last = history.pop();
            last.undo();
            activityLog.addLog("[COMMAND] Undo: Reverted '" + last.getClass().getSimpleName() + "'");
            notificationContainer.addNotification("Last action (" + last.getClass().getSimpleName() + ") undone successfully.");
        }
    }


    public void clear() {
        history.clear();
    }


    public boolean isEmpty() {
        return history.isEmpty();
    }
}
