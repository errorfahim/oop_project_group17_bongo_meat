package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class IncomingQARequest implements Serializable {

    private String batchID;
    private String type;
    private int quantity;
    private String note;
    private String status; // Pending, Approved, Rejected

    public IncomingQARequest(String batchID, String type, int quantity, String note, String status) {
        this.batchID = batchID;
        this.type = type;
        this.quantity = quantity;
        this.note = note;
        this.status = status;
    }

    // JavaFX Property methods for TableView
    public StringProperty batchIdProperty() { return new SimpleStringProperty(batchID); }
    public StringProperty typeProperty() { return new SimpleStringProperty(type); }
    public IntegerProperty quantityProperty() { return new SimpleIntegerProperty(quantity); }
    public StringProperty noteProperty() { return new SimpleStringProperty(note); }
    public StringProperty statusProperty() { return new SimpleStringProperty(status); }

    // Standard getters
    public String getBatchId() { return batchID; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public String getNote() { return note; }
    public String getStatus() { return status; }

    // Setter for status (approve/reject)
    public void setStatus(String status) { this.status = status; }
}

