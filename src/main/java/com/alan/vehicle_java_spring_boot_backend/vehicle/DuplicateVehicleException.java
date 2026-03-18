package com.alan.vehicle_java_spring_boot_backend.vehicle;

public class DuplicateVehicleException extends RuntimeException {

    public DuplicateVehicleException(String vehicleId) {
        super("Vehicle with ID '%s' already exists".formatted(vehicleId));
    }
}
