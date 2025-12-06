package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;

/**
 * Simple serializable model for QA table rows.
 * We use plain fields for serialization and provide getters/setters.
 */
public class MeatTestingRequest implements Serializable {

    //private static final long serialVersionUID = 1L;

    private String batchId;
    private String slaughterTime;
    private String weight;
    private String type;

    // QA fields: -1 denotes invalid / not provided
    private int ph = -1;
    private int colorScore = -1;
    private int microbialCount = -1;
    private int moisture = -1;
    private String odor = "";
    private int meatTemp = -1;
    private String remarks = "";

    // status values: "Request Sent", "Delivered", "Rejected", "Test Complete (Approved)", "Test Complete (Rejected)"
    private String status = "Request Sent";

    public MeatTestingRequest() { }

    public MeatTestingRequest(String batchId, String slaughterTime, String weight, String type, String status) {
        this.batchId = batchId;
        this.slaughterTime = slaughterTime;
        this.weight = weight;
        this.type = type;
        this.status = status;
    }

    // --- Getters & setters ---
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getSlaughterTime() { return slaughterTime; }
    public void setSlaughterTime(String slaughterTime) { this.slaughterTime = slaughterTime; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getPh() { return ph; }
    public void setPh(int ph) { this.ph = ph; }

    public int getColorScore() { return colorScore; }
    public void setColorScore(int colorScore) { this.colorScore = colorScore; }

    public int getMicrobialCount() { return microbialCount; }
    public void setMicrobialCount(int microbialCount) { this.microbialCount = microbialCount; }

    public int getMoisture() { return moisture; }
    public void setMoisture(int moisture) { this.moisture = moisture; }

    public String getOdor() { return odor; }
    public void setOdor(String odor) { this.odor = odor; }

    public int getMeatTemp() { return meatTemp; }
    public void setMeatTemp(int meatTemp) { this.meatTemp = meatTemp; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

