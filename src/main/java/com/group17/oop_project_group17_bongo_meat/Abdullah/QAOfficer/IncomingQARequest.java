package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class IncomingQARequest implements Serializable {

    private String batchId;
    private String type;
    private int quantity;
    private String note;
    private String qaRequestStatus;

    // Transient JavaFX properties
    private transient StringProperty batchIdProperty;
    private transient StringProperty typeProperty;
    private transient IntegerProperty quantityProperty;
    private transient StringProperty noteProperty;
    private transient StringProperty qaRequestStatusProperty;

    public IncomingQARequest(String batchId, String type, int quantity, String note, String qaRequestStatus) {
        this.batchId = batchId;
        this.type = type;
        this.quantity = quantity;
        this.note = note;
        this.qaRequestStatus = qaRequestStatus;
        initProperties();
    }

    private void initProperties() {
        batchIdProperty = new SimpleStringProperty(batchId);
        typeProperty = new SimpleStringProperty(type);
        quantityProperty = new SimpleIntegerProperty(quantity);
        noteProperty = new SimpleStringProperty(note);
        qaRequestStatusProperty = new SimpleStringProperty(qaRequestStatus);
    }

    public void refreshProperties() {
        initProperties();
    }

    // Getters & Setters
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

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (quantityProperty != null) quantityProperty.set(quantity);
    }

    public String getNote() { return note; }
    public void setNote(String note) {
        this.note = note;
        if (noteProperty != null) noteProperty.set(note);
    }

    public String getQaRequestStatus() { return qaRequestStatus; }
    public void setQaRequestStatus(String qaRequestStatus) {
        this.qaRequestStatus = qaRequestStatus;
        if (qaRequestStatusProperty != null) qaRequestStatusProperty.set(qaRequestStatus);
    }

    // JavaFX Property Methods
    public StringProperty batchIdProperty() {
        if (batchIdProperty == null) initProperties();
        return batchIdProperty;
    }

    public StringProperty typeProperty() {
        if (typeProperty == null) initProperties();
        return typeProperty;
    }

    public IntegerProperty quantityProperty() {
        if (quantityProperty == null) initProperties();
        return quantityProperty;
    }

    public StringProperty noteProperty() {
        if (noteProperty == null) initProperties();
        return noteProperty;
    }

    public StringProperty qaRequestStatusProperty() {
        if (qaRequestStatusProperty == null) initProperties();
        return qaRequestStatusProperty;
    }

    @Override
    public String toString() {
        return batchId + " | " + type + " | " + quantity + " | " + note + " | " + qaRequestStatus;
    }
}
