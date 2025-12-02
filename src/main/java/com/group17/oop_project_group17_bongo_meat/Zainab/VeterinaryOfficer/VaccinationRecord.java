package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class VaccinationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // schedule columns (top TableView)
    private String batchID;
    private String species;
    private int age;
    private String requiredVaccine;
    private String dueDate;
    private String previousDoseDate;
    private String status;

    // form fields (bottom vaccination form)
    private String vaccineType;
    private String vaccineBatchNo;
    private String dose;
    private String administrationRoute;
    private String vaccinationDate;   // store as String (from DatePicker)
    private String notes;

    public VaccinationRecord(String batchID,
                             String species,
                             int age,
                             String requiredVaccine,
                             String dueDate,
                             String previousDoseDate,
                             String status,
                             String vaccineType,
                             String vaccineBatchNo,
                             String dose,
                             String administrationRoute,
                             String vaccinationDate,
                             String notes) {
        this.batchID = batchID;
        this.species = species;
        this.age = age;
        this.requiredVaccine = requiredVaccine;
        this.dueDate = dueDate;
        this.previousDoseDate = previousDoseDate;
        this.status = status;
        this.vaccineType = vaccineType;
        this.vaccineBatchNo = vaccineBatchNo;
        this.dose = dose;
        this.administrationRoute = administrationRoute;
        this.vaccinationDate = vaccinationDate;
        this.notes = notes;
    }

    // ---- getters for saving / pdf ----
    public String getBatchID() { return batchID; }
    public String getSpecies() { return species; }
    public int getAge() { return age; }
    public String getRequiredVaccine() { return requiredVaccine; }
    public String getDueDate() { return dueDate; }
    public String getPreviousDoseDate() { return previousDoseDate; }
    public String getStatus() { return status; }
    public String getVaccineType() { return vaccineType; }
    public String getVaccineBatchNo() { return vaccineBatchNo; }
    public String getDose() { return dose; }
    public String getAdministrationRoute() { return administrationRoute; }
    public String getVaccinationDate() { return vaccinationDate; }
    public String getNotes() { return notes; }

    // ---- JavaFX properties for TableView columns ----
    public StringProperty batchIDProperty() {
        return new SimpleStringProperty(batchID);
    }

    public StringProperty speciesProperty() {
        return new SimpleStringProperty(species);
    }

    public IntegerProperty ageProperty() {
        return new SimpleIntegerProperty(age);
    }

    public StringProperty requiredVaccineProperty() {
        return new SimpleStringProperty(requiredVaccine);
    }

    public StringProperty dueDateProperty() {
        return new SimpleStringProperty(dueDate);
    }

    public StringProperty previousDoseDateProperty() {
        return new SimpleStringProperty(previousDoseDate);
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(status);
    }
}
