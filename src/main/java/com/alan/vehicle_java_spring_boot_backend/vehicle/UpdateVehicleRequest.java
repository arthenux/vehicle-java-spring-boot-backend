package com.alan.vehicle_java_spring_boot_backend.vehicle;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record UpdateVehicleRequest(
        @NotBlank(message = "Make is required")
        @Size(max = 50, message = "Make must be 50 characters or fewer")
        String make,
        @NotBlank(message = "Model is required")
        @Size(max = 50, message = "Model must be 50 characters or fewer")
        String model,
        @NotNull(message = "Year is required")
        @Min(value = 1886, message = "Year must be 1886 or later")
        @Max(value = 2100, message = "Year must be 2100 or earlier")
        Integer year,
        @NotBlank(message = "Body style is required")
        @Size(max = 50, message = "Body style must be 50 characters or fewer")
        String bodyStyle,
        @NotBlank(message = "Trim level is required")
        @Size(max = 50, message = "Trim level must be 50 characters or fewer")
        String trimLevel,
        @NotBlank(message = "Colour is required")
        @Size(max = 50, message = "Colour must be 50 characters or fewer")
        String colour,
        @NotNull(message = "Inventory date is required")
        @PastOrPresent(message = "Inventory date cannot be in the future")
        LocalDate inventoryDate) {
}
