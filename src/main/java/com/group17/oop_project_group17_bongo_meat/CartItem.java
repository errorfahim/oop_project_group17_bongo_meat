package com.group17.oop_project_group17_bongo_meat;

import java.io.Serializable;

public class CartItem implements Serializable {
    private ProductDetails product;
    private int quantity;

    public CartItem(ProductDetails product, int quantity) {
        this.product = product;
        this.quantity = quantity;

    }

    public ProductDetails getProduct() {
        return product;
    }

    public void setProduct(ProductDetails product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
