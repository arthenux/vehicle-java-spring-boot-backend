package com.alan.vehicle_java_spring_boot_backend.auth;

import java.util.List;

import org.springframework.security.core.Authentication;

public record UserProfileResponse(
        String username,
        List<String> roles) {

    public static UserProfileResponse from(Authentication authentication) {
        List<String> roleNames = authentication.getAuthorities()
                .stream()
                .map((authority) -> authority.getAuthority())
                .toList();
        return new UserProfileResponse(authentication.getName(), roleNames);
    }
}
