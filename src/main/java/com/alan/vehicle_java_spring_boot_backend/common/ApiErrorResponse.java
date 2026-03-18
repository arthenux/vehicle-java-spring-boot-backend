package com.alan.vehicle_java_spring_boot_backend.common;

import java.util.Map;

public record ApiErrorResponse(
        String message,
        Map<String, String> fieldErrors) {
}
