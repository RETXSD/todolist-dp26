package com.todolist.repository;

import com.todolist.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Manual JDBC repository for users.
 * No Spring Data JPA, no Hibernate, no generated query methods.
 */
@Repository
public class UserRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to count users", e);
        }
    }

    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        }
        return update(user);
    }

    public Optional<User> findByUsername(String username) {
        return findOne("SELECT id, username, email, password, created_at FROM users WHERE username = ?", username);
    }

    public Optional<User> findByEmail(String email) {
        return findOne("SELECT id, username, email, password, created_at FROM users WHERE email = ?", email);
    }

    public boolean existsByUsername(String username) {
        return exists("SELECT COUNT(*) FROM users WHERE username = ?", username);
    }

    public boolean existsByEmail(String email) {
        return exists("SELECT COUNT(*) FROM users WHERE email = ?", email);
    }

    private User insert(User user) {
        String sql = "INSERT INTO users (username, email, password, created_at) VALUES (?, ?, ?, ?)";
        LocalDateTime createdAt = user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setTimestamp(4, Timestamp.valueOf(createdAt));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
            user.setCreatedAt(createdAt);
            return user;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to insert user", e);
        }
    }

    private User update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setLong(4, user.getId());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update user", e);
        }
    }

    private Optional<User> findOne(String sql, String value) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to query user", e);
        }
    }

    private boolean exists(String sql, String value) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getLong(1) > 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to check user existence", e);
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        return user;
    }
}
