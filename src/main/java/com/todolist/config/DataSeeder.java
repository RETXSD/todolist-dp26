package com.todolist.config;

import com.todolist.facade.TaskFacade;
import com.todolist.model.User;
import com.todolist.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Data Seeder — runs on application startup to populate demo data.
 * Only seeds if the database is empty (safe to run in production).
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository  userRepository;
    private final TaskFacade      taskFacade;

    public DataSeeder(UserRepository userRepository,
                      TaskFacade taskFacade) {
        this.userRepository  = userRepository;
        this.taskFacade      = taskFacade;
    }

    @Override
    public void run(String... args) {
        // Only seed if no users exist in the database
        if (userRepository.count() > 0) {
            return;
        }

        // Create demo user
        User demoUser = User.builder()
                .username("demo")
                .email("demo@taskflow.com")
                .password("demo123")
                .build();
        demoUser = userRepository.save(demoUser);

        LocalDate today = LocalDate.now();

        // Create 6 demo tasks using TaskFacade (which uses Factory + Builder internally)
        taskFacade.createTask(demoUser, "Submit design pattern report",
                "Finalize and submit the university project report.", today, "WORK", "URGENT");

        taskFacade.createTask(demoUser, "Code review with teammates",
                "Review pull requests and leave comments.", today, "WORK", "PRIORITY");

        taskFacade.createTask(demoUser, "Buy groceries",
                "Milk, eggs, bread, and vegetables.", today, "PERSONAL", "PRIORITY");

        taskFacade.createTask(demoUser, "Browse new stationery",
                "Check for new notebooks and pens online.", today, "SHOPPING", "OPTIONAL");

        taskFacade.createTask(demoUser, "Morning jog",
                "30 minutes around the park.", today, "PERSONAL", "OPTIONAL");

        taskFacade.createTask(demoUser, "Fix critical bug before deadline",
                "Investigate and resolve the null pointer exception in the auth flow.", today, "WORK", "URGENT");

        System.out.println("[DataSeeder] Demo data seeded successfully! Login: demo / demo123");
    }
}
