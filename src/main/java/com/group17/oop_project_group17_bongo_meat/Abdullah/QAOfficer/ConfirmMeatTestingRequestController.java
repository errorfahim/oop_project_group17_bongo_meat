package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.MeatTransferRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ConfirmMeatTestingRequestController {

    @FXML private TableView<MeatTestingRequest> requestTable;

    @FXML private TableColumn<MeatTestingRequest, String> colBatchId;
    @FXML private TableColumn<MeatTestingRequest, String> colWeight;
    @FXML private TableColumn<MeatTestingRequest, String> colType;

    @FXML private TableColumn<MeatTestingRequest, String> colPH;
    @FXML private TableColumn<MeatTestingRequest, String> colColor;
    @FXML private TableColumn<MeatTestingRequest, String> colMicrobial;
    @FXML private TableColumn<MeatTestingRequest, String> colMoisture;
    @FXML private TableColumn<MeatTestingRequest, String> colOdor;
    @FXML private TableColumn<MeatTestingRequest, String> colTemp;
    @FXML private TableColumn<MeatTestingRequest, String> colRemarks;
    @FXML private TableColumn<MeatTestingRequest, String> colStatus;

    @FXML private TextField tfPH;
    @FXML private TextField tfColor;
    @FXML private TextField tfMicrobial;
    @FXML private TextField tfMoisture;
    @FXML private TextField tfOdor;
    @FXML private TextField tfTemp;
    @FXML private TextField tfRemarks;
    @FXML private Label lblMessage;

    private final ObservableList<MeatTestingRequest> data = FXCollections.observableArrayList();

    private static final String TRANSFER_FILE = "transferRequest.dat";
    private static final String QA_OUTPUT_FILE = "meatQualityTesting.dat";

    @FXML
    public void initialize() {

        // BASIC COLUMNS
        colBatchId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        //colSlaughterTime.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSlaughterTime()));
        colWeight.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getWeight()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        // QA COLUMNS
        colPH.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(displayInt(c.getValue().getPh())));
        colColor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(displayInt(c.getValue().getColorScore())));
        colMicrobial.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(displayInt(c.getValue().getMicrobialCount())));
        colMoisture.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(displayInt(c.getValue().getMoisture())));
        colOdor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(emptyIfNull(c.getValue().getOdor())));
        colTemp.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(displayInt(c.getValue().getMeatTemp())));
        colRemarks.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(emptyIfNull(c.getValue().getRemarks())));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(emptyIfNull(c.getValue().getStatus())));

        requestTable.setItems(data);

        // CLICK ROW → LOAD INPUT FIELDS
        requestTable.getSelectionModel().selectedItemProperty().addListener((obs, oldv, newv) -> {
            if (newv != null) populateInputs(newv);
        });

        lblMessage.setText("");
    }

    // Display -1 as "Invalid"
    private String displayInt(int v) { return v < 0 ? "Invalid" : String.valueOf(v); }
    private String emptyIfNull(String s) { return (s == null ? "" : s); }

    // -------------------------------------------------------
    // SHOW BUTTON: Load transferRequests.dat
    // -------------------------------------------------------
    @FXML
    private void handleShow(ActionEvent event) {
        data.clear();
        lblMessage.setText("");

        File f = new File(TRANSFER_FILE);
        if (!f.exists()) {
            showAlert(Alert.AlertType.INFORMATION, "No transferRequests.dat found.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (!(obj instanceof ArrayList<?> list)) {
                showAlert(Alert.AlertType.ERROR, "Invalid file format.");
                return;
            }

            for (Object o : list) {
                if (o instanceof MeatTransferRequest mt) {

                    String deliveryTime = mt.getSlaughterTime() == null ? "" : String.valueOf(mt.getSlaughterTime());
                    String weight = mt.getWeight() == null ? "" : String.valueOf(mt.getWeight());
                    String type = mt.getType() == null ? "" : mt.getType();
                    String status = mt.getStatus() == null ? "Request Sent" : mt.getStatus();

                    MeatTestingRequest r = new MeatTestingRequest(
                            mt.getBatchId(),
                            deliveryTime,
                            weight,
                            type,
                            status
                    );

                    data.add(r);
                }
            }


            requestTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Loaded " + data.size() + " record(s).");

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Failed to load transferRequests.dat");
        }
    }

    // -------------------------------------------------------
    // APPROVE BUTTON
    // -------------------------------------------------------
    @FXML
    private void handleApprove(ActionEvent event) {
        MeatTestingRequest sel = requestTable.getSelectionModel().getSelectedItem();
        if (sel == null) { lblMessage.setText("Select a record first."); return; }

        sel.setStatus("Delivered");
        requestTable.refresh();

        updateTransferFileStatus(sel.getBatchId(), "Delivered");
        lblMessage.setText("Approved. Status updated.");
    }

    // -------------------------------------------------------
    // REJECT BUTTON
    // -------------------------------------------------------
    @FXML
    private void handleReject(ActionEvent event) {
        MeatTestingRequest sel = requestTable.getSelectionModel().getSelectedItem();
        if (sel == null) { lblMessage.setText("Select a record first."); return; }

        sel.setStatus("Rejected");
        requestTable.refresh();

        updateTransferFileStatus(sel.getBatchId(), "Rejected");

        lblMessage.setText("Rejected. Status updated.");
    }

    // -------------------------------------------------------
    // SAVE BUTTON -> Save QA Results
    // -------------------------------------------------------
    @FXML
    private void handleSave(ActionEvent event) {
        MeatTestingRequest sel = requestTable.getSelectionModel().getSelectedItem();
        if (sel == null) { lblMessage.setText("Select a record first."); return; }

        Integer ph = parseInt(tfPH.getText(), "pH"); if (ph == null) return;
        Integer color = parseInt(tfColor.getText(), "Color Score"); if (color == null) return;
        Integer microbial = parseInt(tfMicrobial.getText(), "Microbial Count"); if (microbial == null) return;
        Integer moisture = parseInt(tfMoisture.getText(), "Moisture"); if (moisture == null) return;
        Integer temp = parseInt(tfTemp.getText(), "Meat Temp"); if (temp == null) return;

        sel.setPh(ph);
        sel.setColorScore(color);
        sel.setMicrobialCount(microbial);
        sel.setMoisture(moisture);
        sel.setMeatTemp(temp);

        sel.setOdor(tfOdor.getText().trim());
        sel.setRemarks(tfRemarks.getText().trim());

        if ("Rejected".equalsIgnoreCase(sel.getStatus())) {
            sel.setStatus("Test Complete (Rejected)");
        } else {
            sel.setStatus("Test Complete (Approved)");
        }

        requestTable.refresh();

        saveQARecord(sel);
        lblMessage.setText("QA data saved.");
        clearInputs();
    }

    // Validate integer input
    private Integer parseInt(String text, String fieldName) {
        try { return Integer.parseInt(text.trim()); }
        catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, fieldName + " must be an integer.");
            return null;
        }
    }

    // -------------------------------------------------------
    // Save QA results to meatQualityTesting.dat
    // -------------------------------------------------------
    private void saveQARecord(MeatTestingRequest r) {
        ArrayList<MeatTestingRequest> list = new ArrayList<>();
        File f = new File(QA_OUTPUT_FILE);

        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?> arr) {
                    for (Object o : arr) if (o instanceof MeatTestingRequest m) list.add(m);
                }
            } catch (Exception ignored) {}
        }

        list.add(r);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(list);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed to save QA file.");
        }
    }

    // -------------------------------------------------------
    // Update transferRequests.dat
    // -------------------------------------------------------
    private void updateTransferFileStatus(String batchId, String status) {
        File f = new File(TRANSFER_FILE);
        if (!f.exists()) return;

        try {
            ArrayList<Object> list;

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                list = (ArrayList<Object>) ois.readObject();
            }

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof MeatTransferRequest mt) {
                    if (mt.getBatchId().equals(batchId)) {
                        mt.setStatus(status);
                        list.set(i, mt);
                    }
                }
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
                oos.writeObject(list);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Failed updating transferRequests.dat");
        }
    }

    // -------------------------------------------------------
    // Helper Methods
    // -------------------------------------------------------
    private void populateInputs(MeatTestingRequest r) {
        tfPH.setText(r.getPh() < 0 ? "" : String.valueOf(r.getPh()));
        tfColor.setText(r.getColorScore() < 0 ? "" : String.valueOf(r.getColorScore()));
        tfMicrobial.setText(r.getMicrobialCount() < 0 ? "" : String.valueOf(r.getMicrobialCount()));
        tfMoisture.setText(r.getMoisture() < 0 ? "" : String.valueOf(r.getMoisture()));
        tfOdor.setText(emptyIfNull(r.getOdor()));
        tfTemp.setText(r.getMeatTemp() < 0 ? "" : String.valueOf(r.getMeatTemp()));
        tfRemarks.setText(emptyIfNull(r.getRemarks()));
    }

    private void clearInputs() {
        tfPH.clear();
        tfColor.clear();
        tfMicrobial.clear();
        tfMoisture.clear();
        tfOdor.clear();
        tfTemp.clear();
        tfRemarks.clear();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg).show();
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
    }
}
