package com.alan.vehicle_java_spring_boot_backend.vehicle;

public record VehicleSummaryResponse(
        String vehicleId,
        String make,
        String model,
        Integer year) {

    public static VehicleSummaryResponse from(Vehicle vehicle) {
        return new VehicleSummaryResponse(
                vehicle.getVehicleId(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear());
    }
}
