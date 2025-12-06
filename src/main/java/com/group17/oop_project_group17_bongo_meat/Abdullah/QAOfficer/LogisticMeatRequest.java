package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;
import java.time.LocalDate;

public class LogisticMeatRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String batchId;
    private String type;
    private double weight;
    private String slaughterTime;
    private String qaStatus;
    private String vetStatus;
    private String exportStatus;
    private String requestStatus;
    private LocalDate requestDate;

    public LogisticMeatRequest(String batchId, String type, double weight, String slaughterTime,
                               String qaStatus, String vetStatus, String exportStatus,
                               String requestStatus, LocalDate requestDate) {
        this.batchId = batchId;
        this.type = type;
        this.weight = weight;
        this.slaughterTime = slaughterTime;
        this.qaStatus = qaStatus;
        this.vetStatus = vetStatus;
        this.exportStatus = exportStatus;
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
    }

    // --- Getters ---
    public String getBatchId() { return batchId; }
    public String getType() { return type; }
    public double getWeight() { return weight; }
    public String getSlaughterTime() { return slaughterTime; }
    public String getQaStatus() { return qaStatus; }
    public String getVetStatus() { return vetStatus; }
    public String getExportStatus() { return exportStatus; }
    public String getRequestStatus() { return requestStatus; }
    public LocalDate getRequestDate() { return requestDate; }

    // --- Setters ---
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public void setType(String type) { this.type = type; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setSlaughterTime(String slaughterTime) { this.slaughterTime = slaughterTime; }
    public void setQaStatus(String qaStatus) { this.qaStatus = qaStatus; }
    public void setVetStatus(String vetStatus) { this.vetStatus = vetStatus; }
    public void setExportStatus(String exportStatus) { this.exportStatus = exportStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
}
