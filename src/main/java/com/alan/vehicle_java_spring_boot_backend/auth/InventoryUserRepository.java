package com.alan.vehicle_java_spring_boot_backend.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryUserRepository extends JpaRepository<InventoryUser, Long> {

    Optional<InventoryUser> findByUsernameIgnoreCase(String username);
}
