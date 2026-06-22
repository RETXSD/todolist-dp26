package com.todolist.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores activity logs of design patterns executed during the user's session.
 * Used to display a real-time console of design patterns in action in the UI.
 */
@Component
@SessionScope
public class PatternActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<String> logs = new ArrayList<>();

    public void addLog(String log) {
        logs.add(0, log); // Add to top (newest first)
        if (logs.size() > 15) {
            logs.remove(logs.size() - 1); // Keep last 15 logs
        }
    }

    public List<String> getLogs() {
        return logs;
    }

    public void clear() {
        logs.clear();
    }
}
