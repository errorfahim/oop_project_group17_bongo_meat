package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import java.io.Serializable;

public class Feedback implements Serializable {
    private String orderId;
    private String customerEmail;
    private int rating;
    private String comment;

    public Feedback(String orderId, String customerEmail, int rating, String comment) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.rating = rating;
        this.comment = comment;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "orderId='" + orderId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}

