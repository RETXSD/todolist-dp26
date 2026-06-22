package com.todolist.controller;

import com.todolist.dto.TaskRequest;
import com.todolist.dto.TaskResponse;
import com.todolist.facade.TaskFacade;
import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.model.User;
import com.todolist.pattern.behavioral.state.TaskState;
import com.todolist.pattern.behavioral.state.TaskStateResolver;
import com.todolist.pattern.structural.BaseTask;
import com.todolist.pattern.structural.RecurringTaskDecorator;
import com.todolist.pattern.structural.TaskComponent;
import com.todolist.pattern.structural.UrgentTaskDecorator;
import com.todolist.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Main task controller — handles the task list page and all task actions.
 * Delegates all business logic to TaskFacade (Facade Pattern).
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskFacade  taskFacade;
    private final UserService userService;
    private final com.todolist.service.PatternActivityLog activityLog;
    private final com.todolist.service.NotificationContainer notificationContainer;

    public TaskController(TaskFacade taskFacade, UserService userService, com.todolist.service.PatternActivityLog activityLog, com.todolist.service.NotificationContainer notificationContainer) {
        this.taskFacade  = taskFacade;
        this.userService = userService;
        this.activityLog = activityLog;
        this.notificationContainer = notificationContainer;
    }

    // ----------------------------------------------------------------
    // Main task list page
    // ----------------------------------------------------------------

    @GetMapping
    public String taskListPage(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String sort,
            Authentication auth,
            Model model) {

        User currentUser = getUser(auth);
        LocalDate selectedDate = (date != null) ? date : LocalDate.now();

        List<Task> tasks;

        // Facade hides all the pattern complexity — just call the right method
        if (sort != null && !sort.isBlank()) {
            tasks = taskFacade.getTasksSorted(currentUser.getId(), selectedDate, sort);
        } else if (category != null && !category.isBlank()) {
            tasks = taskFacade.getTasksByDateAndCategory(currentUser.getId(), selectedDate, category);
        } else if (urgency != null && !urgency.isBlank()) {
            tasks = taskFacade.getTasksByDateAndUrgency(currentUser.getId(), selectedDate, urgency);
        } else {
            tasks = taskFacade.getTasksByDate(currentUser.getId(), selectedDate);
        }

        // Map to TaskResponse (applying Decorator Pattern and State Pattern)
        List<TaskResponse> taskResponses = tasks.stream()
                .map(this::toResponse)
                .toList();

        model.addAttribute("tasks",       taskResponses);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("username",    currentUser.getUsername());
        model.addAttribute("categories",  Category.values());
        model.addAttribute("urgencies",   Urgency.values());
        model.addAttribute("taskDates",   taskFacade.getTaskDatesForUser(currentUser.getId()));
        model.addAttribute("newTask",     new TaskRequest());
        model.addAttribute("patternLogs", activityLog.getLogs());
        model.addAttribute("notifications", notificationContainer.consumeNotifications());
        model.addAttribute("appName",      com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppName());
        model.addAttribute("appVersion",   com.todolist.config.AppConfig.ConfigHolder.getInstance().getAppVersion());
        model.addAttribute("overdueCount",  taskFacade.getOverdueCount(currentUser.getId()));

        return "tasks";
    }

    // ----------------------------------------------------------------
    // Create task
    // ----------------------------------------------------------------

    @PostMapping("/create")
    public String createTask(@ModelAttribute TaskRequest req,
                             @RequestParam(required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate redirectDate,
                             Authentication auth) {
        User currentUser = getUser(auth);
        taskFacade.createTask(
                currentUser,
                req.getTitle(),
                req.getDescription(),
                req.getTaskDate() != null ? req.getTaskDate() : LocalDate.now(),
                req.getCategory(),
                req.getUrgency(),
                req.getRecurrencePattern()
        );
        LocalDate date = req.getTaskDate() != null ? req.getTaskDate() : LocalDate.now();
        return "redirect:/tasks?date=" + date;
    }

    // ----------------------------------------------------------------
    // Edit task
    // ----------------------------------------------------------------

    @PostMapping("/{id}/edit")
    public String editTask(@PathVariable Long id,
                           @ModelAttribute TaskRequest req,
                           @RequestParam(required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate redirectDate) {
        taskFacade.editTask(
                id,
                req.getTitle(),
                req.getDescription(),
                req.getTaskDate() != null ? req.getTaskDate() : LocalDate.now(),
                req.getCategory(),
                req.getUrgency(),
                req.getRecurrencePattern()
        );
        LocalDate date = req.getTaskDate() != null ? req.getTaskDate() : LocalDate.now();
        return "redirect:/tasks?date=" + date;
    }

    // ----------------------------------------------------------------
    // Complete task
    // ----------------------------------------------------------------

    @PostMapping("/{id}/complete")
    public Object completeTask(@PathVariable Long id,
                               @RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
        taskFacade.completeTask(id);
        if ("XMLHttpRequest".equals(requestedWith)) {
            // Retrieve notifications generated by the Observer Pattern!
            List<String> list = notificationContainer.consumeNotifications();
            return org.springframework.http.ResponseEntity.ok().body(list);
        }
        return "redirect:/tasks?date=" + (date != null ? date : LocalDate.now());
    }

    // ----------------------------------------------------------------
    // Delete task
    // ----------------------------------------------------------------

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id,
                             @RequestParam(required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        taskFacade.deleteTask(id);
        return "redirect:/tasks?date=" + (date != null ? date : LocalDate.now());
    }

    // ----------------------------------------------------------------
    // Undo last action
    // ----------------------------------------------------------------

    @PostMapping("/undo")
    public String undoLastAction(@RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        taskFacade.undoLastAction();
        return "redirect:/tasks?date=" + (date != null ? date : LocalDate.now());
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    /**
     * Gets the currently logged-in User entity from the SecurityContext.
     */
    private User getUser(Authentication auth) {
        return userService.findByUsername(auth.getName());
    }

    /**
     * Maps a Task entity to TaskResponse DTO, applying:
     * - Decorator Pattern for displayLabel.
     * - State Pattern for status label and CSS class.
     */
    private TaskResponse toResponse(Task task) {
        // Apply Decorator Pattern to get the display label
        TaskComponent component = new BaseTask(task);
        if (task.getUrgency() == Urgency.URGENT) {
            component = new UrgentTaskDecorator(component);
        }
        if (task.getRecurrencePattern() != null && !task.getRecurrencePattern().isBlank()) {
            component = new RecurringTaskDecorator(component, task.getRecurrencePattern());
        }

        TaskState state = TaskStateResolver.resolve(task, LocalDate.now());

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskDate(task.getTaskDate())
                .category(task.getCategory().name())
                .urgency(task.getUrgency().name())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .recurrencePattern(task.getRecurrencePattern())
                .displayLabel(component.getDisplayLabel())
                .statusLabel(state.getStatusLabel())
                .statusCssClass(state.getCssClass())
                .actionable(state.isActionable())
                .build();
    }
}
