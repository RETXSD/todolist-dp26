# 📋 Todo List App — Design Pattern (Java Spring Boot)
> Full guide + AI prompts — updated with user auth, categories, daily filter, urgency levels

---

## 🗂️ Database Schema (MySQL)

```sql
CREATE DATABASE todolist_db;
USE todolist_db;

-- Users table
CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tasks table
CREATE TABLE tasks (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    task_date    DATE         NOT NULL,               -- chosen day
    category     ENUM('PERSONAL','WORK','SHOPPING') NOT NULL,
    urgency      ENUM('URGENT','PRIORITY','OPTIONAL') NOT NULL DEFAULT 'OPTIONAL',
    completed    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Index for the daily filter API
CREATE INDEX idx_tasks_user_date     ON tasks (user_id, task_date);
CREATE INDEX idx_tasks_user_urgency  ON tasks (user_id, urgency);
CREATE INDEX idx_tasks_user_category ON tasks (user_id, category);
```

### Useful Queries

```sql
-- Get all tasks for a user on a specific day
SELECT * FROM tasks
WHERE user_id = ? AND task_date = '2025-01-15'
ORDER BY FIELD(urgency, 'URGENT','PRIORITY','OPTIONAL'), created_at;

-- Get tasks filtered by day + category
SELECT * FROM tasks
WHERE user_id = ? AND task_date = ? AND category = ?
ORDER BY FIELD(urgency, 'URGENT','PRIORITY','OPTIONAL');

-- Get tasks filtered by day + urgency
SELECT * FROM tasks
WHERE user_id = ? AND task_date = ? AND urgency = ?;

-- Count tasks per urgency for a given day (dashboard stats)
SELECT urgency, COUNT(*) AS total, SUM(completed) AS done
FROM tasks
WHERE user_id = ? AND task_date = ?
GROUP BY urgency;

-- Get all distinct dates that have tasks (for calendar highlight)
SELECT DISTINCT task_date FROM tasks
WHERE user_id = ?
ORDER BY task_date;
```

---

## 🗂️ Project Structure

```
todolist-app/
├── src/main/java/com/todolist/
│   ├── TodolistApplication.java
│   ├── config/
│   │   ├── AppConfig.java                  ← Singleton
│   │   ├── SecurityConfig.java
│   │   └── DataSeeder.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Task.java
│   │   ├── Category.java                   (enum)
│   │   └── Urgency.java                    (enum)
│   ├── pattern/
│   │   ├── creational/
│   │   │   └── TaskFactory.java            ← Factory
│   │   ├── structural/
│   │   │   ├── TaskComponent.java          ← Decorator (interface)
│   │   │   ├── BaseTask.java
│   │   │   ├── UrgentTaskDecorator.java
│   │   │   └── RecurringTaskDecorator.java
│   │   └── behavioral/
│   │       ├── strategy/
│   │       │   ├── SortStrategy.java
│   │       │   ├── SortByUrgency.java
│   │       │   ├── SortByCategory.java
│   │       │   └── SortByCreatedDate.java
│   │       ├── observer/
│   │       │   ├── TaskCompletedEvent.java
│   │       │   ├── TaskEventPublisher.java
│   │       │   └── TaskNotificationListener.java
│   │       └── command/
│   │           ├── TaskCommand.java
│   │           ├── CompleteTaskCommand.java
│   │           ├── DeleteTaskCommand.java
│   │           └── CommandHistory.java
│   ├── facade/
│   │   └── TaskFacade.java                 ← Facade
│   ├── service/
│   │   ├── TaskService.java
│   │   └── UserService.java
│   ├── controller/
│   │   ├── TaskController.java
│   │   └── AuthController.java
│   ├── repository/
│   │   ├── TaskRepository.java
│   │   └── UserRepository.java
│   └── dto/
│       ├── TaskRequest.java
│       ├── TaskResponse.java
│       └── AuthRequest.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

---


## 🗺️ API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login → returns JWT token |
| GET | `/api/tasks?date=2025-01-15` | Get tasks for a specific day |
| GET | `/api/tasks?date=2025-01-15&category=WORK` | Filter by day + category |
| GET | `/api/tasks?date=2025-01-15&urgency=URGENT` | Filter by day + urgency |
| GET | `/api/tasks?date=2025-01-15&sort=urgency` | Sort tasks (Strategy) |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create task (Factory + Builder) |
| PUT | `/api/tasks/{id}/complete` | Mark complete (Command + Observer) |
| DELETE | `/api/tasks/{id}` | Delete task (Command) |
| POST | `/api/tasks/undo` | Undo last action (Command) |

All `/api/tasks` endpoints require `Authorization: Bearer <token>` header.

---

## 🎯 8 Design Patterns Summary

| # | Pattern | Class | Purpose |
|---|---------|-------|---------|
| 1 | Singleton | `AppConfig` | Single global config instance |
| 2 | Factory | `TaskFactory` | Create task based on category + urgency |
| 3 | Builder | `Task` via Lombok | Construct complex Task objects |
| 4 | Decorator | `UrgentTaskDecorator` | Add behavior to task without changing the class |
| 5 | Strategy | `SortByUrgency`, etc. | Swap sorting algorithm dynamically |
| 6 | Observer | `TaskEventPublisher` | Auto-notify when task is completed |
| 7 | Facade | `TaskFacade` | Simplify complex operations into one method |
| 8 | Command | `CompleteTaskCommand` | Undo/redo task actions |

---

---

# 🤖 AI PROMPTS — COPY PASTE READY

> Use these prompts **one by one in order**.
> Always start a new AI session with Prompt 0.

---

## PROMPT 0 — Initial Context (Run First Every Session)

```
You are a senior Java developer helping me build a Todo List App using Spring Boot with 8 design patterns for a university Design Pattern course.

Project context:
- Java 21
- Spring Boot 4,1
- Database: MySQL (database name: todolist_db)
- Build tool: Maven
- Lombok for boilerplate reduction
- Base package: com.todolist

Database tables:
1. users: id, username, email, password (BCrypt), created_at
2. tasks: id, user_id (FK), title, description, task_date (DATE), category (ENUM: PERSONAL/WORK/SHOPPING), urgency (ENUM: URGENT/PRIORITY/OPTIONAL), completed (BOOLEAN), created_at, updated_at

Key requirements:
- Users can register and login
- Each task belongs to a user
- Tasks have a chosen date (task_date), a category, and an urgency level
- API filters tasks by date (required), and optionally by category or urgency
- Web frontend in English

8 design patterns to implement:
1. Singleton → AppConfig
2. Factory → TaskFactory (creates tasks based on category + urgency)
3. Builder → Task entity via Lombok @Builder
4. Decorator → UrgentTaskDecorator, RecurringTaskDecorator
5. Strategy → SortByUrgency, SortByCategory, SortByCreatedDate
6. Observer → Spring @EventListener when task is completed
7. Facade → TaskFacade (hides complexity from controller)
8. Command → CompleteTaskCommand, DeleteTaskCommand with undo support

Confirm you understand this context and are ready for the next instruction. Do not generate any code yet.
```

---

## PROMPT 1 — Models & Enums

```
Based on the project context described earlier, create these files:

1. src/main/java/com/todolist/model/User.java
   - JPA Entity: @Entity, @Table(name="users"), @Id, @GeneratedValue
   - Lombok: @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor
   - Fields: id (Long), username (String), email (String), password (String), createdAt (LocalDateTime)
   - @PrePersist to auto-set createdAt

2. src/main/java/com/todolist/model/Task.java
   - JPA Entity: @Entity, @Table(name="tasks")
   - Lombok: @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor
   - Fields: id (Long), user (User - @ManyToOne @JoinColumn), title (String), description (String), taskDate (LocalDate), category (Category enum), urgency (Urgency enum), completed (boolean), createdAt (LocalDateTime), updatedAt (LocalDateTime)
   - @PrePersist for createdAt, @PreUpdate for updatedAt
   - @Enumerated(EnumType.STRING) on both enum fields

3. src/main/java/com/todolist/model/Category.java
   - Enum: PERSONAL, WORK, SHOPPING

4. src/main/java/com/todolist/model/Urgency.java
   - Enum: URGENT, PRIORITY, OPTIONAL

Provide complete code for all 4 files. No placeholders or TODO comments.
```

---

## PROMPT 2 — Singleton Pattern

```
Based on the project context, create the Singleton Pattern implementation:

File: src/main/java/com/todolist/config/AppConfig.java

Requirements:
- Use Spring @Configuration + @Bean (Singleton by default in Spring)
- Also include a pure Java Singleton as an inner static class using double-checked locking
- Expose these beans: appName (String "TaskFlow"), appVersion (String "1.0.0"), jwtSecret (String, a random 64-char hex string)
- Add Java comments explaining WHY this is a Singleton pattern and its benefit (one shared instance)

Provide complete code.
```

---

## PROMPT 3 — Factory Pattern

```
Based on the project context, create the Factory Pattern:

File: src/main/java/com/todolist/pattern/creational/TaskFactory.java

Requirements:
- @Component so Spring can inject it
- Method: public Task createTask(User user, String title, String description, LocalDate taskDate, String category, String urgency)
- Based on category, apply different default behaviors:
  * PERSONAL: if urgency is null, default to OPTIONAL
  * WORK: if urgency is null, default to PRIORITY
  * SHOPPING: if urgency is null, default to OPTIONAL; if taskDate is null, default to today + 3 days
- Use Task.builder() (Builder Pattern) inside the factory
- Throw IllegalArgumentException with a clear message if category is unrecognized
- Add comments explaining WHY this is a Factory Pattern

Provide complete code.
```

---

## PROMPT 4 — Decorator Pattern

```
Based on the project context, create the Decorator Pattern:

Create these 4 files:

1. src/main/java/com/todolist/pattern/structural/TaskComponent.java
   - Interface with methods: getTitle(), getDescription(), getUrgency(), getDisplayLabel()

2. src/main/java/com/todolist/pattern/structural/BaseTask.java
   - Implements TaskComponent, wraps a Task JPA entity
   - getDisplayLabel() returns: "[CATEGORY] title"

3. src/main/java/com/todolist/pattern/structural/UrgentTaskDecorator.java
   - Wraps TaskComponent
   - getDisplayLabel() adds prefix "🔴 URGENT: "
   - getUrgency() always returns "URGENT"

4. src/main/java/com/todolist/pattern/structural/RecurringTaskDecorator.java
   - Wraps TaskComponent
   - getDisplayLabel() adds suffix " (Recurring)"
   - Add field: String recurrencePattern (DAILY, WEEKLY, MONTHLY)

Add comments in each file explaining the role (Component/ConcreteComponent/Decorator) in the Decorator Pattern.
Provide complete code.
```

---

## PROMPT 5 — Strategy Pattern

```
Based on the project context, create the Strategy Pattern for sorting:

Create 4 files:

1. src/main/java/com/todolist/pattern/behavioral/strategy/SortStrategy.java
   - Interface with method: List<Task> sort(List<Task> tasks)

2. src/main/java/com/todolist/pattern/behavioral/strategy/SortByUrgency.java
   - @Component("sortByUrgency")
   - Sort order: URGENT first, then PRIORITY, then OPTIONAL

3. src/main/java/com/todolist/pattern/behavioral/strategy/SortByCategory.java
   - @Component("sortByCategory")
   - Sort alphabetically by category name

4. src/main/java/com/todolist/pattern/behavioral/strategy/SortByCreatedDate.java
   - @Component("sortByCreatedDate")
   - Sort descending by createdAt (newest first)

Add comments explaining WHY this is Strategy Pattern and what benefit it provides (swappable algorithms).
Provide complete code.
```

---

## PROMPT 6 — Observer Pattern

```
Based on the project context, create the Observer Pattern using Spring Events:

Create 3 files:

1. src/main/java/com/todolist/pattern/behavioral/observer/TaskCompletedEvent.java
   - Extends ApplicationEvent
   - Fields: Task task, String message
   - Constructor calling super(source)

2. src/main/java/com/todolist/pattern/behavioral/observer/TaskEventPublisher.java
   - @Component
   - Inject ApplicationEventPublisher
   - Method: publishTaskCompleted(Task task)
   - Method: publishTaskDeleted(Task task)

3. src/main/java/com/todolist/pattern/behavioral/observer/TaskNotificationListener.java
   - @Component
   - @EventListener method for TaskCompletedEvent
   - On task completed: log "Task '[title]' marked as done on [date] by [username]"
   - Second @EventListener for TaskDeletedEvent: log deletion

Add comments identifying Subject and Observer roles in this pattern.
Provide complete code.
```

---

## PROMPT 7 — Command Pattern

```
Based on the project context, create the Command Pattern with undo support:

Create 4 files:

1. src/main/java/com/todolist/pattern/behavioral/command/TaskCommand.java
   - Interface with methods: execute() and undo()

2. src/main/java/com/todolist/pattern/behavioral/command/CompleteTaskCommand.java
   - Implements TaskCommand
   - Fields: Task task, TaskRepository repository
   - execute(): set task.completed = true, save to repo
   - undo(): set task.completed = false, save to repo
   - Save previous state (boolean) before execute for accurate undo

3. src/main/java/com/todolist/pattern/behavioral/command/DeleteTaskCommand.java
   - Implements TaskCommand
   - execute(): delete task from repository (save full Task object first for undo)
   - undo(): re-save the stored Task object back to repository

4. src/main/java/com/todolist/pattern/behavioral/command/CommandHistory.java
   - @Component, @Scope("singleton")
   - Use Stack<TaskCommand> for history
   - Methods: push(TaskCommand), undo() (pop + call undo), clear(), isEmpty()

Add comments identifying Invoker, Command interface, and Receiver roles.
Provide complete code.
```

---

## PROMPT 8 — Facade Pattern

```
Based on the project context, create the Facade Pattern:

File: src/main/java/com/todolist/facade/TaskFacade.java

Requirements:
- @Service
- Inject: TaskRepository, TaskFactory, TaskEventPublisher, CommandHistory, and the 3 SortStrategy beans by name
- Provide these methods:
  1. createTask(User user, String title, String description, LocalDate taskDate, String category, String urgency)
     → use TaskFactory → save → return Task
  2. completeTask(Long taskId)
     → build CompleteTaskCommand → execute → publish event → return Task
  3. deleteTask(Long taskId)
     → build DeleteTaskCommand → execute → publish event
  4. undoLastAction()
     → call CommandHistory.undo()
  5. getTasksByDate(Long userId, LocalDate date)
     → query repository by userId + date → return list
  6. getTasksByDateAndCategory(Long userId, LocalDate date, String category)
     → query repository → return filtered list
  7. getTasksByDateAndUrgency(Long userId, LocalDate date, String urgency)
     → query repository → return filtered list
  8. getTasksSorted(Long userId, LocalDate date, String sortType)
     → get tasks by date → pick correct SortStrategy → return sorted list

Add a class-level comment explaining that Facade hides the complexity of patterns from the controller layer.
Provide complete code.
```

---

## PROMPT 9 — Repository, UserService, JWT

```
Based on the project context, create:

1. src/main/java/com/todolist/repository/UserRepository.java
   - extends JpaRepository<User, Long>
   - findByUsername(String username) → Optional<User>
   - findByEmail(String email) → Optional<User>
   - existsByUsername(String username) → boolean
   - existsByEmail(String email) → boolean

2. src/main/java/com/todolist/repository/TaskRepository.java
   - extends JpaRepository<Task, Long>
   - findByUserIdAndTaskDate(Long userId, LocalDate date) → List<Task>
   - findByUserIdAndTaskDateAndCategory(Long userId, LocalDate date, Category category) → List<Task>
   - findByUserIdAndTaskDateAndUrgency(Long userId, LocalDate date, Urgency urgency) → List<Task>
   - findDistinctTaskDateByUserId(Long userId) → List<LocalDate>  (for calendar highlights)

3. src/main/java/com/todolist/service/UserService.java
   - @Service, implements UserDetailsService
   - Inject UserRepository, BCryptPasswordEncoder
   - Method: register(String username, String email, String password) → saves hashed password
   - Method: loadUserByUsername(String username) → for Spring Security

4. src/main/java/com/todolist/config/JwtUtil.java
   - @Component
   - generateToken(String username) → returns JWT string
   - extractUsername(String token) → returns username
   - validateToken(String token, String username) → boolean
   - Use the jwtSecret from AppConfig, expiry 24 hours

Provide complete code for all 4 files.
```

---

## PROMPT 10 — Security Config + Auth Controller

```
Based on the project context, create:

1. src/main/java/com/todolist/config/SecurityConfig.java
   - @Configuration, @EnableWebSecurity
   - Permit: POST /api/auth/register, POST /api/auth/login
   - Require authentication for all /api/tasks/** endpoints
   - Add JWT filter (JwtAuthFilter) before UsernamePasswordAuthenticationFilter
   - BCryptPasswordEncoder bean
   - Disable CSRF (REST API)
   - Allow CORS from http://localhost:3000 and http://localhost:8080

2. src/main/java/com/todolist/config/JwtAuthFilter.java
   - OncePerRequestFilter
   - Extract Bearer token from Authorization header
   - Validate and set SecurityContext

3. src/main/java/com/todolist/controller/AuthController.java
   - @RestController, @RequestMapping("/api/auth")
   - POST /api/auth/register → { username, email, password } → return 201 + message
   - POST /api/auth/login → { username, password } → return 200 + { token, username }
   - Handle duplicate username/email with 409 Conflict

4. src/main/resources/application.properties
   - MySQL: url=jdbc:mysql://localhost:3306/todolist_db, username=root, password=YOUR_PASSWORD
   - JPA: ddl-auto=update, show-sql=true, dialect=MySQLDialect
   - Logging: INFO

Provide complete code for all 4 files.
```

---

## PROMPT 11 — Task Controller + DTOs

```
Based on the project context, create:

1. src/main/java/com/todolist/dto/TaskRequest.java
   - Fields: title (String), description (String), taskDate (LocalDate), category (String), urgency (String)
   - Lombok: @Data, @NoArgsConstructor, @AllArgsConstructor

2. src/main/java/com/todolist/dto/TaskResponse.java
   - Fields: id, title, description, taskDate, category, urgency, completed, createdAt, displayLabel (from Decorator)
   - Lombok: @Data, @Builder

3. src/main/java/com/todolist/controller/TaskController.java
   - @RestController, @RequestMapping("/api/tasks")
   - Get current user from SecurityContext in each method
   - Endpoints:
     * GET /api/tasks?date=2025-01-15 → getByDate
     * GET /api/tasks?date=2025-01-15&category=WORK → getByDateAndCategory
     * GET /api/tasks?date=2025-01-15&urgency=URGENT → getByDateAndUrgency
     * GET /api/tasks?date=2025-01-15&sort=urgency → getSorted
     * GET /api/tasks/{id} → getById
     * POST /api/tasks → create (body: TaskRequest)
     * PUT /api/tasks/{id}/complete → complete task
     * DELETE /api/tasks/{id} → delete task
     * POST /api/tasks/undo → undo last action
   - Use ResponseEntity with correct HTTP status codes
   - Map Task entity to TaskResponse DTO using Decorator to get displayLabel

Provide complete code for all 3 files.
```

---

## PROMPT 12 — Data Seeder

```
Based on the project context, create a data seeder for demo purposes:

File: src/main/java/com/todolist/config/DataSeeder.java

Requirements:
- @Component, implements CommandLineRunner
- Inject UserRepository, TaskFacade, BCryptPasswordEncoder
- Only seed if database is empty (check userRepository.count() == 0)
- Create 1 demo user: username="demo", email="demo@taskflow.com", password="demo123" (hashed)
- Create 6 demo tasks for today's date:
  1. WORK, URGENT: "Submit design pattern report" 
  2. WORK, PRIORITY: "Code review with teammates"
  3. PERSONAL, PRIORITY: "Buy groceries"
  4. SHOPPING, OPTIONAL: "Browse new stationery"
  5. PERSONAL, OPTIONAL: "Morning jog"
  6. WORK, URGENT: "Fix critical bug before deadline"

Provide complete code.
```

---

## PROMPT 13 — Troubleshooting

If you get an error, paste it using this template:

```
I am building a Todo List Spring Boot app with 8 design patterns (Singleton, Factory, Builder, Decorator, Strategy, Observer, Facade, Command). Users can login, create tasks with a date, category (PERSONAL/WORK/SHOPPING), and urgency (URGENT/PRIORITY/OPTIONAL). The API filters tasks by date.

I got this error:
[PASTE ERROR HERE]

Problematic file: [FILE NAME]
Current content:
[PASTE CODE HERE]

Please:
1. Explain what caused the error
2. Give the complete fixed code
3. Do not change the design pattern logic, only fix the error
```

---

## 📝 Recommended Build Order

1. Create Spring Boot project at [start.spring.io](https://start.spring.io)
   - Dependencies: Spring Web, Spring Data JPA, Spring Security, MySQL Driver, Lombok
2. Run **Prompt 0** (context — always do this first in a new AI session)
3. Run **Prompt 1** (Models + Enums)
4. Run **Prompts 2–8** one by one (one pattern each)
5. Run **Prompt 9** (Repositories, UserService, JWT)
6. Run **Prompt 10** (Security + Auth)
7. Run **Prompt 11** (Controller + DTOs)
8. Run **Prompt 12** (Data Seeder)
9. Create the MySQL database: `CREATE DATABASE todolist_db;`
10. Run the app and test with Postman

---

## ✅ Pre-Submit Checklist

- [ ] `CREATE DATABASE todolist_db;` executed in MySQL
- [ ] App runs without error on `mvn spring-boot:run`
- [ ] Can register a new user via POST `/api/auth/register`
- [ ] Can login and receive a JWT token
- [ ] Can create a task with date, category, and urgency
- [ ] Can filter tasks by date, by date+category, by date+urgency
- [ ] Can mark a task complete, then undo it
- [ ] Each pattern file has comments explaining the pattern
- [ ] All 8 patterns are present and functional
