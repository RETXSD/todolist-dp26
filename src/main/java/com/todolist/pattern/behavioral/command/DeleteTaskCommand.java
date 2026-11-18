package com.todolist.pattern.behavioral.command;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;


public class DeleteTaskCommand implements TaskCommand {


    private final TaskRepository taskRepository;

    private final Task task;


    private Task deletedTaskSnapshot;

    public DeleteTaskCommand(Task task, TaskRepository taskRepository) {
        this.task           = task;
        this.taskRepository = taskRepository;
    }


    @Override
    public void execute() {


        this.deletedTaskSnapshot = Task.builder()
                .user(task.getUser())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskDate(task.getTaskDate())
                .taskTime(task.getTaskTime())
                .category(task.getCategory())
                .urgency(task.getUrgency())
                .recurrencePattern(task.getRecurrencePattern())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();

        taskRepository.delete(task);
    }


    @Override
    public void undo() {
        taskRepository.save(deletedTaskSnapshot);
    }
}
