package com.todolist.pattern.behavioral.command;

/**
 * COMMAND PATTERN — Command Interface
 *
 * WHY COMMAND:
 * - We want to encapsulate task actions (complete, delete) as objects,
 *   so they can be stored, queued, and undone.
 * - This separates "what to do" from "who does it".
 *
 * Roles in this pattern:
 * - Command Interface  → TaskCommand (this file)
 * - Concrete Commands  → CompleteTaskCommand, DeleteTaskCommand
 * - Invoker           → CommandHistory (stores + triggers commands)
 * - Receiver          → TaskRepository (performs the actual DB operation)
 *
 * BENEFIT:
 * - Undo/redo support is easy: just call undo() on the last command.
 * - Commands can be queued, logged, or even serialized.
 */
public interface TaskCommand {

    /**
     * Execute the command (perform the action).
     */
    void execute();

    /**
     * Undo the previously executed command (reverse the action).
     */
    void undo();
}
