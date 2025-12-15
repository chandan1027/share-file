package com.securefileshare.config;

import com.securefileshare.model.User;
import com.securefileshare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {

            // 🔒 HARD GUARD — runs only once, ever
            if (userRepository.count() > 0) {
                System.out.println("ℹ️ Users already exist. Skipping initialization.");
                return;
            }

            User user = new User();
            user.setUsername("user");
            user.setPassword("user@123");

            userRepository.save(user);

            System.out.println("✅ Default user created: user / user@123");
        };
    }
}
