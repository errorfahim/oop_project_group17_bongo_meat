package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class RecordIncomingLivestockController {

    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField breedTextField;
    @FXML private TextField quantityTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField healthObservationTextField;
    @FXML private Label batchOutputLabel;
    @FXML private Label outputLabel;
    @FXML private TableView<RecordIncomingLivestock> recorderIncomingTableView;

    @FXML private TableColumn<RecordIncomingLivestock, String> batchIDColumn;
    @FXML private TableColumn<RecordIncomingLivestock, String> typeColumn;
    @FXML private TableColumn<RecordIncomingLivestock, String> breedColumn;
    @FXML private TableColumn<RecordIncomingLivestock, Integer> quantityColumn;
    @FXML private TableColumn<RecordIncomingLivestock, Double> weightColumn;
    @FXML private TableColumn<RecordIncomingLivestock, String> healthObservationColumn;

    private final String FILE_NAME = "incomingLivestock.dat";
    private ObservableList<RecordIncomingLivestock> livestockList = FXCollections.observableArrayList();
    private String generatedBatchID = "";

    @FXML
    public void initialize() {

        typeComboBox.getItems().addAll("Cow", "Goat", "Sheep", "Chicken", "Other");

        batchIDColumn.setCellValueFactory(c -> c.getValue().batchIDProperty());
        typeColumn.setCellValueFactory(c -> c.getValue().typeProperty());
        breedColumn.setCellValueFactory(c -> c.getValue().breedProperty());
        quantityColumn.setCellValueFactory(c -> c.getValue().quantityProperty().asObject());
        weightColumn.setCellValueFactory(c -> c.getValue().weightProperty().asObject());
        healthObservationColumn.setCellValueFactory(c -> c.getValue().healthObservationProperty());

        recorderIncomingTableView.setItems(livestockList);
        loadRecordsFromFile();
    }

    @FXML
    private void generateBatchIDButton(ActionEvent event) {
        generatedBatchID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        batchOutputLabel.setText("Batch ID: " + generatedBatchID + " successfully created");
    }

    @FXML
    private void saveRecordButton(ActionEvent event) {

        try {
            if (generatedBatchID.isEmpty()) {
                showAlert("Generate a Batch ID first.");
                return;
            }

            String type = typeComboBox.getValue();
            String breed = breedTextField.getText().trim();
            String healthObs = healthObservationTextField.getText().trim();

            if (type == null || breed.isEmpty()) {
                showAlert("Please fill all fields.");
                return;
            }

            int quantity = Integer.parseInt(quantityTextField.getText().trim());
            double weight = Double.parseDouble(weightTextField.getText().trim());

            if (quantity <= 0 || weight <= 0) {
                showAlert("Enter valid numeric values.");
                return;
            }

            RecordIncomingLivestock record = new RecordIncomingLivestock(
                    generatedBatchID,
                    type,
                    breed,
                    quantity,
                    weight,
                    healthObs
            );

            livestockList.add(record);
            saveRecordsToFile();
            clearFields();
            outputLabel.setText("Record successfully saved!");

        } catch (Exception e) {
            showAlert("Invalid input. Check your values.");
        }
    }

    @FXML
    private void showButton(ActionEvent event) {
        recorderIncomingTableView.refresh();
    }

    private void loadRecordsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<RecordIncomingLivestock> list = (ArrayList<RecordIncomingLivestock>) ois.readObject();
            livestockList.setAll(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveRecordsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(livestockList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        typeComboBox.setValue(null);
        breedTextField.clear();
        quantityTextField.clear();
        weightTextField.clear();
        healthObservationTextField.clear();
        batchOutputLabel.setText("");
        generatedBatchID = "";
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void backButton(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", event);
    }
}
