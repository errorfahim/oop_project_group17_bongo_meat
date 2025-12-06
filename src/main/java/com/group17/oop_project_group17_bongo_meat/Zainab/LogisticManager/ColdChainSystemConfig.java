package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import java.io.Serializable;

public class ColdChainSystemConfig implements Serializable {

    private String meatType;
    private double minSafeTemp;
    private double maxSafeTemp;
    private double humidity;
    private String timestamp;

    public ColdChainSystemConfig(String meatType,
                                 double minSafeTemp,
                                 double maxSafeTemp,
                                 double humidity,
                                 String timestamp) {
        this.meatType = meatType;
        this.minSafeTemp = minSafeTemp;
        this.maxSafeTemp = maxSafeTemp;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }

    public String getMeatType() {
        return meatType;
    }

    public void setMeatType(String meatType) {
        this.meatType = meatType;
    }

    public double getMinSafeTemp() {
        return minSafeTemp;
    }

    public void setMinSafeTemp(double minSafeTemp) {
        this.minSafeTemp = minSafeTemp;
    }

    public double getMaxSafeTemp() {
        return maxSafeTemp;
    }

    public void setMaxSafeTemp(double maxSafeTemp) {
        this.maxSafeTemp = maxSafeTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
