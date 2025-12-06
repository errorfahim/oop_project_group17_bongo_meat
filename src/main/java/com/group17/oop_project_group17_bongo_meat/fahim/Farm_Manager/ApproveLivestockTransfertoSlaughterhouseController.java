package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.time.LocalDate;
import java.util.Random;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ApproveLivestockTransfertoSlaughterhouseController {

    @javafx.fxml.FXML
    private TableView<LivestockTransfer> LivestockTransferTableView;
    @javafx.fxml.FXML
    private TableColumn<LivestockTransfer, LocalDate> departureDateCol;
    @javafx.fxml.FXML
    private TableColumn<LivestockTransfer, String> batchIDCol;
    @javafx.fxml.FXML
    private TableColumn<LivestockTransfer, String> typeCol;
    @javafx.fxml.FXML
    private TableColumn<LivestockTransfer, String> breedCol;
    @javafx.fxml.FXML
    private TableColumn<LivestockTransfer, String> healthCertificateCol;
    @javafx.fxml.FXML
    private TableColumn<LivestockTransfer, Integer> quantityCol;

    @javafx.fxml.FXML
    private ComboBox<String> typeCB;
    @javafx.fxml.FXML
    private ComboBox<String> breedCB;
    @javafx.fxml.FXML
    private ComboBox<String> healthCertificateCB;
    @javafx.fxml.FXML
    private TextField batchId;
    @javafx.fxml.FXML
    private TextField quantity;
    @javafx.fxml.FXML
    private DatePicker departureDateDP;
    @javafx.fxml.FXML
    private Label label;

    private final String FILE_NAME = "livestock_transfer.bin";
    private ObservableList<LivestockTransfer> transferList = FXCollections.observableArrayList();

    @javafx.fxml.FXML
    public void initialize() {

        typeCB.getItems().addAll("Cow", "Goat", "Sheep", "Buffalo");
        breedCB.getItems().addAll("Local", "Cross", "Hybrid");
        healthCertificateCB.getItems().addAll("Valid", "Pending", "Expired");

        batchId.setText(generateBatchId());

        // TABLE COLUMNS
        departureDateCol.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        batchIDCol.setCellValueFactory(new PropertyValueFactory<>("batchId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed"));
        healthCertificateCol.setCellValueFactory(new PropertyValueFactory<>("healthCertificate"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadBinaryFile();
    }

    private String generateBatchId() {
        return "BATCH-" + (10000 + new Random().nextInt(90000));
    }

    @javafx.fxml.FXML
    public void approveTransferButton(ActionEvent actionEvent) {

        if (departureDateDP.getValue() == null ||
                typeCB.getValue() == null ||
                breedCB.getValue() == null ||
                healthCertificateCB.getValue() == null ||
                quantity.getText().isEmpty()) {

            label.setText("Please fill all fields!");
            return;
        }

        LocalDate departureDate = departureDateDP.getValue();
        String id = batchId.getText();
        String type = typeCB.getValue();
        String breed = breedCB.getValue();
        String certificate = healthCertificateCB.getValue();
        int qty = Integer.parseInt(quantity.getText());

        LivestockTransfer entry = new LivestockTransfer(
                departureDate, id, type, breed, certificate, qty
        );

        transferList.add(entry);
        LivestockTransferTableView.setItems(transferList);

        saveBinaryFile();

        label.setText("Transfer Approved Successfully!");

        batchId.setText(generateBatchId());
    }

    private void saveBinaryFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new java.util.ArrayList<>(transferList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBinaryFile() {
        File file = new File(FILE_NAME);

        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            java.util.ArrayList<LivestockTransfer> saved =
                    (java.util.ArrayList<LivestockTransfer>) ois.readObject();

            transferList.addAll(saved);
            LivestockTransferTableView.setItems(transferList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent)  {

    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml",actionEvent);
    }
}
