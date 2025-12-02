package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class TransportScheduleFormController {
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

    private final String DELIVERY_FILE = "deliveryAssignments.dat";

    @javafx.fxml.FXML
    public void initialize() {

        batchIDCol.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        meatTypeCol.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        volumnCol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        requiredTemperatureCol.setCellValueFactory(new PropertyValueFactory<>("requiredTemperature"));
        deliveryPriorityCol.setCellValueFactory(new PropertyValueFactory<>("deliveryPriority"));

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
    }

    // CSV loading unchanged
    @javafx.fxml.FXML
    public void loadShipmentButtonOnAction(ActionEvent actionEvent) {
        String filePath = "shipments.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            shipmentList.clear();
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

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

    @javafx.fxml.FXML
    public void assignVehicleButtonOnAction(ActionEvent actionEvent) {
        ShipmentSchedule selected = pendingShipmentsTableView.getSelectionModel().getSelectedItem();


        }
    }
