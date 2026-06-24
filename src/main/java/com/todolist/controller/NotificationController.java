package com.todolist.controller;

import com.todolist.dto.ReminderNotification;
import com.todolist.model.User;
import com.todolist.service.TaskReminderService;
import com.todolist.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Shows reminder notifications generated from urgent task notification strategies.
 */
@Controller
public class NotificationController {

    private final UserService userService;
    private final TaskReminderService taskReminderService;

    public NotificationController(UserService userService, TaskReminderService taskReminderService) {
        this.userService = userService;
        this.taskReminderService = taskReminderService;
    }

    @GetMapping("/notifications")
    public String notificationsPage(Authentication auth, Model model) {
        User currentUser = userService.findByUsername(auth.getName());
        List<ReminderNotification> reminders = taskReminderService.getUrgentNotifications(currentUser.getId());

        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("appName", com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppName());
        model.addAttribute("appVersion", com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppVersion());
        model.addAttribute("now", LocalDateTime.now());
        model.addAttribute("reminders", reminders);
        model.addAttribute("activeReminderCount", reminders.stream().filter(ReminderNotification::isActive).count());

        return "notifications";
    }
}
