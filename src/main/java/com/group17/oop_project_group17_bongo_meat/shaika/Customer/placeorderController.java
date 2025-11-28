package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.CartItem;
import com.group17.oop_project_group17_bongo_meat.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class placeorderController {

    @FXML
    private TableView<CartItem> checkoutTable;
    @FXML
    private TableColumn<CartItem, String> productnamecol;
    @FXML
    private TableColumn<CartItem, Integer> qtycol;
    @FXML
    private TableColumn<CartItem, Double> pricecol;

    @FXML
    private Label checkoutdetailsLabel;
    @FXML
    private TextArea deliveryAddressTA;
    @FXML
    private ComboBox<String> paymentmethodCB;
    @FXML
    private Label outputLabel;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private double subtotal = 0.0;
    private final String ORDER_FILE = "orders.dat";
    @FXML
    private Label orderidTF;
    private String loggedInCustomer;


    // Initialize method runs when scene is loaded
    @FXML
    public void initialize() {
        // Table columns
        productnamecol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProduct().getName()
        ));
        qtycol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getQuantity()
        ).asObject());
        pricecol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getTotalPrice()
        ).asObject());

        // Load selected items from Cart
        cartItems.addAll(Cart.getCartItems());
        checkoutTable.setItems(cartItems);

        // Calculate subtotal
        calculateSubtotal();

        // Populate payment methods
        paymentmethodCB.getItems().addAll("Bkash", "COD", "Card");
        loggedInCustomer = Cart.getCustomerEmail();
    }

    private void calculateSubtotal() {
        subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }
        checkoutdetailsLabel.setText("Subtotal: " + subtotal + " BDT");
    }

    // Back button handler
    @FXML
    private void backOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", actionEvent);
    }

    // Logout button handler
    @FXML
    private void logoutOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);

    }

    // Place Order button handler
    @FXML
    private void placeorderOA(ActionEvent event) {
        String address = deliveryAddressTA.getText().trim();
        String payment = paymentmethodCB.getValue();

        // Validate input
        if (address.isEmpty() || payment == null) {
            outputLabel.setText("Please fill in all required fields!");
            return;
        }

        // Generate unique 5-digit order ID
        String orderId = generateOrderId();
        orderidTF.setText(orderId);

        // Create Order object
        Order order = new Order(orderId, new ArrayList<>(cartItems), subtotal, address, payment, "Processing",LoginController.loggedInEmail );
        // Save order to binary file
        saveOrder(order);

        outputLabel.setText("Order Placed Successfully! Order ID: " + orderId + "\nStatus: " + order.getStatus());

        // Clear input fields and optionally clear cart
        deliveryAddressTA.clear();
        paymentmethodCB.getSelectionModel().clearSelection();
        orderidTF.setText("");

        // Optional: clear cart after order
        Cart.clearCart();
    }

    // Cancel button handler
    @FXML
    private void cancelOA(ActionEvent event) {
        deliveryAddressTA.clear();
        paymentmethodCB.getSelectionModel().clearSelection();
        orderidTF.setText("");
        outputLabel.setText("");
    }

    // Generate 5-digit random order ID
    private String generateOrderId() {
        Random rand = new Random();
        int number = rand.nextInt(90000) + 10000; // 10000 to 99999
        return String.valueOf(number);
    }

    // Save Order object to binary file
    private void saveOrder(Order order) {
        ArrayList<Order> orders = new ArrayList<>();

        // Load existing orders
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            orders = (ArrayList<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File not found is fine, first order
        }

        orders.add(order);

        // Write back to file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            outputLabel.setText("Error saving order!");
            e.printStackTrace();
        }
    }
}
