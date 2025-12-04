package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import java.io.Serializable;
import java.time.LocalDate;

public class DeliveryIssue implements Serializable {

    private String deliveryId;
    private LocalDate issueDate;
    private String description;

    public DeliveryIssue(String deliveryId, LocalDate issueDate, String description) {
        this.deliveryId = deliveryId;
        this.issueDate = issueDate;
        this.description = description;
    }

    public String getDeliveryId() { return deliveryId; }
    public LocalDate getIssueDate() { return issueDate; }
    public String getDescription() { return description; }
}

