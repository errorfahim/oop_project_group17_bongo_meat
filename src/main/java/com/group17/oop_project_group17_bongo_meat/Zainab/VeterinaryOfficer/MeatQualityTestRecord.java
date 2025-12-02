package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.beans.property.*;
import java.io.Serializable;

public class MeatQualityTestRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    // --- Top table (batch info) ---
    private String batchId;
    private String cutType;
    private double weight;
    private String processingDate;
    private String storageTime;
    private String temperature;

    // --- Lab test fields ---
    private double pHValue;
    private int microbialCount;
    private Double moisturePercent;          // can be null (optional)
    private double storageTemp;
    private int colorScore;                  // 1–5
    private String odorEvaluation;
    private String foreignMatterCheck;
    private String remarks;

    // --- Evaluation meta ---
    private String evaluationResult;         // "Pass" / "Fail"
    private String vetId;
    private String vetLicense;
    private String testDateTime;

    public MeatQualityTestRecord(String batchId,
                                 String cutType,
                                 double weight,
                                 String processingDate,
                                 String storageTime,
                                 String temperature,
                                 double pHValue,
                                 int microbialCount,
                                 Double moisturePercent,
                                 double storageTemp,
                                 int colorScore,
                                 String odorEvaluation,
                                 String foreignMatterCheck,
                                 String remarks,
                                 String evaluationResult,
                                 String vetId,
                                 String vetLicense,
                                 String testDateTime) {
        this.batchId = batchId;
        this.cutType = cutType;
        this.weight = weight;
        this.processingDate = processingDate;
        this.storageTime = storageTime;
        this.temperature = temperature;
        this.pHValue = pHValue;
        this.microbialCount = microbialCount;
        this.moisturePercent = moisturePercent;
        this.storageTemp = storageTemp;
        this.colorScore = colorScore;
        this.odorEvaluation = odorEvaluation;
        this.foreignMatterCheck = foreignMatterCheck;
        this.remarks = remarks;
        this.evaluationResult = evaluationResult;
        this.vetId = vetId;
        this.vetLicense = vetLicense;
        this.testDateTime = testDateTime;
    }

    // ---- getters for saving / PDF ----
    public String getBatchId() { return batchId; }
    public String getCutType() { return cutType; }
    public double getWeight() { return weight; }
    public String getProcessingDate() { return processingDate; }
    public String getStorageTime() { return storageTime; }
    public String getTemperature() { return temperature; }

    public double getpHValue() { return pHValue; }
    public int getMicrobialCount() { return microbialCount; }
    public Double getMoisturePercent() { return moisturePercent; }
    public double getStorageTemp() { return storageTemp; }
    public int getColorScore() { return colorScore; }
    public String getOdorEvaluation() { return odorEvaluation; }
    public String getForeignMatterCheck() { return foreignMatterCheck; }
    public String getRemarks() { return remarks; }

    public String getEvaluationResult() { return evaluationResult; }
    public String getVetId() { return vetId; }
    public String getVetLicense() { return vetLicense; }
    public String getTestDateTime() { return testDateTime; }

    // ---- JavaFX properties for the TableView ----
    public StringProperty batchIdProperty() {
        return new SimpleStringProperty(batchId);
    }

    public StringProperty cutTypeProperty() {
        return new SimpleStringProperty(cutType);
    }

    public DoubleProperty weightProperty() {
        return new SimpleDoubleProperty(weight);
    }

    public StringProperty processingDateProperty() {
        return new SimpleStringProperty(processingDate);
    }

    public StringProperty storageTimeProperty() {
        return new SimpleStringProperty(storageTime);
    }

    public StringProperty temperatureProperty() {
        return new SimpleStringProperty(temperature);
    }
}
