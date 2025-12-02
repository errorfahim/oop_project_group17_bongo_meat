package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class HealthCheckRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---- core fields (these are actually serialized) ----
    private String batchID;
    private int animalCount;
    private String lastCheckDate;
    private String status;

    private String temperature;
    private String symptoms;
    private String behavior;
    private String hydrationLevel;
    private String injuryNotes;
    private String treatmentGiven;
    private int medicineDosage;

    public HealthCheckRecord(String batchID,
                             int animalCount,
                             String lastCheckDate,
                             String status,
                             String temperature,
                             String symptoms,
                             String behavior,
                             String hydrationLevel,
                             String injuryNotes,
                             String treatmentGiven,
                             int medicineDosage) {
        this.batchID = batchID;
        this.animalCount = animalCount;
        this.lastCheckDate = lastCheckDate;
        this.status = status;
        this.temperature = temperature;
        this.symptoms = symptoms;
        this.behavior = behavior;
        this.hydrationLevel = hydrationLevel;
        this.injuryNotes = injuryNotes;
        this.treatmentGiven = treatmentGiven;
        this.medicineDosage = medicineDosage;
    }

    // ---------- getters (for binary & PDF) ----------

    public String getBatchID() {
        return batchID;
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public String getLastCheckDate() {
        return lastCheckDate;
    }

    public String getStatus() {
        return status;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getBehavior() {
        return behavior;
    }

    public String getHydrationLevel() {
        return hydrationLevel;
    }

    public String getInjuryNotes() {
        return injuryNotes;
    }

    public String getTreatmentGiven() {
        return treatmentGiven;
    }

    public int getMedicineDosage() {
        return medicineDosage;
    }

    // ---------- JavaFX property wrappers for TableView ----------

    public StringProperty batchIDProperty() {
        return new SimpleStringProperty(batchID);
    }

    public IntegerProperty animalCountProperty() {
        return new SimpleIntegerProperty(animalCount);
    }

    public StringProperty lastCheckDateProperty() {
        return new SimpleStringProperty(lastCheckDate);
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(status);
    }
}
