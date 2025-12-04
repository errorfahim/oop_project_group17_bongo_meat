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

public class SlaughterDailyReportController {

    @FXML private TableView<SlaughterDailyReport> reportTableView;

    @FXML private TableColumn<SlaughterDailyReport, String> batchIDColumn;
    @FXML private TableColumn<SlaughterDailyReport, String> typeColumn;
    @FXML private TableColumn<SlaughterDailyReport, String> slaughterTimeColumn;
    @FXML private TableColumn<SlaughterDailyReport, Integer> quantityColumn;
    @FXML private TableColumn<SlaughterDailyReport, Integer> remainingColumn;
    @FXML private TableColumn<SlaughterDailyReport, String> staffColumn;
    @FXML private TableColumn<SlaughterDailyReport, String> statusColumn;

    @FXML private Label outputLabel;
    @FXML private DatePicker dailyReportDatePicker;

    private final ObservableList<SlaughterDailyReport> data = FXCollections.observableArrayList();

    private static final String ASSIGNED_FILE = "assignedLivestock.dat";

    // =============================================================
    // INITIALIZE
    // =============================================================
    @FXML
    public void initialize() {

        batchIDColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        typeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        slaughterTimeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSlaughterTime()));
        quantityColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        remainingColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getRemaining()).asObject());
        staffColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStaff()));
        statusColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        reportTableView.setItems(data);
    }

    // =============================================================
    // LOAD DATA FROM assignedLivestock.dat
    // =============================================================
    @FXML
    private void handleRefresh() {

        data.clear();
        outputLabel.setText("");

        File file = new File(ASSIGNED_FILE);

        if (!file.exists()) {
            outputLabel.setText("No assigned livestock file found.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            Object obj = ois.readObject();

            if (obj instanceof ArrayList<?>) {

                for (Object o : (ArrayList<?>) obj) {
                    if (o instanceof AssignLivestockForSlaughter rec) {

                        SlaughterDailyReport report = new SlaughterDailyReport(
                                rec.getBatchId(),
                                rec.getType(),
                                rec.getAssignedTime(),
                                rec.getTotalQuantity(),
                                rec.getRemainingQuantity(),
                                rec.getStaffName(),
                                "Awaiting For delivery"
                        );

                        data.add(report);
                    }
                }
            }

            outputLabel.setText("Loaded " + data.size() + " record(s).");

        } catch (Exception e) {
            e.printStackTrace();
            outputLabel.setText("Error loading data!");
        }
    }

    // =============================================================
    // PDF GENERATION
    // =============================================================
    @FXML
    private void handleGeneratePDF(ActionEvent event) {

        if (data.isEmpty()) {
            outputLabel.setText("No data to generate PDF!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Slaughter Daily Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(outputLabel.getScene().getWindow());

        if (file == null) {
            outputLabel.setText("Save cancelled.");
            return;
        }

        try {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, new FileOutputStream(file));
            pdf.open();

            pdf.add(new Paragraph("Slaughter Daily Report"));
            pdf.add(new Paragraph("Date: " + LocalDate.now()));
            pdf.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(7);
            table.addCell("Batch ID");
            table.addCell("Type");
            table.addCell("Time");
            table.addCell("Quantity");
            table.addCell("Remaining");
            table.addCell("Staff");
            table.addCell("Status");

            for (SlaughterDailyReport r : data) {
                table.addCell(r.getBatchId());
                table.addCell(r.getType());
                table.addCell(r.getSlaughterTime());
                table.addCell(String.valueOf(r.getQuantity()));
                table.addCell(String.valueOf(r.getRemaining()));
                table.addCell(r.getStaff());
                table.addCell(r.getStatus());
            }

            pdf.add(table);
            pdf.close();
            outputLabel.setText("PDF saved to: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            outputLabel.setText("Error generating PDF!");
        }
    }
    // =============================================================
    // BACK BUTTON
    // =============================================================
    @FXML
    public void backButton(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }
}
