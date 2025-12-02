package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class MedicineInventoryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String medicineName;
    private String batchNo;
    private int quantity;
    private String dosage;
    private String expiryDate;   // store as ISO string (yyyy-MM-dd)
    private String status;       // OK / Low Stock / Expiring Soon / Expired

    public MedicineInventoryItem(String medicineName,
                                 String batchNo,
                                 int quantity,
                                 String dosage,
                                 String expiryDate,
                                 String status) {
        this.medicineName = medicineName;
        this.batchNo = batchNo;
        this.quantity = quantity;
        this.dosage = dosage;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // ---- getters & setters (for logic / saving) ----
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ---- JavaFX properties for TableView ----
    public StringProperty medicineNameProperty() {
        return new SimpleStringProperty(medicineName);
    }

    public StringProperty batchNoProperty() {
        return new SimpleStringProperty(batchNo);
    }

    public IntegerProperty quantityProperty() {
        return new SimpleIntegerProperty(quantity);
    }

    public StringProperty dosageProperty() {
        return new SimpleStringProperty(dosage);
    }

    public StringProperty expiryDateProperty() {
        return new SimpleStringProperty(expiryDate);
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(status);
    }
}
