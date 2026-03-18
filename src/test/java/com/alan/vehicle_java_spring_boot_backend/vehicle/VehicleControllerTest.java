package com.alan.vehicle_java_spring_boot_backend.vehicle;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.alan.vehicle_java_spring_boot_backend.auth.InventoryUser;
import com.alan.vehicle_java_spring_boot_backend.auth.InventoryUserRepository;
import com.alan.vehicle_java_spring_boot_backend.common.RestExceptionHandler;
import com.alan.vehicle_java_spring_boot_backend.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VehicleController.class)
@Import({SecurityConfig.class, RestExceptionHandler.class})
class VehicleControllerTest {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private InventoryUserRepository inventoryUserRepository;

    @Test
    void getVehiclesReturnsVehicleList() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));
        given(vehicleService.getAllVehicles()).willReturn(List.of(
                new VehicleSummaryResponse("VH-100", "Toyota", "Camry", 2022)));

        mockMvc.perform(get("/api/vehicles").with(httpBasic("Bob", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleId").value("VH-100"))
                .andExpect(jsonPath("$[0].make").value("Toyota"));
    }

    @Test
    void getVehicleReturnsVehicleDetails() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));
        given(vehicleService.getVehicleById("VH-100")).willReturn(sampleVehicle());

        mockMvc.perform(get("/api/vehicles/VH-100").with(httpBasic("Bob", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleId").value("VH-100"))
                .andExpect(jsonPath("$.bodyStyle").value("Sedan"));
    }

    @Test
    void createVehicleReturnsCreatedVehicle() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));
        given(vehicleService.createVehicle(eq(new CreateVehicleRequest(
                "VH-100", "Toyota", "Camry", 2022, "Sedan", "SE", "Blue", LocalDate.of(2024, 1, 10)))))
                .willReturn(sampleVehicle());

        mockMvc.perform(post("/api/vehicles")
                        .with(httpBasic("Bob", "password"))
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "vehicleId": "VH-100",
                                  "make": "Toyota",
                                  "model": "Camry",
                                  "year": 2022,
                                  "bodyStyle": "Sedan",
                                  "trimLevel": "SE",
                                  "colour": "Blue",
                                  "inventoryDate": "2024-01-10"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/vehicles/VH-100"))
                .andExpect(jsonPath("$.vehicleId").value("VH-100"));
    }

    @Test
    void createVehicleRejectsInvalidInput() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));
        mockMvc.perform(post("/api/vehicles")
                        .with(httpBasic("Bob", "password"))
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "vehicleId": "",
                                  "make": "",
                                  "model": "",
                                  "year": 1200,
                                  "bodyStyle": "",
                                  "trimLevel": "",
                                  "colour": "",
                                  "inventoryDate": "2999-01-10"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void updateVehicleReturnsUpdatedVehicle() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));
        given(vehicleService.updateVehicle(eq("VH-100"), eq(new UpdateVehicleRequest(
                "Toyota", "Camry", 2023, "Sedan", "XSE", "Black", LocalDate.of(2024, 2, 1)))))
                .willReturn(new VehicleDetailResponse(
                        "VH-100", "Toyota", "Camry", 2023, "Sedan", "XSE", "Black", LocalDate.of(2024, 2, 1)));

        mockMvc.perform(put("/api/vehicles/VH-100")
                        .with(httpBasic("Bob", "password"))
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "make": "Toyota",
                                  "model": "Camry",
                                  "year": 2023,
                                  "bodyStyle": "Sedan",
                                  "trimLevel": "XSE",
                                  "colour": "Black",
                                  "inventoryDate": "2024-02-01"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trimLevel").value("XSE"))
                .andExpect(jsonPath("$.colour").value("Black"));
    }

    @Test
    void deleteVehicleReturnsNoContent() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));
        mockMvc.perform(delete("/api/vehicles/VH-100").with(httpBasic("Bob", "password")))
                .andExpect(status().isNoContent());
    }

    private InventoryUser storedBob() {
        InventoryUser inventoryUser = new InventoryUser();
        inventoryUser.setId(1L);
        inventoryUser.setUsername("Bob");
        inventoryUser.setPasswordHash(PASSWORD_ENCODER.encode("password"));
        inventoryUser.setRole("ROLE_MANAGER");
        return inventoryUser;
    }

    private VehicleDetailResponse sampleVehicle() {
        return new VehicleDetailResponse(
                "VH-100",
                "Toyota",
                "Camry",
                2022,
                "Sedan",
                "SE",
                "Blue",
                LocalDate.of(2024, 1, 10));
    }
}
