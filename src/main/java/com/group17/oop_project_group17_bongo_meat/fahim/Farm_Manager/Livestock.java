package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import java.io.Serializable;

public class Livestock implements Serializable {
    private static final long serialVersionUID = 1L;

    private String animalId;
    private String animalType;
    private String weight;
    private String age;
    private String availability;
    private String source;
    private String location;
    private String additionalNotes;
    private String healthStatus;

    // Constructor
    public Livestock(String animalId, String animalType, String weight, String age,
                     String availability, String source, String location,
                     String additionalNotes, String healthStatus) {
        this.animalId = animalId;
        this.animalType = animalType;
        this.weight = weight;
        this.age = age;
        this.availability = availability;
        this.source = source;
        this.location = location;
        this.additionalNotes = additionalNotes;
        this.healthStatus = healthStatus;
    }

    // Getters and Setters
    public String getAnimalId() { return animalId; }
    public void setAnimalId(String animalId) { this.animalId = animalId; }

    public String getAnimalType() { return animalType; }
    public void setAnimalType(String animalType) { this.animalType = animalType; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAdditionalNotes() { return additionalNotes; }
    public void setAdditionalNotes(String additionalNotes) { this.additionalNotes = additionalNotes; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    // JavaFX Property methods (for TableView compatibility)
    public javafx.beans.property.SimpleStringProperty animalIdProperty() {
        return new javafx.beans.property.SimpleStringProperty(animalId);
    }

    public javafx.beans.property.SimpleStringProperty animalTypeProperty() {
        return new javafx.beans.property.SimpleStringProperty(animalType);
    }

    public javafx.beans.property.SimpleStringProperty weightProperty() {
        return new javafx.beans.property.SimpleStringProperty(weight);
    }

    public javafx.beans.property.SimpleStringProperty ageProperty() {
        return new javafx.beans.property.SimpleStringProperty(age);
    }

    public javafx.beans.property.SimpleStringProperty availabilityProperty() {
        return new javafx.beans.property.SimpleStringProperty(availability);
    }

    public javafx.beans.property.SimpleStringProperty sourceProperty() {
        return new javafx.beans.property.SimpleStringProperty(source);
    }

    public javafx.beans.property.SimpleStringProperty locationProperty() {
        return new javafx.beans.property.SimpleStringProperty(location);
    }

    public javafx.beans.property.SimpleStringProperty additionalNotesProperty() {
        return new javafx.beans.property.SimpleStringProperty(additionalNotes);
    }

    public javafx.beans.property.SimpleStringProperty healthStatusProperty() {
        return new javafx.beans.property.SimpleStringProperty(healthStatus);
    }
}