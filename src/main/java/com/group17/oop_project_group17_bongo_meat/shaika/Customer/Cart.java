package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.CartItem;
import java.util.ArrayList;

public class Cart {
    private static ArrayList<CartItem> cartItems = new ArrayList<>();
    private static String customerEmail;
    // Set current logged-in customer
    public static void setCustomerEmail(String email) {
        customerEmail = email;
        clearCart(); // clear previous customer's cart when new customer logs in
    }

    // Get logged-in customer email
    public static String getCustomerEmail() {
        return customerEmail;
    }

    // Add item to cart
    public static void addItem(CartItem item) {
        cartItems.add(item);
    }

    // Remove an item from cart
    public static void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    // Get all items in cart
    public static ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    // Clear the cart
    public static void clearCart() {
        cartItems.clear();
    }
}
