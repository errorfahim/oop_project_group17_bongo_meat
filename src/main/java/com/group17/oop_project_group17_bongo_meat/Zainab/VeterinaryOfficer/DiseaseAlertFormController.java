package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class DiseaseAlertFormController {

    // ---------- FXML NODES ----------

    @FXML private TableView<DiseaseBatchAlert> batchListTableView;
    @FXML private TableColumn<DiseaseBatchAlert, String> batchIDCol;
    @FXML private TableColumn<DiseaseBatchAlert, Number> animalCountCol;
    @FXML private TableColumn<DiseaseBatchAlert, String> locationCol;
    @FXML private TableColumn<DiseaseBatchAlert, String> currentHealthStatusCol;

    @FXML private TextField numberAffectedTextField;
    @FXML private TextArea symptomsTextArea;
    @FXML private DatePicker onSetDatePicker;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML private CheckBox yesCheckerBox;
    @FXML private CheckBox noCheckerBox;

    @FXML private Label selectedBatchMessageLabel;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private Label alertSaveMessageLabel;
    @FXML private Label reportGenerateMessageLabel;
    @FXML private Label alertAndQuarantinedSuccessMessageLabel;
    @FXML private Label successandErrorMessageLabel1;

    // ---------- DATA ----------

    private final ObservableList<DiseaseBatchAlert> batchList =
            FXCollections.observableArrayList();

    private static final String DISEASE_BATCH_FILE = "diseaseBatches.dat";
    private static final String DISEASE_ALERT_FILE = "diseaseAlerts.dat";
    private static final String DISEASE_ALERT_QUEUE_FILE = "diseaseAlertQueue.dat";

    private DiseaseBatchAlert selectedBatch;
    private DiseaseAlertRecord currentAlert;    // latest draft/published alert
    private boolean readyToPublish = false;     // controlled by Yes / No buttons

    @FXML
    public void initialize() {

        // bind columns
        batchIDCol.setCellValueFactory(data -> data.getValue().batchIdProperty());
        animalCountCol.setCellValueFactory(data -> data.getValue().animalCountProperty());
        locationCol.setCellValueFactory(data -> data.getValue().locationProperty());
        currentHealthStatusCol.setCellValueFactory(data -> data.getValue().currentHealthStatusProperty());

        batchListTableView.setItems(batchList);

        // initial messages
        selectedBatchMessageLabel.setText("*Selected the Affected batch*");
        validationSuccessMessageLabel.setText("Are you sure you want to publish the Disease Alert?");
        alertSaveMessageLabel.setText("*Disease Alert Published & Confirmed*");
        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");
        alertAndQuarantinedSuccessMessageLabel.setText("*Disease Alert Published and Batch Quarantined Successfully.*");
        successandErrorMessageLabel1.setText("System validates the alert confirmation OR publishing is aborted, alert stays as draft");

        // priority options
        priorityComboBox.getItems().setAll("Low", "Medium", "High");

        // Samples collected? Yes/No – make them behave like radio buttons
        yesCheckerBox.selectedProperty().addListener((obs, was, isNow) -> {
            if (isNow) noCheckerBox.setSelected(false);
        });
        noCheckerBox.selectedProperty().addListener((obs, was, isNow) -> {
            if (isNow) yesCheckerBox.setSelected(false);
        });

        // selection listener for table
        batchListTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> {
                    selectedBatch = newSel;
                    if (newSel != null) {
                        selectedBatchMessageLabel.setText(
                                "Selected -> Batch ID: " + newSel.getBatchId() +
                                        " | Animals: " + newSel.getAnimalCount() +
                                        " | Location: " + newSel.getLocation() +
                                        " | Status: " + newSel.getCurrentHealthStatus()
                        );
                    } else {
                        selectedBatchMessageLabel.setText("*Selected the Affected batch*");
                    }
                });

        // start with empty table – load only after button click
        batchList.clear();
    }

    // ---------- LOAD LIVE-STOCK BATCH ----------

    @FXML
    public void loadLivestockBatchButtonOnAction(ActionEvent actionEvent) {
        File file = new File(DISEASE_BATCH_FILE);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<DiseaseBatchAlert> list =
                        (ArrayList<DiseaseBatchAlert>) ois.readObject();
                batchList.setAll(list);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Failed to load livestock batches.").show();
                return;
            }
        } else {
            // dummy data (same style as other screens)
            batchList.clear();
            batchList.add(new DiseaseBatchAlert("Batch001", 50, "Farm A - Shed 1", "Healthy"));
            batchList.add(new DiseaseBatchAlert("Batch002", 30, "Farm A - Shed 2", "Under Observation"));
            batchList.add(new DiseaseBatchAlert("Batch003", 70, "Farm B - Pasture", "Healthy"));
            saveBatchesToFile();
        }

        batchListTableView.refresh();

        StringBuilder sb = new StringBuilder("Batches received from Supervisor:\n");
        for (DiseaseBatchAlert b : batchList) {
            sb.append(b.getBatchId()).append(" | ")
                    .append(b.getAnimalCount()).append(" animals | ")
                    .append(b.getLocation()).append(" | ")
                    .append(b.getCurrentHealthStatus()).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Livestock Batches Loaded");
        alert.setHeaderText("Message received from Supervisor.");
        alert.setContentText(sb.toString());
        alert.show();
    }

    // ---------- VALIDATION FOR ALERT FORM ----------

    private boolean validateAlertForm() {
        StringBuilder errors = new StringBuilder();

        if (selectedBatch == null) {
            errors.append("Select a batch from the table.\n");
        }

        String numText = numberAffectedTextField.getText().trim();
        if (numText.isEmpty()) {
            errors.append("Number Affected is required.\n");
        } else {
            try {
                int num = Integer.parseInt(numText);
                if (num <= 0) errors.append("Number Affected must be a positive integer.\n");
            } catch (NumberFormatException e) {
                errors.append("Number Affected must be an integer.\n");
            }
        }

        if (symptomsTextArea.getText().trim().isEmpty()) {
            errors.append("Symptoms field is required.\n");
        }

        LocalDate onset = onSetDatePicker.getValue();
        if (onset == null) {
            errors.append("Onset Date must be selected.\n");
        } else if (onset.isAfter(LocalDate.now())) {
            errors.append("Onset Date cannot be in the future.\n");
        }

        if (priorityComboBox.getValue() == null) {
            errors.append("Priority must be selected (Low / Medium / High).\n");
        }

        if (!yesCheckerBox.isSelected() && !noCheckerBox.isSelected()) {
            errors.append("Please specify if samples were collected (Yes/No).\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Alert form valid. You can submit the alert.");
            return true;
        }
    }

    // ---------- SUBMIT ALERT (SAVE DRAFT) ----------

    @FXML
    public void submitAlertButtonOnAction(ActionEvent actionEvent) {
        if (!validateAlertForm()) return;

        ArrayList<DiseaseAlertRecord> allAlerts = loadAlertsFromFile();

        String newAlertId = "ALERT-" + (allAlerts.size() + 1);

        int numAffected = Integer.parseInt(numberAffectedTextField.getText().trim());
        String symptoms = symptomsTextArea.getText().trim();
        String onsetDate = onSetDatePicker.getValue().toString();
        String priority = priorityComboBox.getValue();
        boolean samplesCollected = yesCheckerBox.isSelected();

        currentAlert = new DiseaseAlertRecord(
                newAlertId,
                selectedBatch.getBatchId(),
                numAffected,
                symptoms,
                onsetDate,
                priority,
                samplesCollected,
                "Draft"
        );

        allAlerts.add(currentAlert);
        saveAlertsToFile(allAlerts);

        alertSaveMessageLabel.setStyle("-fx-text-fill: green;");
        alertSaveMessageLabel.setText("Alert saved as DRAFT with ID " + newAlertId + ".");
        successandErrorMessageLabel1.setStyle("-fx-text-fill: green;");
        successandErrorMessageLabel1.setText("Alert stored as draft. Choose Yes/No to confirm publishing decision.");
    }

    // ---------- YES / NO BUTTONS (PUBLISH DECISION) ----------

    @FXML
    public void yesDiseaseButtonOnAction(ActionEvent actionEvent) {
        readyToPublish = true;
        successandErrorMessageLabel1.setStyle("-fx-text-fill: green;");
        successandErrorMessageLabel1.setText("You chose YES. Click 'Confirm & Publish' to publish the disease alert.");
    }

    @FXML
    public void noDiseaseButtonOnAction(ActionEvent actionEvent) {
        readyToPublish = false;
        successandErrorMessageLabel1.setStyle("-fx-text-fill: red;");
        successandErrorMessageLabel1.setText("Publishing aborted. Alert remains saved as draft.");
    }

    // ---------- CONFIRM & PUBLISH ----------

    @FXML
    public void confirmAndPublishButtonOnAction(ActionEvent actionEvent) {
        if (currentAlert == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Submit an alert (draft) first.").show();
            return;
        }
        if (!readyToPublish) {
            new Alert(Alert.AlertType.WARNING,
                    "Publishing not confirmed. Click 'Yes' if you want to publish.").show();
            return;
        }

        ArrayList<DiseaseAlertRecord> allAlerts = loadAlertsFromFile();

        // update status to Published
        for (DiseaseAlertRecord r : allAlerts) {
            if (r.getAlertId().equals(currentAlert.getAlertId())) {
                r.setStatus("Published");
                currentAlert = r;
                break;
            }
        }
        saveAlertsToFile(allAlerts);

        // quarantine the batch
        if (selectedBatch != null) {
            selectedBatch.setCurrentHealthStatus("QUARANTINED");
            batchListTableView.refresh();
            saveBatchesToFile();
        }

        alertSaveMessageLabel.setStyle("-fx-text-fill: green;");
        alertSaveMessageLabel.setText("Disease Alert PUBLISHED & batch QUARANTINED.");
        alertAndQuarantinedSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        alertAndQuarantinedSuccessMessageLabel.setText("*Disease Alert Published and Batch Quarantined Successfully.*");
    }

    // ---------- LOAD UPDATED BATCH (SHOW QUARANTINED) ----------

    @FXML
    public void loadUpdateBatchButtonOnAction(ActionEvent actionEvent) {
        File file = new File(DISEASE_BATCH_FILE);
        if (!file.exists()) {
            new Alert(Alert.AlertType.ERROR,
                    "No batch file found yet. Load first using 'Load Livestock Batch'.").show();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<DiseaseBatchAlert> list =
                    (ArrayList<DiseaseBatchAlert>) ois.readObject();
            batchList.setAll(list);
            batchListTableView.refresh();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Failed to load updated batches.").show();
        }
    }

    // ---------- GENERATE REPORT (PDF) ----------

    @FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
        if (currentAlert == null) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Submit an alert first (draft or published).");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Disease Alert Report");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = chooser.showSaveDialog(null);
        if (file == null) return;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Disease Outbreak Alert Report"));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("Alert ID: " + currentAlert.getAlertId()));
            document.add(new Paragraph("Batch ID: " + currentAlert.getBatchId()));
            document.add(new Paragraph("Number Affected: " + currentAlert.getNumberAffected()));
            document.add(new Paragraph("Symptoms: " + currentAlert.getSymptoms()));
            document.add(new Paragraph("Onset Date: " + currentAlert.getOnsetDate()));
            document.add(new Paragraph("Priority: " + currentAlert.getPriority()));
            document.add(new Paragraph("Samples Collected: " +
                    (currentAlert.isSamplesCollected() ? "Yes" : "No")));
            document.add(new Paragraph("Status: " + currentAlert.getStatus()));
            document.add(new Paragraph("Vet ID: VET-XXXX (placeholder)"));
            document.add(new Paragraph("Diagnosis: To be confirmed by lab."));

            document.close();

            reportGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            reportGenerateMessageLabel.setText("Report Generated Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Error generating report.");
        }
    }

    // ---------- SUBMIT TO FARM MANAGER & ADMIN ----------

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        if (currentAlert == null || !"Published".equalsIgnoreCase(currentAlert.getStatus())) {
            new Alert(Alert.AlertType.WARNING,
                    "You can submit only a PUBLISHED alert.").show();
            return;
        }

        ArrayList<DiseaseAlertRecord> queue = new ArrayList<>();
        File file = new File(DISEASE_ALERT_QUEUE_FILE);

        try {
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    queue = (ArrayList<DiseaseAlertRecord>) ois.readObject();
                }
            }

            queue.add(currentAlert);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(queue);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Disease Alert Submitted");
            alert.setHeaderText("Immediate notification sent.");
            alert.setContentText("Disease Alert " + currentAlert.getAlertId() +
                    " has been sent to Farm Manager and Admin.");
            alert.show();

            alertAndQuarantinedSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            alertAndQuarantinedSuccessMessageLabel.setText(
                    "Alert submitted to Farm Manager and Admin.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error while submitting alert.").show();
        }
    }

    // ---------- RETURN BUTTON ----------

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) {
        try {
            switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/veterinaryOfficerDashboard.fxml",
                    actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- FILE HELPERS ----------

    private void saveBatchesToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(DISEASE_BATCH_FILE))) {
            oos.writeObject(new ArrayList<>(batchList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DiseaseAlertRecord> loadAlertsFromFile() {
        ArrayList<DiseaseAlertRecord> list = new ArrayList<>();
        File file = new File(DISEASE_ALERT_FILE);
        if (!file.exists()) return list;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            list = (ArrayList<DiseaseAlertRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void saveAlertsToFile(ArrayList<DiseaseAlertRecord> alerts) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(DISEASE_ALERT_FILE))) {
            oos.writeObject(alerts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
