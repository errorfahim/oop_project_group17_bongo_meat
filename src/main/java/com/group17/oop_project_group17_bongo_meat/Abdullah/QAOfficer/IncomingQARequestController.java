package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class IncomingQARequestController {

    @FXML private TableView<IncomingQARequest> incomingTable;
    @FXML private TableColumn<IncomingQARequest, String> colBatchId;
    @FXML private TableColumn<IncomingQARequest, String> colType;
    @FXML private TableColumn<IncomingQARequest, Integer> colQuantity;
    @FXML private TableColumn<IncomingQARequest, String> colNote;
    @FXML private TableColumn<IncomingQARequest, String> colStatus;
    @FXML private TextArea txtReason;
    @FXML private Label lblSelectedBatch;

    private final ObservableList<IncomingQARequest> incomingList = FXCollections.observableArrayList();
    private final String INCOMING_FILE = "incomingQARequests.dat";

    @FXML
    public void initialize() {
        colBatchId.setCellValueFactory(c -> c.getValue().batchIdProperty());
        colType.setCellValueFactory(c -> c.getValue().typeProperty());
        colQuantity.setCellValueFactory(c -> c.getValue().quantityProperty().asObject());
        colNote.setCellValueFactory(c -> c.getValue().noteProperty());
        colStatus.setCellValueFactory(c -> c.getValue().qaRequestStatusProperty());

        // DO NOT load requests automatically
        incomingTable.setItems(incomingList);

        incomingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) lblSelectedBatch.setText(newV.getBatchId());
        });

        System.out.println("Initialization done. Table is ready. Press Show to load requests.");
    }


    // LOAD REQUESTS FROM FILE (with conversion and clearing)
    private void loadIncomingRequests() {
        incomingList.clear();  // Prevent duplicates
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INCOMING_FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) {
                ArrayList<?> temp = (ArrayList<?>) obj;
                for (Object item : temp) {
                    if (item instanceof IncomingQARequest) {
                        incomingList.add((IncomingQARequest) item);
                    } else if (item instanceof com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.RequestQAForIncomingLivestock) {
                        // Convert to IncomingQARequest
                        com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.RequestQAForIncomingLivestock oldReq =
                                (com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.RequestQAForIncomingLivestock) item;
                        IncomingQARequest newReq = new IncomingQARequest(
                                oldReq.getBatchId(),
                                oldReq.getType(),
                                oldReq.getQuantity(),
                                oldReq.getNote(),
                                oldReq.getQaRequestStatus()
                        );
                        incomingList.add(newReq);
                    }
                }
            }
            // Refresh JavaFX properties
            for (IncomingQARequest req : incomingList) {
                req.refreshProperties();
            }
            incomingTable.setItems(incomingList);
            System.out.println("Loaded " + incomingList.size() + " requests.");
        } catch (FileNotFoundException e) {
            System.out.println("No incoming requests file found. Table will be empty.");
            incomingTable.setItems(incomingList);
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            incomingTable.setItems(incomingList);
        }
    }

    // SAVE REQUESTS TO FILE (convert back to RequestQAForIncomingLivestock)
    private void saveIncomingRequests() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INCOMING_FILE))) {
            ArrayList<com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.RequestQAForIncomingLivestock> temp = new ArrayList<>();
            for (IncomingQARequest req : incomingList) {
                com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.RequestQAForIncomingLivestock convertedReq =
                        new com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior.RequestQAForIncomingLivestock(
                                req.getBatchId(),
                                req.getType(),
                                req.getQuantity(),
                                req.getNote(),
                                req.getQaRequestStatus()
                        );
                temp.add(convertedReq);
            }
            oos.writeObject(temp);
            System.out.println("Saved " + temp.size() + " requests (converted to Supervisor format).");
        } catch (Exception e) {
            System.err.println("Error saving requests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // SHOW BUTTON
    @FXML
    private void showRequests(ActionEvent event) {
        System.out.println("Show button pressed - reloading requests.");
        loadIncomingRequests();
    }

    // APPROVE BUTTON
    @FXML
    private void approve(ActionEvent event) {
        IncomingQARequest selected = incomingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Error", "Please select a request first!");
            return;
        }
        selected.setQaRequestStatus("APPROVED");
        selected.setNote(selected.getNote());  // Keep existing note
        saveIncomingRequests();
        incomingTable.refresh();
        alert("Success", "Request approved!");
    }

    // REJECT BUTTON
    @FXML
    private void reject(ActionEvent event) {
        IncomingQARequest selected = incomingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Error", "Please select a request first!");
            return;
        }
        String reason = txtReason.getText().trim();
        if (reason.isEmpty()) {
            alert("Error", "Please enter rejection reason.");
            return;
        }
        selected.setQaRequestStatus("REJECTED");
        selected.setNote(reason);
        saveIncomingRequests();
        incomingTable.refresh();
        alert("Success", "Request rejected!");
    }

    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    public void backButton(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
    }
}