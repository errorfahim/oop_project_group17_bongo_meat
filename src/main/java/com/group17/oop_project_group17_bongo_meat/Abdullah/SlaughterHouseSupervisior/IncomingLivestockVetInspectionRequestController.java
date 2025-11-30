package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

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

    @FXML
    public void initialize() {
        batchIdColumn.setCellValueFactory(data -> data.getValue().batchIdProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        statusColumn.setCellValueFactory(data -> data.getValue().vetRequestStatusProperty());

        batchTableView.setItems(requestList);
    }

    @FXML
    public void showButtonClicked(ActionEvent event) {
        File file = new File(LIVESTOCK_FILE);
        if (!file.exists()) {
            new Alert(Alert.AlertType.ERROR, "No livestock records found.").show();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<RecordIncomingLivestock> list =
                    (ArrayList<RecordIncomingLivestock>) ois.readObject();

            ArrayList<IncomingLivestockVetInspectionRequest> vetList = new ArrayList<>();
            for (RecordIncomingLivestock r : list) {
                IncomingLivestockVetInspectionRequest req = new IncomingLivestockVetInspectionRequest(
                        r.getBatchID(),
                        r.getType(),
                        r.getQuantity(),
                        "",            // default note empty
                        "Pending"      // default status
                );
                vetList.add(req);
            }

            requestList.setAll(vetList);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load livestock records.").show();
        }
    }

    @FXML
    public void sendVetRequest(ActionEvent event) {
        IncomingLivestockVetInspectionRequest selected =
                batchTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a batch first!").show();
            return;
        }

        String note = noteTextField.getText().trim();

        ArrayList<IncomingLivestockVetInspectionRequest> vetRequests = new ArrayList<>();

        try {
            // Load existing requests if file exists
            File file = new File(VET_REQUEST_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    vetRequests = (ArrayList<IncomingLivestockVetInspectionRequest>) ois.readObject();
                }
            }

            // Add new request
            IncomingLivestockVetInspectionRequest request = new IncomingLivestockVetInspectionRequest(
                    selected.getBatchId(),
                    selected.getType(),
                    selected.getQuantity(),
                    note,
                    "Pending"
            );

            vetRequests.add(request);

            // Save back to file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(VET_REQUEST_FILE))) {
                oos.writeObject(vetRequests);
            }

            new Alert(Alert.AlertType.INFORMATION, "Vet Request Sent!").show();
            noteTextField.clear();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
        }
    }

    @FXML
    public void backButton(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }
}
