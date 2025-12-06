package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;
import java.time.LocalDate;

public class DailyQAReport implements Serializable {
    private String batchId;
    private String type;
    private double weight;
    private boolean meatQualityCheck;
    private boolean livestockConditionCheck;
    private boolean traceabilityCheck;
    private boolean wasteManagementCheck;
    private boolean exportCertStatus;
    private boolean packagingCheck;
    private String notes;
    private LocalDate reportDate;

    public DailyQAReport(String batchId, String type, double weight,
                         boolean meatQualityCheck, boolean livestockConditionCheck,
                         boolean traceabilityCheck, boolean wasteManagementCheck,
                         boolean exportCertStatus, boolean packagingCheck,
                         String notes, LocalDate reportDate) {
        this.batchId = batchId;
        this.type = type;
        this.weight = weight;
        this.meatQualityCheck = meatQualityCheck;
        this.livestockConditionCheck = livestockConditionCheck;
        this.traceabilityCheck = traceabilityCheck;
        this.wasteManagementCheck = wasteManagementCheck;
        this.exportCertStatus = exportCertStatus;
        this.packagingCheck = packagingCheck;
        this.notes = notes;
        this.reportDate = reportDate;
    }

    // Getters
    public String getBatchId() { return batchId; }
    public String getType() { return type; }
    public double getWeight() { return weight; }
    public boolean isMeatQualityCheck() { return meatQualityCheck; }
    public boolean isLivestockConditionCheck() { return livestockConditionCheck; }
    public boolean isTraceabilityCheck() { return traceabilityCheck; }
    public boolean isWasteManagementCheck() { return wasteManagementCheck; }
    public boolean isExportCertStatus() { return exportCertStatus; }
    public boolean isPackagingCheck() { return packagingCheck; }
    public String getNotes() { return notes; }
    public LocalDate getReportDate() { return reportDate; }

    // Setters
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public void setType(String type) { this.type = type; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setMeatQualityCheck(boolean meatQualityCheck) { this.meatQualityCheck = meatQualityCheck; }
    public void setLivestockConditionCheck(boolean livestockConditionCheck) { this.livestockConditionCheck = livestockConditionCheck; }
    public void setTraceabilityCheck(boolean traceabilityCheck) { this.traceabilityCheck = traceabilityCheck; }
    public void setWasteManagementCheck(boolean wasteManagementCheck) { this.wasteManagementCheck = wasteManagementCheck; }
    public void setExportCertStatus(boolean exportCertStatus) { this.exportCertStatus = exportCertStatus; }
    public void setPackagingCheck(boolean packagingCheck) { this.packagingCheck = packagingCheck; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
}
