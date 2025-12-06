package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class QAScheduleCheckController {

    @FXML private TableView<CheckQARequestInSchedule> tableSchedule;

    @FXML private TableColumn<CheckQARequestInSchedule, String> colBatchId;
    @FXML private TableColumn<CheckQARequestInSchedule, String> colStatus;
    @FXML private TableColumn<CheckQARequestInSchedule, String> colProcessingArea;
    @FXML private TableColumn<CheckQARequestInSchedule, String> colRequiredTest;
    @FXML private TableColumn<CheckQARequestInSchedule, String> colPriority;
    @FXML private TableColumn<CheckQARequestInSchedule, String> colSubmissionDate;

    @FXML private TextField txtProcessingArea, txtRequiredTest, txtPriority;
    @FXML private TextArea txtEvaluationNotes;

    @FXML private Button btnShow, btnApprove, btnReject;
    @FXML private Label lblMessage;

    private final ObservableList<CheckQARequestInSchedule> data = FXCollections.observableArrayList();

    private static final String QA_SCHEDULE_FILE = "qaScheduledRequests.dat";
    private static final String EXPORT_FILE = "exportCertified.dat";

    @FXML
    public void initialize() {

        colBatchId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
        colProcessingArea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProcessingArea()));
        colRequiredTest.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRequiredTest()));
        colPriority.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPriority()));
        colSubmissionDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSubmissionDate().toString()));

        tableSchedule.setItems(data);

        data.addAll(loadRequests());
    }

    // ---------------- SHOW ---------------------
    @FXML
    private void handleShow(ActionEvent e) {
        ArrayList<ExportBatch> list = loadExportCertified();
        if (list.isEmpty()) {
            lblMessage.setText("No export-certified batches found!");
            return;
        }

        ExportBatch b = list.get(0);

        CheckQARequestInSchedule req =
                new CheckQARequestInSchedule(b.getBatchId(), "Request for Test");

        data.add(req);
        saveRequests();

        lblMessage.setText("Batch loaded for QA testing.");
    }

    // ---------------- APPROVE ---------------------
    @FXML
    private void handleApprove(ActionEvent event) {

        CheckQARequestInSchedule selected = tableSchedule.getSelectionModel().getSelectedItem();
        if (selected == null) { lblMessage.setText("Select an item!"); return; }

        if (txtProcessingArea.getText().isEmpty() ||
                txtRequiredTest.getText().isEmpty() ||
                txtPriority.getText().isEmpty() ||
                txtEvaluationNotes.getText().isEmpty()) {

            lblMessage.setText("Fill all fields!");
            return;
        }

        selected.setProcessingArea(txtProcessingArea.getText());
        selected.setRequiredTest(txtRequiredTest.getText());
        selected.setPriority(txtPriority.getText());
        selected.setNotes(txtEvaluationNotes.getText());
        selected.setStatus("Sent to Lab");

        // Make full columns visible
        colProcessingArea.setVisible(true);
        colRequiredTest.setVisible(true);
        colPriority.setVisible(true);
        colSubmissionDate.setVisible(true);

        tableSchedule.refresh();
        saveRequests();

        lblMessage.setText("Sent to lab.");
    }

    // ---------------- REJECT ---------------------
    @FXML
    private void handleReject(ActionEvent event) {
        CheckQARequestInSchedule selected = tableSchedule.getSelectionModel().getSelectedItem();
        if (selected == null) { lblMessage.setText("Select an item!"); return; }

        selected.setStatus("Rejected");
        tableSchedule.refresh();
        saveRequests();

        lblMessage.setText("Request rejected.");
    }

    private ArrayList<CheckQARequestInSchedule> loadRequests() {
        File f = new File(QA_SCHEDULE_FILE);
        if (!f.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (ArrayList<CheckQARequestInSchedule>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<ExportBatch> loadExportCertified() {
        File f = new File(EXPORT_FILE);
        if (!f.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (ArrayList<ExportBatch>) in.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void saveRequests() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(QA_SCHEDULE_FILE))) {
            out.writeObject(new ArrayList<>(data));
        } catch (Exception ignored) {}
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", actionEvent);
    }
}

