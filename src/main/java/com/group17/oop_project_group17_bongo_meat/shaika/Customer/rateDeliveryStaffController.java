package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.LoginController;
import com.group17.oop_project_group17_bongo_meat.shaika.Customer.Order;
import com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff.DeliveryAssignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class rateDeliveryStaffController {

    @FXML private TableView<DeliveredRow> ratedeliveryTable;
    @FXML private TableColumn<DeliveredRow, String> orderIDcol;
    @FXML private TableColumn<DeliveredRow, String> deliverystaffidcol;
    @FXML private TableColumn<DeliveredRow, Integer> qtyidcol;
    @FXML private TableColumn<DeliveredRow, Double> totalcol;

    @FXML private ComboBox<Integer> ratingsCB;
    @FXML private Label outputLabel;

    private static final String ORDERS_FILE = "orders.dat";
    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String RATING_FILE = "deliveryRatings.dat";

    private List<Order> allOrders;
    private List<DeliveryAssignment> allAssignments;

    @FXML
    public void initialize() {

        orderIDcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("orderId"));
        deliverystaffidcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deliveryId"));
        qtyidcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("qty"));
        totalcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("total"));

        ratingsCB.getItems().addAll(1, 2, 3, 4, 5);

        loadDeliveredOrders();
    }

    // ---------------- LOAD DELIVERED ORDERS ------------------

    private void loadDeliveredOrders() {
        allOrders = loadOrders();
        allAssignments = loadAssignments();

        ObservableList<DeliveredRow> rows = FXCollections.observableArrayList();

        String loggedEmail = LoginController.loggedInEmail;

        for (Order order : allOrders) {

            // NULL-SAFE CUSTOMER CHECK
            if (loggedEmail != null &&
                    order.getCustomerEmail() != null &&
                    order.getCustomerEmail().equals(loggedEmail) &&
                    "Delivered".equalsIgnoreCase(order.getStatus())) {

                // find delivery assignment
                DeliveryAssignment da = allAssignments.stream()
                        .filter(a -> a.getOrderId().equals(order.getOrderId()))
                        .findFirst()
                        .orElse(null);

                if (da != null) {
                    rows.add(new DeliveredRow(
                            order.getOrderId(),
                            da.getDeliveryId(),
                            order.getItems().size(),
                            order.getSubtotal()
                    ));
                }
            }
        }

        ratedeliveryTable.setItems(rows);
    }


    // ---------------- BUTTON: SUBMIT RATING ------------------

    @FXML
    public void submitOA(ActionEvent event) {

        DeliveredRow selected = ratedeliveryTable.getSelectionModel().getSelectedItem();
        Integer rating = ratingsCB.getValue();

        if (selected == null) {
            outputLabel.setText("Please select an order from the table.");
            return;
        }

        if (rating == null) {
            outputLabel.setText("Please select a rating value.");
            return;
        }

        saveRating(selected.getDeliveryId(), selected.getOrderId(), rating);

        outputLabel.setText("Rating submitted successfully!");
        ratingsCB.getSelectionModel().clearSelection();
        ratedeliveryTable.getSelectionModel().clearSelection();
    }

    // ---------------- SAVE RATING ------------------

    private void saveRating(String deliveryId, String orderId, int rating) {

        ArrayList<DeliveryRating> list = loadRatings();

        list.add(new DeliveryRating(deliveryId, orderId, rating));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RATING_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DeliveryRating> loadRatings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RATING_FILE))) {
            return (ArrayList<DeliveryRating>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    // ---------------- BINARY FILE LOADERS ------------------

    private ArrayList<Order> loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDERS_FILE))) {
            return (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<DeliveryAssignment> loadAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELIVERY_FILE))) {
            return (ArrayList<DeliveryAssignment>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ---------------- NAVIGATION ------------------

    @FXML
    public void backOA(ActionEvent event) {
        try {
            switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", event);
        } catch (IOException ignored) {}
    }

    @FXML
    public void logoutOA(ActionEvent event) {
        try {
            switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
        } catch (IOException ignored) {}
    }
}
