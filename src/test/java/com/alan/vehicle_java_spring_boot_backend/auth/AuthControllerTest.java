package com.alan.vehicle_java_spring_boot_backend.auth;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alan.vehicle_java_spring_boot_backend.auth.InventoryUser;
import com.alan.vehicle_java_spring_boot_backend.auth.InventoryUserRepository;
import com.alan.vehicle_java_spring_boot_backend.common.RestExceptionHandler;
import com.alan.vehicle_java_spring_boot_backend.config.SecurityConfig;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, RestExceptionHandler.class})
class AuthControllerTest {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryUserRepository inventoryUserRepository;

    @Test
    void loginReturnsUserProfileForValidCredentials() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "Bob",
                                  "password": "password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Bob"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_MANAGER"));
    }

    @Test
    void loginReturnsValidationErrorsForMissingFields() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "",
                                  "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.username").value("Username is required"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password is required"));
    }

    @Test
    void currentUserReturnsAuthenticatedProfile() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));

        mockMvc.perform(get("/api/auth/me").with(httpBasic("Bob", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Bob"));
    }

    @Test
    void logoutReturnsNoContent() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));

        mockMvc.perform(post("/api/auth/logout").with(httpBasic("Bob", "password")))
                .andExpect(status().isNoContent());
    }

    @Test
    void invalidCredentialsReturnUnauthorized() throws Exception {
        given(inventoryUserRepository.findByUsernameIgnoreCase("Bob"))
                .willReturn(Optional.of(storedBob()));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "Bob",
                                  "password": "wrong"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    private InventoryUser storedBob() {
        InventoryUser inventoryUser = new InventoryUser();
        inventoryUser.setId(1L);
        inventoryUser.setUsername("Bob");
        inventoryUser.setPasswordHash(PASSWORD_ENCODER.encode("password"));
        inventoryUser.setRole("ROLE_MANAGER");
        return inventoryUser;
    }
}
