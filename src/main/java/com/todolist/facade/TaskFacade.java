package com.todolist.facade;

import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.model.User;
import com.todolist.pattern.behavioral.command.CommandHistory;
import com.todolist.pattern.behavioral.command.CompleteTaskCommand;
import com.todolist.pattern.behavioral.command.CreateTaskCommand;
import com.todolist.pattern.behavioral.command.DeleteTaskCommand;
import com.todolist.pattern.behavioral.command.EditTaskCommand;
import com.todolist.pattern.behavioral.observer.TaskEventPublisher;
import com.todolist.pattern.behavioral.strategy.SortStrategy;
import com.todolist.pattern.creational.TaskFactory;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * FACADE PATTERN — TaskFacade
 *
 * WHY FACADE:
 * - The controller should not need to know about Factory, Strategy, Command,
 *   Observer, and Repository all at once. That would create massive coupling.
 * - TaskFacade provides a SINGLE, simplified interface that hides the complexity
 *   of all these interacting patterns behind clean, high-level methods.
 * - The controller just calls createTask(), completeTask(), deleteTask(), etc.
 *   and the Facade orchestrates all the moving parts internally.
 *
 * BENEFIT:
 * - Reduces controller complexity dramatically.
 * - Single point of change: if internal pattern structure changes,
 *   only the Facade needs to be updated — not every controller method.
 */
@Service
public class TaskFacade {

    private final TaskRepository     taskRepository;
    private final TaskFactory        taskFactory;
    private final TaskEventPublisher eventPublisher;
    private final CommandHistory     commandHistory;
    private final SortStrategy       sortByUrgency;
    private final SortStrategy       sortByCategory;
    private final SortStrategy       sortByCreatedDate;

    public TaskFacade(
            TaskRepository taskRepository,
            TaskFactory taskFactory,
            TaskEventPublisher eventPublisher,
            CommandHistory commandHistory,
            @Qualifier("sortByUrgency")     SortStrategy sortByUrgency,
            @Qualifier("sortByCategory")    SortStrategy sortByCategory,
            @Qualifier("sortByCreatedDate") SortStrategy sortByCreatedDate) {

        this.taskRepository   = taskRepository;
        this.taskFactory      = taskFactory;
        this.eventPublisher   = eventPublisher;
        this.commandHistory   = commandHistory;
        this.sortByUrgency    = sortByUrgency;
        this.sortByCategory   = sortByCategory;
        this.sortByCreatedDate = sortByCreatedDate;
    }

    // ----------------------------------------------------------------
    // 1. Create Task (Factory + Builder + Command + Persist)
    // ----------------------------------------------------------------

    public Task createTask(User user, String title, String description,
                           LocalDate taskDate, String category, String urgency) {
        return createTask(user, title, description, taskDate, category, urgency, null);
    }

    public Task createTask(User user, String title, String description,
                           LocalDate taskDate, String category, String urgency, String recurrencePattern) {
        // Factory creates the Task with proper defaults applied
        Task task = taskFactory.createTask(user, title, description, taskDate, category, urgency, recurrencePattern);
        CreateTaskCommand cmd = new CreateTaskCommand(task, taskRepository);
        commandHistory.push(cmd);
        Task savedTask = cmd.getTask();
        eventPublisher.publishTaskCreated(savedTask); // Publish event
        return savedTask;
    }

    // ----------------------------------------------------------------
    // 1b. Edit Task (Command + Persist)
    // ----------------------------------------------------------------

    public void editTask(Long taskId, String title, String description,
                         LocalDate taskDate, String category, String urgency, String recurrencePattern) {
        Task task = findTaskById(taskId);
        Category cat = Category.valueOf(category.toUpperCase());
        Urgency urg = Urgency.valueOf(urgency.toUpperCase());
        String rec = recurrencePattern != null && !recurrencePattern.isBlank() ? recurrencePattern.toUpperCase() : null;

        EditTaskCommand cmd = new EditTaskCommand(task, title, description, taskDate, cat, urg, rec, taskRepository);
        commandHistory.push(cmd);
        eventPublisher.publishTaskEdited(task); // Publish event
    }

    // ----------------------------------------------------------------
    // 2. Complete Task (Command + Observer)
    // ----------------------------------------------------------------

    public Task completeTask(Long taskId) {
        Task task = findTaskById(taskId);
        // Command encapsulates the action (with undo support)
        CompleteTaskCommand cmd = new CompleteTaskCommand(task, taskRepository);
        commandHistory.push(cmd); // Invoker: execute + push to history
        // Observer: publish event so listeners can react
        eventPublisher.publishTaskCompleted(task);
        return task;
    }

    // ----------------------------------------------------------------
    // 3. Delete Task (Command + Observer)
    // ----------------------------------------------------------------

    public void deleteTask(Long taskId) {
        Task task = findTaskById(taskId);
        // Command encapsulates the action (with undo support)
        DeleteTaskCommand cmd = new DeleteTaskCommand(task, taskRepository);
        commandHistory.push(cmd); // Invoker: execute + push to history
        // Observer: publish event so listeners can react
        eventPublisher.publishTaskDeleted(task);
    }

    // ----------------------------------------------------------------
    // 4. Undo Last Action (Command)
    // ----------------------------------------------------------------

    public void undoLastAction() {
        commandHistory.undo();
    }

    // ----------------------------------------------------------------
    // 5-7. Get Tasks (filtered queries)
    // ----------------------------------------------------------------

    public List<Task> getTasksByDate(Long userId, LocalDate date) {
        return taskRepository.findByUserIdAndTaskDate(userId, date);
    }

    public List<Task> getTasksByDateAndCategory(Long userId, LocalDate date, String category) {
        Category cat = Category.valueOf(category.toUpperCase());
        return taskRepository.findByUserIdAndTaskDateAndCategory(userId, date, cat);
    }

    public List<Task> getTasksByDateAndUrgency(Long userId, LocalDate date, String urgency) {
        Urgency urg = Urgency.valueOf(urgency.toUpperCase());
        return taskRepository.findByUserIdAndTaskDateAndUrgency(userId, date, urg);
    }

    // ----------------------------------------------------------------
    // 8. Get Tasks Sorted (Strategy Pattern)
    // ----------------------------------------------------------------

    public List<Task> getTasksSorted(Long userId, LocalDate date, String sortType) {
        List<Task> tasks = taskRepository.findByUserIdAndTaskDate(userId, date);

        // Pick the correct Strategy at runtime based on the sortType parameter
        SortStrategy strategy = switch (sortType.toLowerCase()) {
            case "category"    -> sortByCategory;
            case "date"        -> sortByCreatedDate;
            default            -> sortByUrgency; // default: sort by urgency
        };

        return strategy.sort(tasks);
    }

    // ----------------------------------------------------------------
    // Helper
    // ----------------------------------------------------------------

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
    }

    public List<LocalDate> getTaskDatesForUser(Long userId) {
        return taskRepository.findDistinctTaskDateByUserId(userId);
    }

    public long getOverdueCount(Long userId) {
        return taskRepository.countByUserIdAndTaskDateBeforeAndCompletedFalse(userId, LocalDate.now());
    }
}
