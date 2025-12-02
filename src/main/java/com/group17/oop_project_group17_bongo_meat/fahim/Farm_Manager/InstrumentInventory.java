package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import java.io.Serializable;
import java.time.LocalDate;

public class InstrumentInventory implements Serializable {
    private String instrumentCategory;
    private String instrumentName;
    private String instrumentId;
    private int quantity;
    private LocalDate entryDate;

    public String getInstrumentCategory() {
        return instrumentCategory;
    }

    public void setInstrumentCategory(String instrumentCategory) {
        this.instrumentCategory = instrumentCategory;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public InstrumentInventory(String instrumentCategory, String instrumentName, String instrumentId, int quantity, LocalDate entryDate) {
        this.instrumentCategory = instrumentCategory;
        this.instrumentName = instrumentName;
        this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.entryDate = entryDate;
    }

    @Override
    public String toString() {
        return "InstrumentInventory{" +
                "instrumentCategory='" + instrumentCategory + '\'' +
                ", instrumentName='" + instrumentName + '\'' +
                ", instrumentId='" + instrumentId + '\'' +
                ", quantity=" + quantity +
                ", entryDate=" + entryDate +
                '}';
    }
}
