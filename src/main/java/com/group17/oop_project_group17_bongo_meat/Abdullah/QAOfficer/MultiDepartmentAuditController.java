package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class MultiDepartmentAuditController {

    @FXML private DatePicker startDate, endDate;
    @FXML private CheckBox farmDept, slaughterDept, logisticsDept, deliveryDept;
    @FXML private ComboBox<String> auditTypeCombo;
    @FXML private TextArea observationArea;
    @FXML private Button generateReportBtn, submitReportBtn, backButton;
    @FXML private Label statusLabel;

    private static final String AUDIT_REPORT_FILE = "multiDepartmentAudits.dat";

    @FXML
    public void initialize() {
        // Add example audit types
        auditTypeCombo.getItems().addAll("Full Audit", "Random Sampling", "Compliance Check");
    }

    // ---------------- Handle Back ----------------
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
        } catch (IOException e) {
            statusLabel.setText("Error navigating back!");
            e.printStackTrace();
        }
    }

    // ---------------- Generate PDF Report ----------------
    @FXML
    private void handleGenerateReport(ActionEvent event) {
        if (!validateInputs()) return;

        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        List<String> selectedDepts = getSelectedDepartments();
        String auditType = auditTypeCombo.getValue();
        String observations = observationArea.getText().trim();

        try {
            // Save report object
            MultiDepartmentAudit report = new MultiDepartmentAudit(start, end, selectedDepts, auditType, observations, LocalDate.now());
            saveReport(report);

            // Generate PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Audit Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(generateReportBtn.getScene().getWindow());
            if (file == null) { statusLabel.setText("PDF save cancelled."); return; }

            Document pdf = new Document();
            PdfWriter.getInstance(pdf, new FileOutputStream(file));
            pdf.open();
            pdf.add(new Paragraph("Multi-Department Quality Audit Report"));
            pdf.add(new Paragraph("Submission Date: " + LocalDate.now()));
            pdf.add(new Paragraph("Audit Period: " + start + " to " + end));
            pdf.add(new Paragraph("Departments Audited: " + String.join(", ", selectedDepts)));
            pdf.add(new Paragraph("Audit Type: " + auditType));
            pdf.add(new Paragraph("Officer Observations:"));
            pdf.add(new Paragraph(observations));
            pdf.close();

            statusLabel.setText("Audit report generated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error generating PDF!");
        }
    }

    // ---------------- Submit Report ----------------
    @FXML
    private void handleSubmitReport(ActionEvent event) {
        if (!validateInputs()) return;
        statusLabel.setText("Report submitted to Admin successfully!");
    }

    // ---------------- Validation ----------------
    private boolean validateInputs() {
        if (startDate.getValue() == null || endDate.getValue() == null) {
            statusLabel.setText("Please select start and end dates.");
            return false;
        }
        if (getSelectedDepartments().isEmpty()) {
            statusLabel.setText("Select at least one department.");
            return false;
        }
        if (auditTypeCombo.getValue() == null || auditTypeCombo.getValue().isEmpty()) {
            statusLabel.setText("Select an audit type.");
            return false;
        }
        if (observationArea.getText().trim().isEmpty()) {
            statusLabel.setText("Please enter officer observations.");
            return false;
        }
        return true;
    }

    private List<String> getSelectedDepartments() {
        List<String> depts = new ArrayList<>();
        if (farmDept.isSelected()) depts.add("Farm");
        if (slaughterDept.isSelected()) depts.add("Slaughterhouse");
        if (logisticsDept.isSelected()) depts.add("Logistics");
        if (deliveryDept.isSelected()) depts.add("Delivery");
        return depts;
    }

    // ---------------- Save & Load ----------------
    private void saveReport(MultiDepartmentAudit report) {
        List<MultiDepartmentAudit> reports = loadReports();
        reports.add(report);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(AUDIT_REPORT_FILE))) {
            out.writeObject(reports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<MultiDepartmentAudit> loadReports() {
        File f = new File(AUDIT_REPORT_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<MultiDepartmentAudit>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
