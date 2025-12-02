package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import java.io.Serializable;
import java.time.LocalDate;

public class VetActivityRecord implements Serializable {

    private String animalId;
    private String batchId;
    private String animalType;
    private String activityType;   // Health Check, Vaccination, etc.
    private LocalDate date;
    private String vetId;
    private String remarks;

    public VetActivityRecord(String animalId,
                             String batchId,
                             String animalType,
                             String activityType,
                             LocalDate date,
                             String vetId,
                             String remarks) {
        this.animalId = animalId;
        this.batchId = batchId;
        this.animalType = animalType;
        this.activityType = activityType;
        this.date = date;
        this.vetId = vetId;
        this.remarks = remarks;
    }

    public String getAnimalId()     { return animalId; }
    public String getBatchId()      { return batchId; }
    public String getAnimalType()   { return animalType; }
    public String getActivityType() { return activityType; }
    public LocalDate getDate()      { return date; }
    public String getVetId()        { return vetId; }
    public String getRemarks()      { return remarks; }
}
