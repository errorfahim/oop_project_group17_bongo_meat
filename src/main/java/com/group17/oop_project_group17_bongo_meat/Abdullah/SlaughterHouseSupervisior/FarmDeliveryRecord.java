package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import java.io.Serializable;
import java.time.LocalDate;

public class FarmDeliveryRecord implements Serializable {

    private String animalId;
    private String type;
    private String healthCertificate;
    private int quantity;
    private String status;
    private LocalDate arrivalDate;
    private String note;
    private String breed; // NEW FIELD

    public FarmDeliveryRecord(String animalId, String type, String healthCertificate,
                              int quantity, String status, LocalDate arrivalDate,
                              String note, String breed) {
        this.animalId = animalId;
        this.type = type;
        this.healthCertificate = healthCertificate;
        this.quantity = quantity;
        this.status = status;
        this.arrivalDate = arrivalDate;
        this.note = note;
        this.breed = breed;
    }

    public String getAnimalId() { return animalId; }
    public String getType() { return type; }
    public String getHealthCertificate() { return healthCertificate; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public LocalDate getArrivalDate() { return arrivalDate; }
    public String getNote() { return note; }
    public String getBreed() { return breed; }

    public void setStatus(String status) { this.status = status; }
    public void setNote(String note) { this.note = note; }
}

