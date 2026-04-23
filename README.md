# Vehicle Inventory Backend

Spring Boot 4 REST API for a vehicle inventory management application. The backend is responsible for authentication, validation, persistence, and CRUD operations for vehicle records stored in MySQL.

## Overview

This service powers a single-role inventory management workflow:

- Authenticate an inventory manager through a database-backed user account
- Return a searchable list of vehicles for the home page
- Create new vehicle records with validation
- Load full vehicle details for an individual record
- Update editable vehicle fields while keeping the vehicle ID immutable
- Delete vehicles and persist the change immediately

The project is structured as a clean layered application with dedicated packages for authentication, configuration, shared API error handling, and the vehicle domain.

## Implemented Functionality

### Authentication and Access Control

- Database-backed user lookup through Spring Security
- Password hashing with BCrypt
- Stateless HTTP Basic authentication for API requests
- Demo user seeding supported through environment variables rather than hard-coded secrets
- Centralized unauthorized and validation error responses

### Vehicle Management

- `GET /api/vehicles` returns the inventory list for the home page
- `GET /api/vehicles/{vehicleId}` returns the full vehicle details view
- `POST /api/vehicles` creates a new vehicle record
- `PUT /api/vehicles/{vehicleId}` updates editable vehicle fields
- `DELETE /api/vehicles/{vehicleId}` removes a vehicle from the database

### Validation and Data Quality

- Request DTO validation using Jakarta Validation
- Field length, required-field, date, and year constraints
- Duplicate vehicle ID detection
- Consistent JSON error payloads for invalid requests and missing resources

## Technology Stack

- Java 25
- Spring Boot 4.0.3
- Spring MVC
- Spring Data JPA
- Spring Security
- MySQL
- H2 for test configuration
- JUnit 5, MockMvc, Mockito

## Architecture

### Package Layout

- `auth`: login request handling, user persistence, and security principals
- `config`: Spring Security and CORS configuration
- `common`: reusable API error responses and exception handling
- `vehicle`: entity, DTOs, repository, controller, and service logic

### Design Notes

- Controllers remain thin and delegate business logic to services
- Request and response DTOs keep the API contract explicit
- Persistence is handled through Spring Data repositories
- Security configuration is isolated from business logic
- Seed data is configurable and safe to omit in shared environments

## Local Setup

### Prerequisites

- Java 25
- MySQL 8 or compatible
- A database named `vehicle`

### Configuration

The application reads database and demo-user settings from environment variables.

#### Required for local development

```bash
export DB_URL="jdbc:mysql://localhost:3306/vehicle?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME="your_db_username"
export DB_PASSWORD="your_db_password"
export APP_SEED_USERNAME="your_demo_username"
export APP_SEED_PASSWORD="your_demo_password"
export APP_SEED_ROLE="ROLE_MANAGER"
```

If `APP_SEED_PASSWORD` is not provided, the application starts normally but skips demo-user seeding.

### Run the API

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080`.

## Related Repository

This repository contains the Spring Boot backend API for the Vehicle Inventory application.

Frontend repository:
[vehicle-angular-frontend](https://github.com/arthenux/vehicle-angular-frontend)

## Testing

### Run the test suite

```bash
./mvnw test
```

### Generate coverage report

```bash
./mvnw verify -Pcoverage
```

The `coverage` profile enforces a minimum 70% instruction coverage threshold with JaCoCo.

## Security Notes

- No database password is stored in the committed application configuration
- Demo-user credentials are not documented in this public README
- Passwords are stored hashed in the database
- Validation and ORM usage reduce SQL injection risk by avoiding string-built queries
- CORS is restricted to the local Angular development origins configured for this project

## API Summary

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/api/auth/login` | Validate credentials and return the authenticated user profile |
| `GET` | `/api/auth/me` | Return the currently authenticated user |
| `POST` | `/api/auth/logout` | Logout endpoint placeholder for the client flow |
| `GET` | `/api/vehicles` | Return all vehicles for the inventory dashboard |
| `GET` | `/api/vehicles/{vehicleId}` | Return full details for a single vehicle |
| `POST` | `/api/vehicles` | Create a vehicle |
| `PUT` | `/api/vehicles/{vehicleId}` | Update an existing vehicle |
| `DELETE` | `/api/vehicles/{vehicleId}` | Delete a vehicle |

## Why This Project Is Strong

- Clear separation between security, domain logic, and HTTP concerns
- Practical full-stack API design aligned to the frontend workflow
- Input validation and error handling implemented as first-class features
- Configuration designed for safe source control and repeatable local setup
- Test scaffolding included for controllers and services, with coverage support built into Maven
