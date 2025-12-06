package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import java.io.Serializable;
import java.time.LocalDate;

public class SystemAlert implements Serializable {

    private LocalDate date;
    private String message;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SystemAlert(LocalDate date, String message) {
        this.date = date;
        this.message = message;
    }

    @Override
    public String toString() {
        return "SystemAlert{" +
                "date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}