package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import java.io.Serializable;

public class SlaughterOutputReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchId;
    private String type;
    private int quantity;
    private int remaining;
    private String weight;
    private String wastes;
    private String staff;
    private String status;
    private String observation;
    private String slaughterTime; // optional if you want to store time separately

    public SlaughterOutputReport(String batchId, String type, int quantity, int remaining,
                                 String weight, String wastes, String staff,
                                 String status, String observation) {
        this.batchId = batchId;
        this.type = type;
        this.quantity = quantity;
        this.remaining = remaining;
        this.weight = weight;
        this.wastes = wastes;
        this.staff = staff;
        this.status = status;
        this.observation = observation;
        this.slaughterTime = ""; // default empty, can be updated later
    }

    // =================== GETTERS & SETTERS ===================
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getRemaining() { return remaining; }
    public void setRemaining(int remaining) { this.remaining = remaining; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getWastes() { return wastes; }
    public void setWastes(String wastes) { this.wastes = wastes; }

    public String getStaff() { return staff; }
    public void setStaff(String staff) { this.staff = staff; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public String getSlaughterTime() { return slaughterTime; }
    public void setSlaughterTime(String slaughterTime) { this.slaughterTime = slaughterTime; }

    @Override
    public String toString() {
        return batchId + " | " + type + " | " + quantity + " | " + remaining + " | " +
                weight + " | " + wastes + " | " + staff + " | " + status + " | " + observation;
    }
}

