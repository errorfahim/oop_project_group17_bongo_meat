package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class NutritionFeedingRecord implements Serializable {

    private String animalId;
    private String type;
    private double weight;
    private String feedType;
    private int quantity;
    private String feedingTime;   // date/time as string
    private String status;
    private String notes;

    public NutritionFeedingRecord(String animalId,
                                  String type,
                                  double weight,
                                  String feedType,
                                  int quantity,
                                  String feedingTime,
                                  String status,
                                  String notes) {
        this.animalId = animalId;
        this.type = type;
        this.weight = weight;
        this.feedType = feedType;
        this.quantity = quantity;
        this.feedingTime = feedingTime;
        this.status = status;
        this.notes = notes;
    }

    // ---------- JavaFX properties for TableView ----------

    public StringProperty animalIdProperty() {
        return new SimpleStringProperty(animalId);
    }

    public StringProperty typeProperty() {
        return new SimpleStringProperty(type);
    }

    public DoubleProperty weightProperty() {
        return new SimpleDoubleProperty(weight);
    }

    public StringProperty feedTypeProperty() {
        return new SimpleStringProperty(feedType);
    }

    public IntegerProperty quantityProperty() {
        return new SimpleIntegerProperty(quantity);
    }

    public StringProperty feedingTimeProperty() {
        return new SimpleStringProperty(feedingTime);
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(status);
    }

    // ---------- getters / setters ----------

    public String getAnimalId() { return animalId; }
    public String getType() { return type; }
    public double getWeight() { return weight; }
    public String getFeedType() { return feedType; }
    public int getQuantity() { return quantity; }
    public String getFeedingTime() { return feedingTime; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    public void setFeedType(String feedType) { this.feedType = feedType; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setFeedingTime(String feedingTime) { this.feedingTime = feedingTime; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
}
