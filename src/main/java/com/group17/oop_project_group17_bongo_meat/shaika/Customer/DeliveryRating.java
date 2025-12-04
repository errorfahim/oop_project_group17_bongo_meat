package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import java.io.Serializable;

public class DeliveryRating implements Serializable {

    private String deliveryId;
    private String orderId;
    private int rating;

    public DeliveryRating(String deliveryId, String orderId, int rating) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.rating = rating;
    }

    public String getDeliveryId() { return deliveryId; }
    public String getOrderId() { return orderId; }
    public int getRating() { return rating; }
}


