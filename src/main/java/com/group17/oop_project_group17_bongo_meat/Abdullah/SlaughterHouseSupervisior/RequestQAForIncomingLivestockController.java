package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class RequestQAForIncomingLivestockController {

    @FXML private TableView<RequestQAForIncomingLivestock> batchTableView;
    @FXML private TableColumn<RequestQAForIncomingLivestock, String> batchIdColumn;
    @FXML private TableColumn<RequestQAForIncomingLivestock, String> typeColumn;
    @FXML private TableColumn<RequestQAForIncomingLivestock, Integer> quantityColumn;
    @FXML private TableColumn<RequestQAForIncomingLivestock, String> statusColumn;
    @FXML private TextField noteTextField;

    private ObservableList<RequestQAForIncomingLivestock> requestList = FXCollections.observableArrayList();

    private final String LIVESTOCK_FILE = "incomingLivestock.dat";
    private final String QA_REQUEST_FILE = "incomingQARequests.dat";

    @FXML
    public void initialize() {
        batchIdColumn.setCellValueFactory(data -> data.getValue().batchIdProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        statusColumn.setCellValueFactory(data -> data.getValue().qaRequestStatusProperty());

        batchTableView.setItems(requestList);
    }

    @FXML
    public void showButtonClicked(ActionEvent event) {
        ArrayList<RecordIncomingLivestock> livestockList;
        ArrayList<RequestQAForIncomingLivestock> qaList = new ArrayList<>();

        // Load livestock
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LIVESTOCK_FILE))) {
            livestockList = (ArrayList<RecordIncomingLivestock>) ois.readObject();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load livestock records.").show();
            return;
        }

        // Load existing QA decisions (APPROVED / REJECTED / Pending)
        try {
            File file = new File(QA_REQUEST_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    qaList = (ArrayList<RequestQAForIncomingLivestock>) ois.readObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Build final request list with correct statuses
        requestList.clear();

        for (RecordIncomingLivestock r : livestockList) {
            String status = "Pending";
            String note = "";

            // Check if QA already processed this batch
            for (RequestQAForIncomingLivestock qa : qaList) {
                if (qa.getBatchId().equals(r.getBatchID())) {
                    status = qa.getQaRequestStatus();
                    note = qa.getNote();
                    break;
                }
            }

            RequestQAForIncomingLivestock req = new RequestQAForIncomingLivestock(
                    r.getBatchID(),
                    r.getType(),
                    r.getQuantity(),
                    note,
                    status
            );

            requestList.add(req);
        }

        batchTableView.setItems(requestList);
        batchTableView.refresh();

        new Alert(Alert.AlertType.INFORMATION,
                "Loaded " + requestList.size() + " livestock record(s).").show();
    }


    @FXML
    public void sendQARequest(ActionEvent event) {
        RequestQAForIncomingLivestock selected =
                batchTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a batch first!").show();
            return;
        }

        String note = noteTextField.getText().trim();

        if (note.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a note for the QA request.").show();
            return;
        }

        ArrayList<RequestQAForIncomingLivestock> qaRequests = new ArrayList<>();

        try {
            File file = new File(QA_REQUEST_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    qaRequests = (ArrayList<RequestQAForIncomingLivestock>) ois.readObject();
                }
            }

            // Check if request already exists for this batch
            boolean alreadyExists = false;
            for (RequestQAForIncomingLivestock req : qaRequests) {
                if (req.getBatchId().equals(selected.getBatchId())) {
                    alreadyExists = true;
                    break;
                }
            }

            if (alreadyExists) {
                new Alert(Alert.AlertType.WARNING,
                        "QA request for batch " + selected.getBatchId() + " already exists.").show();
                return;
            }

            RequestQAForIncomingLivestock request = new RequestQAForIncomingLivestock(
                    selected.getBatchId(),
                    selected.getType(),
                    selected.getQuantity(),
                    note,
                    "Pending"
            );

            qaRequests.add(request);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(QA_REQUEST_FILE))) {
                oos.writeObject(qaRequests);
            }

            // Update the status in the table
            selected.setQaRequestStatus("Request Sent");
            batchTableView.refresh();

            new Alert(Alert.AlertType.INFORMATION,
                    "QA Request Sent for Batch " + selected.getBatchId() + "!").show();
            noteTextField.clear();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save QA request: " + e.getMessage()).show();
        }
    }

    @FXML
    public void backButton(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }

}