package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.util.ArrayList;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class MeatTransferRequestController {

    @FXML private TableView<MeatTransferRequest> transferTable;

    @FXML private TableColumn<MeatTransferRequest, String> batchIDColumn;
    @FXML private TableColumn<MeatTransferRequest, String> typeColumn;
    @FXML private TableColumn<MeatTransferRequest, String> weightColumn;
    @FXML private TableColumn<MeatTransferRequest, String> slaughterTimeColumn;
    @FXML private TableColumn<MeatTransferRequest, String> statusColumn;

    @FXML private TextField observationTextField;
    @FXML private Label messageLabel;

    private final ObservableList<MeatTransferRequest> data = FXCollections.observableArrayList();

    private static final String OUTPUT_FILE = "outputSlaughter.dat";
    private static final String REQUEST_FILE = "transferRequest.dat";

    // ===================== INITIALIZE =====================
    @FXML
    public void initialize() {
        batchIDColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBatchId()));
        typeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        weightColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getWeight()));
        slaughterTimeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSlaughterTime()));
        statusColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        transferTable.setItems(data);
    }

    // ===================== SHOW OUTPUT SLAUGHTER DATA =====================
    @FXML
    private void handleShow(ActionEvent event) {
        ArrayList<SlaughterOutputReport> slaughterList;
        ArrayList<MeatTransferRequest> requestListFromFile = new ArrayList<>();

        // Load slaughter output data
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            slaughterList = (ArrayList<SlaughterOutputReport>) ois.readObject();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load slaughter output records.").show();
            return;
        }

        // Load existing meat transfer requests
        try {
            File file = new File(REQUEST_FILE);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    requestListFromFile = (ArrayList<MeatTransferRequest>) ois.readObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Build final list with correct statuses
        data.clear(); // ObservableList bound to TableView

        for (SlaughterOutputReport s : slaughterList) {
            String status = "Pending";
            String observation = s.getObservation();

            // Check if request already exists
            for (MeatTransferRequest req : requestListFromFile) {
                if (req.getBatchId().equals(s.getBatchId())) {
                    status = req.getStatus();
                    observation = req.getObservation();
                    break;
                }
            }

            MeatTransferRequest request = new MeatTransferRequest(
                    s.getBatchId(),
                    s.getType(),
                    s.getWeight(),
                    s.getSlaughterTime(),
                    status,
                    observation
            );

            data.add(request);
        }

        transferTable.setItems(data);
        transferTable.refresh();

        new Alert(Alert.AlertType.INFORMATION,
                "Loaded " + data.size() + " record(s).").show();
    }



    // ===================== SEND REQUEST =====================
    @FXML
    private void sendRequest(ActionEvent event) {
        MeatTransferRequest selected = transferTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a record to send request.");
            return;
        }

        selected.setStatus("Send Request");
        selected.setObservation(observationTextField.getText());

        transferTable.refresh();

        // Save request to file
        saveRequestToFile(selected);

        observationTextField.clear();
        messageLabel.setText("Request sent successfully!");
    }

    private void saveRequestToFile(MeatTransferRequest request) {
        ArrayList<MeatTransferRequest> requestList = new ArrayList<>();

        File file = new File(REQUEST_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    for (Object o : (ArrayList<?>) obj) {
                        if (o instanceof MeatTransferRequest r) {
                            requestList.add(r);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        requestList.add(request);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(requestList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== BACK BUTTON =====================
    @FXML
    public void backButton(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }
}
