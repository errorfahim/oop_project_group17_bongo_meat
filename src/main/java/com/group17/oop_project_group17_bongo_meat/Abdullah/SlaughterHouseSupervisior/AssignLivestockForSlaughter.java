package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import java.io.Serializable;

public class AssignLivestockForSlaughter implements Serializable {
    // static final long serialVersionUID = 1L;

    private String batchId;
    private String type;
    private int totalQuantity;

    private String vetApproved;
    private String qaApproved;

    private String assignedTime;
    private String staffName;

    private int slaughterAmount;
    private int remainingQuantity;

    public AssignLivestockForSlaughter(String batchId, String type, int totalQuantity) {
        this.batchId = batchId;
        this.type = type;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = totalQuantity;

        this.vetApproved = "Pending";
        this.qaApproved = "Pending";

        this.assignedTime = "";
        this.staffName = "";
        this.slaughterAmount = 0;
    }

    // Getters and setters
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = totalQuantity - this.slaughterAmount;
    }

    public String getVetApproved() { return vetApproved; }
    public void setVetApproved(String vetApproved) { this.vetApproved = vetApproved; }

    public String getQaApproved() { return qaApproved; }
    public void setQaApproved(String qaApproved) { this.qaApproved = qaApproved; }

    public String getAssignedTime() { return assignedTime; }
    public void setAssignedTime(String assignedTime) { this.assignedTime = assignedTime; }

    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }

    public int getSlaughterAmount() { return slaughterAmount; }
    public void setSlaughterAmount(int slaughterAmount) {
        this.slaughterAmount = slaughterAmount;
        this.remainingQuantity = totalQuantity - slaughterAmount;
    }

    public int getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(int remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    @Override
    public String toString() {
        return "BatchID: " + batchId + ", Type: " + type + ", Total: " + totalQuantity +
                ", Vet: " + vetApproved + ", QA: " + qaApproved +
                ", AssignedTime: " + assignedTime + ", Staff: " + staffName +
                ", Slaughter: " + slaughterAmount + ", Remaining: " + remainingQuantity;
    }
}



