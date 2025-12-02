package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class PreSlaughterInspectionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // Top table info (from Supervisor file)
    private String batchId;
    private String type;             // comes from Abdullah's file (not in table columns)
    private int animalCount;         // dummy or quantity from Abdullah
    private String lastHealthCheckDate; // dummy
    private String holdingTime;         // dummy

    // Inspection form fields
    private String temperature;
    private String movementBehavior;
    private String hydration;
    private String injuryCheck;
    private String stressLevel;
    private String feedWithdrawalStatus;
    private String cleanliness;
    private String finalRemarks;

    // Decision & meta
    private String decision;         // "Approved for Slaughter" / "Quarantine Required"
    private String vetId;            // dummy
    private String vetLicense;       // dummy
    private String inspectionDateTime;

    public PreSlaughterInspectionRecord(String batchId,
                                        String type,
                                        int animalCount,
                                        String lastHealthCheckDate,
                                        String holdingTime,
                                        String temperature,
                                        String movementBehavior,
                                        String hydration,
                                        String injuryCheck,
                                        String stressLevel,
                                        String feedWithdrawalStatus,
                                        String cleanliness,
                                        String finalRemarks,
                                        String decision,
                                        String vetId,
                                        String vetLicense,
                                        String inspectionDateTime) {
        this.batchId = batchId;
        this.type = type;
        this.animalCount = animalCount;
        this.lastHealthCheckDate = lastHealthCheckDate;
        this.holdingTime = holdingTime;
        this.temperature = temperature;
        this.movementBehavior = movementBehavior;
        this.hydration = hydration;
        this.injuryCheck = injuryCheck;
        this.stressLevel = stressLevel;
        this.feedWithdrawalStatus = feedWithdrawalStatus;
        this.cleanliness = cleanliness;
        this.finalRemarks = finalRemarks;
        this.decision = decision;
        this.vetId = vetId;
        this.vetLicense = vetLicense;
        this.inspectionDateTime = inspectionDateTime;
    }

    // -------- getters used for saving / PDF --------
    public String getBatchId() { return batchId; }
    public String getType() { return type; }
    public int getAnimalCount() { return animalCount; }
    public String getLastHealthCheckDate() { return lastHealthCheckDate; }
    public String getHoldingTime() { return holdingTime; }

    public String getTemperature() { return temperature; }
    public String getMovementBehavior() { return movementBehavior; }
    public String getHydration() { return hydration; }
    public String getInjuryCheck() { return injuryCheck; }
    public String getStressLevel() { return stressLevel; }
    public String getFeedWithdrawalStatus() { return feedWithdrawalStatus; }
    public String getCleanliness() { return cleanliness; }
    public String getFinalRemarks() { return finalRemarks; }

    public String getDecision() { return decision; }
    public String getVetId() { return vetId; }
    public String getVetLicense() { return vetLicense; }
    public String getInspectionDateTime() { return inspectionDateTime; }

    // -------- JavaFX properties for TableView --------
    public StringProperty batchIdProperty() { return new SimpleStringProperty(batchId); }
    public IntegerProperty animalCountProperty() { return new SimpleIntegerProperty(animalCount); }
    public StringProperty lastHealthCheckDateProperty() { return new SimpleStringProperty(lastHealthCheckDate); }
    public StringProperty holdingTimeProperty() { return new SimpleStringProperty(holdingTime); }
}
