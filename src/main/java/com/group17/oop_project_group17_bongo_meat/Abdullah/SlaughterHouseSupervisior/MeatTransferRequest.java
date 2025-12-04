package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class MeatTransferRequest implements Serializable {

    private String batchId;
    private String type;
    private String weight;
    private String slaughterTime;
    private String status;
    private String observation;

    // Transient JavaFX properties for TableView binding
    private transient StringProperty batchIdProperty;
    private transient StringProperty typeProperty;
    private transient StringProperty weightProperty;
    private transient StringProperty slaughterTimeProperty;
    private transient StringProperty statusProperty;
    private transient StringProperty observationProperty;

    public MeatTransferRequest(String batchId, String type, String weight,
                               String slaughterTime, String status, String observation) {
        this.batchId = batchId;
        this.type = type;
        this.weight = weight;
        this.slaughterTime = slaughterTime;
        this.status = status;
        this.observation = observation;
        initProperties();
    }

    private void initProperties() {
        batchIdProperty = new SimpleStringProperty(batchId);
        typeProperty = new SimpleStringProperty(type);
        weightProperty = new SimpleStringProperty(weight);
        slaughterTimeProperty = new SimpleStringProperty(slaughterTime);
        statusProperty = new SimpleStringProperty(status);
        observationProperty = new SimpleStringProperty(observation);
    }

    public void refreshProperties() {
        initProperties();
    }

    // ===================== Getters & Setters =====================
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
        if (batchIdProperty != null) batchIdProperty.set(batchId);
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
        if (typeProperty != null) typeProperty.set(type);
    }

    public String getWeight() { return weight; }
    public void setWeight(String weight) {
        this.weight = weight;
        if (weightProperty != null) weightProperty.set(weight);
    }

    public String getSlaughterTime() { return slaughterTime; }
    public void setSlaughterTime(String slaughterTime) {
        this.slaughterTime = slaughterTime;
        if (slaughterTimeProperty != null) slaughterTimeProperty.set(slaughterTime);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        if (statusProperty != null) statusProperty.set(status);
    }

    public String getObservation() { return observation; }
    public void setObservation(String observation) {
        this.observation = observation;
        if (observationProperty != null) observationProperty.set(observation);
    }

    // ===================== JavaFX Properties =====================
    public StringProperty batchIdProperty() {
        if (batchIdProperty == null) initProperties();
        return batchIdProperty;
    }

    public StringProperty typeProperty() {
        if (typeProperty == null) initProperties();
        return typeProperty;
    }

    public StringProperty weightProperty() {
        if (weightProperty == null) initProperties();
        return weightProperty;
    }

    public StringProperty slaughterTimeProperty() {
        if (slaughterTimeProperty == null) initProperties();
        return slaughterTimeProperty;
    }

    public StringProperty statusProperty() {
        if (statusProperty == null) initProperties();
        return statusProperty;
    }

    public StringProperty observationProperty() {
        if (observationProperty == null) initProperties();
        return observationProperty;
    }

    @Override
    public String toString() {
        return batchId + " | " + type + " | " + weight + " | " + slaughterTime + " | " + status + " | " + observation;
    }
}


