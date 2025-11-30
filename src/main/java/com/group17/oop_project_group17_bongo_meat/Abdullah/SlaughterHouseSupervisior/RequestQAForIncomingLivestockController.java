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
    private Label outputLabel;

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
        File file = new File(LIVESTOCK_FILE);
        if (!file.exists()) {
            new Alert(Alert.AlertType.ERROR, "No livestock records found.").show();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<RecordIncomingLivestock> list =
                    (ArrayList<RecordIncomingLivestock>) ois.readObject();

            ArrayList<RequestQAForIncomingLivestock> qaList = new ArrayList<>();
            for (RecordIncomingLivestock r : list) {
                RequestQAForIncomingLivestock req = new RequestQAForIncomingLivestock(
                        r.getBatchID(),
                        r.getType(),
                        r.getQuantity(),
                        "",           // default note empty
                        "Pending"     // default status
                );
                qaList.add(req);
            }

            requestList.setAll(qaList);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load livestock records.").show();
        }
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

        ArrayList<RequestQAForIncomingLivestock> qaRequests = new ArrayList<>();

        try {
            File file = new File(QA_REQUEST_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    qaRequests = (ArrayList<RequestQAForIncomingLivestock>) ois.readObject();
                }
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

            new Alert(Alert.AlertType.INFORMATION, "QA Request Sent!").show();
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
