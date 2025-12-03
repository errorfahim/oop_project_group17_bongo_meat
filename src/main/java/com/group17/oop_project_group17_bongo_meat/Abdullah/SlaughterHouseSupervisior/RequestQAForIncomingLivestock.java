package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.beans.property.*;

import java.io.IOException;
import java.io.Serializable;

public class RequestQAForIncomingLivestock implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchId;
    private String type;
    private int quantity;
    private String note;
    private String qaRequestStatus;

    private transient StringProperty batchIdProperty;
    private transient StringProperty typeProperty;
    private transient IntegerProperty quantityProperty;
    private transient StringProperty noteProperty;
    private transient StringProperty qaRequestStatusProperty;

    public RequestQAForIncomingLivestock() {
        this("", "", 0, "", "Pending");
    }

    public RequestQAForIncomingLivestock(String batchId, String type, int quantity, String note, String qaRequestStatus) {
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

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initProperties();
    }

    // Getters
    public String getBatchId() { return batchId; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public String getNote() { return note; }
    public String getQaRequestStatus() { return qaRequestStatus; }

    // Setters
    public void setBatchId(String batchId) {
        this.batchId = batchId;
        if (batchIdProperty != null) batchIdProperty.set(batchId);
    }

    public void setType(String type) {
        this.type = type;
        if (typeProperty != null) typeProperty.set(type);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (quantityProperty != null) quantityProperty.set(quantity);
    }

    public void setNote(String note) {
        this.note = note;
        if (noteProperty != null) noteProperty.set(note);
    }

    public void setQaRequestStatus(String qaRequestStatus) {
        this.qaRequestStatus = qaRequestStatus;
        if (qaRequestStatusProperty != null) qaRequestStatusProperty.set(qaRequestStatus);
    }

    // Properties
    public StringProperty batchIdProperty() {
        if (batchIdProperty == null) batchIdProperty = new SimpleStringProperty(batchId);
        return batchIdProperty;
    }

    public StringProperty typeProperty() {
        if (typeProperty == null) typeProperty = new SimpleStringProperty(type);
        return typeProperty;
    }

    public IntegerProperty quantityProperty() {
        if (quantityProperty == null) quantityProperty = new SimpleIntegerProperty(quantity);
        return quantityProperty;
    }

    public StringProperty noteProperty() {
        if (noteProperty == null) noteProperty = new SimpleStringProperty(note);
        return noteProperty;
    }

    public StringProperty qaRequestStatusProperty() {
        if (qaRequestStatusProperty == null) qaRequestStatusProperty = new SimpleStringProperty(qaRequestStatus);
        return qaRequestStatusProperty;
    }

    @Override
    public String toString() {
        return "RequestQAForIncomingLivestock{" +
                "batchId='" + batchId + '\'' +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                ", note='" + note + '\'' +
                ", qaRequestStatus='" + qaRequestStatus + '\'' +
                '}';
    }
}


