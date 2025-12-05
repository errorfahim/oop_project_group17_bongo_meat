package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer.PreSlaughterInspectionRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class IncomingLivestockVetInspectionRequestController {

    @FXML private TableView<IncomingLivestockVetInspectionRequest> batchTableView;
    @FXML private TableColumn<IncomingLivestockVetInspectionRequest, String> batchIdColumn;
    @FXML private TableColumn<IncomingLivestockVetInspectionRequest, String> typeColumn;
    @FXML private TableColumn<IncomingLivestockVetInspectionRequest, Integer> quantityColumn;
    @FXML private TableColumn<IncomingLivestockVetInspectionRequest, String> statusColumn;
    @FXML private TextField noteTextField;

    private ObservableList<IncomingLivestockVetInspectionRequest> requestList = FXCollections.observableArrayList();

    private final String LIVESTOCK_FILE = "incomingLivestock.dat";
    private final String VET_REQUEST_FILE = "incomingVetRequests.dat";
    private final String VET_APPROVED_FILE = "preSlaughterInspectionRecords.dat";

    @FXML
    public void initialize() {
        batchIdColumn.setCellValueFactory(data -> data.getValue().batchIdProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        statusColumn.setCellValueFactory(data -> data.getValue().vetRequestStatusProperty());

        batchTableView.setItems(requestList);
    }

    // ---------------- SHOW LIVESTOCK AND STATUS ----------------
    @FXML
    public void showButtonClicked(ActionEvent event) {
        requestList.clear();

        ArrayList<String> approvedBatchIds = new ArrayList<>();

        // Load vet-approved records if exists
        File vetApprovedFile = new File(VET_APPROVED_FILE);
        if (vetApprovedFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(vetApprovedFile))) {
                ArrayList<PreSlaughterInspectionRecord> approvedList =
                        (ArrayList<PreSlaughterInspectionRecord>) ois.readObject();
                for (PreSlaughterInspectionRecord rec : approvedList) {
                    approvedBatchIds.add(rec.getBatchId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Load incoming livestock
        File livestockFile = new File(LIVESTOCK_FILE);
        if (!livestockFile.exists()) {
            new Alert(Alert.AlertType.ERROR, "No livestock records found.").show();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(livestockFile))) {
            ArrayList<RecordIncomingLivestock> list =
                    (ArrayList<RecordIncomingLivestock>) ois.readObject();

            ArrayList<IncomingLivestockVetInspectionRequest> vetList = new ArrayList<>();
            for (RecordIncomingLivestock r : list) {
                String status = "Pending";

                // Check if vet already sent request
                File vetRequestFile = new File(VET_REQUEST_FILE);
                if (vetRequestFile.exists()) {
                    try (ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(vetRequestFile))) {
                        ArrayList<IncomingLivestockVetInspectionRequest> existingRequests =
                                (ArrayList<IncomingLivestockVetInspectionRequest>) ois2.readObject();
                        for (IncomingLivestockVetInspectionRequest req : existingRequests) {
                            if (req.getBatchId().equals(r.getBatchID())) {
                                status = req.getVetRequestStatus();
                                break;
                            }
                        }
                    } catch (Exception ignored) {}
                }

                // Override status if approved by vet
                if (approvedBatchIds.contains(r.getBatchID())) {
                    status = "Approved";
                }

                IncomingLivestockVetInspectionRequest req = new IncomingLivestockVetInspectionRequest(
                        r.getBatchID(),
                        r.getType(),
                        r.getQuantity(),
                        "",  // note
                        status
                );
                vetList.add(req);
            }

            requestList.setAll(vetList);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load livestock records.").show();
        }
    }

    // ---------------- SEND VET REQUEST ----------------
    @FXML
    public void sendVetRequest(ActionEvent event) {
        IncomingLivestockVetInspectionRequest selected =
                batchTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a batch first!").show();
            return;
        }

        if (selected.getVetRequestStatus().equals("Approved")) {
            new Alert(Alert.AlertType.INFORMATION, "This batch is already approved!").show();
            return;
        }

        String note = noteTextField.getText().trim();

        ArrayList<IncomingLivestockVetInspectionRequest> vetRequests = new ArrayList<>();

        try {
            // Load existing requests
            File file = new File(VET_REQUEST_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    vetRequests = (ArrayList<IncomingLivestockVetInspectionRequest>) ois.readObject();
                }
            }

            // Add/update request
            boolean updated = false;
            for (IncomingLivestockVetInspectionRequest req : vetRequests) {
                if (req.getBatchId().equals(selected.getBatchId())) {
                    req.setNote(note);
                    req.setVetRequestStatus("Request Sent");
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                IncomingLivestockVetInspectionRequest newReq = new IncomingLivestockVetInspectionRequest(
                        selected.getBatchId(),
                        selected.getType(),
                        selected.getQuantity(),
                        note,
                        "Request Sent"
                );
                vetRequests.add(newReq);
            }

            // Save back to file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VET_REQUEST_FILE))) {
                oos.writeObject(vetRequests);
            }

            new Alert(Alert.AlertType.INFORMATION, "Vet Request Sent!").show();
            noteTextField.clear();
            showButtonClicked(new ActionEvent()); // refresh table

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    // ---------------- BACK BUTTON ----------------
    @FXML
    public void backButton(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }
}
