package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class RequestLogisticForTransferMeatController {

    // TableView and Columns
    @FXML private TableView<LogisticMeatRequest> transferTable;
    @FXML private TableColumn<LogisticMeatRequest, String> colBatch;
    @FXML private TableColumn<LogisticMeatRequest, String> colType;
    @FXML private TableColumn<LogisticMeatRequest, String> colWeight;
    @FXML private TableColumn<LogisticMeatRequest, String> colSlaughter;
    @FXML private TableColumn<LogisticMeatRequest, String> colQA;
    @FXML private TableColumn<LogisticMeatRequest, String> colVet;
    @FXML private TableColumn<LogisticMeatRequest, String> colExport;
    @FXML private TableColumn<LogisticMeatRequest, String> colStatus;
    @FXML private TableColumn<LogisticMeatRequest, String> colDate;

    // Form Inputs
    @FXML private TextField txtBatchId, txtType, txtWeight;
    @FXML private DatePicker dpRequestDate;
    @FXML private Label lblMessage;
    @FXML private Button btnBack;

    private final ObservableList<LogisticMeatRequest> data = FXCollections.observableArrayList();

    private static final String LOGISTIC_REQUEST_FILE = "logisticTransferRequests.dat";
    private static final String EXPORT_FILE = "exportCertified.dat";

    @FXML
    public void initialize() {
        colBatch.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        colWeight.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getWeight())));
        colSlaughter.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSlaughterTime()));
        colQA.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getQaStatus()));
        colVet.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getVetStatus()));
        colExport.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getExportStatus()));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRequestStatus()));
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getRequestDate() == null ? "" : c.getValue().getRequestDate().toString()
        ));

        transferTable.setItems(data);
        lblMessage.setText("");
    }

    // ---------------- Show all export + previous logistic requests ----------------
    @FXML
    private void handleShow(ActionEvent event) {
        data.clear();

        ArrayList<ExportBatch> exportList = loadExportCertified();
        ArrayList<LogisticMeatRequest> previousRequests = loadLogisticRequests();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (ExportBatch e : exportList) {

            LogisticMeatRequest existing = previousRequests.stream()
                    .filter(r -> r.getBatchId().equals(e.getBatchId()))
                    .findFirst()
                    .orElse(null);

            double weightValue = 0.0;
            try { weightValue = Double.parseDouble(e.getWeight()); }
            catch (Exception ignored) {}

            String slaughterTime = existing != null
                    ? existing.getSlaughterTime()
                    : LocalDateTime.now().format(formatter);

            LogisticMeatRequest merged;

            if (existing != null) {
                merged = existing;   // <-- THIS PRESERVES "Request Sent"
            } else {
                merged = new LogisticMeatRequest(
                        e.getBatchId(),
                        e.getType(),
                        weightValue,
                        slaughterTime,
                        e.getQaStatus(),
                        e.getVetStatus(),
                        e.getExportStatus(),
                        "Pending",
                        null
                );
            }

            data.add(merged);
        }

        transferTable.refresh();
        lblMessage.setText("Loaded " + data.size() + " records.");
    }

    // ---------------- Save new request ----------------
    @FXML
    private void handleSave(ActionEvent event) {
        if (txtBatchId.getText().isEmpty() || txtType.getText().isEmpty() || txtWeight.getText().isEmpty() || dpRequestDate.getValue() == null) {
            lblMessage.setText("Please fill all fields and select request date.");
            return;
        }

        double weightValue;
        try { weightValue = Double.parseDouble(txtWeight.getText()); }
        catch (NumberFormatException e) { lblMessage.setText("Invalid weight."); return; }

        String slaughterTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        LogisticMeatRequest req = new LogisticMeatRequest(
                txtBatchId.getText(),
                txtType.getText(),
                weightValue,
                slaughterTime,
                "QA Pending",
                "Vet Pending",
                "Export Pending",
                "Pending",
                dpRequestDate.getValue()
        );

        data.add(req);
        saveLogisticRequest(req);
        transferTable.refresh();
        lblMessage.setText("Saved new request.");
    }

    // ---------------- Send request ----------------
    @FXML
    private void handleSend(ActionEvent event) {
        LogisticMeatRequest selected = transferTable.getSelectionModel().getSelectedItem();
        if (selected == null) { lblMessage.setText("Select a batch first!"); return; }
        if (dpRequestDate.getValue() == null) { lblMessage.setText("Select request date!"); return; }

        selected.setRequestStatus("Request Sent");
        selected.setRequestDate(dpRequestDate.getValue());

        saveLogisticRequest(selected);
        transferTable.refresh();
        lblMessage.setText("Request sent to Logistics.");
    }

    // ---------------- Back ----------------
    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
    }

    // ---------------- File operations ----------------
    private ArrayList<ExportBatch> loadExportCertified() {
        ArrayList<ExportBatch> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXPORT_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?> arr)
                for (Object o : arr)
                    if (o instanceof ExportBatch e)
                        list.add(e);
        } catch (Exception ignored) {}
        return list;
    }

    private ArrayList<LogisticMeatRequest> loadLogisticRequests() {
        ArrayList<LogisticMeatRequest> list = new ArrayList<>();
        File f = new File(LOGISTIC_REQUEST_FILE);
        if (!f.exists()) return list;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList<?> arr)
                for (Object o : arr) if (o instanceof LogisticMeatRequest lr) list.add(lr);
        } catch (Exception ignored) {}
        return list;
    }

    private void saveLogisticRequest(LogisticMeatRequest req) {
        ArrayList<LogisticMeatRequest> list = loadLogisticRequests();
        list.removeIf(r -> r.getBatchId().equals(req.getBatchId()));
        list.add(req);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(LOGISTIC_REQUEST_FILE))) {
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Saving error!");
        }
    }
}
