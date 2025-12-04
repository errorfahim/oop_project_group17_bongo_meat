package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Table;
import org.openpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class MeatQualityTestFormController {

    // ---------- FXML NODES ----------

    @FXML private TableColumn<MeatQualityTestRecord, String> storageTimeCol;
    @FXML private ComboBox<Integer> colorScoreComboBox;
    @FXML private TextField moistureTextField;
    @FXML private TextField pHValueTextField;
    @FXML private TextArea odorEvaluationTextArea;
    @FXML private Label passFailStatusSaveMessageLabel;
    @FXML private Label selectedMeatBatchMessageLabel;
    @FXML private TextField microbialCountTextField;
    @FXML private TableView<MeatQualityTestRecord> batchListTableView;
    @FXML private TableColumn<MeatQualityTestRecord, Double> weightCol;
    @FXML private TableColumn<MeatQualityTestRecord, String> cutTypeCol;
    @FXML private TableColumn<MeatQualityTestRecord, String> batchIDCol;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private TableColumn<MeatQualityTestRecord, String> processingDateCol;
    @FXML private TableColumn<MeatQualityTestRecord, String> temperatureCol;
    @FXML private TextArea foreignMatterCheckTextArea;
    @FXML private TextArea remarksTextArea;
    @FXML private TextField storageTempTextField;
    @FXML private Label reportGenerateMessageLabel;

    // ---------- DATA STRUCTURES ----------

    private final ObservableList<MeatQualityTestRecord> batchList =
            FXCollections.observableArrayList();

    private final ArrayList<MeatQualityTestRecord> savedRecords =
            new ArrayList<>();

    private MeatQualityTestRecord selectedBatch;
    private MeatQualityTestRecord lastSavedRecord;

    private String evaluationResult = null; // "Pass" or "Fail"

    // binary file names
    private static final String MEAT_QUALITY_FILE = "meatQualityRecords.dat";
    private static final String MEAT_QUALITY_QA_QUEUE_FILE = "meatQualityQAQueue.dat";

    private static final String VET_ID = "VET-007";
    private static final String VET_LICENSE = "LIC-2025-MQT";

    @FXML
    public void initialize() {
        batchIDCol.setCellValueFactory(data -> data.getValue().batchIdProperty());
        cutTypeCol.setCellValueFactory(data -> data.getValue().cutTypeProperty());
        weightCol.setCellValueFactory(data -> data.getValue().weightProperty().asObject());
        processingDateCol.setCellValueFactory(data -> data.getValue().processingDateProperty());
        storageTimeCol.setCellValueFactory(data -> data.getValue().storageTimeProperty());
        temperatureCol.setCellValueFactory(data -> data.getValue().temperatureProperty());

        batchListTableView.setItems(batchList);

        // Color score 1-5
        colorScoreComboBox.getItems().setAll(1, 2, 3, 4, 5);

        selectedMeatBatchMessageLabel.setText("*Selected a Meat Batch*");
        validationSuccessMessageLabel.setText("*Success/Error messages*");
        passFailStatusSaveMessageLabel.setText("*Updated Pass/Fail status saved*");
        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");

        // row selection
        batchListTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedBatch = newSel;
                    if (newSel != null) {
                        selectedMeatBatchMessageLabel.setText(
                                "Selected -> Batch " + newSel.getBatchId() +
                                        " | Cut: " + newSel.getCutType() +
                                        " | Weight: " + newSel.getWeight() + " kg" +
                                        " | Storage: " + newSel.getStorageTime()
                        );
                    } else {
                        selectedMeatBatchMessageLabel.setText("*Selected a Meat Batch*");
                    }
                });

        loadSavedRecordsFromFile();
    }

    // ================== BUTTON HANDLERS ==================

    @FXML
    public void loadProcessedMeatBatchButtonOnAction(ActionEvent actionEvent) {
        // dummy processed meat batches
        batchList.clear();

        batchList.add(new MeatQualityTestRecord(
                "MQB-101", "Steak", 120.0, "2025-12-01",
                "24h", "4 °C",
                0, 0, null, 0, 0,
                "", "", "", "", "", "", "")); // lab fields empty

        batchList.add(new MeatQualityTestRecord(
                "MQB-102", "Mince", 80.5, "2025-12-01",
                "18h", "3 °C",
                0, 0, null, 0, 0,
                "", "", "", "", "", "", ""));

        batchList.add(new MeatQualityTestRecord(
                "MQB-103", "Ribs", 95.0, "2025-11-30",
                "30h", "2 °C",
                0, 0, null, 0, 0,
                "", "", "", "", "", "", ""));
    }

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateForm();
    }

    @FXML
    public void passBatchButtonOnAction(ActionEvent actionEvent) {
        evaluationResult = "Pass";
        passFailStatusSaveMessageLabel.setStyle("-fx-text-fill: green;");
        passFailStatusSaveMessageLabel.setText("Batch marked as PASS (pending save).");
    }

    @FXML
    public void rejectBatchButtonOnAction(ActionEvent actionEvent) {
        evaluationResult = "Fail";
        passFailStatusSaveMessageLabel.setStyle("-fx-text-fill: red;");
        passFailStatusSaveMessageLabel.setText("Batch marked as REJECTED (pending save).");
    }

    @FXML
    public void saveEvaluationButtonOnAction(ActionEvent actionEvent) {
        if (!validateForm()) {
            return;
        }

        if (selectedBatch == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Please select a meat batch from the table first.");
            return;
        }

        if (evaluationResult == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Please click Pass Batch or Reject Batch before saving.");
            return;
        }

        try {
            double pH = Double.parseDouble(pHValueTextField.getText().trim());
            int microbial = Integer.parseInt(microbialCountTextField.getText().trim());

            Double moisture = null;
            String moistureText = moistureTextField.getText().trim();
            if (!moistureText.isEmpty()) {
                moisture = Double.parseDouble(moistureText);
            }

            double storageTemp = Double.parseDouble(storageTempTextField.getText().trim());
            int colorScore = colorScoreComboBox.getValue();

            String odorEval = odorEvaluationTextArea.getText().trim();
            String foreignMatter = foreignMatterCheckTextArea.getText().trim();
            String remarks = remarksTextArea.getText().trim();

            String dateTime = LocalDateTime.now().toString();

            MeatQualityTestRecord fullRecord = new MeatQualityTestRecord(
                    selectedBatch.getBatchId(),
                    selectedBatch.getCutType(),
                    selectedBatch.getWeight(),
                    selectedBatch.getProcessingDate(),
                    selectedBatch.getStorageTime(),
                    selectedBatch.getTemperature(),
                    pH,
                    microbial,
                    moisture,
                    storageTemp,
                    colorScore,
                    odorEval,
                    foreignMatter,
                    remarks,
                    evaluationResult,
                    VET_ID,
                    VET_LICENSE,
                    dateTime
            );

            savedRecords.add(fullRecord);
            lastSavedRecord = fullRecord;
            saveRecordsToFile();

            if ("Pass".equals(evaluationResult)) {
                passFailStatusSaveMessageLabel.setStyle("-fx-text-fill: green;");
                passFailStatusSaveMessageLabel.setText("Evaluation saved: BATCH PASSED.");
            } else {
                passFailStatusSaveMessageLabel.setStyle("-fx-text-fill: red;");
                passFailStatusSaveMessageLabel.setText("Evaluation saved: BATCH REJECTED.");
            }

        } catch (NumberFormatException e) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Numeric conversion error. Please check the numbers again.");
        }
    }

    // ======== UPDATED: PDF GENERATION USING OpenPDF TABLE (like your group) ========

    @FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
        if (lastSavedRecord == null) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Save an evaluation before generating report.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Meat Quality Report");
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

            MeatQualityTestRecord r = lastSavedRecord;

            // Title
            document.add(new Paragraph("Meat Quality Test Report"));
            document.add(new Paragraph(" "));

            // 2-column table with key/value pairs
            Table table = new Table(2);

            table.addCell("Batch ID");
            table.addCell(r.getBatchId());

            table.addCell("Cut Type");
            table.addCell(r.getCutType());

            table.addCell("Weight");
            table.addCell(r.getWeight() + " kg");

            table.addCell("Processing Date");
            table.addCell(r.getProcessingDate());

            table.addCell("Storage Time");
            table.addCell(r.getStorageTime());

            table.addCell("Storage Temperature");
            table.addCell(r.getStorageTemp() + " °C");

            table.addCell("Display Temperature");
            table.addCell(r.getTemperature());

            table.addCell("Vet ID");
            table.addCell(r.getVetId());

            table.addCell("Vet License");
            table.addCell(r.getVetLicense());

            table.addCell("Test DateTime");
            table.addCell(r.getTestDateTime());

            table.addCell("pH Value");
            table.addCell(String.valueOf(r.getpHValue()));

            table.addCell("Microbial Count");
            table.addCell(String.valueOf(r.getMicrobialCount()));

            if (r.getMoisturePercent() != null) {
                table.addCell("Moisture %");
                table.addCell(String.valueOf(r.getMoisturePercent()));
            }

            table.addCell("Color Score");
            table.addCell(String.valueOf(r.getColorScore()));

            table.addCell("Odor Evaluation");
            table.addCell(r.getOdorEvaluation());

            table.addCell("Foreign Matter Check");
            table.addCell(r.getForeignMatterCheck());

            table.addCell("Remarks");
            table.addCell(r.getRemarks());

            table.addCell("Decision");
            table.addCell(r.getEvaluationResult());

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Report generated by Veterinary System."));

            reportGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            reportGenerateMessageLabel.setText("Report Generated Successfully!");

        } catch (DocumentException | IOException e) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Error generating report.");
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        if (lastSavedRecord == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please validate, evaluate, and save before submitting to QA.").show();
            return;
        }

        addToQaQueue(lastSavedRecord);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sent to QA");
        alert.setHeaderText("Meat Quality report sent to QA Verification Queue");
        alert.setContentText("Report for Batch " + lastSavedRecord.getBatchId()
                + " has been submitted to QA.");
        alert.show();
    }

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) throws IOException {
        // back to previous vet screen – adjust if you use a different one
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/veterinaryOfficerDashboard.fxml",
                actionEvent);
    }

    // ================== VALIDATION ==================

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (selectedBatch == null) {
            errors.append("Please select a meat batch from the table.\n");
        }

        String pHText = pHValueTextField.getText().trim();
        if (pHText.isEmpty()) {
            errors.append("pH Value cannot be empty.\n");
        } else {
            try {
                Double.parseDouble(pHText);
            } catch (NumberFormatException e) {
                errors.append("pH Value must be numeric.\n");
            }
        }

        String microText = microbialCountTextField.getText().trim();
        if (microText.isEmpty()) {
            errors.append("Microbial Count cannot be empty.\n");
        } else {
            try {
                Integer.parseInt(microText);
            } catch (NumberFormatException e) {
                errors.append("Microbial Count must be an integer.\n");
            }
        }

        String moistureText = moistureTextField.getText().trim();
        if (!moistureText.isEmpty()) {        // optional, but if given must be numeric
            try {
                Double.parseDouble(moistureText);
            } catch (NumberFormatException e) {
                errors.append("Moisture % must be numeric.\n");
            }
        }

        String storageTemp = storageTempTextField.getText().trim();
        if (storageTemp.isEmpty()) {
            errors.append("Storage Temp cannot be empty.\n");
        } else {
            try {
                Double.parseDouble(storageTemp);
            } catch (NumberFormatException e) {
                errors.append("Storage Temp must be numeric.\n");
            }
        }

        if (colorScoreComboBox.getValue() == null) {
            errors.append("Color Score must be selected.\n");
        }

        if (odorEvaluationTextArea.getText().trim().isEmpty()) {
            errors.append("Odor Evaluation cannot be empty.\n");
        }
        if (foreignMatterCheckTextArea.getText().trim().isEmpty()) {
            errors.append("Foreign Matter Check cannot be empty.\n");
        }
        if (remarksTextArea.getText().trim().isEmpty()) {
            errors.append("Remarks cannot be empty.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Validation successful. You can evaluate and save.");
            return true;
        }
    }

    // ================== BINARY HELPERS ==================

    private void loadSavedRecordsFromFile() {
        File file = new File(MEAT_QUALITY_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<MeatQualityTestRecord> list =
                    (ArrayList<MeatQualityTestRecord>) ois.readObject();
            savedRecords.clear();
            savedRecords.addAll(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveRecordsToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(MEAT_QUALITY_FILE))) {
            oos.writeObject(savedRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToQaQueue(MeatQualityTestRecord record) {
        File file = new File(MEAT_QUALITY_QA_QUEUE_FILE);
        ArrayList<MeatQualityTestRecord> queue = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<MeatQualityTestRecord> loaded =
                        (ArrayList<MeatQualityTestRecord>) ois.readObject();
                queue.addAll(loaded);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        queue.add(record);

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(MEAT_QUALITY_QA_QUEUE_FILE))) {
            oos.writeObject(queue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
