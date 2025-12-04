package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
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

public class VaccinationScheduleRecordController {

    // --------- FXML NODES ----------

    @FXML private TableView<VaccinationRecord> batchForVaccinationShowingTableView;
    @FXML private TableColumn<VaccinationRecord, String> batchIDCol;
    @FXML private TableColumn<VaccinationRecord, String> speciesCol;
    @FXML private TableColumn<VaccinationRecord, Integer> ageCol;
    @FXML private TableColumn<VaccinationRecord, String> requiredVaccineCol;
    @FXML private TableColumn<VaccinationRecord, String> dueDateCol1;
    @FXML private TableColumn<VaccinationRecord, String> previousDoseDateCol1;
    @FXML private TableColumn<VaccinationRecord, String> statusCol;

    @FXML private ComboBox<String> vaccineTypeComboBox;
    @FXML private TextField vaccineBatchNoTextField;
    @FXML private TextArea doseTextArea;
    @FXML private ComboBox<String> administrationRouteComboBox;
    @FXML private DatePicker vaccinationDatePicker;
    @FXML private TextArea notesTextArea;

    @FXML private Label recordSaveMessageLabel;
    @FXML private Label vaccinationRecordSavedMessageLabel;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private Label selectedBatchMessageLabel;

    // --------- DATA STRUCTURES ----------

    private final ObservableList<VaccinationRecord> scheduleList =
            FXCollections.observableArrayList();

    // all saved vaccination records (for binary persistence)
    private final ArrayList<VaccinationRecord> savedRecords = new ArrayList<>();

    // selected batch from table
    private VaccinationRecord selectedBatch;

    // last saved record – required for report / submit
    private VaccinationRecord lastSavedRecord;

    // binary file names
    private static final String VACC_RECORD_FILE = "vaccinationRecords.dat";
    private static final String VACC_SUPERVISOR_QUEUE_FILE = "vaccinationSupervisorQueue.dat";

    @FXML
    public void initialize() {

        // TableView column bindings
        batchIDCol.setCellValueFactory(data -> data.getValue().batchIDProperty());
        speciesCol.setCellValueFactory(data -> data.getValue().speciesProperty());
        ageCol.setCellValueFactory(data -> data.getValue().ageProperty().asObject());
        requiredVaccineCol.setCellValueFactory(data -> data.getValue().requiredVaccineProperty());
        dueDateCol1.setCellValueFactory(data -> data.getValue().dueDateProperty());
        previousDoseDateCol1.setCellValueFactory(data -> data.getValue().previousDoseDateProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        batchForVaccinationShowingTableView.setItems(scheduleList);

        // combo options
        vaccineTypeComboBox.getItems().setAll("FMD", "Rabies", "Brucellosis", "Anthrax");
        administrationRouteComboBox.getItems().setAll("IM", "SC", "Oral");

        // label defaults
        selectedBatchMessageLabel.setText("*Selected Batch for Vaccination*");
        recordSaveMessageLabel.setText("* Record Saved Successfully!*");
        vaccinationRecordSavedMessageLabel.setText("*Vaccination Record Saved Successfully!*");
        validationSuccessMessageLabel.setText("*Validity for correctness message*");

        // row selection listener
        batchForVaccinationShowingTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedBatch = newSel;
                    if (newSel != null) {
                        selectedBatchMessageLabel.setText(
                                "Selected -> Batch " + newSel.getBatchID() +
                                        " | Species: " + newSel.getSpecies() +
                                        " | Age: " + newSel.getAge() +
                                        " | Required Vaccine: " + newSel.getRequiredVaccine() +
                                        " | Status: " + newSel.getStatus()
                        );
                    } else {
                        selectedBatchMessageLabel.setText("*Selected Batch for Vaccination*");
                    }
                });

        // load already-saved records from binary (if any)
        loadSavedRecordsFromFile();
    }

    // ===================== BUTTON HANDLERS =====================

    @FXML
    public void loadVaccinationListButtonOnAction(ActionEvent actionEvent) {
        // dummy vaccination schedule data
        scheduleList.clear();

        scheduleList.add(new VaccinationRecord(
                "B201", "Cattle", 2, "FMD", "2025-12-10", "2025-06-10", "Due",
                "", "", "", "", "", ""));
        scheduleList.add(new VaccinationRecord(
                "B202", "Goat", 1, "PPR", "2025-12-12", "2025-06-12", "Pending",
                "", "", "", "", "", ""));
        scheduleList.add(new VaccinationRecord(
                "B203", "Sheep", 3, "Rabies", "2025-12-15", "2025-06-15", "Due",
                "", "", "", "", "", ""));
    }

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateForm();
    }

    @FXML
    public void recordVaccinationButtonOnAction(ActionEvent actionEvent) {
        // must pass validation first
        if (!validateForm()) {
            return;
        }

        if (selectedBatch == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Please select a batch from the table first.");
            return;
        }

        // build a full vaccination record from selected batch + form fields
        String batchID = selectedBatch.getBatchID();
        String species = selectedBatch.getSpecies();
        int age = selectedBatch.getAge();
        String requiredVaccine = selectedBatch.getRequiredVaccine();
        String dueDate = selectedBatch.getDueDate();
        String prevDoseDate = selectedBatch.getPreviousDoseDate();
        String status = "Completed"; // vaccination done

        String vaccineType = vaccineTypeComboBox.getValue();
        String vaccineBatchNo = vaccineBatchNoTextField.getText().trim();
        String dose = doseTextArea.getText().trim();
        String route = administrationRouteComboBox.getValue();
        LocalDate vDate = vaccinationDatePicker.getValue();
        String vaccinationDate = (vDate != null) ? vDate.toString() : "";
        String notes = notesTextArea.getText().trim();

        VaccinationRecord newRecord = new VaccinationRecord(
                batchID,
                species,
                age,
                requiredVaccine,
                dueDate,
                prevDoseDate,
                status,
                vaccineType,
                vaccineBatchNo,
                dose,
                route,
                vaccinationDate,
                notes
        );

        // store in memory & file
        savedRecords.add(newRecord);
        lastSavedRecord = newRecord;
        saveRecordsToFile();

        // optional: also add to table as “history”
        scheduleList.add(newRecord);

        recordSaveMessageLabel.setStyle("-fx-text-fill: green;");
        recordSaveMessageLabel.setText("Record saved successfully for Batch " + batchID + ".");
    }

    @FXML
    public void generateReportButtonOnAction(ActionEvent actionEvent) {
        if (lastSavedRecord == null) {
            recordSaveMessageLabel.setStyle("-fx-text-fill: red;");
            recordSaveMessageLabel.setText("Record a vaccination before generating report.");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Vaccination Medical Report");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File chosenFile = fileChooser.showSaveDialog(null);

            if (chosenFile == null) {
                return;
            }

            // OpenPDF (org.openpdf.text.*) style
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(chosenFile));
            document.open();

            document.add(new Paragraph("Vaccination Medical Report"));
            document.add(new Paragraph("--------------------------------------"));
            document.add(new Paragraph("Batch ID          : " + lastSavedRecord.getBatchID()));
            document.add(new Paragraph("Species           : " + lastSavedRecord.getSpecies()));
            document.add(new Paragraph("Age               : " + lastSavedRecord.getAge()));
            document.add(new Paragraph("Required Vaccine  : " + lastSavedRecord.getRequiredVaccine()));
            document.add(new Paragraph("Due Date          : " + lastSavedRecord.getDueDate()));
            document.add(new Paragraph("Previous Dose     : " + lastSavedRecord.getPreviousDoseDate()));
            document.add(new Paragraph("Status            : " + lastSavedRecord.getStatus()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Vaccine Type      : " + lastSavedRecord.getVaccineType()));
            document.add(new Paragraph("Vaccine Batch No. : " + lastSavedRecord.getVaccineBatchNo()));
            document.add(new Paragraph("Dose              : " + lastSavedRecord.getDose()));
            document.add(new Paragraph("Admin Route       : " + lastSavedRecord.getAdministrationRoute()));
            document.add(new Paragraph("Vaccination Date  : " + lastSavedRecord.getVaccinationDate()));
            document.add(new Paragraph("Notes             : " + lastSavedRecord.getNotes()));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Generated on      : " + java.time.LocalDateTime.now()));
            document.add(new Paragraph("Generated by      : Veterinary System"));

            document.close();

            recordSaveMessageLabel.setStyle("-fx-text-fill: green;");
            recordSaveMessageLabel.setText("Medical report generated successfully.");

        } catch (DocumentException | IOException e) {
            recordSaveMessageLabel.setStyle("-fx-text-fill: red;");
            recordSaveMessageLabel.setText("Error generating report.");
            e.printStackTrace();
        }
    }

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        // must have a saved record
        if (lastSavedRecord == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please validate and record a vaccination before submitting.").show();
            return;
        }

        addToSupervisorQueue(lastSavedRecord);

        vaccinationRecordSavedMessageLabel.setStyle("-fx-text-fill: green;");
        vaccinationRecordSavedMessageLabel.setText("Vaccination record saved & sent to Farm Manager.");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vaccination Submitted");
        alert.setHeaderText("Notification sent to Farm Manager");
        alert.setContentText("Vaccination record for Batch " +
                lastSavedRecord.getBatchID() +
                " has been added to medical history and sent to the Farm Manager.");
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

        // 1. Batch must be selected
        if (selectedBatch == null) {
            errors.append("Please select a batch from the vaccination list.\n");
        }

        // 2. Vaccine Type
        if (vaccineTypeComboBox.getSelectionModel().getSelectedItem() == null) {
            errors.append("Vaccine Type must be selected.\n");
        }

        // 3. Vaccine Batch Number (INTEGER)
        String batchNoText = vaccineBatchNoTextField.getText().trim();
        if (batchNoText.isEmpty()) {
            errors.append("Vaccine Batch No cannot be empty.\n");
        } else {
            try {
                Integer.parseInt(batchNoText);
            } catch (NumberFormatException e) {
                errors.append("Vaccine Batch No must be a numeric integer value.\n");
            }
        }

        // 4. Dose (INTEGER)
        String doseText = doseTextArea.getText().trim();
        if (doseText.isEmpty()) {
            errors.append("Dose field cannot be empty.\n");
        } else {
            try {
                Integer.parseInt(doseText);
            } catch (NumberFormatException e) {
                errors.append("Dose must be a numeric integer value.\n");
            }
        }

        // 5. Administration Route
        if (administrationRouteComboBox.getSelectionModel().getSelectedItem() == null) {
            errors.append("Administration Route must be selected.\n");
        }

        // 6. Vaccination Date
        if (vaccinationDatePicker.getValue() == null) {
            errors.append("Vaccination Date must be chosen.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Validation successful. You can record the vaccination.");
            return true;
        }
    }

    // ===================== BINARY HELPERS =====================

    private void loadSavedRecordsFromFile() {
        File file = new File(VACC_RECORD_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<VaccinationRecord> list = (ArrayList<VaccinationRecord>) ois.readObject();
            savedRecords.clear();
            savedRecords.addAll(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveRecordsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VACC_RECORD_FILE))) {
            oos.writeObject(savedRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToSupervisorQueue(VaccinationRecord record) {
        File file = new File(VACC_SUPERVISOR_QUEUE_FILE);
        ArrayList<VaccinationRecord> queue = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                ArrayList<VaccinationRecord> loaded = (ArrayList<VaccinationRecord>) ois.readObject();
                queue.addAll(loaded);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        queue.add(record);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VACC_SUPERVISOR_QUEUE_FILE))) {
            oos.writeObject(queue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
