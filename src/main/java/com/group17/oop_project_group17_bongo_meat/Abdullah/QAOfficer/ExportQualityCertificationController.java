package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer.MeatTestingRequest;
import com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.SlaughterOutputReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.openpdf.text.Document;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ExportQualityCertificationController {

    // ------------------ FXML CONTROLS ------------------
    @FXML private TableView<ExportBatch> exportTable;
    @FXML private TableColumn<ExportBatch, String> colBatchId;
    @FXML private TableColumn<ExportBatch, String> colType;
    @FXML private TableColumn<ExportBatch, String> colWeight;
    @FXML private TableColumn<ExportBatch, String> colQaStatus;
    @FXML private TableColumn<ExportBatch, String> colVetStatus;
    @FXML private TableColumn<ExportBatch, String> colExportStatus;

    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnShow;
    @FXML private Button btnPDF;
    @FXML private Label messageLabel;

    // ------------------ DATA ------------------
    private final ObservableList<ExportBatch> data = FXCollections.observableArrayList();

    private static final String SLAUGHTER_FILE = "outputSlaughter.dat";
    private static final String QA_FILE = "meatQualityTesting.dat";
    private static final String EXPORT_FILE = "exportCertified.dat";

    // ------------------ INITIALIZE ------------------
    @FXML
    public void initialize() {
        colBatchId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        colWeight.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getWeight()));
        //colSlaughterTime.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSlaughterTime()));
        colQaStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getQaStatus()));
        colVetStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getVetStatus()));
        colExportStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getExportStatus()));

        exportTable.setItems(data);
        messageLabel.setText("");
    }

    // ------------------ LOAD DATA ------------------
    @FXML
    private void handleShow(ActionEvent event) {
        data.clear();
        messageLabel.setText("");

        ArrayList<SlaughterOutputReport> slaughterList = new ArrayList<>();
        ArrayList<MeatTestingRequest> qaList = new ArrayList<>();
        ArrayList<ExportBatch> exportList = new ArrayList<>();

        // Load slaughterhouse data
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SLAUGHTER_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?> list) {
                for (Object o : list) if (o instanceof SlaughterOutputReport s) slaughterList.add(s);
            }
        } catch (Exception ignored) {}

        // Load QA data
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(QA_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?> list) {
                for (Object o : list) if (o instanceof MeatTestingRequest q) qaList.add(q);
            }
        } catch (Exception ignored) {}

        // Load previous export decisions
        File fExport = new File(EXPORT_FILE);
        if (fExport.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fExport))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?> list) {
                    for (Object o : list) if (o instanceof ExportBatch e) exportList.add(e);
                }
            } catch (Exception ignored) {}
        }

        // Merge data
        for (SlaughterOutputReport s : slaughterList) {
            String qaStatus = "Pending";
            for (MeatTestingRequest q : qaList) {
                if (q.getBatchId().equals(s.getBatchId())) {
                    qaStatus = q.getStatus();
                    break;
                }
            }

            String vetStatus = "Approved"; // Vet always approved
            String exportStatus = "Pending";
            for (ExportBatch e : exportList) {
                if (e.getBatchId().equals(s.getBatchId())) {
                    exportStatus = e.getExportStatus();
                    break;
                }
            }

            ExportBatch batch = new ExportBatch(
                    s.getBatchId(),
                    s.getType(),
                    s.getWeight(),
                    s.getSlaughterTime(),
                    qaStatus,
                    vetStatus,
                    exportStatus
            );
            data.add(batch);
        }

        exportTable.refresh();
        showAlert(Alert.AlertType.INFORMATION, "Loaded " + data.size() + " record(s).");
    }

    // ------------------ APPROVE ------------------
    @FXML
    private void handleApprove(ActionEvent event) {
        ExportBatch sel = exportTable.getSelectionModel().getSelectedItem();
        if (sel == null) { messageLabel.setText("Select a batch first."); return; }

        if (!"Test Complete (Approved)".equalsIgnoreCase(sel.getQaStatus())) {
            messageLabel.setText("Cannot approve: QA not approved yet.");
            return;
        }

        sel.setExportStatus("Approved");
        exportTable.refresh();
        saveExportDecision(sel);
        messageLabel.setText("Batch approved for export.");
    }

    // ------------------ REJECT ------------------
    @FXML
    private void handleReject(ActionEvent event) {
        ExportBatch sel = exportTable.getSelectionModel().getSelectedItem();
        if (sel == null) { messageLabel.setText("Select a batch first."); return; }

        sel.setExportStatus("Rejected");
        exportTable.refresh();
        saveExportDecision(sel);
        messageLabel.setText("Batch rejected for export.");
    }

    // ------------------ SAVE EXPORT DECISION ------------------
    private void saveExportDecision(ExportBatch batch) {
        ArrayList<ExportBatch> list = new ArrayList<>();

        File f = new File(EXPORT_FILE);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?> arr) for (Object o : arr) if (o instanceof ExportBatch e) list.add(e);
            } catch (Exception ignored) {}
        }

        list.removeIf(e -> e.getBatchId().equals(batch.getBatchId()));
        list.add(batch);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EXPORT_FILE))) {
            oos.writeObject(list);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to save export decisions.");
        }
    }

    // ------------------ GENERATE PDF ------------------
    @FXML
    private void generatePDF(ActionEvent event) {
        if (data.isEmpty()) {
            messageLabel.setText("No data to generate PDF!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Export Quality Certification Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(messageLabel.getScene().getWindow());
        if (file == null) { messageLabel.setText("Save cancelled."); return; }

        try {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, new FileOutputStream(file));
            pdf.open();
            pdf.add(new Paragraph("Export Quality Certification Report"));
            pdf.add(new Paragraph("Date: " + java.time.LocalDate.now()));
            pdf.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(7);
            table.addCell("Batch ID"); table.addCell("Type"); table.addCell("Weight"); table.addCell("Slaughter Time");
            table.addCell("QA Status"); table.addCell("Vet Status"); table.addCell("Export Status");

            for (ExportBatch batch : data) {
                table.addCell(batch.getBatchId()); table.addCell(batch.getType()); table.addCell(batch.getWeight());
                table.addCell(batch.getSlaughterTime()); table.addCell(batch.getQaStatus()); table.addCell(batch.getVetStatus());
                table.addCell(batch.getExportStatus());
            }

            pdf.add(table);
            pdf.close();

            messageLabel.setText("PDF saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error generating PDF!");
        }
    }

    // ------------------ BACK BUTTON ------------------
    @FXML
    public void handleBackButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", actionEvent);
    }

    // ------------------ HELPER ------------------
    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }
}

