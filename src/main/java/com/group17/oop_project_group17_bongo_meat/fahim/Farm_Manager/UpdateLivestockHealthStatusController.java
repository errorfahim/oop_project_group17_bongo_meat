package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class UpdateLivestockHealthStatusController
{
    @javafx.fxml.FXML
    private TableColumn animalTypeCol;
    @javafx.fxml.FXML
    private TableView animalInfoTableView;
    @javafx.fxml.FXML
    private TableColumn animalIdCol;
    @javafx.fxml.FXML
    private ComboBox animalTypeCB;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private TextField animalID;
    @javafx.fxml.FXML
    private ComboBox vaccinationCB;
    @javafx.fxml.FXML
    private TextField symptoms;
    @javafx.fxml.FXML
    private TableColumn statusCol;
    @javafx.fxml.FXML
    private ComboBox healthStatus;
    @javafx.fxml.FXML
    private TableColumn vaccinationCol;
    @javafx.fxml.FXML
    private TextField temperature;
    @javafx.fxml.FXML
    private TableColumn temperatureCol;
    @javafx.fxml.FXML
    private TableColumn symptomsCol;

    private ArrayList<HealthStatus> recordList = new ArrayList<>();
    private final String FILE_NAME = "health_records.bin";


    @javafx.fxml.FXML
    public void initialize() {
        animalTypeCB.getItems().addAll("Cow", "Sheep", "Goat", "Chicken", "Duck", "Turkey");
        vaccinationCB.getItems().addAll("FMD", "HS", "BQ","Brucellosis Vaccine", "Anthrax Vaccine","Enterotoxaemia","Theileriosis Vaccine","Lumpy Skin Disease (LSD) Vaccine");
        healthStatus.getItems().addAll("Healthy", "Sick", "Under Treatment", "Recovering");

        animalIdCol.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        animalTypeCol.setCellValueFactory(new PropertyValueFactory<>("animalType"));
        temperatureCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        symptomsCol.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
        vaccinationCol.setCellValueFactory(new PropertyValueFactory<>("vaccination"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("healthStatus"));



        loadData();

        animalInfoTableView.setItems(FXCollections.observableArrayList(recordList));
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent)throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml",actionEvent);
    }


    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent)throws  IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/ScheduleAnimalFeeding.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void cancelButton(ActionEvent actionEvent) {
        clearInputs();
        label.setText("Canceled.");
    }

    @javafx.fxml.FXML
    public void updatehealthStatusButton(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(animalID.getText().trim());
            String type = animalTypeCB.getValue().toString();
            double temp = Double.parseDouble(temperature.getText().trim());
            String sym = symptoms.getText().trim();
            String vac = vaccinationCB.getValue().toString();
            String status = healthStatus.getValue().toString();


            if (type == null || vac == null || status == null || sym.isEmpty()) {
                label.setText("Please fill all fields properly.");
                return;
            }


            HealthStatus record = new HealthStatus(id, type, temp, sym, vac, status);

            recordList.add(record);

            saveData();

            animalInfoTableView.setItems(FXCollections.observableArrayList(recordList));

            label.setText("Health status updated successfully!");

            clearInputs();

        } catch (Exception e) {
            label.setText("Invalid input. Please check your entries.");
        }
    }
    private void clearInputs() {
        animalID.clear();
        temperature.clear();
        symptoms.clear();
        animalTypeCB.setValue(null);
        vaccinationCB.setValue(null);
        healthStatus.setValue(null);
    }
    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(recordList);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            recordList = (ArrayList<HealthStatus>) in.readObject();
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
}