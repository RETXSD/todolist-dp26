package com.todolist.config;

import com.todolist.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration.
 * Uses session-based form login (no JWT) since this is a Thymeleaf web app.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService     = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Spring Boot 4.1 / Spring Security 6.x: constructor takes UserDetailsService
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                // All other pages require login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/tasks", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            // CSRF enabled for Thymeleaf form submissions (default)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }
}
