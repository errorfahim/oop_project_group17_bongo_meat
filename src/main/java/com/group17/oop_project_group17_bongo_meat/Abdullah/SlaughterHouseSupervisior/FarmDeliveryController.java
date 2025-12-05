package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class FarmDeliveryController {

    private final String TRANSFER_FILE = "approvedLivestockTransfer.dat";
    private final String DELIVERY_OUTPUT_FILE = "farmDeliveryStatus.dat";

    @FXML private TableView<FarmDeliveryRecord> tblDelivery;
    @FXML private TableColumn<FarmDeliveryRecord, LocalDate> colArrivalDate;
    @FXML private TableColumn<FarmDeliveryRecord, String> colAnimalId;
    @FXML private TableColumn<FarmDeliveryRecord, String> colType;
    @FXML private TableColumn<FarmDeliveryRecord, String> colBreed; // NEW COLUMN
    @FXML private TableColumn<FarmDeliveryRecord, String> colHealth;
    @FXML private TableColumn<FarmDeliveryRecord, Integer> colQuantity;
    @FXML private TableColumn<FarmDeliveryRecord, String> colStatus;

    @FXML private TextArea txtNote;

    private ObservableList<FarmDeliveryRecord> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colAnimalId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAnimalId()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));
        colBreed.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBreed()));
        colHealth.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getHealthCertificate()));
        colQuantity.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
        colArrivalDate.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getArrivalDate()));
    }

    // SHOW RECORDS
    @FXML
    private void handleShow() {
        data.clear();

        File file = new File(TRANSFER_FILE);

        if (!file.exists()) {
            generateDummyData();
        } else {
            loadFromTransferFile();
        }

        tblDelivery.setItems(data);
        tblDelivery.refresh();
    }

    private void loadFromTransferFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRANSFER_FILE))) {
            ArrayList<FarmDeliveryRecord> list = (ArrayList<FarmDeliveryRecord>) ois.readObject();
            data.addAll(list);
        } catch (Exception e) {
            generateDummyData();
        }
    }

    // GENERATE DUMMY DATA WITH BREED
    private void generateDummyData() {
        Random r = new Random();

        String[] types = {"Cow", "Goat", "Sheep", "Chicken", "Other"};

        String[] cowBreeds = {"Australian", "Sahiwal", "Brahman", "Holstein"};
        String[] goatBreeds = {"Black Bengal", "Jamunapari", "Boer"};
        String[] sheepBreeds = {"Merino", "Dorper", "Suffolk"};
        String[] chickenBreeds = {"Cock", "Broiler", "Layer"};
        String[] otherBreeds = {"Mixed Breed", "Unknown"};

        for (int i = 0; i < 5; i++) {
            String type = types[r.nextInt(types.length)];
            String breed;

            if (type.equals("Cow")) {
                breed = cowBreeds[r.nextInt(cowBreeds.length)];
            }
            else if (type.equals("Goat")) {
                breed = goatBreeds[r.nextInt(goatBreeds.length)];
            }
            else if (type.equals("Sheep")) {
                breed = sheepBreeds[r.nextInt(sheepBreeds.length)];
            }
            else if (type.equals("Chicken")) {
                breed = chickenBreeds[r.nextInt(chickenBreeds.length)];
            }
            else {
                breed = otherBreeds[r.nextInt(otherBreeds.length)];
            }


            String animalId = "ANM-" + (10000 + r.nextInt(90000));
            int quantity = 10 + r.nextInt(5);

            data.add(new FarmDeliveryRecord(
                    animalId,
                    type,
                    "Yes",
                    quantity,
                    "Waiting for Confirmation",
                    LocalDate.now(),   // always today's date
                    "",
                    breed
            ));
        }
    }

    // APPROVE BUTTON
    @FXML
    private void handleApprove() {
        FarmDeliveryRecord selected = tblDelivery.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a record to approve.");
            return;
        }

        selected.setStatus("Approved");
        saveUpdatedList();
        tblDelivery.refresh();
    }

    // REJECT BUTTON
    @FXML
    private void handleReject() {
        FarmDeliveryRecord selected = tblDelivery.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a record to reject.");
            return;
        }

        if (txtNote.getText().trim().isEmpty()) {
            showError("Reason is required for rejection.");
            return;
        }

        selected.setStatus("Rejected");
        selected.setNote(txtNote.getText().trim());

        saveUpdatedList();
        tblDelivery.refresh();
    }

    // SAVE
    private void saveUpdatedList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DELIVERY_OUTPUT_FILE))) {
            ArrayList<FarmDeliveryRecord> list = new ArrayList<>(data);
            oos.writeObject(list);
        } catch (Exception e) {
            showError("Failed to save!");
        }
    }

    // BACK BUTTON
    @FXML
    private void handleBack(javafx.event.ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}


