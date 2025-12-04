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
import java.time.LocalDate;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class NutritionFeedingRecordController {

    // --------- FXML NODES ---------

    @FXML private TableView<NutritionFeedingRecord> animalListTableView;
    @FXML private TableColumn<NutritionFeedingRecord, String> animalIDCol;
    @FXML private TableColumn<NutritionFeedingRecord, String> typeCol;
    @FXML private TableColumn<NutritionFeedingRecord, Number> weightCol;
    @FXML private TableColumn<NutritionFeedingRecord, String> feedTypeCol;
    @FXML private TableColumn<NutritionFeedingRecord, Number> quantityCol;
    @FXML private TableColumn<NutritionFeedingRecord, String> feedingTimeCol;
    @FXML private TableColumn<NutritionFeedingRecord, String> statusCol;

    @FXML private ComboBox<String> feedTypeComboBox;
    @FXML private ComboBox<String> quantityComboBox;
    @FXML private DatePicker feedingDatePicker;
    @FXML private TextArea noteTextArea;

    @FXML private Label selectedBatchMessageLabel;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private Label recordSaveMessageLabel;
    @FXML private Label reportGenerateMessageLabel;
    @FXML private Label feedingScheduleSuccessMessageLabel;

    // --------- DATA ---------

    private final ObservableList<NutritionFeedingRecord> nutritionList =
            FXCollections.observableArrayList();

    private static final String NUTRITION_FILE = "nutritionScheduleRecords.dat";
    private static final String NUTRITION_QUEUE_FILE = "nutritionScheduleQueue.dat";

    private NutritionFeedingRecord selectedRecord;
    // last schedule that was saved/assigned – used for PDF
    private NutritionFeedingRecord lastSavedRecord;

    @FXML
    public void initialize() {

        // Bind columns
        animalIDCol.setCellValueFactory(data -> data.getValue().animalIdProperty());
        typeCol.setCellValueFactory(data -> data.getValue().typeProperty());
        weightCol.setCellValueFactory(data -> data.getValue().weightProperty());
        feedTypeCol.setCellValueFactory(data -> data.getValue().feedTypeProperty());
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty());
        feedingTimeCol.setCellValueFactory(data -> data.getValue().feedingTimeProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        animalListTableView.setItems(nutritionList);

        // initial messages
        selectedBatchMessageLabel.setText("*Selected Animal Batch for Nutrition Schedule*");
        validationSuccessMessageLabel.setText("*Success/Error messages*");
        recordSaveMessageLabel.setText("*Assigned feeding schedule record saved*");
        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");
        feedingScheduleSuccessMessageLabel.setText("*Feeding Schedule Assigned and Approved Successfully!*");

        // dummy feed types and quantities
        feedTypeComboBox.getItems().setAll(
                "Grass / Hay",
                "Concentrate Feed",
                "Silage",
                "Grain Mix",
                "Mineral Supplement"
        );

        quantityComboBox.getItems().setAll("1", "2", "3", "5", "10", "15", "20");

        // row selection → label update
        animalListTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> {
                    selectedRecord = newSel;
                    if (newSel != null) {
                        selectedBatchMessageLabel.setText(
                                "Selected -> ID: " + newSel.getAnimalId() +
                                        " | Type: " + newSel.getType() +
                                        " | Weight: " + newSel.getWeight() +
                                        " kg | Status: " + newSel.getStatus()
                        );
                    } else {
                        selectedBatchMessageLabel.setText("*Selected Animal Batch for Nutrition Schedule*");
                    }
                });

        // DO NOT load data yet; only after button click
        nutritionList.clear();
    }

    // --------------- LOAD BUTTON ---------------

    @FXML
    public void loadLivestockBatchButtonOnAction(ActionEvent actionEvent) {
        File file = new File(NUTRITION_FILE);

        if (file.exists()) {
            // load existing schedules
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<NutritionFeedingRecord> list =
                        (ArrayList<NutritionFeedingRecord>) ois.readObject();
                nutritionList.setAll(list);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Failed to load nutrition records from file.").show();
                return;
            }
        } else {
            // dummy initial data
            nutritionList.clear();
            nutritionList.add(new NutritionFeedingRecord(
                    "A-101", "Cattle", 450.0,
                    "Grass / Hay", 5, "Not Scheduled", "Pending", ""));
            nutritionList.add(new NutritionFeedingRecord(
                    "B-220", "Goat", 55.0,
                    "Concentrate Feed", 2, "Not Scheduled", "Pending", ""));
            nutritionList.add(new NutritionFeedingRecord(
                    "C-330", "Sheep", 70.0,
                    "Grass / Hay", 3, "Not Scheduled", "Pending", ""));
            nutritionList.add(new NutritionFeedingRecord(
                    "D-450", "Cattle", 500.0,
                    "Silage", 6, "Not Scheduled", "Pending", ""));
            saveNutritionToFile();
        }

        animalListTableView.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nutrition Batches Loaded");
        alert.setHeaderText("Livestock batches loaded for scheduling nutrition.");
        alert.setContentText("Total batches: " + nutritionList.size());
        alert.show();

        validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel.setText("Batches loaded successfully.");
    }

    // --------------- VALIDATION ---------------

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateForm();
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (selectedRecord == null) {
            errors.append("Select an animal batch from the table.\n");
        }

        if (feedTypeComboBox.getValue() == null) {
            errors.append("Feed Type must be selected.\n");
        }

        String qtyStr = quantityComboBox.getValue();
        if (qtyStr == null || qtyStr.trim().isEmpty()) {
            errors.append("Quantity must be selected.\n");
        } else {
            try {
                int q = Integer.parseInt(qtyStr.trim());
                if (q <= 0) {
                    errors.append("Quantity must be a positive integer.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Quantity must be numeric.\n");
            }
        }

        LocalDate date = feedingDatePicker.getValue();
        if (date == null) {
            errors.append("Feeding Time / Date must be selected.\n");
        } else if (date.isBefore(LocalDate.now())) {
            errors.append("Feeding date cannot be in the past.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Validation successful. You can save and assign.");
            return true;
        }
    }

    // --------------- SAVE & ASSIGN ---------------

    @FXML
    public void saveANdAssignButtonOnAction(ActionEvent actionEvent) {
        if (!validateForm()) return;

        String feedType = feedTypeComboBox.getValue();
        int qty = Integer.parseInt(quantityComboBox.getValue().trim());
        LocalDate date = feedingDatePicker.getValue();
        String notes = noteTextArea.getText().trim();

        selectedRecord.setFeedType(feedType);
        selectedRecord.setQuantity(qty);
        selectedRecord.setFeedingTime(date.toString());
        selectedRecord.setNotes(notes);
        selectedRecord.setStatus("Scheduled");

        // remember last saved record for PDF
        lastSavedRecord = selectedRecord;

        animalListTableView.refresh();
        saveNutritionToFile();

        recordSaveMessageLabel.setStyle("-fx-text-fill: green;");
        recordSaveMessageLabel.setText("Feeding schedule saved for animal ID: " +
                selectedRecord.getAnimalId());

        feedingScheduleSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        feedingScheduleSuccessMessageLabel.setText("Feeding Schedule Assigned and Approved Successfully!");
    }

    // --------------- GENERATE REPORT (PDF) ---------------

    @FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
        if (lastSavedRecord == null) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Save a feeding schedule before generating report.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Feeding Schedule Report");
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

            NutritionFeedingRecord r = lastSavedRecord;

            // Title
            document.add(new Paragraph("Feeding Schedule Report"));
            document.add(new Paragraph(" "));

            // 2-column table with key/value pairs
            Table table = new Table(2);

            table.addCell("Animal ID");
            table.addCell(r.getAnimalId());

            table.addCell("Type");
            table.addCell(r.getType());

            table.addCell("Weight (kg)");
            table.addCell(String.valueOf(r.getWeight()));

            table.addCell("Feed Type");
            table.addCell(r.getFeedType());

            table.addCell("Quantity");
            table.addCell(String.valueOf(r.getQuantity()));

            table.addCell("Feeding Time");
            table.addCell(r.getFeedingTime());

            table.addCell("Status");
            table.addCell(r.getStatus());

            table.addCell("Notes");
            table.addCell(r.getNotes() == null ? "" : r.getNotes());

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now()));
            document.add(new Paragraph("Generated by: Veterinary System"));

            reportGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            reportGenerateMessageLabel.setText("Feeding Schedule Report Generated Successfully!");

        } catch (DocumentException | IOException e) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Error generating report.");
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    // --------------- SUBMIT TO FARM MANAGER ---------------

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        if (selectedRecord == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Select a scheduled feeding record to submit.").show();
            return;
        }

        if (!"Scheduled".equalsIgnoreCase(selectedRecord.getStatus())) {
            new Alert(Alert.AlertType.WARNING,
                    "Only scheduled records can be submitted.").show();
            return;
        }

        ArrayList<NutritionFeedingRecord> queue = new ArrayList<>();
        File file = new File(NUTRITION_QUEUE_FILE);

        try {
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    queue = (ArrayList<NutritionFeedingRecord>) ois.readObject();
                }
            }

            queue.add(selectedRecord);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(queue);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Feeding Schedule Submitted");
            alert.setHeaderText("Notification sent to Farm Manager.");
            alert.setContentText("Feeding schedule for Animal ID "
                    + selectedRecord.getAnimalId() + " submitted.");
            alert.show();

            feedingScheduleSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            feedingScheduleSuccessMessageLabel.setText(
                    "Feeding schedule submitted to Farm Manager.");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Error while submitting feeding schedule.").show();
        }
    }

    // --------------- RETURN BUTTON ---------------

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/veterinaryOfficerDashboard.fxml",
                actionEvent);
    }

    // --------------- FILE HELPERS ---------------

    private void saveNutritionToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(NUTRITION_FILE))) {
            oos.writeObject(new ArrayList<>(nutritionList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
