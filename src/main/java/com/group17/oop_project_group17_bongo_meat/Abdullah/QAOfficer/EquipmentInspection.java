package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import java.io.Serializable;
import java.time.LocalDate;

public class EquipmentInspection implements Serializable {
    private String department;
    private String equipment;
    private String status;
    private String notes;
    private LocalDate date;

    public EquipmentInspection(String department, String equipment, String status, String notes, LocalDate date) {
        this.department = department;
        this.equipment = equipment;
        this.status = status;
        this.notes = notes;
        this.date = date;
    }

    // Getters and setters
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}


