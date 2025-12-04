package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

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

public class recievedeliveryassignmentController {

    @FXML
    private TableView<DeliveryAssignment> deliveryAssignmentTable;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliveryIdcol;

    @FXML
    private TableColumn<DeliveryAssignment, String> vehicletypecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliverytimecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> deliveryAddresscol;

    @FXML
    private TextField deliveryidTF;

    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String ORDER_FILE = "orders.dat";

    private ArrayList<DeliveryAssignment> assignmentList = new ArrayList<>();

    // ---------------------------------------------------------
    // INITIALIZE VIEW
    // ---------------------------------------------------------
    @FXML
    public void initialize() {

        deliveryIdcol.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        vehicletypecol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        deliverytimecol.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
        deliveryAddresscol.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));

        loadAssignmentsIntoTable();
    }

    // ---------------------------------------------------------
    // ACCEPT DELIVERY BUTTON
    // ---------------------------------------------------------
    @FXML
    private void acceptDeliveryOA(ActionEvent event) {

        String enteredId = deliveryidTF.getText().trim();

        if (enteredId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter a Delivery ID!");
            return;
        }

        assignmentList = loadDeliveryAssignments();

        DeliveryAssignment selectedAssignment = null;

        for (DeliveryAssignment da : assignmentList) {
            if (da.getDeliveryId().equalsIgnoreCase(enteredId)) {

                selectedAssignment = da;

                if (da.getStatus().equalsIgnoreCase("Accepted")) {
                    showAlert(Alert.AlertType.INFORMATION,
                            "This delivery has already been accepted.");
                    return;
                }

                da.setStatus("Accepted");
                break;
            }
        }

        if (selectedAssignment == null) {
            showAlert(Alert.AlertType.ERROR,
                    "No delivery found with ID: " + enteredId);
            return;
        }

        // Save updated delivery assignment list
        saveDeliveryAssignments(assignmentList);

        // Update matching customer order
        updateOrderStatus(selectedAssignment.getOrderId());

        showAlert(Alert.AlertType.INFORMATION,
                "Delivery Accepted Successfully!");

        deliveryidTF.clear();
        loadAssignmentsIntoTable();
    }

    // ---------------------------------------------------------
    // BACK BUTTON
    // ---------------------------------------------------------
    @FXML
    private void backOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/deliveryStaffDashboard.fxml", event);
    }

    // ---------------------------------------------------------
    // LOGOUT BUTTON
    // ---------------------------------------------------------
    @FXML
    private void logoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }

    // ---------------------------------------------------------
    // LOAD DELIVERIES FOR THE TABLE
    // ---------------------------------------------------------
    private void loadAssignmentsIntoTable() {
        assignmentList = loadDeliveryAssignments();

        ObservableList<DeliveryAssignment> assignedList = FXCollections.observableArrayList();

        for (DeliveryAssignment da : assignmentList) {
            if (da.getStatus().equalsIgnoreCase("Assigned")) {
                assignedList.add(da);
            }
        }

        deliveryAssignmentTable.setItems(assignedList);
    }

    // ---------------------------------------------------------
    // LOAD DELIVERY ASSIGNMENTS FROM FILE
    // ---------------------------------------------------------
    private ArrayList<DeliveryAssignment> loadDeliveryAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELIVERY_FILE))) {
            return (ArrayList<DeliveryAssignment>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ---------------------------------------------------------
    // SAVE DELIVERY ASSIGNMENTS BACK TO FILE
    // ---------------------------------------------------------
    private void saveDeliveryAssignments(ArrayList<DeliveryAssignment> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DELIVERY_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // UPDATE CUSTOMER ORDER STATUS
    // ---------------------------------------------------------
    private void updateOrderStatus(String orderId) {

        ArrayList<Order> orders;

        // Load from orders.dat
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            orders = (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error reading orders.dat: " + e.getMessage());
            return;
        }

        boolean updated = false;

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {

                order.setStatus("Accepted");
                updated = true;
                break;
            }
        }

        if (!updated) {
            System.out.println("No matching order found for ID: " + orderId);
            return;
        }

        // Save updated orders
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // ALERT HELPER
    // ---------------------------------------------------------
    private void showAlert(Alert.AlertType type, String message) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
