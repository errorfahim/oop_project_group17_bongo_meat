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

public class collectorderfrompackageController {

    @FXML
    private TextField tempTF;
    @FXML
    private ComboBox<String> packagecondComboBox;
    @FXML
    private TextField deliveryidTF;
    @FXML
    private Label packageinfoLabel;
    @FXML
    private TextArea additinalnoteTA;
    @FXML
    private ListView<String> deliveryidlistview;

    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String ORDER_FILE = "orders.dat";

    private DeliveryAssignment selectedAssignment = null;

    // ---------------------------------------------------------
    // INITIALIZE
    // ---------------------------------------------------------
    @FXML
    public void initialize() {
        packagecondComboBox.setItems(FXCollections.observableArrayList("Good", "Damaged", "Leaking"));

        loadAcceptedDeliveries();
    }

    private void loadAcceptedDeliveries() {
        ArrayList<DeliveryAssignment> list = loadDeliveryAssignments();
        ObservableList<String> acceptedIds = FXCollections.observableArrayList();

        for (DeliveryAssignment da : list) {
            if (da.getStatus().equalsIgnoreCase("Accepted")) {
                acceptedIds.add(da.getDeliveryId());
            }
        }

        deliveryidlistview.setItems(acceptedIds);
    }

    // ---------------------------------------------------------
    // SHOW BUTTON → Display Package Info
    // ---------------------------------------------------------
    @FXML
    public void showOA(ActionEvent actionEvent) {
        String id = deliveryidTF.getText().trim();

        if (id.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter a Delivery ID!");
            return;
        }

        ArrayList<DeliveryAssignment> list = loadDeliveryAssignments();
        selectedAssignment = null;

        for (DeliveryAssignment da : list) {
            if (da.getDeliveryId().equalsIgnoreCase(id) && da.getStatus().equalsIgnoreCase("Accepted")) {
                selectedAssignment = da;
                break;
            }
        }

        if (selectedAssignment == null) {
            packageinfoLabel.setText("No Accepted Delivery Found For This ID!");
            return;
        }

        packageinfoLabel.setText(
                "Delivery Address: " + selectedAssignment.getDeliveryAddress() +
                        " | Vehicle: " + selectedAssignment.getVehicleType() +
                        " | Time: " + selectedAssignment.getDeliveryTime()
        );
    }

    // ---------------------------------------------------------
    // CONFIRM COLLECTION
    // ---------------------------------------------------------
    @FXML
    public void confirmcollectionOA(ActionEvent actionEvent) {

        if (selectedAssignment == null) {
            showAlert(Alert.AlertType.ERROR, "Please show delivery details first!");
            return;
        }

        if (packagecondComboBox.getValue() == null || tempTF.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all package details!");
            return;
        }

        String condition = packagecondComboBox.getValue();
        String temperature = tempTF.getText();
        String note = additinalnoteTA.getText();

        // Save package details in assignment
        selectedAssignment.setPackageCondition(condition);
        selectedAssignment.setTemperature(temperature);
        selectedAssignment.setNotes(note);

        // UPDATE assignment status to "Out for Delivery"
        updateAssignmentStatus(selectedAssignment.getDeliveryId(), "Out for Delivery");

        // UPDATE related customer order
        updateOrderStatus(selectedAssignment.getOrderId(), "Out for Delivery");

        showAlert(Alert.AlertType.INFORMATION, "Order Collected Successfully!");

        tempTF.clear();
        additinalnoteTA.clear();
        packagecondComboBox.getSelectionModel().clearSelection();

        loadAcceptedDeliveries();
    }

    // ---------------------------------------------------------
    // FILE HANDLING: DELIVERY ASSIGNMENTS
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

    private void updateAssignmentStatus(String deliveryId, String newStatus) {
        ArrayList<DeliveryAssignment> list = loadDeliveryAssignments();

        for (DeliveryAssignment da : list) {
            if (da.getDeliveryId().equals(deliveryId)) {
                da.setStatus(newStatus);
                break;
            }
        }

        saveDeliveryAssignments(list);
    }

    // ---------------------------------------------------------
    // UPDATE CUSTOMER ORDER STATUS
    // ---------------------------------------------------------
    private void updateOrderStatus(String orderId, String newStatus) {

        ArrayList<Order> orders;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            orders = (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            System.out.println("Error reading orders.dat: " + e.getMessage());
            return;
        }

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(newStatus);
                break;
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // BACK + LOGOUT
    // ---------------------------------------------------------
    @FXML
    public void backOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/DeliverystaffDashboard.fxml",
                actionEvent);
    }

    @FXML
    public void logoutOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }

    // ---------------------------------------------------------
    // ALERTS
    // ---------------------------------------------------------
    private void showAlert(Alert.AlertType type, String message) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
