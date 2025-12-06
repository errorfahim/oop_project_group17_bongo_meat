package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Table;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class SlaughterOutputReportController {

    @FXML private TableView<SlaughterOutputReport> slaughterTable;

    @FXML private TableColumn<SlaughterOutputReport, String> batchIDColumn;
    @FXML private TableColumn<SlaughterOutputReport, String> animalTypeColumn;
    @FXML private TableColumn<SlaughterOutputReport, String> staffColumn;
    @FXML private TableColumn<SlaughterOutputReport, String> assignTimeColumn;
    @FXML private TableColumn<SlaughterOutputReport, Integer> quantityColumn;
    @FXML private TableColumn<SlaughterOutputReport, Integer> remainingColumn;
    @FXML private TableColumn<SlaughterOutputReport, String> weightColumn;
    @FXML private TableColumn<SlaughterOutputReport, String> wastesColumn;
    @FXML private TableColumn<SlaughterOutputReport, String> statusColumn;

    @FXML private TextField weightTextField;
    @FXML private CheckBox wastesYesCheckBox;
    @FXML private CheckBox wastesNoCheckBox;
    @FXML private TextField observationTextField;
    @FXML private Label messageLabel;

    private final ObservableList<SlaughterOutputReport> data = FXCollections.observableArrayList();

    private static final String ASSIGNED_FILE = "assignedLivestock.dat";
    private static final String OUTPUT_FILE = "outputSlaughter.dat";

    // ===================== INITIALIZE =====================
    @FXML
    public void initialize() {
        batchIDColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        animalTypeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        staffColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStaff()));
        assignTimeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getObservation()));
        quantityColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        remainingColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getRemaining()).asObject());
        weightColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getWeight()));
        wastesColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getWastes()));
        statusColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        slaughterTable.setItems(data);
    }

    // ===================== SHOW ASSIGNED DATA =====================
    @FXML
    private void handleShow(ActionEvent event) {
        data.clear();
        messageLabel.setText("");

        File file = new File(ASSIGNED_FILE);
        if (!file.exists()) {
            messageLabel.setText("No assigned livestock file found.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?>) {
                for (Object o : (ArrayList<?>) obj) {
                    if (o instanceof AssignLivestockForSlaughter rec) {
                        SlaughterOutputReport report = new SlaughterOutputReport(
                                rec.getBatchId(),
                                rec.getType(),
                                rec.getTotalQuantity(),
                                rec.getRemainingQuantity(),
                                "", // Weight empty initially
                                "", // Wastes empty initially
                                rec.getStaffName(),
                                "Awaiting for Deliveries",
                                "" // Observation empty initially
                        );
                        data.add(report);
                    }
                }
            }
            messageLabel.setText("Loaded " + data.size() + " record(s).");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading data!");
        }
    }

    // ===================== SAVE RECORD =====================
    @FXML
    private void saveRecord(ActionEvent event) {
        SlaughterOutputReport selected = slaughterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a record to update.");
            return;
        }

        // Update weight
        String weight = weightTextField.getText().trim();
        if (!weight.isEmpty()) selected.setWeight(weight);

        // Update wastes
        if (wastesYesCheckBox.isSelected()) selected.setWastes("Yes");
        else if (wastesNoCheckBox.isSelected()) selected.setWastes("No");

        // Update status
        selected.setStatus("Awaiting for Deliveries");

        // Add observation
        selected.setObservation(observationTextField.getText());

        // Refresh table to show updates immediately
        slaughterTable.refresh();

        // Save all data to outputSlaughter.dat
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(new ArrayList<>(data));
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Successfully saving record!");
            return;
        }

        // Clear inputs
        weightTextField.clear();
        wastesYesCheckBox.setSelected(false);
        wastesNoCheckBox.setSelected(false);
        observationTextField.clear();

        messageLabel.setText("Successfully saved the record!");
    }

    // ===================== GENERATE PDF =====================
    @FXML
    private void generatePDF(ActionEvent event) {

        if (data.isEmpty()) {
            messageLabel.setText("No data to generate PDF!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Slaughter Output Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(messageLabel.getScene().getWindow());

        if (file == null) {
            messageLabel.setText("Save cancelled.");
            return;
        }

        try {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, new FileOutputStream(file));
            pdf.open();

            pdf.add(new Paragraph("Slaughter Output Report"));
            pdf.add(new Paragraph("Date: " + LocalDate.now()));
            pdf.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(9); // 9 columns: batchID, type, quantity, remaining, weight, wastes, staff, status, observation
            table.addCell("Batch ID");
            table.addCell("Type");
            table.addCell("Quantity");
            table.addCell("Remaining");
            table.addCell("Weight");
            table.addCell("Wastes");
            table.addCell("Staff");
            table.addCell("Status");
            table.addCell("Observation");

            for (SlaughterOutputReport r : data) {
                table.addCell(r.getBatchId());
                table.addCell(r.getType());
                table.addCell(String.valueOf(r.getQuantity()));
                table.addCell(String.valueOf(r.getRemaining()));
                table.addCell(r.getWeight());
                table.addCell(r.getWastes());
                table.addCell(r.getStaff());
                table.addCell(r.getStatus());
                table.addCell(r.getObservation());
            }

            pdf.add(table);
            pdf.close();

            messageLabel.setText("PDF saved to: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error generating PDF!");
        }
    }

    // ===================== BACK BUTTON =====================
    @FXML
    public void backButton(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }
}

