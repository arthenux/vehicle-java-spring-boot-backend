package com.alan.vehicle_java_spring_boot_backend.vehicle;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @Column(name = "vehicle_id", nullable = false, length = 50, updatable = false)
    private String vehicleId;

    @Column(nullable = false, length = 50)
    private String make;

    @Column(nullable = false, length = 50)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "body_style", nullable = false, length = 50)
    private String bodyStyle;

    @Column(name = "trim_level", nullable = false, length = 50)
    private String trimLevel;

    @Column(nullable = false, length = 50)
    private String colour;

    @Column(name = "inventory_date", nullable = false)
    private LocalDate inventoryDate;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public String getTrimLevel() {
        return trimLevel;
    }

    public void setTrimLevel(String trimLevel) {
        this.trimLevel = trimLevel;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public LocalDate getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(LocalDate inventoryDate) {
        this.inventoryDate = inventoryDate;
    }
}
