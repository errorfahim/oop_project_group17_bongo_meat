package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import java.io.Serializable;

public class SlaughterDailyReport implements Serializable {

    private String batchId;
    private String type;
    private String slaughterTime;
    private int quantity;
    private int remaining;
    private String staff;
    private String status;

    public SlaughterDailyReport() {}

    public SlaughterDailyReport(String batchId, String type, String slaughterTime,
                                int quantity, int remaining, String staff, String status) {
        this.batchId = batchId;
        this.type = type;
        this.slaughterTime = slaughterTime;
        this.quantity = quantity;
        this.remaining = remaining;
        this.staff = staff;
        this.status = status;
    }

    public String getBatchId() { return batchId; }
    public String getType() { return type; }
    public String getSlaughterTime() { return slaughterTime; }
    public int getQuantity() { return quantity; }
    public int getRemaining() { return remaining; }
    public String getStaff() { return staff; }
    public String getStatus() { return status; }
}

