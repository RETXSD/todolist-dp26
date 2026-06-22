package com.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SINGLETON PATTERN — AppConfig
 *
 * WHY SINGLETON:
 * - The application configuration (app name, version, secret key) must be shared
 *   across the entire application as a single, consistent instance.
 * - Spring @Configuration + @Bean creates beans as singletons by default —
 *   meaning Spring's IoC container will only instantiate each bean ONCE
 *   and reuse the same instance wherever it's injected.
 *
 * BENEFIT:
 * - Ensures all parts of the app share the same config values.
 * - Avoids duplication and inconsistency across multiple instances.
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

        private ConfigHolder() {
            this.appName    = "TASKFLOW";
            this.appVersion = "4.10.2";
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
    }
}
