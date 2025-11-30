package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.beans.property.*;
import java.io.Serializable;

public class RequestQAForIncomingLivestock implements Serializable {

    //private static final long serialVersionUID = 1L;

    private String batchID;
    private String type;
    private int quantity;
    private String note;
    private String qaRequestStatus;

    public RequestQAForIncomingLivestock(String batchID, String type, int quantity, String note, String qaRequestStatus) {
        this.batchID = batchID;
        this.type = type;
        this.quantity = quantity;
        this.note = note;
        this.qaRequestStatus = qaRequestStatus;
    }

    // --- JavaFX Property methods for TableView ---
    public StringProperty batchIdProperty() {
        return new SimpleStringProperty(batchID);
    }

    public StringProperty typeProperty() {
        return new SimpleStringProperty(type);
    }

    public IntegerProperty quantityProperty() {
        return new SimpleIntegerProperty(quantity);
    }

    public StringProperty qaRequestStatusProperty() {
        return new SimpleStringProperty(qaRequestStatus);
    }

    // --- Standard getters ---
    public String getBatchId() { return batchID; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public String getNote() { return note; }
    public String getQaRequestStatus() { return qaRequestStatus; }

    // --- Setters ---
    public void setNote(String note) { this.note = note; }
    public void setQaRequestStatus(String qaRequestStatus) { this.qaRequestStatus = qaRequestStatus; }
}



