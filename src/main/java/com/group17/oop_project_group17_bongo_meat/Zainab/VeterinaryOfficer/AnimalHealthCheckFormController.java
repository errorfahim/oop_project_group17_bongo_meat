package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

// ======= OpenPDF imports (same style as your teammate) =======
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Table;
import org.openpdf.text.pdf.PdfWriter;

public class AnimalHealthCheckFormController {

    // --------- FXML NODES ----------

    @FXML private TableView<HealthCheckRecord> batchShowingTableView;
    @FXML private TableColumn<HealthCheckRecord, String> batchIDCol;
    @FXML private TableColumn<HealthCheckRecord, Integer> animalCountCol;
    @FXML private TableColumn<HealthCheckRecord, String> lastCheckDateCol;
    @FXML private TableColumn<HealthCheckRecord, String> statusCol;

    @FXML private TextField temperatureTextField;
    @FXML private TextField treatmentGivenTextField;
    @FXML private TextField medicineDosageTextField;
    @FXML private TextField injuryNoteTextField;
    @FXML private TextArea symptomsTextArea;
    @FXML private TextArea behaviorTextArea;
    @FXML private ComboBox<String> hydrationLevelComboBox;

    @FXML private Label recordSaveMessageLabel;
    @FXML private Label certificateGenerateMessageLabel;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private Label selectedBatchMessageLabel;

    // --------- DATA STRUCTURES ----------

    private final ObservableList<HealthCheckRecord> healthCheckList =
            FXCollections.observableArrayList();

    // all saved health checks (for binary persistence)
    private final ArrayList<HealthCheckRecord> savedRecords = new ArrayList<>();

    // currently selected batch (row)
    private HealthCheckRecord selectedBatch;

    // last saved record – required for certificate / submit
    private HealthCheckRecord lastSavedRecord;

    // binary file names
    private static final String HEALTH_RECORDS_FILE = "healthCheckRecords.dat";
    private static final String SUPERVISOR_QUEUE_FILE = "supervisorQueue.dat";

    // ===================== INITIALIZE =====================

    @FXML
    public void initialize() {
        // Setup TableView columns
        batchIDCol.setCellValueFactory(data -> data.getValue().batchIDProperty());
        animalCountCol.setCellValueFactory(data -> data.getValue().animalCountProperty().asObject());
        lastCheckDateCol.setCellValueFactory(data -> data.getValue().lastCheckDateProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        batchShowingTableView.setItems(healthCheckList);

        // hydration options
        hydrationLevelComboBox.getItems().setAll("Normal", "Low", "Dehydrated");

        // initial label texts
        selectedBatchMessageLabel.setText("*Selected Batch for Inspection*");
        validationSuccessMessageLabel.setText("");
        recordSaveMessageLabel.setText("");
        certificateGenerateMessageLabel.setText("");

        // when a row is clicked, remember selected batch and show in label
        batchShowingTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedBatch = newSel;
                    if (newSel != null) {
                        selectedBatchMessageLabel.setText(
                                "Selected -> Batch " + newSel.getBatchID() +
                                        " | Animals: " + newSel.getAnimalCount() +
                                        " | Last Check: " + newSel.getLastCheckDate() +
                                        " | Status: " + newSel.getStatus()
                        );
                    } else {
                        selectedBatchMessageLabel.setText("*Selected Batch for Inspection*");
                    }
                });

        // load already-saved records (if any) – for binary history
        loadSavedRecordsFromFile();
    }

    // ===================== BUTTON HANDLERS =====================

    @FXML
    public void loadHealthCheakupBatchButtonOnAction(ActionEvent actionEvent) {
        // For now: ONLY dummy values, like your previous loadSampleData()
        healthCheckList.clear();

        healthCheckList.add(new HealthCheckRecord(
                "Batch001", 10, "2025-12-02", "Pending",
                "", "", "", "", "", "", 0));

        healthCheckList.add(new HealthCheckRecord(
                "Batch002", 15, "2025-12-01", "Pending",
                "", "", "", "", "", "", 0));

        healthCheckList.add(new HealthCheckRecord(
                "Batch003", 20, "2025-11-30", "Under Observation",
                "", "", "", "", "", "", 0));
    }

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateForm(); // shows messages; save/generate/submit also call this
    }

    @FXML
    public void saveHealthCheckButtonOnAction(ActionEvent actionEvent) {
        // 1) all fields must be valid and non-empty
        if (!validateForm()) {
            return;
        }

        // 2) we must have a selected batch
        if (selectedBatch == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Please select a batch from the table first.");
            return;
        }

        try {
            String batchID = selectedBatch.getBatchID();
            int animalCount = selectedBatch.getAnimalCount();
            String lastCheckDate = selectedBatch.getLastCheckDate();
            String status = "Checked"; // can tweak if needed

            String temperature = temperatureTextField.getText().trim();
            String symptoms = symptomsTextArea.getText().trim();
            String behavior = behaviorTextArea.getText().trim();
            String hydration = hydrationLevelComboBox.getValue();
            String injuryNotes = injuryNoteTextField.getText().trim();
            String treatmentGiven = treatmentGivenTextField.getText().trim();
            int dosage = Integer.parseInt(medicineDosageTextField.getText().trim());

            HealthCheckRecord newRecord = new HealthCheckRecord(
                    batchID,
                    animalCount,
                    lastCheckDate,
                    status,
                    temperature,
                    symptoms,
                    behavior,
                    hydration,
                    injuryNotes,
                    treatmentGiven,
                    dosage
            );

            // update list & keep as last saved
            healthCheckList.add(newRecord);   // stays in UI
            savedRecords.add(newRecord);      // for binary
            lastSavedRecord = newRecord;

            saveRecordsToFile();

            recordSaveMessageLabel.setStyle("-fx-text-fill: green;");
            recordSaveMessageLabel.setText("Updated health record saved for Batch " + batchID);

        } catch (NumberFormatException e) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Medicine dosage must be an integer value.");
        }
    }

    @FXML
    public void generateCertificateButtonOnAction(ActionEvent actionEvent) {
        // must have already saved a record successfully
        if (lastSavedRecord == null) {
            certificateGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            certificateGenerateMessageLabel.setText("Save a valid health record before generating certificate.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Veterinary Health Certificate");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File chosenFile = fileChooser.showSaveDialog(null);

        if (chosenFile == null) {
            return;
        }

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(chosenFile));
            document.open();

            // Title
            document.add(new Paragraph("Veterinary Health Certificate"));
            document.add(new Paragraph(" "));

            // simple two-column table like your teammate's style
            Table table = new Table(2);
            table.addCell("Batch ID");
            table.addCell(lastSavedRecord.getBatchID());

            table.addCell("Vet License");
            table.addCell("12345-XYZ"); // dummy license

            table.addCell("Timestamp");
            table.addCell(java.time.LocalDateTime.now().toString());

            table.addCell("Health Status");
            table.addCell(lastSavedRecord.getStatus());

            table.addCell("Temperature (°C)");
            table.addCell(lastSavedRecord.getTemperature());

            table.addCell("Symptoms");
            table.addCell(lastSavedRecord.getSymptoms());

            table.addCell("Behavior");
            table.addCell(lastSavedRecord.getBehavior());

            table.addCell("Hydration");
            table.addCell(lastSavedRecord.getHydrationLevel());

            table.addCell("Injury Notes");
            table.addCell(lastSavedRecord.getInjuryNotes());

            table.addCell("Treatment Given");
            table.addCell(lastSavedRecord.getTreatmentGiven());

            table.addCell("Medicine Dosage (mg)");
            table.addCell(String.valueOf(lastSavedRecord.getMedicineDosage()));

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("This certificate is auto-generated by the Veterinary System."));

            certificateGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            certificateGenerateMessageLabel.setText("Certificate Generated Successfully!");

        } catch (DocumentException | IOException e) {
            certificateGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            certificateGenerateMessageLabel.setText("Error generating certificate.");
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        // sequence: must have saved record (so validation already passed)
        if (lastSavedRecord == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please validate and save the health check before submitting.").show();
            return;
        }

        addToSupervisorQueue(lastSavedRecord);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Submitted");
        alert.setHeaderText("Health Check Submitted");
        alert.setContentText("Health check data and certificate sent to Supervisor queue and Farm Manager.");
        alert.show();
    }

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/veterinaryOfficerDashboard.fxml",
                actionEvent);
    }

    // ===================== VALIDATION =====================

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

        if (symptomsTextArea.getText().trim().isEmpty()) {
            errors.append("Symptoms field cannot be empty.\n");
        }
        if (behaviorTextArea.getText().trim().isEmpty()) {
            errors.append("Behavior field cannot be empty.\n");
        }
        if (injuryNoteTextField.getText().trim().isEmpty()) {
            errors.append("Injury Notes field cannot be empty.\n");
        }
        if (treatmentGivenTextField.getText().trim().isEmpty()) {
            errors.append("Treatment Given field cannot be empty.\n");
        }

        String dosageText = medicineDosageTextField.getText().trim();
        if (dosageText.isEmpty()) {
            errors.append("Medicine Dosage field cannot be empty.\n");
        } else {
            try {
                Integer.parseInt(dosageText);
            } catch (NumberFormatException e) {
                errors.append("Medicine Dosage must be an integer value.\n");
            }
        }

        if (hydrationLevelComboBox.getSelectionModel().getSelectedItem() == null) {
            errors.append("Hydration level must be selected.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Validation successful. You can now save the health check.");
            return true;
        }
    }

    // ===================== BINARY HELPERS =====================

    private void loadSavedRecordsFromFile() {
        File file = new File(HEALTH_RECORDS_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<HealthCheckRecord> list = (ArrayList<HealthCheckRecord>) ois.readObject();
            savedRecords.clear();
            savedRecords.addAll(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveRecordsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HEALTH_RECORDS_FILE))) {
            oos.writeObject(savedRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToSupervisorQueue(HealthCheckRecord record) {
        File file = new File(SUPERVISOR_QUEUE_FILE);
        ArrayList<HealthCheckRecord> queue = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<HealthCheckRecord> loaded = (ArrayList<HealthCheckRecord>) ois.readObject();
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
