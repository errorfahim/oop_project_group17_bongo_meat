package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import com.group17.oop_project_group17_bongo_meat.shaika.Customer.Order;
import com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff.DeliveryAssignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class TransportScheduleFormController {

    @FXML
    private ComboBox<Integer> vehicleIDComboBox;

    @FXML
    private ComboBox<String> orderIdComboBox;

    @FXML
    private ComboBox<String> vehicleTypeComboBox;

    @FXML
    private ComboBox<Integer> driverIDComboBox;

    @FXML
    private TableView<ShipmentSchedule> pendingShipmentsTableView;

    @FXML
    private TableColumn<ShipmentSchedule, Integer> batchIDCol;

    @FXML
    private TableColumn<ShipmentSchedule, String> destinationCol;

    @FXML
    private TableColumn<ShipmentSchedule, String> meatTypeCol;

    @FXML
    private TableColumn<ShipmentSchedule, Double> volumnCol;

    @FXML
    private TableColumn<ShipmentSchedule, Double> requiredTemperatureCol;

    @FXML
    private TableColumn<ShipmentSchedule, String> deliveryPriorityCol;

    @FXML
    private Label selectedBatchLabel;

    @FXML
    private Label saveMessageLabel;

    @FXML
    private Label validationMessageLabel;

    @FXML
    private Label saveAndGenerateSuccessfulMessageLabel;

    @FXML
    private Label deliveryIDLabel;

    @FXML
    private TextField batchIDTextField;

    @FXML
    private TextField shipmentIDTextField;

    @FXML
    private TextField arrivalTimeTextField;

    @FXML
    private DatePicker departureTimeDatePicker;

    @FXML
    private TextArea routeTextField;

    @FXML
    private VBox assignVehicleVBox;

    private ObservableList<ShipmentSchedule> shipmentList = FXCollections.observableArrayList();

    private final String DELIVERY_FILE = "deliveryAssignments.dat";

    // ==========================================================
    // INITIALIZE
    // ==========================================================
    @FXML
    public void initialize() {

        batchIDCol.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        meatTypeCol.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        volumnCol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        requiredTemperatureCol.setCellValueFactory(new PropertyValueFactory<>("requiredTemperature"));
        deliveryPriorityCol.setCellValueFactory(new PropertyValueFactory<>("deliveryPriority"));

        // Load Order IDs into ComboBox
        ArrayList<Order> orders = loadOrders();
        for (Order o : orders) {
            orderIdComboBox.getItems().add(o.getOrderId());
        }

        vehicleTypeComboBox.setItems(FXCollections.observableArrayList("Refrigerated Van", "Truck", "Bike", "Car"));
        vehicleIDComboBox.setItems(FXCollections.observableArrayList(101958, 102464, 103346));
        driverIDComboBox.setItems(FXCollections.observableArrayList(201542, 202564, 203654));

        assignVehicleVBox.setVisible(false);

        pendingShipmentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedBatchLabel.setText("Selected Batch: " + newSel.getBatchID());
                batchIDTextField.setText(String.valueOf(newSel.getBatchID()));
            }
        });

        // Load dummy shipments initially
        loadDummyShipments();
    }

    // ----------------- Dummy Data Method ------------------
    private void loadDummyShipments() {
        shipmentList.clear();

        shipmentList.add(new ShipmentSchedule(101, "Dhaka", "Beef", 500.0, -2.0, "High"));
        shipmentList.add(new ShipmentSchedule(102, "Chittagong", "Chicken", 300.0, 0.0, "Medium"));
        shipmentList.add(new ShipmentSchedule(103, "Khulna", "Mutton", 200.0, -1.0, "Low"));
        shipmentList.add(new ShipmentSchedule(104, "Sylhet", "Fish", 400.0, 2.0, "High"));
        shipmentList.add(new ShipmentSchedule(105, "Rajshahi", "Beef", 350.0, -3.0, "Medium"));

        pendingShipmentsTableView.setItems(shipmentList);
        saveMessageLabel.setText("Dummy shipments loaded successfully!");
    }

    // ==========================================================
    // LOAD SHIPMENTS FROM CSV
    // ==========================================================
    @FXML
    public void loadShipmentButtonOnAction(ActionEvent actionEvent) {
        String filePath = "shipments.csv";
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
        }
    }

    // ==========================================================
    // ASSIGN VEHICLE
    // ==========================================================
    @FXML
    public void assignVehicleButtonOnAction(ActionEvent actionEvent) {
        ShipmentSchedule selected = pendingShipmentsTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            validationMessageLabel.setText("Please select a shipment first!");
            return;
        }

        assignVehicleVBox.setVisible(true);
    }

    // ==========================================================
    // SAVE TRANSPORT PLAN
    // ==========================================================
    @FXML
    public void saveTransportPlanButtonOnAction(ActionEvent actionEvent) {

        if (batchIDTextField.getText().isEmpty() || shipmentIDTextField.getText().isEmpty() ||
                vehicleIDComboBox.getValue() == null || driverIDComboBox.getValue() == null ||
                vehicleTypeComboBox.getValue() == null || routeTextField.getText().isEmpty() ||
                departureTimeDatePicker.getValue() == null || arrivalTimeTextField.getText().isEmpty()) {

            validationMessageLabel.setText("Please fill all fields!");
            return;
        }

        if (orderIdComboBox.getValue() == null) {
            validationMessageLabel.setText("Please select an Order ID!");
            return;
        }

        try {
            int shipmentID = Integer.parseInt(shipmentIDTextField.getText());
            int batchID = Integer.parseInt(batchIDTextField.getText());

            String deliveryID = generateDeliveryID();
            deliveryIDLabel.setText("Delivery ID: " + deliveryID);

            ShipmentSchedule selected = pendingShipmentsTableView.getSelectionModel().getSelectedItem();

            if (selected == null) {
                validationMessageLabel.setText("Please select a shipment from the table!");
                return;
            }

            // Create DeliveryAssignment (base fields)
            DeliveryAssignment assignment = new DeliveryAssignment(
                    deliveryID,
                    String.valueOf(departureTimeDatePicker.getValue()),
                    vehicleTypeComboBox.getValue(),
                    selected.getDestination(),
                    "Assigned",
                    orderIdComboBox.getValue()   // actual customer orderId
            );

            // ----------- NEW: set vehicle details for Goal 5 ----------
            String vehicleIdStr = String.valueOf(vehicleIDComboBox.getValue());
            assignment.setVehicleId(vehicleIdStr);

            // Simple sample data (you can improve later)
            String licensePlate = "BG-" + vehicleIdStr;
            assignment.setLicensePlate(licensePlate);

            // Use departure date as last service date placeholder
            String lastService = String.valueOf(departureTimeDatePicker.getValue());
            assignment.setLastServiceDate(lastService);

            // Placeholder fuel status
            assignment.setFuelStatus("Full Tank");

            // ---------------------------------------------------------

            ArrayList<DeliveryAssignment> list = loadDeliveryAssignments();
            list.add(assignment);
            saveDeliveryAssignments(list);

            saveAndGenerateSuccessfulMessageLabel.setText("Transport Plan Saved & Delivery ID Generated Successfully.");
            validationMessageLabel.setText("");

        } catch (NumberFormatException e) {
            validationMessageLabel.setText("Batch ID & Shipment ID must be integers!");
        }
    }

    // ==========================================================
    // SEND NOTIFICATION
    // ==========================================================
    @FXML
    public void notificationSendButtonOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText("Notification sent to delivery staff.");
        alert.showAndWait();
    }

    // ==========================================================
    // BINARY FILE HANDLING
    // ==========================================================
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

    private String generateDeliveryID() {
        Random random = new Random();
        return "DEL-" + (100000 + random.nextInt(900000));
    }

    private ArrayList<Order> loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("orders.dat"))) {
            return (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ==========================================================
    // BACK BUTTON
    // ==========================================================
    @FXML
    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/logisticManagerDashboard.fxml", actionEvent);
    }
}
