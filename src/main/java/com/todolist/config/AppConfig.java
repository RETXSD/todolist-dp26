package com.todolist.config;

import com.todolist.model.Category;
import com.todolist.model.Urgency;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Map;

/**
 * SINGLETON PATTERN — AppConfig
 *
 * WHY SINGLETON:
 * - Application settings and task rules must be shared across the entire
 *   application as one consistent source of truth.
 * - Spring @Configuration + @Bean creates beans as singletons by default —
 *   meaning Spring's IoC container will only instantiate each bean ONCE
 *   and reuse the same instance wherever it's injected.
 *
 * BENEFIT:
 * - Ensures Factory and Strategy use the same task rules.
 * - Avoids duplicated urgency/date logic in multiple classes.
 *
 * PURE JAVA SINGLETON (inner static class):
 * - Uses double-checked locking for thread-safe lazy initialization.
 * - Guarantees only one instance of ConfigHolder is created, even in multi-threaded environments.
 */
@Configuration
public class AppConfig {

    /**
     * PasswordEncoder bean defined here (not in SecurityConfig) to avoid
     * circular dependency: SecurityConfig → UserService → PasswordEncoder → SecurityConfig.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword == null ? "" : rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String storedPassword) {
                String raw = rawPassword == null ? "" : rawPassword.toString();
                return raw.equals(storedPassword);
            }
        };
    }

    @Bean
    public String appName() {
        return "TaskFlow";
    }

    @Bean
    public String appVersion() {
        return "1.0.0";
    }

    // -------------------------------------------------------
    // Pure Java Singleton using Double-Checked Locking
    // -------------------------------------------------------
    public static class ConfigHolder {

        private static volatile ConfigHolder instance;

        private final String appName;
        private final String appVersion;
        private final Map<Category, Urgency> defaultUrgencyByCategory;
        private final Map<Urgency, Integer> urgencyPriorityOrder;

        private ConfigHolder() {
            this.appName    = "TASKFLOW";
            this.appVersion = "4.10.2";
            this.defaultUrgencyByCategory = Map.of(
                    Category.PERSONAL, Urgency.OPTIONAL,
                    Category.WORK, Urgency.PRIORITY,
                    Category.SHOPPING, Urgency.OPTIONAL
            );
            this.urgencyPriorityOrder = Map.of(
                    Urgency.URGENT, 0,
                    Urgency.PRIORITY, 1,
                    Urgency.OPTIONAL, 2
            );
        }

        /**
         * Double-checked locking ensures thread-safe initialization
         * without the overhead of synchronizing every call.
         */
        public static ConfigHolder getInstance() {
            if (instance == null) {
                synchronized (ConfigHolder.class) {
                    if (instance == null) {
                        instance = new ConfigHolder();
                    }
                }
            }
            return instance;
        }

        public String getAppName()    { return appName; }
        public String getAppVersion() { return appVersion; }

        public Urgency getDefaultUrgency(Category category) {
            return defaultUrgencyByCategory.getOrDefault(category, Urgency.OPTIONAL);
        }

        public LocalDate getDefaultTaskDate(Category category) {
            if (category == Category.SHOPPING) {
                return LocalDate.now().plusDays(3);
            }
            return LocalDate.now();
        }

        public int getUrgencyPriority(Urgency urgency) {
            return urgencyPriorityOrder.getOrDefault(urgency, 99);
        }
    }
}
