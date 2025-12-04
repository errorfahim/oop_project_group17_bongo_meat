package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import com.group17.oop_project_group17_bongo_meat.shaika.Customer.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class confirmdeliveryController {

    @FXML
    private TableView<DeliveryAssignment> confirmdeliverytable;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliveryidcol;

    @FXML
    private TableColumn<DeliveryAssignment, String> orderidcol;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliverytimecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliveryadresscol;

    @FXML
    private TextField deliveryidTF;

    @FXML
    private Label outputLabel;

    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String ORDER_FILE = "orders.dat";

    private ArrayList<DeliveryAssignment> assignments = new ArrayList<>();

    // ---------------------------------------------------------
    // INITIALIZE
    // ---------------------------------------------------------
    @FXML
    public void initialize() {

        deliveryidcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deliveryId"));
        orderidcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("orderId"));
        deliverytimecol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deliveryTime"));
        deliveryadresscol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("deliveryAddress"));

        loadOutForDeliveryAssignments();
    }

    // ---------------------------------------------------------
    // LOAD ASSIGNMENTS WITH STATUS = OUT FOR DELIVERY
    // ---------------------------------------------------------
    private void loadOutForDeliveryAssignments() {
        assignments = loadDeliveryAssignments();

        ObservableList<DeliveryAssignment> list = FXCollections.observableArrayList();
        for (DeliveryAssignment da : assignments) {
            if ("Out for Delivery".equalsIgnoreCase(da.getStatus())) {
                list.add(da);
            }
        }

        confirmdeliverytable.setItems(list);
    }

    // ---------------------------------------------------------
    // CONFIRM DELIVERY BUTTON
    // ---------------------------------------------------------
    @FXML
    private void confirmdeliveryOA(ActionEvent event) {

        String enteredId = deliveryidTF.getText().trim();

        if (enteredId.isEmpty()) {
            outputLabel.setText("❌ Please enter a Delivery ID!");
            return;
        }

        DeliveryAssignment selected = null;

        for (DeliveryAssignment da : assignments) {
            if (da.getDeliveryId().equalsIgnoreCase(enteredId)) {

                // VALIDATION
                if (!"Out for Delivery".equalsIgnoreCase(da.getStatus())) {
                    outputLabel.setText("❌ Cannot confirm. Delivery is not 'Out for Delivery'.");
                    return;
                }

                selected = da;
                break;
            }
        }

        if (selected == null) {
            outputLabel.setText("❌ No matching delivery found!");
            return;
        }

        // UPDATE STATUS
        selected.setStatus("Delivered");
        saveDeliveryAssignments(assignments);

        // UPDATE CUSTOMER ORDER
        updateOrderStatus(selected.getOrderId(), "Delivered");

        outputLabel.setText("✅ Delivery Confirmed Successfully!");

        deliveryidTF.clear();
        loadOutForDeliveryAssignments();  // refresh table
    }

    // ---------------------------------------------------------
    // FILE HANDLING: READ & WRITE DELIVERY FILE
    // ---------------------------------------------------------
    private ArrayList<DeliveryAssignment> loadDeliveryAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELIVERY_FILE))) {
            return (ArrayList<DeliveryAssignment>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void saveDeliveryAssignments(ArrayList<DeliveryAssignment> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DELIVERY_FILE))) {
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // UPDATE ORDER STATUS IN orders.dat
    // ---------------------------------------------------------
    private void updateOrderStatus(String orderId, String newStatus) {

        ArrayList<Order> orders;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            orders = (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error reading orders.dat");
            return;
        }

        for (Order o : orders) {
            if (o.getOrderId().equals(orderId)) {
                o.setStatus(newStatus);
                break;
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // BACK + LOGOUT
    // ---------------------------------------------------------
    @FXML
    public void backOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/DeliverystaffDashboard.fxml", event);
    }

    @FXML
    public void logoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }
}
