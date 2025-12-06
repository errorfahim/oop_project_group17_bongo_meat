package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import java.io.Serializable;
import java.time.LocalDate;

public class VetInspection implements Serializable {
    private String animalId;
    private String animalType;
    private LocalDate lastInspectionDate;
    private String reason;
    private String farmId;

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public VetInspection(String animalId, String animalType, LocalDate lastInspectionDate, String reason, String farmId) {
        this.animalId = animalId;
        this.animalType = animalType;
        this.lastInspectionDate = lastInspectionDate;
        this.reason = reason;
        this.farmId = farmId;
    }

    @Override
    public String toString() {
        return "VetInspection{" +
                "animalId='" + animalId + '\'' +
                ", animalType='" + animalType + '\'' +
                ", lastInspectionDate=" + lastInspectionDate +
                ", reason='" + reason + '\'' +
                ", farmId='" + farmId + '\'' +
                '}';
    }
}
