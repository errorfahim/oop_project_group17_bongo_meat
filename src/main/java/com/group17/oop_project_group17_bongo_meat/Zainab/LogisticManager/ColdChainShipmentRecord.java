package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import java.io.Serializable;

public class ColdChainShipmentRecord implements Serializable {

    private String vehicleId;
    private String shipmentId;
    private String meatType;
    private double volume;
    private String vehicleType;
    private String timestamp;
    private Double temperatureReading;  // may be null when not recorded
    private String status;              // PENDING / OK / OUT_OF_RANGE / UNKNOWN_CONFIG
    private Double humidity;           // optional

    public ColdChainShipmentRecord(String vehicleId,
                                   String shipmentId,
                                   String meatType,
                                   double volume,
                                   String vehicleType,
                                   String timestamp,
                                   Double temperatureReading,
                                   String status) {
        this.vehicleId = vehicleId;
        this.shipmentId = shipmentId;
        this.meatType = meatType;
        this.volume = volume;
        this.vehicleType = vehicleType;
        this.timestamp = timestamp;
        this.temperatureReading = temperatureReading;
        this.status = status;
    }

    // getters & setters (used by TableView PropertyValueFactory)

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperatureReading() {
        return temperatureReading;
    }

    public void setTemperatureReading(Double temperatureReading) {
        this.temperatureReading = temperatureReading;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
}
