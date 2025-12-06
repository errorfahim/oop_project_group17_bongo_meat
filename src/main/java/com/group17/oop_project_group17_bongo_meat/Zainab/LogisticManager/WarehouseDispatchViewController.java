package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

import org.openpdf.text.Document;
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
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class WarehouseDispatchViewController {

    // ---------- FXML NODES ----------

    @FXML private TableView<WarehouseDispatchBatch> pendingShipmentsTableView;
    @FXML private TableColumn<WarehouseDispatchBatch, String> batchIDCol;
    @FXML private TableColumn<WarehouseDispatchBatch, String> meatTypeCol;
    @FXML private TableColumn<WarehouseDispatchBatch, Integer> quantityCol;
    @FXML private TableColumn<WarehouseDispatchBatch, String> packagingStatusCol;
    @FXML private TableColumn<WarehouseDispatchBatch, String> qaStatusCol;

    @FXML private Label verificationSuccessLabel;
    @FXML private Label validationMessageLabel;
    @FXML private Label selectedBatchLabel;
    @FXML private Label saveMessageLabel;
    @FXML private Label saveAndGenerateSuccessfulMessageLabel;

    // ---------- DATA ----------

    private final ObservableList<WarehouseDispatchBatch> pendingBatches =
            FXCollections.observableArrayList();

    private final ArrayList<WarehouseDispatchBatch> savedPlans =
            new ArrayList<>();

    private WarehouseDispatchBatch selectedBatch;

    private boolean packagingVerified = false;
    private boolean labelsVerified = false;

    private static final String PENDING_BATCH_FILE = "warehousePendingBatches.dat";
    private static final String DISPATCH_PLAN_FILE = "warehouseDispatchPlans.dat";
    private static final String TRANSPORT_SCHEDULE_QUEUE_FILE = "transportScheduleQueue.dat";

    @FXML
    public void initialize() {

        batchIDCol.setCellValueFactory(new PropertyValueFactory<>("batchId"));
        meatTypeCol.setCellValueFactory(new PropertyValueFactory<>("meatType"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        packagingStatusCol.setCellValueFactory(new PropertyValueFactory<>("packagingStatus"));
        qaStatusCol.setCellValueFactory(new PropertyValueFactory<>("qaStatus"));

        pendingShipmentsTableView.setItems(pendingBatches);

        selectedBatchLabel.setText("Selected Batch");
        verificationSuccessLabel.setText("*Verification Success Label*");
        validationMessageLabel.setText("*Validation Successful Message*");
        saveMessageLabel.setText("*Save plan Success Message*");
        saveAndGenerateSuccessfulMessageLabel.setText("*Plan Saved and Report Generated Successfully.*");

        pendingShipmentsTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedBatch = newSel;
                    if (newSel != null) {
                        selectedBatchLabel.setText(
                                "Selected -> Batch " + newSel.getBatchId() +
                                        " | Meat: " + newSel.getMeatType() +
                                        " | Qty: " + newSel.getQuantity() +
                                        " | QA: " + newSel.getQaStatus() +
                                        " | Packaging: " + newSel.getPackagingStatus() +
                                        " | Dispatch: " + newSel.getDispatchStatus()
                        );
                    } else {
                        selectedBatchLabel.setText("Selected Batch");
                    }
                });

        loadSavedPlansFromFile();
    }

    // =====================================================================
    // LOAD PACKAGING TEAM APPROVED BATCHES
    // =====================================================================

    @FXML
    public void loadShipmentButtonOnAction(ActionEvent actionEvent) {
        List<WarehouseDispatchBatch> list = loadPendingBatchesFromFile();

        if (list.isEmpty()) {
            list = new ArrayList<>();
            list.add(new WarehouseDispatchBatch("B-701", "Beef", 120,
                    "Packed", "Approved", "Ready for Dispatch",
                    "V-101", "DRV-01",
                    "TBD", "TBD", null));
            list.add(new WarehouseDispatchBatch("B-702", "Chicken", 80,
                    "Packed", "Approved", "Ready for Dispatch",
                    "V-102", "DRV-02",
                    "TBD", "TBD", null));
            list.add(new WarehouseDispatchBatch("B-703", "Mutton", 60,
                    "Packed", "Approved", "Ready for Dispatch",
                    "V-103", "DRV-03",
                    "TBD", "TBD", null));
            savePendingBatchesToFile(list);
        }

        pendingBatches.setAll(list);
        pendingShipmentsTableView.refresh();

        validationMessageLabel.setStyle("-fx-text-fill: green;");
        validationMessageLabel.setText("Packaging Team approved batches loaded successfully.");
    }

    // =====================================================================
    // CHECKBOX HANDLERS (update booleans)
    // =====================================================================

    @FXML
    public void packagingVerifyCheckerBox(ActionEvent actionEvent) {
        CheckBox cb = (CheckBox) actionEvent.getSource();
        packagingVerified = cb.isSelected();
        updateVerificationLabel();
    }

    @FXML
    public void labelsVerifyCheckerBox(ActionEvent actionEvent) {
        CheckBox cb = (CheckBox) actionEvent.getSource();
        labelsVerified = cb.isSelected();
        updateVerificationLabel();
    }

    private void updateVerificationLabel() {
        if (packagingVerified && labelsVerified) {
            verificationSuccessLabel.setStyle("-fx-text-fill: green;");
            verificationSuccessLabel.setText("Packaging & Labels verified for selected batch.");
        } else if (packagingVerified || labelsVerified) {
            verificationSuccessLabel.setStyle("-fx-text-fill: orange;");
            verificationSuccessLabel.setText("Both checkboxes must be ticked before dispatch.");
        } else {
            verificationSuccessLabel.setStyle("-fx-text-fill: black;");
            verificationSuccessLabel.setText("*Verification Success Label*");
        }
    }

    // =====================================================================
    // VALIDATE ENTRY
    // =====================================================================

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateForDispatch();
    }

    private boolean validateForDispatch() {
        StringBuilder errors = new StringBuilder();

        if (selectedBatch == null) {
            errors.append("Select a batch from the table.\n");
        }
        if (!packagingVerified) {
            errors.append("Packaging not verified.\n");
        }
        if (!labelsVerified) {
            errors.append("Labels not verified.\n");
        }

        if (errors.length() > 0) {
            validationMessageLabel.setStyle("-fx-text-fill: red;");
            validationMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationMessageLabel.setStyle("-fx-text-fill: green;");
            validationMessageLabel.setText("Validation successful. You can now save the dispatch plan.");
            return true;
        }
    }

    // =====================================================================
    // SAVE PLAN (MARK AS SCHEDULING)
    // =====================================================================

    @FXML
    public void selectLabeltempButtonOnAction(ActionEvent actionEvent) {
        if (!validateForDispatch()) {
            return;
        }

        if (selectedBatch == null) return;

        selectedBatch.setDispatchStatus("Scheduling");
        pendingShipmentsTableView.refresh();

        if (!savedPlans.contains(selectedBatch)) {
            savedPlans.add(selectedBatch);
        }
        savePlansToFile();

        saveMessageLabel.setStyle("-fx-text-fill: green;");
        saveMessageLabel.setText("Dispatch plan saved for Batch " + selectedBatch.getBatchId() + ".");
    }

    // =====================================================================
    // NOTIFICATION TO TRANSPORT SCHEDULE TEAM
    // =====================================================================

    @FXML
    public void notificationSendButtonOnAction(ActionEvent actionEvent) {
        if (selectedBatch == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Select a batch before sending assignment.").show();
            return;
        }
        if (!"Scheduling".equalsIgnoreCase(selectedBatch.getDispatchStatus())) {
            new Alert(Alert.AlertType.WARNING,
                    "Save plan first (status must be 'Scheduling').").show();
            return;
        }

        ArrayList<WarehouseDispatchBatch> queue = new ArrayList<>();
        File file = new File(TRANSPORT_SCHEDULE_QUEUE_FILE);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                //noinspection unchecked
                queue = (ArrayList<WarehouseDispatchBatch>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        queue.add(selectedBatch);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(queue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Assignment Sent");
        alert.setHeaderText("Batch sent to Transport Schedule Team");
        alert.setContentText("Batch " + selectedBatch.getBatchId()
                + " is now in Scheduling for vehicle assignment.");
        alert.show();
    }

    // =====================================================================
    // GENERATE DISPATCH SLIP (REPORT)
    // =====================================================================

    @FXML
    public void generateDeliveryIDButtonOnAction(ActionEvent actionEvent) {
        if (selectedBatch == null) {
            saveAndGenerateSuccessfulMessageLabel.setStyle("-fx-text-fill: red;");
            saveAndGenerateSuccessfulMessageLabel.setText("Select a batch before generating report.");
            return;
        }

        // simple DispatchID
        String dispatchId = "DSP-" + System.currentTimeMillis();
        selectedBatch.setDispatchId(dispatchId);

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Dispatch Slip");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = chooser.showSaveDialog(null);
        if (file == null) return;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            WarehouseDispatchBatch b = selectedBatch;

            document.add(new Paragraph("Warehouse Dispatch Slip"));
            document.add(new Paragraph("-------------------------------------"));
            document.add(new Paragraph("Dispatch ID   : " + b.getDispatchId()));
            document.add(new Paragraph("Batch ID      : " + b.getBatchId()));
            document.add(new Paragraph("Meat Type     : " + b.getMeatType()));
            document.add(new Paragraph("Quantity      : " + b.getQuantity()));
            document.add(new Paragraph("QA Status     : " + b.getQaStatus()));
            document.add(new Paragraph("Packaging     : " + b.getPackagingStatus()));
            document.add(new Paragraph("Dispatch Stat : " + b.getDispatchStatus()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Vehicle       : " + b.getVehicleId()));
            document.add(new Paragraph("Driver        : " + b.getDriverId()));
            document.add(new Paragraph("Departure     : " + b.getDepartureTime()));
            document.add(new Paragraph("Arrival (ETA) : " + b.getArrivalTime()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Generated On  : " + java.time.LocalDateTime.now()));
            document.add(new Paragraph("Generated By  : Warehouse Dispatch System"));

            document.close();

            saveAndGenerateSuccessfulMessageLabel.setStyle("-fx-text-fill: green;");
            saveAndGenerateSuccessfulMessageLabel.setText("Plan Saved and Report Generated Successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            saveAndGenerateSuccessfulMessageLabel.setStyle("-fx-text-fill: red;");
            saveAndGenerateSuccessfulMessageLabel.setText("Error generating dispatch report.");
        }
    }

    // =====================================================================
    // RETURN BUTTON
    // =====================================================================

    @FXML
    public void backButtonOnAction(ActionEvent actionEvent) {
        try {
            switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/logisticManagerDashboard.fxml",
                    actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================================
    // FILE HELPERS
    // =====================================================================

    private List<WarehouseDispatchBatch> loadPendingBatchesFromFile() {
        List<WarehouseDispatchBatch> list = new ArrayList<>();
        File file = new File(PENDING_BATCH_FILE);
        if (!file.exists()) return list;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //noinspection unchecked
            list = (List<WarehouseDispatchBatch>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void savePendingBatchesToFile(List<WarehouseDispatchBatch> list) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(PENDING_BATCH_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSavedPlansFromFile() {
        File file = new File(DISPATCH_PLAN_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //noinspection unchecked
            ArrayList<WarehouseDispatchBatch> list =
                    (ArrayList<WarehouseDispatchBatch>) ois.readObject();
            savedPlans.clear();
            savedPlans.addAll(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePlansToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(DISPATCH_PLAN_FILE))) {
            oos.writeObject(savedPlans);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
