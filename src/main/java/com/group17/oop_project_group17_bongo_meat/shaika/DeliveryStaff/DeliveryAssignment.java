package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import java.io.Serializable;

public class DeliveryAssignment implements Serializable {

    // ==============================================================
    // EXISTING DELIVERY FIELDS
    // ==============================================================
    private String deliveryId;
    private String deliveryTime;
    private String vehicleType;
    private String deliveryAddress;
    private String status;         // Assigned / Accepted / Out for Delivery / Delivered
    private String orderId;        // Customer Order ID

    // ==============================================================
    // PACKAGE INSPECTION FIELDS (Goal 2)
    // ==============================================================
    private String packageCondition;  // Good / Damaged / Leaking
    private String temperature;       // Example: "5°C"
    private String notes;             // Additional Notes

    // ==============================================================
    // VEHICLE DETAILS (Goal 5)
    // ==============================================================
    private String vehicleId;         // Example: "101958"
    private String licensePlate;      // Example: "DHA-12345"
    private String lastServiceDate;   // Example: "2025-01-20"
    private String fuelStatus;        // Example: "Half Tank"


    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================
    public DeliveryAssignment(String deliveryId,
                              String deliveryTime,
                              String vehicleType,
                              String deliveryAddress,
                              String status,
                              String orderId) {

        this.deliveryId = deliveryId;
        this.deliveryTime = deliveryTime;
        this.vehicleType = vehicleType;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.orderId = orderId;

        // Default values for package inspection (Goal 2)
        this.packageCondition = "";
        this.temperature = "";
        this.notes = "";

        // Default values for vehicle info (Goal 5)
        this.vehicleId = "";
        this.licensePlate = "";
        this.lastServiceDate = "";
        this.fuelStatus = "";
    }

    // ==============================================================
    // GETTERS
    // ==============================================================
    public String getDeliveryId() { return deliveryId; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getVehicleType() { return vehicleType; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getStatus() { return status; }
    public String getOrderId() { return orderId; }

    public String getPackageCondition() { return packageCondition; }
    public String getTemperature() { return temperature; }
    public String getNotes() { return notes; }

    public String getVehicleId() { return vehicleId; }
    public String getLicensePlate() { return licensePlate; }
    public String getLastServiceDate() { return lastServiceDate; }
    public String getFuelStatus() { return fuelStatus; }


    // ==============================================================
    // SETTERS
    // ==============================================================
    public void setStatus(String status) { this.status = status; }

    public void setPackageCondition(String packageCondition) {
        this.packageCondition = packageCondition;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setLastServiceDate(String lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    public void setFuelStatus(String fuelStatus) {
        this.fuelStatus = fuelStatus;
    }
}
