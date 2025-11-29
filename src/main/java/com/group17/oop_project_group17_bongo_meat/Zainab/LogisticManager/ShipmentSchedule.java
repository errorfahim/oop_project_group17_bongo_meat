package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

public class ShipmentSchedule {
    private int batchID;
    private String destination;
    private String meatType;
    private double volume;
    private double requiredTemperature;
    private String deliveryPriority;

    public ShipmentSchedule(int batchID, String destination, String meatType, double volume, double requiredTemperature, String deliveryPriority) {
        this.batchID = batchID;
        this.destination = destination;
        this.meatType = meatType;
        this.volume = volume;
        this.requiredTemperature = requiredTemperature;
        this.deliveryPriority = deliveryPriority;
    }

    public int getBatchID() { return batchID; }
    public String getDestination() { return destination; }
    public String getMeatType() { return meatType; }
    public double getVolume() { return volume; }
    public double getRequiredTemperature() { return requiredTemperature; }
    public String getDeliveryPriority() { return deliveryPriority; }

    public void setBatchID(int batchID) { this.batchID = batchID; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setMeatType(String meatType) { this.meatType = meatType; }
    public void setVolume(double volume) { this.volume = volume; }
    public void setRequiredTemperature(double requiredTemperature) { this.requiredTemperature = requiredTemperature; }
    public void setDeliveryPriority(String deliveryPriority) { this.deliveryPriority = deliveryPriority; }
}
