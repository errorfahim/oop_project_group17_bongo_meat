package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import com.group17.oop_project_group17_bongo_meat.User;
import com.group17.oop_project_group17_bongo_meat.shaika.Customer.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class contactcustomerController {

    // FXML COMPONENTS
    @FXML
    private Label outputLabel;

    @FXML
    private TableView<DeliveryAssignment> contactcustomertable;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliveryidcol;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliverytimecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> orderidcol;

    @FXML
    private TextField deliveryidTF;

    // File paths
    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String ORDER_FILE = "orders.dat";
    private static final String USER_FILE = "users.bin";

    private ArrayList<DeliveryAssignment> assignmentList = new ArrayList<>();

    // ---------------------------------------------------------
    // INITIALIZE: LOAD ACCEPTED DELIVERIES
    // ---------------------------------------------------------
    @FXML
    public void initialize() {

        // Set Table Columns
        deliveryidcol.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        orderidcol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        deliverytimecol.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));

        loadAcceptedDeliveriesIntoTable();
    }

    // ---------------------------------------------------------
    // LOAD ALL ACCEPTED DELIVERIES
    // ---------------------------------------------------------
    private void loadAcceptedDeliveriesIntoTable() {

        assignmentList = loadDeliveryAssignments();

        ObservableList<DeliveryAssignment> acceptedList = FXCollections.observableArrayList();

        for (DeliveryAssignment da : assignmentList) {
            if (da.getStatus().equalsIgnoreCase("Accepted") ||
                    da.getStatus().equalsIgnoreCase("Out for Delivery")) {
                acceptedList.add(da);
            }
        }

        contactcustomertable.setItems(acceptedList);

        if (acceptedList.isEmpty()) {
            outputLabel.setText("No accepted deliveries found!");
        } else {
            outputLabel.setText("Accepted deliveries loaded.");
        }
    }

    // ---------------------------------------------------------
    // BUTTON: SHOW CUSTOMER INFORMATION
    // ---------------------------------------------------------
    @FXML
    public void showinfoOA(ActionEvent actionEvent) {

        String enteredDeliveryId = deliveryidTF.getText().trim();

        if (enteredDeliveryId.isEmpty()) {
            outputLabel.setText("Please enter a Delivery ID!");
            return;
        }

        assignmentList = loadDeliveryAssignments();

        DeliveryAssignment selected = null;

        for (DeliveryAssignment da : assignmentList) {
            if (da.getDeliveryId().equalsIgnoreCase(enteredDeliveryId)) {
                selected = da;
                break;
            }
        }

        if (selected == null) {
            outputLabel.setText("No delivery found with ID: " + enteredDeliveryId);
            return;
        }

        // Step 1: Find order
        Order order = findOrderById(selected.getOrderId());
        if (order == null) {
            outputLabel.setText("Order not found for this Delivery!");
            return;
        }

        // Step 2: Find customer from users.bin
        User customer = findUserByEmail(order.getCustomerEmail());
        if (customer == null) {
            outputLabel.setText("Customer record not found!");
            return;
        }

        // Step 3: Show customer details
        outputLabel.setText(
                "Customer Details:\n\n" +
                        "Name: " + customer.getName() + "\n" +
                        "Phone: " + customer.getPhone() + "\n" +
                        "Address: " + order.getDeliveryAddress()
        );
    }

    // ---------------------------------------------------------
    // FIND ORDER BY ID
    // ---------------------------------------------------------
    private Order findOrderById(String orderId) {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {

            ArrayList<Order> orders = (ArrayList<Order>) ois.readObject();

            for (Order o : orders) {
                if (o.getOrderId().equals(orderId)) {
                    return o;
                }
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    // ---------------------------------------------------------
    // FIND USER BY EMAIL
    // ---------------------------------------------------------
    private User findUserByEmail(String email) {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {

            ArrayList<User> users = (ArrayList<User>) ois.readObject();

            for (User u : users) {
                if (u.getEmail().equals(email)) {
                    return u;
                }
            }

        } catch (Exception e) {
            return null;
        }

        return null;
    }

    // ---------------------------------------------------------
    // READ DELIVERYASSIGNMENTS
    // ---------------------------------------------------------
    private ArrayList<DeliveryAssignment> loadDeliveryAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELIVERY_FILE))) {
            return (ArrayList<DeliveryAssignment>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ---------------------------------------------------------
    // BACK BUTTON
    // ---------------------------------------------------------
    @FXML
    public void backOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/DeliverystaffDashboard.fxml", event);
    }

    // ---------------------------------------------------------
    // LOGOUT BUTTON
    // ---------------------------------------------------------
    @FXML
    public void LogoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }
}
