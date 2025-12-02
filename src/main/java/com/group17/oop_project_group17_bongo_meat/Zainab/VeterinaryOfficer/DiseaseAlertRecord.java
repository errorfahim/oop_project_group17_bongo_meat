package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;


public class DiseaseAlertRecord implements Serializable {

    private String alertId;
    private String batchId;
    private int numberAffected;
    private String symptoms;
    private String onsetDate;      // as String
    private String priority;
    private boolean samplesCollected;
    private String status;         // "Draft" / "Published"

    public DiseaseAlertRecord(String alertId,
                              String batchId,
                              int numberAffected,
                              String symptoms,
                              String onsetDate,
                              String priority,
                              boolean samplesCollected,
                              String status) {
        this.alertId = alertId;
        this.batchId = batchId;
        this.numberAffected = numberAffected;
        this.symptoms = symptoms;
        this.onsetDate = onsetDate;
        this.priority = priority;
        this.samplesCollected = samplesCollected;
        this.status = status;
    }

    public String getAlertId() { return alertId; }
    public String getBatchId() { return batchId; }
    public int getNumberAffected() { return numberAffected; }
    public String getSymptoms() { return symptoms; }
    public String getOnsetDate() { return onsetDate; }
    public String getPriority() { return priority; }
    public boolean isSamplesCollected() { return samplesCollected; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
