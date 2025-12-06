package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;

/**
 * Model class representing a batch of meat for export.
 * Stores slaughter info, QA status, vet status, and final export status.
 */
public class ExportBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    private String batchId;
    private String type;
    private String weight;
    private String slaughterTime;
    private String qaStatus;
    private String vetStatus;
    private String exportStatus;

    public ExportBatch(String batchId, String type, String weight, String slaughterTime,
                       String qaStatus, String vetStatus, String exportStatus) {
        this.batchId = batchId;
        this.type = type;
        this.weight = weight;
        this.slaughterTime = slaughterTime;
        this.qaStatus = qaStatus;
        this.vetStatus = vetStatus;
        this.exportStatus = exportStatus;
    }

    // --- Getters ---
    public String getBatchId() { return batchId; }
    public String getType() { return type; }
    public String getWeight() { return weight; }
    public String getSlaughterTime() { return slaughterTime; }
    public String getQaStatus() { return qaStatus; }
    public String getVetStatus() { return vetStatus; }
    public String getExportStatus() { return exportStatus; }

    // --- Setters ---
    public void setExportStatus(String exportStatus) { this.exportStatus = exportStatus; }
}
