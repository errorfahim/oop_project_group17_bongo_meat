package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class DiseaseBatchAlert implements Serializable {

    private String batchId;
    private int animalCount;
    private String location;
    private String currentHealthStatus;

    public DiseaseBatchAlert(String batchId,
                             int animalCount,
                             String location,
                             String currentHealthStatus) {
        this.batchId = batchId;
        this.animalCount = animalCount;
        this.location = location;
        this.currentHealthStatus = currentHealthStatus;
    }

    // --- JavaFX properties for TableView ---

    public StringProperty batchIdProperty() {
        return new SimpleStringProperty(batchId);
    }

    public IntegerProperty animalCountProperty() {
        return new SimpleIntegerProperty(animalCount);
    }

    public StringProperty locationProperty() {
        return new SimpleStringProperty(location);
    }

    public StringProperty currentHealthStatusProperty() {
        return new SimpleStringProperty(currentHealthStatus);
    }

    // --- getters / setters ---

    public String getBatchId() { return batchId; }
    public int getAnimalCount() { return animalCount; }
    public String getLocation() { return location; }
    public String getCurrentHealthStatus() { return currentHealthStatus; }

    public void setCurrentHealthStatus(String currentHealthStatus) {
        this.currentHealthStatus = currentHealthStatus;
    }
}
