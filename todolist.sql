CREATE DATABASE IF NOT EXISTS todolist_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE todolist_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    task_date DATE NOT NULL,
    task_time TIME DEFAULT NULL,
    category ENUM('PERSONAL', 'WORK', 'SHOPPING') NOT NULL,
    urgency ENUM('URGENT', 'PRIORITY', 'OPTIONAL') NOT NULL DEFAULT 'OPTIONAL',
    recurrence_pattern VARCHAR(50) DEFAULT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    INDEX idx_tasks_user_date (user_id, task_date),
    INDEX idx_tasks_user_urgency (user_id, urgency),
    INDEX idx_tasks_user_category (user_id, category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT IGNORE INTO users (id, username, email, password)
VALUES (1, 'demo', 'demo@taskflow.com', 'demo123');

INSERT IGNORE INTO tasks
    (user_id, title, description, task_date, task_time, category, urgency, completed)
VALUES
    (1, 'Submit design pattern report', 'Finalize and submit the university project report.', CURRENT_DATE, '09:00', 'WORK', 'URGENT', FALSE),
    (1, 'Code review with teammates', 'Review pull requests and leave comments.', CURRENT_DATE, '11:00', 'WORK', 'PRIORITY', FALSE),
    (1, 'Buy groceries', 'Milk, eggs, bread, and vegetables.', CURRENT_DATE, '17:00', 'PERSONAL', 'PRIORITY', FALSE),
    (1, 'Browse new stationery', 'Check for new notebooks and pens online.', CURRENT_DATE, '15:00', 'SHOPPING', 'OPTIONAL', FALSE),
    (1, 'Morning jog', '30 minutes around the park.', CURRENT_DATE, '06:30', 'PERSONAL', 'OPTIONAL', FALSE),
    (1, 'Fix critical bug before deadline', 'Investigate and resolve the null pointer exception in the auth flow.', CURRENT_DATE, '16:00', 'WORK', 'URGENT', FALSE);
