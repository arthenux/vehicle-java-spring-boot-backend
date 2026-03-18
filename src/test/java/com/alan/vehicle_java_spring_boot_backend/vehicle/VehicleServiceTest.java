package com.alan.vehicle_java_spring_boot_backend.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService(vehicleRepository);
    }

    @Test
    void getAllVehiclesReturnsSummaries() {
        given(vehicleRepository.findAllByOrderByVehicleIdAsc()).willReturn(List.of(existingVehicle()));

        List<VehicleSummaryResponse> vehicles = vehicleService.getAllVehicles();

        assertThat(vehicles)
                .containsExactly(new VehicleSummaryResponse("VH-100", "Toyota", "Camry", 2022));
    }

    @Test
    void getVehicleByIdReturnsDetails() {
        given(vehicleRepository.findById("VH-100")).willReturn(Optional.of(existingVehicle()));

        VehicleDetailResponse vehicle = vehicleService.getVehicleById("VH-100");

        assertThat(vehicle.vehicleId()).isEqualTo("VH-100");
        assertThat(vehicle.trimLevel()).isEqualTo("SE");
    }

    @Test
    void createVehiclePersistsNewVehicle() {
        CreateVehicleRequest request = new CreateVehicleRequest(
                "VH-100", "Toyota", "Camry", 2022, "Sedan", "SE", "Blue", LocalDate.of(2024, 1, 10));
        given(vehicleRepository.existsById("VH-100")).willReturn(false);
        given(vehicleRepository.save(any(Vehicle.class))).willAnswer((invocation) -> invocation.getArgument(0));

        VehicleDetailResponse created = vehicleService.createVehicle(request);

        assertThat(created.vehicleId()).isEqualTo("VH-100");
        assertThat(created.colour()).isEqualTo("Blue");
    }

    @Test
    void createVehicleRejectsDuplicateIds() {
        CreateVehicleRequest request = new CreateVehicleRequest(
                "VH-100", "Toyota", "Camry", 2022, "Sedan", "SE", "Blue", LocalDate.of(2024, 1, 10));
        given(vehicleRepository.existsById("VH-100")).willReturn(true);

        assertThatThrownBy(() -> vehicleService.createVehicle(request))
                .isInstanceOf(DuplicateVehicleException.class)
                .hasMessageContaining("VH-100");
    }

    @Test
    void updateVehicleOverwritesEditableFields() {
        Vehicle vehicle = existingVehicle();
        given(vehicleRepository.findById("VH-100")).willReturn(Optional.of(vehicle));
        given(vehicleRepository.save(vehicle)).willReturn(vehicle);

        VehicleDetailResponse updated = vehicleService.updateVehicle("VH-100", new UpdateVehicleRequest(
                "Toyota", "Corolla", 2023, "Sedan", "LE", "White", LocalDate.of(2024, 2, 1)));

        assertThat(updated.model()).isEqualTo("Corolla");
        assertThat(updated.vehicleId()).isEqualTo("VH-100");
    }

    @Test
    void deleteVehicleRemovesExistingVehicle() {
        Vehicle vehicle = existingVehicle();
        given(vehicleRepository.findById("VH-100")).willReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle("VH-100");

        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void deleteVehicleRejectsUnknownVehicle() {
        given(vehicleRepository.findById("VH-404")).willReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.deleteVehicle("VH-404"))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessageContaining("VH-404");
    }

    private Vehicle existingVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId("VH-100");
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2022);
        vehicle.setBodyStyle("Sedan");
        vehicle.setTrimLevel("SE");
        vehicle.setColour("Blue");
        vehicle.setInventoryDate(LocalDate.of(2024, 1, 10));
        return vehicle;
    }
}
