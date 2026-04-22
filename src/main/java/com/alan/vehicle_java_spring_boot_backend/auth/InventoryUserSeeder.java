package com.alan.vehicle_java_spring_boot_backend.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InventoryUserSeeder {

    private static final Logger log = LoggerFactory.getLogger(InventoryUserSeeder.class);

    @Bean
    CommandLineRunner seedInventoryManager(InventoryUserRepository inventoryUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed.user.username}") String seedUsername,
            @Value("${app.seed.user.password:}") String seedPassword,
            @Value("${app.seed.user.role}") String seedRole) {
        return (args) -> {
            if (seedPassword.isBlank()) {
                log.info("Skipping demo user seed because APP_SEED_PASSWORD was not provided.");
                return;
            }

            if (inventoryUserRepository.findByUsernameIgnoreCase(seedUsername).isPresent()) {
                return;
            }

            InventoryUser inventoryUser = new InventoryUser();
            inventoryUser.setUsername(seedUsername);
            inventoryUser.setPasswordHash(passwordEncoder.encode(seedPassword));
            inventoryUser.setRole(seedRole);
            inventoryUserRepository.save(inventoryUser);
        };
    }
}
