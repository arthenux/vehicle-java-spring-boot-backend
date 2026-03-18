package com.alan.vehicle_java_spring_boot_backend.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InventoryUserSeeder {

    @Bean
    CommandLineRunner seedInventoryManager(InventoryUserRepository inventoryUserRepository,
            PasswordEncoder passwordEncoder) {
        return (args) -> {
            if (inventoryUserRepository.findByUsernameIgnoreCase("Bob").isPresent()) {
                return;
            }

            InventoryUser inventoryUser = new InventoryUser();
            inventoryUser.setUsername("Bob");
            inventoryUser.setPasswordHash(passwordEncoder.encode("password"));
            inventoryUser.setRole("ROLE_MANAGER");
            inventoryUserRepository.save(inventoryUser);
        };
    }
}
