package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.beans.property.*;
import java.io.Serializable;

public class RecordIncomingLivestock implements Serializable {

    private String batchID;
    private String type;
    private String breed;
    private int quantity;
    private double weight;
    private String healthObservation;

    public RecordIncomingLivestock(String batchID, String type, String breed,
                                   int quantity, double weight, String healthObservation) {

        this.batchID = batchID;
        this.type = type;
        this.breed = breed;
        this.quantity = quantity;
        this.weight = weight;
        this.healthObservation = healthObservation;
    }

    public StringProperty batchIDProperty() { return new SimpleStringProperty(batchID); }
    public StringProperty typeProperty() { return new SimpleStringProperty(type); }
    public StringProperty breedProperty() { return new SimpleStringProperty(breed); }
    public IntegerProperty quantityProperty() { return new SimpleIntegerProperty(quantity); }
    public DoubleProperty weightProperty() { return new SimpleDoubleProperty(weight); }
    public StringProperty healthObservationProperty() { return new SimpleStringProperty(healthObservation); }

    public String getBatchID() { return batchID; }
    public String getType() { return type; }
    public String getBreed() { return breed; }
    public int getQuantity() { return quantity; }
    public double getWeight() { return weight; }
    public String getHealthObservation() { return healthObservation; }
}
