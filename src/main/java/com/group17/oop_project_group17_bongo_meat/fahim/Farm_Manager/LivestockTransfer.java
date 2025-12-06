package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import java.time.LocalDate;

public class LivestockTransfer {
    private LocalDate departureDate;
    private String batchId;
    private String type;
    private String breed;
    private String healthCertificate;
    private int quantity;

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getHealthCertificate() {
        return healthCertificate;
    }

    public void setHealthCertificate(String healthCertificate) {
        this.healthCertificate = healthCertificate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LivestockTransfer(LocalDate departureDate, String batchId, String type, String breed, String healthCertificate, int quantity) {
        this.departureDate = departureDate;
        this.batchId = batchId;
        this.type = type;
        this.breed = breed;
        this.healthCertificate = healthCertificate;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "LivestockTransfer{" +
                "departureDate=" + departureDate +
                ", batchId='" + batchId + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", healthCertificate='" + healthCertificate + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
