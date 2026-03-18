package com.alan.vehicle_java_spring_boot_backend.vehicle;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
@Validated
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleSummaryResponse> getVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{vehicleId}")
    public VehicleDetailResponse getVehicle(@PathVariable String vehicleId) {
        return vehicleService.getVehicleById(vehicleId);
    }

    @PostMapping
    public ResponseEntity<VehicleDetailResponse> createVehicle(@Valid @RequestBody CreateVehicleRequest request) {
        VehicleDetailResponse createdVehicle = vehicleService.createVehicle(request);
        return ResponseEntity.created(URI.create("/api/vehicles/" + createdVehicle.vehicleId())).body(createdVehicle);
    }

    @PutMapping("/{vehicleId}")
    public VehicleDetailResponse updateVehicle(@PathVariable String vehicleId,
            @Valid @RequestBody UpdateVehicleRequest request) {
        return vehicleService.updateVehicle(vehicleId, request);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }
}
