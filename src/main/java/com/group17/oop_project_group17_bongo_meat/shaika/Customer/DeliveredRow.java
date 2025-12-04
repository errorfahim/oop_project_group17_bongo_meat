package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

public class DeliveredRow {
    private String orderId;
    private String deliveryId;
    private int qty;
    private double total;

    public DeliveredRow(String orderId, String deliveryId, int qty, double total) {
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.qty = qty;
        this.total = total;
    }

    public String getOrderId() { return orderId; }
    public String getDeliveryId() { return deliveryId; }
    public int getQty() { return qty; }
    public double getTotal() { return total; }
}


