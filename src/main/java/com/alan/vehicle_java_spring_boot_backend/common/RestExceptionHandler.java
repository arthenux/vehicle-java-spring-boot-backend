package com.alan.vehicle_java_spring_boot_backend.common;

import java.util.LinkedHashMap;
import java.util.Map;

import com.alan.vehicle_java_spring_boot_backend.vehicle.DuplicateVehicleException;
import com.alan.vehicle_java_spring_boot_backend.vehicle.VehicleNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(new ApiErrorResponse("Validation failed", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(VehicleNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(DuplicateVehicleException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorResponse("Invalid username or password", Map.of()));
    }
}
