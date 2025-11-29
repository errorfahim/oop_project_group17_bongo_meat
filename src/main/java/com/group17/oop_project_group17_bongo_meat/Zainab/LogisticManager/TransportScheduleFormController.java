package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class TransportScheduleFormController
{
    @javafx.fxml.FXML
    private ComboBox<Integer> vehicleIDComboBox;
    @javafx.fxml.FXML
    private ComboBox<String> vehicleTypeComboBox;
    @javafx.fxml.FXML
    private ComboBox<Integer> driverIDComboBox;
    @javafx.fxml.FXML
    private TableView<ShipmentSchedule> pendingShipmentsTableView;
    @javafx.fxml.FXML
    private TableColumn<ShipmentSchedule, Integer> batchIDCol;
    @javafx.fxml.FXML
    private TableColumn<ShipmentSchedule, String> destinationCol;
    @javafx.fxml.FXML
    private TableColumn<ShipmentSchedule, String> meatTypeCol;
    @javafx.fxml.FXML
    private TableColumn<ShipmentSchedule, Double> volumnCol;
    @javafx.fxml.FXML
    private TableColumn<ShipmentSchedule, Double> requiredTemperatureCol;
    @javafx.fxml.FXML
    private TableColumn<ShipmentSchedule, String> deliveryPriorityCol;
    @javafx.fxml.FXML
    private Label selectedBatchLabel;
    @javafx.fxml.FXML
    private Label saveMessageLabel;
    @javafx.fxml.FXML
    private Label validationMessageLabel;
    @javafx.fxml.FXML
    private Label saveAndGenerateSuccessfulMessageLabel;
    @javafx.fxml.FXML
    private Label deliveryIDLabel;

    @javafx.fxml.FXML
    private TextField batchIDTextField;
    @javafx.fxml.FXML
    private TextField shipmentIDTextField;
    @javafx.fxml.FXML
    private TextField arrivalTimeTextField;
    @javafx.fxml.FXML
    private DatePicker departureTimeDatePicker;
    @javafx.fxml.FXML
    private TextArea routeTextField;

    private ObservableList<ShipmentSchedule> shipmentList = FXCollections.observableArrayList();
    @javafx.fxml.FXML
    private VBox assignVehicleVBox;

    @javafx.fxml.FXML
    public void initialize() {
        // Table setup
        batchIDCol.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        meatTypeCol.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        volumnCol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        requiredTemperatureCol.setCellValueFactory(new PropertyValueFactory<>("requiredTemperature"));
        deliveryPriorityCol.setCellValueFactory(new PropertyValueFactory<>("deliveryPriority"));

        // ComboBoxes
        vehicleTypeComboBox.setItems(FXCollections.observableArrayList("Refrigerated Van", "Truck", "Bike", "Car"));
        vehicleIDComboBox.setItems(FXCollections.observableArrayList(101958, 102464, 103346));
        driverIDComboBox.setItems(FXCollections.observableArrayList(201542, 202564, 203654));

        // Hide assign vehicle form initially
        assignVehicleVBox.setVisible(false);

        // Table row selection listener
        pendingShipmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedBatchLabel.setText("Selected Batch: " + newSelection.getBatchID());
                batchIDTextField.setText(String.valueOf(newSelection.getBatchID()));
            }
        });
    }

    // Load shipment data from CSV file
    @javafx.fxml.FXML
    public void loadShipmentButtonOnAction(ActionEvent actionEvent) {
        String filePath = "shipments.csv"; // adjust path as needed
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            shipmentList.clear();
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] data = line.split(",");
                int batchID = Integer.parseInt(data[0].trim());
                String destination = data[1].trim();
                String meatType = data[2].trim();
                double volume = Double.parseDouble(data[3].trim());
                double requiredTemp = Double.parseDouble(data[4].trim());
                String priority = data[5].trim();
                shipmentList.add(new ShipmentSchedule(batchID, destination, meatType, volume, requiredTemp, priority));
            }
            pendingShipmentsTableView.setItems(shipmentList);
            saveMessageLabel.setText("Shipments loaded successfully!");
        } catch (IOException e) {
            validationMessageLabel.setText("Error loading shipment file!");
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void assignVehicleButtonOnAction(ActionEvent actionEvent) {
        ShipmentSchedule selected = pendingShipmentsTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            validationMessageLabel.setText("Please select a shipment from the table first!");
            return;
        }
        assignVehicleVBox.setVisible(true);
    }

    @javafx.fxml.FXML
    public void saveTransportPlanButtonOnAction(ActionEvent actionEvent) {
        if (batchIDTextField.getText().isEmpty() || shipmentIDTextField.getText().isEmpty() ||
                vehicleIDComboBox.getValue() == null || driverIDComboBox.getValue() == null ||
                vehicleTypeComboBox.getValue() == null || routeTextField.getText().isEmpty() ||
                departureTimeDatePicker.getValue() == null || arrivalTimeTextField.getText().isEmpty()) {

            validationMessageLabel.setText("Please fill all fields!");
            return;
        }

        try {
            int shipmentID = Integer.parseInt(shipmentIDTextField.getText());
            int batchID = Integer.parseInt(batchIDTextField.getText());

            String deliveryID = generateDeliveryID();

            deliveryIDLabel.setText("Delivery ID: " + deliveryID);
            saveAndGenerateSuccessfulMessageLabel.setText("Transport Plan Saved & Delivery ID Generated Successfully.");
            validationMessageLabel.setText("");
        } catch (NumberFormatException e) {
            validationMessageLabel.setText("Batch ID and Shipment ID must be integers!");
        }
    }

    @javafx.fxml.FXML
    public void notificationSendButtonOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText("Notification sent to delivery staff.");
        alert.showAndWait();
    }

    private String generateDeliveryID() {
        Random random = new Random();
        return "DEL-" + (100000 + random.nextInt(999999));
    }

    @javafx.fxml.FXML
    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/logisticManagerDashboard.fxml", actionEvent);
    }
}