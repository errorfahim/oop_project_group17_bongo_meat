package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import java.io.Serializable;

public class WarehouseDispatchBatch implements Serializable {

    private String batchId;
    private String meatType;
    private int quantity;
    private String packagingStatus;
    private String qaStatus;
    private String dispatchStatus;

    private String vehicleId;
    private String driverId;
    private String departureTime;
    private String arrivalTime;

    private String dispatchId;   // generated when creating slip

    public WarehouseDispatchBatch(String batchId,
                                  String meatType,
                                  int quantity,
                                  String packagingStatus,
                                  String qaStatus,
                                  String dispatchStatus,
                                  String vehicleId,
                                  String driverId,
                                  String departureTime,
                                  String arrivalTime,
                                  String dispatchId) {
        this.batchId = batchId;
        this.meatType = meatType;
        this.quantity = quantity;
        this.packagingStatus = packagingStatus;
        this.qaStatus = qaStatus;
        this.dispatchStatus = dispatchStatus;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.dispatchId = dispatchId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPackagingStatus() {
        return packagingStatus;
    }

    public void setPackagingStatus(String packagingStatus) {
        this.packagingStatus = packagingStatus;
    }

    public String getQaStatus() {
        return qaStatus;
    }

    public void setQaStatus(String qaStatus) {
        this.qaStatus = qaStatus;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }
}
