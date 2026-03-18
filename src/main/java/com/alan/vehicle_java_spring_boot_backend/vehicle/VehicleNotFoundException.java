package com.alan.vehicle_java_spring_boot_backend.vehicle;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String vehicleId) {
        super("Vehicle with ID '%s' was not found".formatted(vehicleId));
    }
}
