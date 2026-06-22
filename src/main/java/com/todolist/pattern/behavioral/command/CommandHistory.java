package com.todolist.pattern.behavioral.command;

import com.todolist.service.PatternActivityLog;
import com.todolist.service.NotificationContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Stack;

/**
 * COMMAND PATTERN — Invoker
 *
 * Role: INVOKER — stores and manages the history of executed commands.
 *
 * The Invoker does NOT know the details of what each command does.
 * It simply:
 * 1. Receives commands and executes them via push().
 * 2. Pops the last command and calls undo() when undo is requested.
 *
 * Using a Stack ensures Last-In-First-Out (LIFO) order for undo operations.
 *
 * Scoped as session so that undo history is isolated to each user session.
 */
@Component
@SessionScope
public class CommandHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final PatternActivityLog activityLog;
    private final NotificationContainer notificationContainer;

    // Stack stores executed commands in order — last command on top
    private final Stack<TaskCommand> history = new Stack<>();

    public CommandHistory(PatternActivityLog activityLog, NotificationContainer notificationContainer) {
        this.activityLog = activityLog;
        this.notificationContainer = notificationContainer;
    }

    /**
     * Execute a command and push it to the history stack.
     *
     * @param command the command to execute
     */
    public void push(TaskCommand command) {
        command.execute();
        history.push(command);
        activityLog.addLog("[COMMAND] Push: Executed '" + command.getClass().getSimpleName() + "'");
    }

    /**
     * Undo the most recently executed command (pops from stack and calls undo).
     * Does nothing if the history is empty.
     */
    public void undo() {
        if (!history.isEmpty()) {
            TaskCommand last = history.pop();
            last.undo();
            activityLog.addLog("[COMMAND] Undo: Reverted '" + last.getClass().getSimpleName() + "'");
            notificationContainer.addNotification("Last action (" + last.getClass().getSimpleName() + ") undone successfully.");
        }
    }

    /**
     * Clear all command history (e.g., on logout or session reset).
     */
    public void clear() {
        history.clear();
    }

    /**
     * Check if there are any commands available to undo.
     *
     * @return true if there is at least one command that can be undone
     */
    public boolean isEmpty() {
        return history.isEmpty();
    }
}
