package com.todolist.repository;

import com.todolist.model.Category;
import com.todolist.model.Task;
import com.todolist.model.Urgency;
import com.todolist.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manual JDBC repository for tasks.
 * Every query is written explicitly so persistence stays visible and framework-free.
 */
@Repository
public class TaskRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Optional<Task> findById(Long id) {
        String sql = """
                SELECT t.id, t.user_id, t.title, t.description, t.task_date, t.category, t.urgency,
                       t.recurrence_pattern, t.completed, t.created_at, t.updated_at,
                       u.username, u.email, u.password, u.created_at AS user_created_at
                FROM tasks t
                JOIN users u ON u.id = t.user_id
                WHERE t.id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapTask(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find task by id", e);
        }
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            return insert(task);
        }
        return update(task);
    }

    public void delete(Task task) {
        if (task.getId() == null) {
            return;
        }

        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, task.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to delete task", e);
        }
    }

    public List<Task> findByUserIdAndTaskDate(Long userId, LocalDate date) {
        return findMany("""
                SELECT t.id, t.user_id, t.title, t.description, t.task_date, t.category, t.urgency,
                       t.recurrence_pattern, t.completed, t.created_at, t.updated_at,
                       u.username, u.email, u.password, u.created_at AS user_created_at
                FROM tasks t
                JOIN users u ON u.id = t.user_id
                WHERE t.user_id = ? AND t.task_date = ?
                ORDER BY t.created_at DESC
                """, userId, date, null, null);
    }

    public List<Task> findByUserIdAndTaskDateAndCategory(Long userId, LocalDate date, Category category) {
        return findMany("""
                SELECT t.id, t.user_id, t.title, t.description, t.task_date, t.category, t.urgency,
                       t.recurrence_pattern, t.completed, t.created_at, t.updated_at,
                       u.username, u.email, u.password, u.created_at AS user_created_at
                FROM tasks t
                JOIN users u ON u.id = t.user_id
                WHERE t.user_id = ? AND t.task_date = ? AND t.category = ?
                ORDER BY t.created_at DESC
                """, userId, date, category, null);
    }

    public List<Task> findByUserIdAndTaskDateAndUrgency(Long userId, LocalDate date, Urgency urgency) {
        return findMany("""
                SELECT t.id, t.user_id, t.title, t.description, t.task_date, t.category, t.urgency,
                       t.recurrence_pattern, t.completed, t.created_at, t.updated_at,
                       u.username, u.email, u.password, u.created_at AS user_created_at
                FROM tasks t
                JOIN users u ON u.id = t.user_id
                WHERE t.user_id = ? AND t.task_date = ? AND t.urgency = ?
                ORDER BY t.created_at DESC
                """, userId, date, null, urgency);
    }

    public List<LocalDate> findDistinctTaskDateByUserId(Long userId) {
        String sql = "SELECT DISTINCT task_date FROM tasks WHERE user_id = ? ORDER BY task_date";
        List<LocalDate> dates = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dates.add(rs.getDate("task_date").toLocalDate());
                }
            }
            return dates;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query task dates", e);
        }
    }

    public long countByUserIdAndTaskDateBeforeAndCompletedFalse(Long userId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND task_date < ? AND completed = false";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to count overdue tasks", e);
        }
    }

    private Task insert(Task task) {
        String sql = """
                INSERT INTO tasks
                    (user_id, title, description, task_date, category, urgency, recurrence_pattern,
                     completed, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = task.getCreatedAt() != null ? task.getCreatedAt() : now;
        LocalDateTime updatedAt = task.getUpdatedAt() != null ? task.getUpdatedAt() : now;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindTaskFields(ps, task, createdAt, updatedAt);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    task.setId(keys.getLong(1));
                }
            }
            task.setCreatedAt(createdAt);
            task.setUpdatedAt(updatedAt);
            return task;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert task", e);
        }
    }

    private Task update(Task task) {
        String sql = """
                UPDATE tasks
                SET user_id = ?, title = ?, description = ?, task_date = ?, category = ?, urgency = ?,
                    recurrence_pattern = ?, completed = ?, created_at = ?, updated_at = ?
                WHERE id = ?
                """;
        LocalDateTime createdAt = task.getCreatedAt() != null ? task.getCreatedAt() : LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindTaskFields(ps, task, createdAt, updatedAt);
            ps.setLong(11, task.getId());
            ps.executeUpdate();
            task.setCreatedAt(createdAt);
            task.setUpdatedAt(updatedAt);
            return task;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update task", e);
        }
    }

    private void bindTaskFields(PreparedStatement ps, Task task,
                                LocalDateTime createdAt, LocalDateTime updatedAt) throws SQLException {
        ps.setLong(1, task.getUser().getId());
        ps.setString(2, task.getTitle());
        ps.setString(3, task.getDescription());
        ps.setDate(4, Date.valueOf(task.getTaskDate()));
        ps.setString(5, task.getCategory().name());
        ps.setString(6, task.getUrgency().name());
        ps.setString(7, task.getRecurrencePattern());
        ps.setBoolean(8, task.isCompleted());
        ps.setTimestamp(9, Timestamp.valueOf(createdAt));
        ps.setTimestamp(10, Timestamp.valueOf(updatedAt));
    }

    private List<Task> findMany(String sql, Long userId, LocalDate date,
                                Category category, Urgency urgency) {
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setDate(2, Date.valueOf(date));
            if (category != null) {
                ps.setString(3, category.name());
            }
            if (urgency != null) {
                ps.setString(3, urgency.name());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
            return tasks;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query tasks", e);
        }
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        Timestamp userCreatedAt = rs.getTimestamp("user_created_at");
        if (userCreatedAt != null) {
            user.setCreatedAt(userCreatedAt.toLocalDateTime());
        }

        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setUser(user);
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setTaskDate(rs.getDate("task_date").toLocalDate());
        task.setCategory(Category.valueOf(rs.getString("category")));
        task.setUrgency(Urgency.valueOf(rs.getString("urgency")));
        task.setRecurrencePattern(rs.getString("recurrence_pattern"));
        task.setCompleted(rs.getBoolean("completed"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            task.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return task;
    }
}
