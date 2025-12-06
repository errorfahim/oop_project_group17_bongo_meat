package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ColdChainMonitorViewController {

    // ---------- FXML NODES ----------

    @FXML private TableView<ColdChainShipmentRecord> shipmentsListTableView;
    @FXML private TableColumn<ColdChainShipmentRecord, String> vehicleIDCol;
    @FXML private TableColumn<ColdChainShipmentRecord, String> shipmentIDCol;
    @FXML private TableColumn<ColdChainShipmentRecord, String> meatTypeCol1;
    @FXML private TableColumn<ColdChainShipmentRecord, Double> volumnCol;
    @FXML private TableColumn<ColdChainShipmentRecord, String> vehicleTypeCol;
    @FXML private TableColumn<ColdChainShipmentRecord, String> timestampCol1;
    @FXML private TableColumn<ColdChainShipmentRecord, Double> temperatureReadCol;
    @FXML private TableColumn<ColdChainShipmentRecord, String> statusCol;

    @FXML private TableView<ColdChainSystemConfig> systemConfigListTableView;
    @FXML private TableColumn<ColdChainSystemConfig, String> meatTypeCol;
    @FXML private TableColumn<ColdChainSystemConfig, Double> minSafeTempCol;
    @FXML private TableColumn<ColdChainSystemConfig, Double> maxSafeTempCol;
    @FXML private TableColumn<ColdChainSystemConfig, Double> humadityCol;
    @FXML private TableColumn<ColdChainSystemConfig, String> timestampCol2;

    @FXML private TextField temperatureTextField;
    @FXML private TextField humadityTextField;
    @FXML private DatePicker timestampDatePicker;

    @FXML private Label selectedVehicleIDLabel;
    @FXML private Label systemConfigSelectedLabel;
    @FXML private Label entryShowingSuccessMessageLabel;
    @FXML private Label statusUpdateMessageLabel;
    @FXML private Label reportGenerateMessageLabel;

    // ---------- DATA STRUCTURES ----------

    private final ObservableList<ColdChainShipmentRecord> shipmentLogs =
            FXCollections.observableArrayList();

    private final ObservableList<ColdChainSystemConfig> systemConfigs =
            FXCollections.observableArrayList();

    private ColdChainShipmentRecord selectedShipment;
    private ColdChainSystemConfig selectedConfig;

    // binary files
    private static final String SHIPMENTS_FILE = "coldChainShipmentLogs.dat";
    private static final String CONFIG_FILE = "coldChainSystemConfig.dat";
    private static final String ALERTS_FILE = "coldChainAlerts.dat";
    private static final String REPORT_QUEUE_FILE = "coldChainReportQueue.dat";

    @FXML
    public void initialize() {

        // --- Shipments table bindings ---
        vehicleIDCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        shipmentIDCol.setCellValueFactory(new PropertyValueFactory<>("shipmentId"));
        meatTypeCol1.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        volumnCol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        vehicleTypeCol.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        timestampCol1.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        temperatureReadCol.setCellValueFactory(new PropertyValueFactory<>("temperatureReading"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        shipmentsListTableView.setItems(shipmentLogs);

        // row colouring for OUT_OF_RANGE
        shipmentsListTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ColdChainShipmentRecord item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if ("OUT_OF_RANGE".equalsIgnoreCase(item.getStatus())) {
                    setStyle("-fx-background-color: #ffcccc;"); // light red
                } else {
                    setStyle("");
                }
            }
        });

        // --- System config table bindings ---
        meatTypeCol.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        minSafeTempCol.setCellValueFactory(new PropertyValueFactory<>("minSafeTemp"));
        maxSafeTempCol.setCellValueFactory(new PropertyValueFactory<>("maxSafeTemp"));
        humadityCol.setCellValueFactory(new PropertyValueFactory<>("humidity"));
        timestampCol2.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        systemConfigListTableView.setItems(systemConfigs);

        // --- Labels default text ---
        selectedVehicleIDLabel.setText("*Selected Vehicle ID *");
        systemConfigSelectedLabel.setText("*Choosen from the Table View *");
        entryShowingSuccessMessageLabel.setText("*Success/Error messages*");
        statusUpdateMessageLabel.setText("*Status (OK/OUT_OF_RANGE)*");
        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");

        // --- Selections ---
        shipmentsListTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedShipment = newSel;
                    if (newSel != null) {
                        selectedVehicleIDLabel.setText(
                                "Selected Vehicle: " + newSel.getVehicleId() +
                                        " | Shipment: " + newSel.getShipmentId() +
                                        " | Meat: " + newSel.getMeatType() +
                                        " | Volume: " + newSel.getVolume()
                        );
                    } else {
                        selectedVehicleIDLabel.setText("*Selected Vehicle ID *");
                    }
                });

        systemConfigListTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedConfig = newSel;
                    if (newSel != null) {
                        systemConfigSelectedLabel.setText(
                                "Selected Config -> Meat: " + newSel.getMeatType() +
                                        " | Min: " + newSel.getMinSafeTemp() +
                                        " | Max: " + newSel.getMaxSafeTemp()
                        );
                    } else {
                        systemConfigSelectedLabel.setText("*Choosen from the Table View *");
                    }
                });

        // start empty – actual data loaded on button click
        shipmentLogs.clear();
        systemConfigs.clear();
    }

    // =====================================================================
    // LOAD SHIPMENTS (from schedule / DB or dummy)
    // =====================================================================

    @FXML
    public void loadVehicleAssignedfromTransportScheduleButtonOnAction(ActionEvent actionEvent) {

        List<ColdChainShipmentRecord> list = loadShipmentsFromFile();

        if (list.isEmpty()) {
            // create dummy scheduled shipments with no readings yet
            list = new ArrayList<>();
            list.add(new ColdChainShipmentRecord(
                    "V-101", "SHP-001", "Beef", 500.0, "Refrigerated Truck",
                    "Not Recorded", null, "PENDING"
            ));
            list.add(new ColdChainShipmentRecord(
                    "V-102", "SHP-002", "Chicken", 300.0, "Refrigerated Van",
                    "Not Recorded", null, "PENDING"
            ));
            list.add(new ColdChainShipmentRecord(
                    "V-103", "SHP-003", "Mutton", 200.0, "Refrigerated Truck",
                    "Not Recorded", null, "PENDING"
            ));
            saveShipmentsToFile(list);
        }

        shipmentLogs.setAll(list);
        shipmentsListTableView.refresh();

        entryShowingSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        entryShowingSuccessMessageLabel.setText("Shipment vehicles loaded from schedule.");
    }

    // =====================================================================
    // LOAD SYSTEM CONFIG (ADMIN)
    // =====================================================================

    @FXML
    public void loadSystemConfigurationfromAdminButtonOnAction(ActionEvent actionEvent) {

        List<ColdChainSystemConfig> list = loadConfigsFromFile();

        if (list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new ColdChainSystemConfig("Beef", -1.0, 4.0, 85.0, LocalDate.now().toString()));
            list.add(new ColdChainSystemConfig("Chicken", -1.0, 3.0, 80.0, LocalDate.now().toString()));
            list.add(new ColdChainSystemConfig("Mutton", -1.0, 4.0, 85.0, LocalDate.now().toString()));
            saveConfigsToFile(list);
        }

        systemConfigs.setAll(list);
        systemConfigListTableView.refresh();

        systemConfigSelectedLabel.setStyle("-fx-text-fill: green;");
        systemConfigSelectedLabel.setText("System configuration loaded from Admin.");
    }

    // =====================================================================
    // VALIDATE ENTRY FORM
    // =====================================================================

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateEntryForm();
    }

    private boolean validateEntryForm() {
        StringBuilder errors = new StringBuilder();

        if (selectedShipment == null) {
            errors.append("Select a vehicle/shipment row from the top table.\n");
        }

        if (selectedConfig == null) {
            errors.append("Select a system configuration row from the bottom table.\n");
        }

        String tempText = temperatureTextField.getText().trim();
        if (tempText.isEmpty()) {
            errors.append("Temperature cannot be empty.\n");
        } else {
            try {
                Double.parseDouble(tempText);
            } catch (NumberFormatException e) {
                errors.append("Temperature must be a numeric value.\n");
            }
        }

        String humText = humadityTextField.getText().trim();
        if (humText.isEmpty()) {
            errors.append("Humidity cannot be empty.\n");
        } else {
            try {
                Double.parseDouble(humText);
            } catch (NumberFormatException e) {
                errors.append("Humidity must be a numeric value.\n");
            }
        }

        if (timestampDatePicker.getValue() == null) {
            errors.append("Timestamp (date) must be selected.\n");
        }

        if (errors.length() > 0) {
            entryShowingSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            entryShowingSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            entryShowingSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            entryShowingSuccessMessageLabel.setText("Entry is valid. You can now click 'Entry to Record'.");
            return true;
        }
    }

    // =====================================================================
    // ENTRY TO RECORD (UPLOAD LOG)
    // =====================================================================

    @FXML
    public void entryRecordButtonOnAction(ActionEvent actionEvent) {
        if (!validateEntryForm()) {
            return;
        }

        double temperature = Double.parseDouble(temperatureTextField.getText().trim());
        double humidity = Double.parseDouble(humadityTextField.getText().trim());
        String timestamp = timestampDatePicker.getValue().toString();

        // find config for this meat type (by name)
        ColdChainSystemConfig config = findConfigForMeat(selectedShipment.getMeatType());
        if (config == null) {
            config = selectedConfig; // fallback
        }

        String status = computeStatus(temperature, config);

        ColdChainShipmentRecord newLog = new ColdChainShipmentRecord(
                selectedShipment.getVehicleId(),
                selectedShipment.getShipmentId(),
                selectedShipment.getMeatType(),
                selectedShipment.getVolume(),
                selectedShipment.getVehicleType(),
                timestamp,
                temperature,
                status
        );
        newLog.setHumidity(humidity);

        shipmentLogs.add(newLog);
        shipmentsListTableView.refresh();

        // persist all logs
        saveShipmentsToFile(new ArrayList<>(shipmentLogs));

        statusUpdateMessageLabel.setText("Status: " + status);
        if ("OUT_OF_RANGE".equalsIgnoreCase(status)) {
            statusUpdateMessageLabel.setStyle("-fx-text-fill: red;");

            // store alert & popup
            storeAlert(newLog);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Temperature Out of Range");
            alert.setHeaderText("Cold Chain Alert");
            alert.setContentText("Temperature out of range for vehicle "
                    + newLog.getVehicleId() + " at " + newLog.getTimestamp()
                    + " (Reading: " + newLog.getTemperatureReading() + " °C)");
            alert.show();
        } else {
            statusUpdateMessageLabel.setStyle("-fx-text-fill: green;");
        }

        entryShowingSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        entryShowingSuccessMessageLabel.setText("Log entry added and status recalculated.");
    }

    private ColdChainSystemConfig findConfigForMeat(String meatType) {
        for (ColdChainSystemConfig cfg : systemConfigs) {
            if (cfg.getMeatType().equalsIgnoreCase(meatType)) {
                return cfg;
            }
        }
        return null;
    }

    private String computeStatus(double temperature, ColdChainSystemConfig config) {
        if (config == null) return "UNKNOWN_CONFIG";
        if (temperature < config.getMinSafeTemp() || temperature > config.getMaxSafeTemp()) {
            return "OUT_OF_RANGE";
        }
        return "OK";
    }

    // =====================================================================
    // LOAD UPDATED SHIPMENTS (REFRESH FROM FILE)
    // =====================================================================

    @FXML
    public void loadUpdatedShipmentsButtonOnAction(ActionEvent actionEvent) {
        List<ColdChainShipmentRecord> list = loadShipmentsFromFile();
        shipmentLogs.setAll(list);
        shipmentsListTableView.refresh();

        entryShowingSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        entryShowingSuccessMessageLabel.setText("Updated shipments/logs loaded from file.");
    }

    // =====================================================================
    // GENERATE REPORT (PDF)
    // =====================================================================

    @FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
        if (shipmentLogs.isEmpty()) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("No logs to report. Load and add entries first.");
            return;
        }

        if (selectedShipment == null) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Select a vehicle/shipment to generate report.");
            return;
        }

        // filter logs for the selected vehicle/shipment
        List<ColdChainShipmentRecord> subset = new ArrayList<>();
        for (ColdChainShipmentRecord r : shipmentLogs) {
            if (r.getVehicleId().equals(selectedShipment.getVehicleId()) &&
                    r.getShipmentId().equals(selectedShipment.getShipmentId())) {
                subset.add(r);
            }
        }

        if (subset.isEmpty()) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("No logs found for selected vehicle/shipment.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Cold Chain Temperature Report");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = chooser.showSaveDialog(null);
        if (file == null) return;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Cold Chain Temperature Report"));
            document.add(new Paragraph("----------------------------------------"));
            document.add(new Paragraph("Vehicle ID : " + selectedShipment.getVehicleId()));
            document.add(new Paragraph("Shipment ID: " + selectedShipment.getShipmentId()));
            document.add(new Paragraph("Meat Type  : " + selectedShipment.getMeatType()));
            document.add(new Paragraph("Vehicle    : " + selectedShipment.getVehicleType()));
            document.add(new Paragraph("Total Logs : " + subset.size()));
            document.add(new Paragraph(" "));

            int outOfRangeCount = 0;
            for (ColdChainShipmentRecord r : subset) {
                if ("OUT_OF_RANGE".equalsIgnoreCase(r.getStatus())) {
                    outOfRangeCount++;
                }
            }
            document.add(new Paragraph("OUT_OF_RANGE Entries: " + outOfRangeCount));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Logs:"));
            for (ColdChainShipmentRecord r : subset) {
                document.add(new Paragraph(
                        r.getTimestamp() +
                                " | Temp: " + r.getTemperatureReading() + "°C" +
                                " | Status: " + r.getStatus()
                ));
            }

            document.close();

            reportGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            reportGenerateMessageLabel.setText("Report Generated Successfully!");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Error generating report.");
        }
    }

    // =====================================================================
    // SUBMIT REPORT (STORE TO QUEUE)
    // =====================================================================

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {

        if (selectedShipment == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Select a vehicle/shipment before submitting report.").show();
            return;
        }

        // gather logs for selected vehicle/shipment
        List<ColdChainShipmentRecord> subset = new ArrayList<>();
        for (ColdChainShipmentRecord r : shipmentLogs) {
            if (r.getVehicleId().equals(selectedShipment.getVehicleId()) &&
                    r.getShipmentId().equals(selectedShipment.getShipmentId())) {
                subset.add(r);
            }
        }

        if (subset.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "No logs for selected vehicle/shipment to submit.").show();
            return;
        }

        // load existing queue
        ArrayList<ColdChainShipmentRecord> queue = new ArrayList<>();
        File file = new File(REPORT_QUEUE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                //noinspection unchecked
                queue = (ArrayList<ColdChainShipmentRecord>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        queue.addAll(subset);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(queue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cold Chain Report Submitted");
        alert.setHeaderText("Report added to Meat Quality / Alert queue.");
        alert.setContentText("Temperature report for Vehicle "
                + selectedShipment.getVehicleId()
                + " / Shipment " + selectedShipment.getShipmentId()
                + " has been submitted.");
        alert.show();
    }

    // =====================================================================
    // RETURN BUTTON
    // =====================================================================

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) {
        try {
            // adjust FXML path if your logistics dashboard has a different name
            switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/logisticManagerDashboard.fxml",
                    actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    // FILE HELPERS
    // =====================================================================

    private List<ColdChainShipmentRecord> loadShipmentsFromFile() {
        List<ColdChainShipmentRecord> list = new ArrayList<>();
        File file = new File(SHIPMENTS_FILE);
        if (!file.exists()) return list;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //noinspection unchecked
            list = (List<ColdChainShipmentRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void saveShipmentsToFile(List<ColdChainShipmentRecord> list) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(SHIPMENTS_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ColdChainSystemConfig> loadConfigsFromFile() {
        List<ColdChainSystemConfig> list = new ArrayList<>();
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return list;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //noinspection unchecked
            list = (List<ColdChainSystemConfig>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void saveConfigsToFile(List<ColdChainSystemConfig> list) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeAlert(ColdChainShipmentRecord record) {
        ArrayList<ColdChainShipmentRecord> alerts = new ArrayList<>();
        File file = new File(ALERTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                //noinspection unchecked
                alerts = (ArrayList<ColdChainShipmentRecord>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        alerts.add(record);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(alerts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
