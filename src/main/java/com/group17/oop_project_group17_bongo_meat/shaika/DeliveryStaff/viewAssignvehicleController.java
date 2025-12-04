package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class viewAssignvehicleController {

    @FXML
    private TableView<DeliveryAssignment> vehicledetailstable;

    @FXML
    private TableColumn<DeliveryAssignment, String> vehicleidcol;

    @FXML
    private TableColumn<DeliveryAssignment, String> vehicletypecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> licenseplatecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> lastservicedatecol;

    @FXML
    private TableColumn<DeliveryAssignment, String> fuelstatuscol;

    @FXML
    private Label outputLabel;

    private static final String DELIVERY_FILE = "deliveryAssignments.dat";

    private ArrayList<DeliveryAssignment> list = new ArrayList<>();

    // ---------------------------------------------------------
    // INITIALIZE VIEW
    // ---------------------------------------------------------
    @FXML
    public void initialize() {

        // Bind columns to DeliveryAssignment fields
        vehicleidcol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicletypecol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        licenseplatecol.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        lastservicedatecol.setCellValueFactory(new PropertyValueFactory<>("lastServiceDate"));
        fuelstatuscol.setCellValueFactory(new PropertyValueFactory<>("fuelStatus"));

        loadAssignedVehicles();
    }

    // ---------------------------------------------------------
    // LOAD VEHICLES FOR THIS DELIVERY STAFF
    // ---------------------------------------------------------
    private void loadAssignedVehicles() {

        list = loadDeliveryAssignments();

        ObservableList<DeliveryAssignment> vehicleList = FXCollections.observableArrayList();

        for (DeliveryAssignment da : list) {

            // Show only Accepted or Out for Delivery or Delivered deliveries
            if (da.getStatus().equalsIgnoreCase("Accepted")
                    || da.getStatus().equalsIgnoreCase("Out for Delivery")
                    || da.getStatus().equalsIgnoreCase("Delivered")) {

                // Vehicle must match real assigned vehicle
                if (da.getVehicleId() != null && !da.getVehicleId().isEmpty()) {
                    vehicleList.add(da);
                }
            }
        }

        if (vehicleList.isEmpty()) {
            outputLabel.setText("No assigned vehicles found.");
        } else {
            outputLabel.setText("Vehicle data loaded successfully.");
        }

        vehicledetailstable.setItems(vehicleList);
    }

    // ---------------------------------------------------------
    // READ BINARY FILE
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
    public void logoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }
}
