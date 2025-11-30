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

    @FXML private TableView<IncomingQARequest> tableView;
    @FXML private TableColumn<IncomingQARequest, String> batchIdColumn;
    @FXML private TableColumn<IncomingQARequest, String> typeColumn;
    @FXML private TableColumn<IncomingQARequest, Integer> quantityColumn;
    @FXML private TableColumn<IncomingQARequest, String> noteColumn;
    @FXML private TableColumn<IncomingQARequest, String> statusColumn;

    private ObservableList<IncomingQARequest> requestList = FXCollections.observableArrayList();

    private final String QA_REQUEST_FILE = "incomingQARequests.dat";
    private final String QA_RESULT_FILE = "qaRequestResults.dat";

    @FXML
    public void initialize() {
        // Setup TableView columns
        batchIdColumn.setCellValueFactory(data -> data.getValue().batchIdProperty());
        typeColumn.setCellValueFactory(data -> data.getValue().typeProperty());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        noteColumn.setCellValueFactory(data -> data.getValue().noteProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        tableView.setItems(requestList);
    }

    // Load QA requests from incoming file
    private void loadRequests() {
        File file = new File(QA_REQUEST_FILE);
        if (!file.exists()) {
            new Alert(Alert.AlertType.INFORMATION, "No QA requests found.").show();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<IncomingQARequest> list = (ArrayList<IncomingQARequest>) ois.readObject();
            requestList.setAll(list);
            tableView.refresh();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load QA requests.").show();
        }
    }

    // Save approved/rejected request to results file
    private void saveResult(IncomingQARequest request) {
        ArrayList<IncomingQARequest> results = new ArrayList<>();
        File file = new File(QA_RESULT_FILE);

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                results = (ArrayList<IncomingQARequest>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        results.add(request);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Show requests in TableView
    @FXML
    public void showButton(ActionEvent event) {
        loadRequests();
    }

    // Approve selected request
    @FXML
    public void approveButton(ActionEvent event) {
        IncomingQARequest selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a batch first!").show();
            return;
        }
        selected.setStatus("Approved");
        tableView.refresh();
        saveResult(selected);
        new Alert(Alert.AlertType.INFORMATION, "Request Approved!").show();
    }

    // Reject selected request
    @FXML
    public void rejectButton(ActionEvent event) {
        IncomingQARequest selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a batch first!").show();
            return;
        }
        selected.setStatus("Rejected");
        tableView.refresh();
        saveResult(selected);
        new Alert(Alert.AlertType.INFORMATION, "Request Rejected!").show();
    }

    // Go back to QA Officer Dashboard
    @FXML
    public void backButton(ActionEvent event) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
    }
}
