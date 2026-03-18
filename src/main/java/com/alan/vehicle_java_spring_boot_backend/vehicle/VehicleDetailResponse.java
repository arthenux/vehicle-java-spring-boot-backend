package com.alan.vehicle_java_spring_boot_backend.vehicle;

import java.time.LocalDate;

public record VehicleDetailResponse(
        String vehicleId,
        String make,
        String model,
        Integer year,
        String bodyStyle,
        String trimLevel,
        String colour,
        LocalDate inventoryDate) {

    public static VehicleDetailResponse from(Vehicle vehicle) {
        return new VehicleDetailResponse(
                vehicle.getVehicleId(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getBodyStyle(),
                vehicle.getTrimLevel(),
                vehicle.getColour(),
                vehicle.getInventoryDate());
    }
}
