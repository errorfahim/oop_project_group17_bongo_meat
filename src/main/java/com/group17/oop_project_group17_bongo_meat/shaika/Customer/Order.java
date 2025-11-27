package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.CartItem;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private List<CartItem> items;
    private double subtotal;
    private String deliveryAddress;
    private String paymentMethod;
    private String status;
    private String customerEmail;
    public Order() {
    }

    public Order(String orderId, List<CartItem> items, double subtotal, String deliveryAddress, String paymentMethod, String status,String customerEmail ) {
        this.orderId = orderId;
        this.items = items;
        this.subtotal = subtotal;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", items=" + items +
                ", subtotal=" + subtotal +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
