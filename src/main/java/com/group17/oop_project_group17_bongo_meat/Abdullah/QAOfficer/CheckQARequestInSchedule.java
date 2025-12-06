package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckQARequestInSchedule implements Serializable {

    private String batchId;
    private String processingArea;
    private String requiredTest;
    private String priority;
    private LocalDate submissionDate;
    private String status;
    private String notes;

    public CheckQARequestInSchedule(String batchId, String status) {
        this.batchId = batchId;
        this.status = status;
        this.processingArea = "N/A";
        this.requiredTest = "N/A";
        this.priority = "N/A";
        this.submissionDate = LocalDate.now();
        this.notes = "";
    }

    public String getBatchId() { return batchId; }
    public String getProcessingArea() { return processingArea; }
    public String getRequiredTest() { return requiredTest; }
    public String getPriority() { return priority; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    public void setProcessingArea(String v) { this.processingArea = v; }
    public void setRequiredTest(String v) { this.requiredTest = v; }
    public void setPriority(String v) { this.priority = v; }
    public void setStatus(String v) { this.status = v; }
    public void setNotes(String v) { this.notes = v; }
}


