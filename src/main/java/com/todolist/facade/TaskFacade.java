package com.todolist.facade;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.todolist.config.AppConfig;
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

    public Task createTask(User user, String title, String description,
                           LocalDate taskDate, String category, String urgency) {
        return createTask(user, title, description, taskDate, null, category, urgency, null);
    }

    public Task createTask(User user, String title, String description,
                           LocalDate taskDate, String category, String urgency, String recurrencePattern) {
        return createTask(user, title, description, taskDate, null, category, urgency, recurrencePattern);
    }

    public Task createTask(User user, String title, String description,
                           LocalDate taskDate, LocalTime taskTime, String category, String urgency, String recurrencePattern) {
        Task task = taskFactory.createTask(user, title, description, taskDate, taskTime, category, urgency, recurrencePattern);
        CreateTaskCommand cmd = new CreateTaskCommand(task, taskRepository);
        commandHistory.push(cmd);
        Task savedTask = cmd.getTask();
        eventPublisher.publishTaskCreated(savedTask);
        return savedTask;
    }

    public void editTask(Long taskId, String title, String description,
                         LocalDate taskDate, LocalTime taskTime, String category, String urgency, String recurrencePattern) {
        Task task = findTaskById(taskId);
        Category cat = Category.valueOf(category.toUpperCase());
        Urgency urg = urgency != null && !urgency.isBlank()
                ? Urgency.valueOf(urgency.toUpperCase())
                : AppConfig.ConfigHolder.getInstance().getDefaultUrgency(cat);
        String rec = recurrencePattern != null && !recurrencePattern.isBlank() ? recurrencePattern.toUpperCase() : null;

        EditTaskCommand cmd = new EditTaskCommand(task, title, description, taskDate, taskTime, cat, urg, rec, taskRepository);
        commandHistory.push(cmd);
        eventPublisher.publishTaskEdited(task);
    }

    public Task completeTask(Long taskId) {
        Task task = findTaskById(taskId);
        CompleteTaskCommand cmd = new CompleteTaskCommand(task, taskRepository);
        commandHistory.push(cmd);
        eventPublisher.publishTaskCompleted(task);
        return task;
    }

    public void deleteTask(Long taskId) {
        Task task = findTaskById(taskId);
        DeleteTaskCommand cmd = new DeleteTaskCommand(task, taskRepository);
        commandHistory.push(cmd);
        eventPublisher.publishTaskDeleted(task);
    }

    public void undoLastAction() {
        commandHistory.undo();
    }

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

    public List<Task> getTasksSorted(Long userId, LocalDate date, String sortType) {
        List<Task> tasks = taskRepository.findByUserIdAndTaskDate(userId, date);

        SortStrategy strategy = switch (sortType.toLowerCase()) {
            case "category"    -> sortByCategory;
            case "date"        -> sortByCreatedDate;
            default            -> sortByUrgency;
        };

        return strategy.sort(tasks);
    }

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
