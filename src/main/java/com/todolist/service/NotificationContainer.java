package com.todolist.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Component
@SessionScope
public class NotificationContainer implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<String> notifications = new ArrayList<>();

    public void addNotification(String message) {
        notifications.add(message);
    }

    public List<String> consumeNotifications() {
        List<String> consumed = new ArrayList<>(notifications);
        notifications.clear();
        return consumed;
    }
}
