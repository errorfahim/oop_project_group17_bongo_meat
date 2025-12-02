package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import java.io.Serializable;
import java.time.LocalDate;

public class Feeding implements Serializable {
    private int animalId;
    private String animalType;
    private String feedType;
    private double quantity;
    private LocalDate feedingDate;
    private String feedingTime;

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

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public LocalDate getFeedingDate() {
        return feedingDate;
    }

    public void setFeedingDate(LocalDate feedingDate) {
        this.feedingDate = feedingDate;
    }

    public String getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(String feedingTime) {
        this.feedingTime = feedingTime;
    }

    public Feeding(int animalId, String animalType, String feedType, double quantity, LocalDate feedingDate, String feedingTime) {
        this.animalId = animalId;
        this.animalType = animalType;
        this.feedType = feedType;
        this.quantity = quantity;
        this.feedingDate = feedingDate;
        this.feedingTime = feedingTime;
    }

    @Override
    public String toString() {
        return "Feeding{" +
                "animalId=" + animalId +
                ", animalType='" + animalType + '\'' +
                ", feedType='" + feedType + '\'' +
                ", quantity=" + quantity +
                ", feedingDate=" + feedingDate +
                ", feedingTime='" + feedingTime + '\'' +
                '}';
    }
}
