package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class VetActivityReportController {

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label fetchSuccessMessageLabel;
    @FXML private Label reportGenerateMessageLabel;

    private static final String VET_ACTIVITY_FILE = "vetActivities.dat";

    private List<VetActivityRecord> allActivities = new ArrayList<>();
    private List<VetActivityRecord> filteredActivities = new ArrayList<>();

    @FXML
    public void initialize() {

        // Filter options (optional – user may leave empty)
        filterComboBox.getItems().setAll(
                "All",
                "Health Check",
                "Vaccination",
                "Pre-Slaughter Inspection",
                "Meat Quality Test",
                "Nutrition / Feeding",
                "Disease Alert"
        );

        fetchSuccessMessageLabel.setText("*Fetch Success Message*");
        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");

        // load / create activity data
        File file = new File(VET_ACTIVITY_FILE);
        if (file.exists()) {
            allActivities = loadActivitiesFromFile();
        } else {
            createDummyActivities();
            saveActivitiesToFile(allActivities);
        }
    }

    // -------------------------------------------------------------------------
    // FETCH DATA  (dates & filter are OPTIONAL now)
    // -------------------------------------------------------------------------

    @FXML
    public void fetchButtonOnAction(ActionEvent actionEvent) {

        reportGenerateMessageLabel.setText("*Report Generated Successfully! or Error*");

        if (allActivities.isEmpty()) {
            fetchSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            fetchSuccessMessageLabel.setText("No activity data available.");
            return;
        }

        LocalDate from = fromDatePicker.getValue();
        LocalDate to   = toDatePicker.getValue();
        String filter  = filterComboBox.getValue();

        // If filter not chosen, treat as "All"
        if (filter == null || filter.isBlank()) {
            filter = "All";
        }

        // If both dates empty → use full range
        if (from == null && to == null) {
            from = allActivities.stream()
                    .map(VetActivityRecord::getDate)
                    .min(LocalDate::compareTo)
                    .orElse(LocalDate.now());

            to = allActivities.stream()
                    .map(VetActivityRecord::getDate)
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.now());
        }
        // If only FROM is set → to = latest activity date
        else if (from != null && to == null) {
            to = allActivities.stream()
                    .map(VetActivityRecord::getDate)
                    .max(LocalDate::compareTo)
                    .orElse(from);
        }
        // If only TO is set → from = earliest activity date
        else if (from == null && to != null) {
            from = allActivities.stream()
                    .map(VetActivityRecord::getDate)
                    .min(LocalDate::compareTo)
                    .orElse(to);
        }

        // Final safety check
        if (to.isBefore(from)) {
            fetchSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            fetchSuccessMessageLabel.setText("Date range invalid: 'To Date' is before 'From Date'.");
            filteredActivities.clear();
            return;
        }

        // Apply filters
        filteredActivities.clear();
        for (VetActivityRecord r : allActivities) {
            LocalDate d = r.getDate();
            if ((d.isEqual(from) || d.isAfter(from)) &&
                    (d.isEqual(to)   || d.isBefore(to))) {

                if (!"All".equalsIgnoreCase(filter)) {
                    if (!r.getActivityType().equalsIgnoreCase(filter)) continue;
                }
                filteredActivities.add(r);
            }
        }

        if (filteredActivities.isEmpty()) {
            fetchSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            fetchSuccessMessageLabel.setText(
                    "No activities found between " + from + " and " + to +
                            " for '" + filter + "'.");
        } else {
            fetchSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            fetchSuccessMessageLabel.setText(
                    "Fetched " + filteredActivities.size() +
                            " records (" + filter + ") from " + from + " to " + to + ".");
        }
    }

    // -------------------------------------------------------------------------
    // EXPORT REPORT (PDF)  — using OpenPDF
    // -------------------------------------------------------------------------

    @FXML
    public void exportReportButtonOnAction(ActionEvent actionEvent) {

        if (filteredActivities.isEmpty()) {
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("No data to export. Click 'Fetch Data' first.");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Veterinary Activity Report");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = chooser.showSaveDialog(null);
        if (file == null) return;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Veterinary Activity Report"));
            document.add(new Paragraph("-----------------------------------"));
            document.add(new Paragraph("Total Records: " + filteredActivities.size()));
            document.add(new Paragraph(" "));

            for (VetActivityRecord r : filteredActivities) {
                document.add(new Paragraph(
                        "Animal ID: " + r.getAnimalId() +
                                " | Batch: " + r.getBatchId() +
                                " | Type: " + r.getAnimalType()));
                document.add(new Paragraph(
                        "Activity: " + r.getActivityType() +
                                " | Date: " + r.getDate() +
                                " | Vet ID: " + r.getVetId()));
                document.add(new Paragraph(
                        "Remarks: " + r.getRemarks()));
                document.add(new Paragraph(" "));
            }

            document.close();

            reportGenerateMessageLabel.setStyle("-fx-text-fill: green;");
            reportGenerateMessageLabel.setText("Report exported successfully!");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            reportGenerateMessageLabel.setStyle("-fx-text-fill: red;");
            reportGenerateMessageLabel.setText("Error exporting report.");
        }
    }

    // -------------------------------------------------------------------------
    // RETURN BUTTON
    // -------------------------------------------------------------------------

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) {
        try {
            switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/veterinaryOfficerDashboard.fxml",
                    actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // DUMMY DATA + FILE HELPERS
    // -------------------------------------------------------------------------

    private void createDummyActivities() {
        allActivities = new ArrayList<>();

        allActivities.add(new VetActivityRecord(
                "A-001", "Batch001", "Cattle",
                "Health Check",
                LocalDate.now().minusDays(10),
                "VET-101",
                "Routine health check – all normal."
        ));

        allActivities.add(new VetActivityRecord(
                "A-010", "Batch002", "Goat",
                "Vaccination",
                LocalDate.now().minusDays(7),
                "VET-101",
                "PPR vaccine dose 1 administered."
        ));

        allActivities.add(new VetActivityRecord(
                "A-020", "Batch001", "Cattle",
                "Pre-Slaughter Inspection",
                LocalDate.now().minusDays(5),
                "VET-102",
                "Batch approved for slaughter."
        ));

        allActivities.add(new VetActivityRecord(
                "A-030", "Batch003", "Cattle",
                "Meat Quality Test",
                LocalDate.now().minusDays(3),
                "VET-103",
                "Lab results within acceptable range."
        ));

        allActivities.add(new VetActivityRecord(
                "A-015", "Batch004", "Sheep",
                "Nutrition / Feeding",
                LocalDate.now().minusDays(2),
                "VET-101",
                "Adjusted feed quantity based on weight."
        ));

        allActivities.add(new VetActivityRecord(
                "A-040", "Batch005", "Cattle",
                "Disease Alert",
                LocalDate.now().minusDays(1),
                "VET-104",
                "Mild respiratory symptoms in 5 animals – under observation."
        ));
    }

    private List<VetActivityRecord> loadActivitiesFromFile() {
        List<VetActivityRecord> list = new ArrayList<>();
        File file = new File(VET_ACTIVITY_FILE);
        if (!file.exists()) return list;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            //noinspection unchecked
            list = (List<VetActivityRecord>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void saveActivitiesToFile(List<VetActivityRecord> list) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(VET_ACTIVITY_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
