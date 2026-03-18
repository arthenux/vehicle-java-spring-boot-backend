package com.alan.vehicle_java_spring_boot_backend.vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findAllByOrderByVehicleIdAsc();
}
