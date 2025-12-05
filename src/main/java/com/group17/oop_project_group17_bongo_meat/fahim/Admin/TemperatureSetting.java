package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import java.io.Serializable;

public class TemperatureSetting implements Serializable {

    private String transportation;
    private String chilling;
    private String cutting;
    private String packaging;
    private String chilledStorage;
    private String blastFreezing;
    private String truck;
    private String reeferCon;
    private String frozen;

    public TemperatureSetting(String transportation, String chilling, String cutting,
                              String packaging, String chilledStorage, String blastFreezing,
                              String truck, String reeferCon, String frozen) {
        this.transportation = transportation;
        this.chilling = chilling;
        this.cutting = cutting;
        this.packaging = packaging;
        this.chilledStorage = chilledStorage;
        this.blastFreezing = blastFreezing;
        this.truck = truck;
        this.reeferCon = reeferCon;
        this.frozen = frozen;
    }

    public String getTransportation() { return transportation; }
    public String getChilling() { return chilling; }
    public String getCutting() { return cutting; }
    public String getPackaging() { return packaging; }
    public String getChilledStorage() { return chilledStorage; }
    public String getBlastFreezing() { return blastFreezing; }
    public String getTruck() { return truck; }
    public String getReeferCon() { return reeferCon; }
    public String getFrozen() { return frozen; }

    public void setTransportation(String transportation) { this.transportation = transportation; }
    public void setChilling(String chilling) { this.chilling = chilling; }
    public void setCutting(String cutting) { this.cutting = cutting; }
    public void setPackaging(String packaging) { this.packaging = packaging; }
    public void setChilledStorage(String chilledStorage) { this.chilledStorage = chilledStorage; }
    public void setBlastFreezing(String blastFreezing) { this.blastFreezing = blastFreezing; }
    public void setTruck(String truck) { this.truck = truck; }
    public void setReeferCon(String reeferCon) { this.reeferCon = reeferCon; }
    public void setFrozen(String frozen) { this.frozen = frozen; }
}
