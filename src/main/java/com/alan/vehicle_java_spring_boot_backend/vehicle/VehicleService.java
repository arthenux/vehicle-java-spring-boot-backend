package com.alan.vehicle_java_spring_boot_backend.vehicle;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public List<VehicleSummaryResponse> getAllVehicles() {
        return vehicleRepository.findAllByOrderByVehicleIdAsc()
                .stream()
                .map(VehicleSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleDetailResponse getVehicleById(String vehicleId) {
        return VehicleDetailResponse.from(findVehicle(vehicleId));
    }

    public VehicleDetailResponse createVehicle(CreateVehicleRequest request) {
        if (vehicleRepository.existsById(request.vehicleId())) {
            throw new DuplicateVehicleException(request.vehicleId());
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(request.vehicleId());
        apply(vehicle, request.make(), request.model(), request.year(), request.bodyStyle(),
                request.trimLevel(), request.colour(), request.inventoryDate());

        return VehicleDetailResponse.from(vehicleRepository.save(vehicle));
    }

    public VehicleDetailResponse updateVehicle(String vehicleId, UpdateVehicleRequest request) {
        Vehicle vehicle = findVehicle(vehicleId);
        apply(vehicle, request.make(), request.model(), request.year(), request.bodyStyle(),
                request.trimLevel(), request.colour(), request.inventoryDate());
        return VehicleDetailResponse.from(vehicleRepository.save(vehicle));
    }

    public void deleteVehicle(String vehicleId) {
        Vehicle vehicle = findVehicle(vehicleId);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findVehicle(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(vehicleId));
    }

    private void apply(Vehicle vehicle, String make, String model, Integer year, String bodyStyle, String trimLevel,
            String colour, java.time.LocalDate inventoryDate) {
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setBodyStyle(bodyStyle);
        vehicle.setTrimLevel(trimLevel);
        vehicle.setColour(colour);
        vehicle.setInventoryDate(inventoryDate);
    }
}
