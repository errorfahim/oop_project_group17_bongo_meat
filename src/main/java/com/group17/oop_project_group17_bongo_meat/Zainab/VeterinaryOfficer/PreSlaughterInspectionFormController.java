package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.IncomingLivestockVetInspectionRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class PreSlaughterInspectionFormController {

    // ---------- FXML NODES ----------

    @FXML private TextField injuryCheckTextField;
    @FXML private TableColumn<PreSlaughterInspectionRecord, String> lastHealthCheckDateCol;
    @FXML private TextField temperatureTextField;
    @FXML private ComboBox<String> feedWithdrawalStatusComboBox;
    @FXML private ComboBox<String> movementBehaviorComboBox;
    @FXML private TableColumn<PreSlaughterInspectionRecord, String> holdingTimeCol;
    @FXML private ComboBox<String> hydrationComboBox;
    @FXML private TextArea finalRemarksTextArea;
    @FXML private ComboBox<String> cleanlinessComboBox;
    @FXML private ComboBox<String> stressLevelComboBox;
    @FXML private RadioButton quarantineRequiredRadioButton;
    @FXML private TableColumn<PreSlaughterInspectionRecord, String> batchIDCol;
    @FXML private TableView<PreSlaughterInspectionRecord> batchShowingTableView;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private Label selectedBatchMessageLabel;
    @FXML private Label decisionSaveMessageLabel;
    @FXML private RadioButton approvedForSlaughterRadioButton;
    @FXML private TableColumn<PreSlaughterInspectionRecord, Integer> animalCountCol;
    @FXML private Label reportGenerateMessageLabel;

    // ---------- DATA STRUCTURES ----------

    private final ObservableList<PreSlaughterInspectionRecord> batchList =
            FXCollections.observableArrayList();

    private final ArrayList<PreSlaughterInspectionRecord> savedInspections =
            new ArrayList<>();

    private PreSlaughterInspectionRecord selectedBatch;
    private PreSlaughterInspectionRecord lastSavedRecord;

    // From Abdullah's side
    private static final String VET_REQUEST_FILE = "incomingVetRequests.dat";

    // Vet-side storage
    private static final String PRE_SLAUGHTER_FILE = "preSlaughterInspectionRecords.dat";
    private static final String PRE_SLAUGHTER_QA_QUEUE_FILE = "preSlaughterQAQueue.dat";

    private ToggleGroup decisionToggleGroup;

    private static final String VET_ID = "VET-007";         // dummy
    private static final String VET_LICENSE = "LIC-2025-PSI"; // dummy

    @FXML
    public void initialize() {
        // Table column bindings
        batchIDCol.setCellValueFactory(data -> data.getValue().batchIdProperty());
        animalCountCol.setCellValueFactory(data -> data.getValue().animalCountProperty().asObject());
        lastHealthCheckDateCol.setCellValueFactory(data -> data.getValue().lastHealthCheckDateProperty());
        holdingTimeCol.setCellValueFactory(data -> data.getValue().holdingTimeProperty());

        batchShowingTableView.setItems(batchList);

        // Combobox options
        movementBehaviorComboBox.getItems().setAll("Normal", "Lethargic", "Aggressive");
        hydrationComboBox.getItems().setAll("Normal", "Low", "Dehydrated");
        stressLevelComboBox.getItems().setAll("Low", "Medium", "High");
        feedWithdrawalStatusComboBox.getItems().setAll("Completed", "Not Completed");
        cleanlinessComboBox.getItems().setAll("Good", "Fair", "Poor");

        selectedBatchMessageLabel.setText("*Selected Batch for Inspection*");
        validationSuccessMessageLabel.setText("*Success/Error messages*");
        decisionSaveMessageLabel.setText("*Updated decision saved*");
        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");

        // Radio buttons in one ToggleGroup
        decisionToggleGroup = new ToggleGroup();
        approvedForSlaughterRadioButton.setToggleGroup(decisionToggleGroup);
        quarantineRequiredRadioButton.setToggleGroup(decisionToggleGroup);

        // table row selection listener
        batchShowingTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedBatch = newSel;
                    if (newSel != null) {
                        selectedBatchMessageLabel.setText(
                                "Selected -> Batch " + newSel.getBatchId() +
                                        " | Type: " + newSel.getType() +
                                        " | Animals: " + newSel.getAnimalCount() +
                                        " | Holding: " + newSel.getHoldingTime()
                        );
                    } else {
                        selectedBatchMessageLabel.setText("*Selected Batch for Inspection*");
                    }
                });

        // load previous inspections if needed
        loadInspectionsFromFile();
    }

    // ================== BUTTON HANDLERS ==================

    @FXML
    public void loadInspectionRequestedBatchButtonOnAction(ActionEvent actionEvent) {
        File file = new File(VET_REQUEST_FILE);
        if (!file.exists()) {
            new Alert(Alert.AlertType.ERROR, "No vet inspection requests received from Supervisor yet.").show();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<IncomingLivestockVetInspectionRequest> vetRequests =
                    (ArrayList<IncomingLivestockVetInspectionRequest>) ois.readObject();

            batchList.clear();

            StringBuilder sb = new StringBuilder();
            sb.append("Inspection requests received from Supervisor:\n\n");

            for (IncomingLivestockVetInspectionRequest r : vetRequests) {
                // We read REAL Batch ID + Type, others dummy or derived
                String batchId = r.getBatchId();
                String type = r.getType();
                int qty = r.getQuantity();

                String lastHealthCheck = "Not Updated";  // dummy
                String holdingTime = "Standard 12h";     // dummy

                PreSlaughterInspectionRecord record = new PreSlaughterInspectionRecord(
                        batchId,
                        type,
                        qty,
                        lastHealthCheck,
                        holdingTime,
                        "", "", "", "", "", "", "", "",
                        "", "", "", ""   // inspection fields empty for now
                );

                batchList.add(record);

                sb.append("BatchID: ").append(batchId)
                        .append(" | Type: ").append(type)
                        .append(" | Quantity: ").append(qty)
                        .append(" | Status: ").append(r.getVetRequestStatus())
                        .append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Requests Received");
            alert.setHeaderText("Inspection requests loaded from Supervisor.");
            alert.setContentText(sb.toString());
            alert.show();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inspection requests.").show();
        }
    }

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateForm();
    }

    @FXML
    public void saveDecisionButtonOnAction(ActionEvent actionEvent) {
        if (!validateForm()) {
            return;
        }

        if (selectedBatch == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Please select a batch from the table first.");
            return;
        }

        if (decisionToggleGroup.getSelectedToggle() == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Please choose a decision: Approved or Quarantine Required.");
            return;
        }

        String decision;
        if (approvedForSlaughterRadioButton.isSelected()) {
            decision = "Approved for Slaughter";
        } else {
            decision = "Quarantine Required";
        }

        String temp = temperatureTextField.getText().trim();
        String movement = movementBehaviorComboBox.getValue();
        String hydration = hydrationComboBox.getValue();
        String injury = injuryCheckTextField.getText().trim();
        String stress = stressLevelComboBox.getValue();
        String feedStatus = feedWithdrawalStatusComboBox.getValue();
        String clean = cleanlinessComboBox.getValue();
        String remarks = finalRemarksTextArea.getText().trim();

        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.toString();

        // Build full inspection record (we reuse batch info from selectedBatch)
        PreSlaughterInspectionRecord fullRecord = new PreSlaughterInspectionRecord(
                selectedBatch.getBatchId(),
                selectedBatch.getType(),
                selectedBatch.getAnimalCount(),
                selectedBatch.getLastHealthCheckDate(),
                selectedBatch.getHoldingTime(),
                temp,
                movement,
                hydration,
                injury,
                stress,
                feedStatus,
                clean,
                remarks,
                decision,
                VET_ID,
                VET_LICENSE,
                dateTime
        );

        savedInspections.add(fullRecord);
        lastSavedRecord = fullRecord;
        saveInspectionsToFile();

        if (decision.equals("Approved for Slaughter")) {
            decisionSaveMessageLabel.setStyle("-fx-text-fill: green;");
            decisionSaveMessageLabel.setText("Batch " + fullRecord.getBatchId()
                    + " approved for slaughter. Decision saved.");
        } else {
            decisionSaveMessageLabel.setStyle("-fx-text-fill: orange;");
            decisionSaveMessageLabel.setText("Batch " + fullRecord.getBatchId()
                    + " marked for QUARANTINE. Decision saved.");
        }
    }

    @FXML
    public void generateCertificateButtonOnAction(ActionEvent actionEvent) {
        // Generate Pre-Slaughter Inspection Report PDF

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Pre-Slaughter Inspection Report");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File chosenFile = fileChooser.showSaveDialog(null);

            if (chosenFile == null) {
                return;
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(chosenFile));
            document.open();

            document.add(new Paragraph("Pre-Slaughter Inspection Report"));
            document.add(new Paragraph("---------------------------------------"));
            document.add(new Paragraph("Batch ID      : " + lastSavedRecord.getBatchId()));
            document.add(new Paragraph("Type          : " + lastSavedRecord.getType()));
            document.add(new Paragraph("Animal Count  : " + lastSavedRecord.getAnimalCount()));
            document.add(new Paragraph("Holding Time  : " + lastSavedRecord.getHoldingTime()));
            document.add(new Paragraph("Last Health Check : " + lastSavedRecord.getLastHealthCheckDate()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Vet ID        : " + lastSavedRecord.getVetId()));
            document.add(new Paragraph("Vet License   : " + lastSavedRecord.getVetLicense()));
            document.add(new Paragraph("Inspection DT : " + lastSavedRecord.getInspectionDateTime()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Temperature   : " + lastSavedRecord.getTemperature() + " °C"));
            document.add(new Paragraph("Movement      : " + lastSavedRecord.getMovementBehavior()));
            document.add(new Paragraph("Hydration     : " + lastSavedRecord.getHydration()));
            document.add(new Paragraph("Injury Check  : " + lastSavedRecord.getInjuryCheck()));
            document.add(new Paragraph("Stress Level  : " + lastSavedRecord.getStressLevel()));
            document.add(new Paragraph("Feed Withdraw : " + lastSavedRecord.getFeedWithdrawalStatus()));
            document.add(new Paragraph("Cleanliness   : " + lastSavedRecord.getCleanliness()));
            document.add(new Paragraph("Final Remarks : " + lastSavedRecord.getFinalRemarks()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Decision      : " + lastSavedRecord.getDecision()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Report generated by Veterinary System."));

            document.close();

            reportGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            reportGenerateMessageLabel.setText("Report Generated Successfully!");

        } catch (Exception e) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Error generating report.");
            e.printStackTrace();
        }

    }

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        if (lastSavedRecord == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please validate and save a decision before submitting to QA.").show();
            return;
        }

        addToQaQueue(lastSavedRecord);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sent to QA");
        alert.setHeaderText("Report sent to QA Verification Queue");
        alert.setContentText("Inspection report for Batch " +
                lastSavedRecord.getBatchId() +
                " has been sent to QA queue and QA Officer notified.");
        alert.show();
    }

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) throws IOException {
        // go to next Vet screen; adjust path as you like
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/veterinaryOfficerDashboard.fxml",
                actionEvent);
    }

    // ================== VALIDATION ==================

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (selectedBatch == null) {
            errors.append("Please select a batch from the table.\n");
        }

        String temp = temperatureTextField.getText().trim();
        if (temp.isEmpty()) {
            errors.append("Temperature field cannot be empty.\n");
        } else {
            try {
                Double.parseDouble(temp);
            } catch (NumberFormatException e) {
                errors.append("Temperature must be a numeric value.\n");
            }
        }

        if (movementBehaviorComboBox.getValue() == null) {
            errors.append("Movement Behavior must be selected.\n");
        }
        if (hydrationComboBox.getValue() == null) {
            errors.append("Hydration must be selected.\n");
        }
        if (injuryCheckTextField.getText().trim().isEmpty()) {
            errors.append("Injury Check field cannot be empty.\n");
        }
        if (stressLevelComboBox.getValue() == null) {
            errors.append("Stress Level must be selected.\n");
        }
        if (feedWithdrawalStatusComboBox.getValue() == null) {
            errors.append("Feed Withdrawal Status must be selected.\n");
        }
        if (cleanlinessComboBox.getValue() == null) {
            errors.append("Cleanliness must be selected.\n");
        }
        if (finalRemarksTextArea.getText().trim().isEmpty()) {
            errors.append("Final Remarks field cannot be empty.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Validation successful. You can save the decision.");
            return true;
        }
    }

    // ================== BINARY HELPERS ==================

    private void loadInspectionsFromFile() {
        File file = new File(PRE_SLAUGHTER_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<PreSlaughterInspectionRecord> list =
                    (ArrayList<PreSlaughterInspectionRecord>) ois.readObject();
            savedInspections.clear();
            savedInspections.addAll(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveInspectionsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRE_SLAUGHTER_FILE))) {
            oos.writeObject(savedInspections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToQaQueue(PreSlaughterInspectionRecord record) {
        File file = new File(PRE_SLAUGHTER_QA_QUEUE_FILE);
        ArrayList<PreSlaughterInspectionRecord> queue = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<PreSlaughterInspectionRecord> loaded =
                        (ArrayList<PreSlaughterInspectionRecord>) ois.readObject();
                queue.addAll(loaded);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        queue.add(record);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(queue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
