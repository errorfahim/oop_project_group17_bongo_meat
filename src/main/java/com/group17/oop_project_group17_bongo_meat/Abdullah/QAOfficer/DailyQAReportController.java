package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class DailyQAReportController {

    // ---------------- FXML Controls ----------------
    @FXML private Button btnBack, btnGenerateReport, btnDownloadPDF;
    @FXML private CheckBox cbMeatQuality, cbLivestockCondition, cbTraceability, cbWasteManagement, cbExportCert, cbPackaging;
    @FXML private TextField txtNotes;
    @FXML private TableView<DailyQAReport> tableReports;
    @FXML private TableColumn<DailyQAReport, String> colBatchId, colType, colWeight,
            colMeatQuality, colLivestockCondition, colTraceability, colWasteManagement,
            colExportCert, colPackaging, colReportDate;
    @FXML private Label lblMessage;

    // ---------------- Data ----------------
    private final ObservableList<DailyQAReport> data = FXCollections.observableArrayList();
    private static final String EXPORT_FILE = "exportCertified.dat";
    private static final String QA_REPORT_FILE = "dailyQAReports.dat";

    // ---------------- Initialize ----------------
    @FXML
    public void initialize() {
        // Set up TableView columns
        colBatchId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        colWeight.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getWeight())));
        colMeatQuality.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isMeatQualityCheck() ? "✔" : "✘"));
        colLivestockCondition.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isLivestockConditionCheck() ? "✔" : "✘"));
        colTraceability.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isTraceabilityCheck() ? "✔" : "✘"));
        colWasteManagement.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isWasteManagementCheck() ? "✔" : "✘"));
        colExportCert.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isExportCertStatus() ? "✔" : "✘"));
        colPackaging.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isPackagingCheck() ? "✔" : "✘"));
        colReportDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getReportDate().toString()));

        tableReports.setItems(data);

        // Load previously saved reports
        data.addAll(loadReports());
    }

    // ---------------- Back Button ----------------
    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
    }

    // ---------------- Generate Report ----------------
    @FXML
    private void handleGenerateReport(ActionEvent event) {
        try {
            // Validation: At least one checkbox must be selected
            if (!cbMeatQuality.isSelected() && !cbLivestockCondition.isSelected() &&
                    !cbTraceability.isSelected() && !cbWasteManagement.isSelected() &&
                    !cbExportCert.isSelected() && !cbPackaging.isSelected()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validation Error");
                alert.setHeaderText("No QA Check Selected");
                alert.setContentText("Please select at least one QA check before generating the report.");
                alert.showAndWait();
                return;
            }

            // Load batches from exportCertified.dat
            ArrayList<ExportBatch> exportList = loadExportCertified();
            if (exportList.isEmpty()) {
                lblMessage.setText("No batches found!");
                return;
            }

            ExportBatch batch = exportList.get(0); // Example: use first batch
            double weightValue;
            try {
                weightValue = Double.parseDouble(batch.getWeight());
            } catch (NumberFormatException e) {
                lblMessage.setText("Invalid weight format in batch data!");
                return;
            }

            // Create Daily QA Report object
            DailyQAReport report = new DailyQAReport(
                    batch.getBatchId(),
                    batch.getType(),
                    weightValue,
                    cbMeatQuality.isSelected(),
                    cbLivestockCondition.isSelected(),
                    cbTraceability.isSelected(),
                    cbWasteManagement.isSelected(),
                    cbExportCert.isSelected(),
                    cbPackaging.isSelected(),
                    txtNotes.getText(),
                    LocalDate.now()
            );

            // Add to TableView and save
            data.add(report);
            saveReport(report);

            lblMessage.setText("Report generated successfully!");

            // Clear inputs
            cbMeatQuality.setSelected(false);
            cbLivestockCondition.setSelected(false);
            cbTraceability.setSelected(false);
            cbWasteManagement.setSelected(false);
            cbExportCert.setSelected(false);
            cbPackaging.setSelected(false);
            txtNotes.clear();

        } catch (Exception e) {
            lblMessage.setText("Error generating report!");
            e.printStackTrace();
        }
    }

    // ---------------- Download PDF ----------------
    @FXML
    private void handleDownloadPDF(ActionEvent event) {
        if (data.isEmpty()) {
            lblMessage.setText("No data to generate PDF!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Daily QA Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(lblMessage.getScene().getWindow());
        if (file == null) {
            lblMessage.setText("Save cancelled.");
            return;
        }

        try {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, new FileOutputStream(file));
            pdf.open();
            pdf.add(new Paragraph("Daily QA Report"));
            pdf.add(new Paragraph("Date: " + LocalDate.now()));
            pdf.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(11);
            table.addCell("Batch ID"); table.addCell("Type"); table.addCell("Weight");
            table.addCell("Meat Quality"); table.addCell("Livestock"); table.addCell("Traceability");
            table.addCell("Waste Mgmt"); table.addCell("Export Cert"); table.addCell("Packaging");
            table.addCell("Notes"); table.addCell("Report Date");

            for (DailyQAReport report : data) {
                table.addCell(report.getBatchId());
                table.addCell(report.getType());
                table.addCell(String.valueOf(report.getWeight()));
                table.addCell(report.isMeatQualityCheck() ? "✔" : "✘");
                table.addCell(report.isLivestockConditionCheck() ? "✔" : "✘");
                table.addCell(report.isTraceabilityCheck() ? "✔" : "✘");
                table.addCell(report.isWasteManagementCheck() ? "✔" : "✘");
                table.addCell(report.isExportCertStatus() ? "✔" : "✘");
                table.addCell(report.isPackagingCheck() ? "✔" : "✘");
                table.addCell(report.getNotes() != null ? report.getNotes() : "");
                table.addCell(report.getReportDate().toString());
            }

            pdf.add(table);
            pdf.close();

            lblMessage.setText("PDF saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            lblMessage.setText("Error generating PDF!");
            e.printStackTrace();
        }
    }

    // ---------------- File Operations ----------------
    private ArrayList<ExportBatch> loadExportCertified() {
        ArrayList<ExportBatch> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXPORT_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?> arr)
                for (Object o : arr)
                    if (o instanceof ExportBatch e) list.add(e);
        } catch (Exception ignored) {}
        return list;
    }

    private void saveReport(DailyQAReport report) {
        ArrayList<DailyQAReport> list = loadReports();
        list.add(report);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(QA_REPORT_FILE))) {
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DailyQAReport> loadReports() {
        ArrayList<DailyQAReport> list = new ArrayList<>();
        File f = new File(QA_REPORT_FILE);
        if (!f.exists()) return list;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?> arr)
                for (Object o : arr)
                    if (o instanceof DailyQAReport r) list.add(r);
        } catch (Exception ignored) {}
        return list;
    }
}

