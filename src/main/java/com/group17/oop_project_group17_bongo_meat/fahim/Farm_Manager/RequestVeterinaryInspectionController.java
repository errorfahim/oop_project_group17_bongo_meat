package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.time.LocalDate;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class RequestVeterinaryInspectionController {

    private final String FILE_NAME = "vet_inspections.bin";

    @FXML private TextField reason;
    @FXML private TableColumn<VetInspection, String> animalTypeCol;
    @FXML private TableColumn<VetInspection, String> reasonCol;
    @FXML private TableView<VetInspection> vetInspectionTableView;
    @FXML private DatePicker lastInspectionDate;
    @FXML private TableColumn<VetInspection, String> animalIdCol;
    @FXML private ComboBox<String> animalTypeCB;
    @FXML private TextField farmID;
    @FXML private Label label;
    @FXML private TextField animalId;
    @FXML private TableColumn<VetInspection, LocalDate> laastInspectionDateCol;
    @FXML private TableColumn<VetInspection, String> farmIdCol;

    private ObservableList<VetInspection> inspectionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        animalTypeCB.getItems().addAll("Cow", "Goat", "Sheep", "Buffalo");


        animalIdCol.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        animalTypeCol.setCellValueFactory(new PropertyValueFactory<>("animalType"));
        laastInspectionDateCol.setCellValueFactory(new PropertyValueFactory<>("lastInspectionDate"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        farmIdCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));

        vetInspectionTableView.setItems(inspectionList);

        loadDataFromFile();
    }

    @FXML
    public void requestForVet(ActionEvent actionEvent) {

        if (animalId.getText().isEmpty() ||
                farmID.getText().isEmpty() ||
                reason.getText().isEmpty() ||
                animalTypeCB.getValue() == null ||
                lastInspectionDate.getValue() == null) {

            label.setText("⚠ Please fill all fields!");
            return;
        }

        VetInspection request = new VetInspection(
                animalId.getText(),
                animalTypeCB.getValue(),
                lastInspectionDate.getValue(),
                reason.getText(),
                farmID.getText()
        );

        inspectionList.add(request);
        saveDataToFile();

        label.setText("✔ Request Saved!");
    }

    private void saveDataToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(new java.util.ArrayList<>(inspectionList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            java.util.ArrayList<VetInspection> list =
                    (java.util.ArrayList<VetInspection>) in.readObject();

            inspectionList.setAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml", actionEvent);
    }

    @FXML
    public void nextButton(ActionEvent actionEvent) { }
}
