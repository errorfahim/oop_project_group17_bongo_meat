package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;
import java.io.Serializable;

public class HealthStatus implements  Serializable{
    private int animalId;            // Animal ID (numeric in most systems)
    private String animalType;       // Example: Cow, Goat, Sheep
    private double temperature;      // Body temperature (decimal)
    private String symptoms;         // Free-text symptoms
    private String vaccination;      // Example: Yes / No / Pending
    private String healthStatus;

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getVaccination() {
        return vaccination;
    }

    public void setVaccination(String vaccination) {
        this.vaccination = vaccination;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public HealthStatus(int animalId, String animalType, double temperature, String symptoms, String vaccination, String healthStatus) {
        this.animalId = animalId;
        this.animalType = animalType;
        this.temperature = temperature;
        this.symptoms = symptoms;
        this.vaccination = vaccination;
        this.healthStatus = healthStatus;
    }

    @Override
    public String toString() {
        return "HealthStatus{" +
                "animalId=" + animalId +
                ", animalType='" + animalType + '\'' +
                ", temperature=" + temperature +
                ", symptoms='" + symptoms + '\'' +
                ", vaccination='" + vaccination + '\'' +
                ", healthStatus='" + healthStatus + '\'' +
                '}';
    }
}
