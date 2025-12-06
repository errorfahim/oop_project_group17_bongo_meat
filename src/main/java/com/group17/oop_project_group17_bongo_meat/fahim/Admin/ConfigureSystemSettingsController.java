package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ConfigureSystemSettingsController {

    private static final String FILE_NAME = "temperature_settings.bin";

    @FXML private TableColumn<TemperatureSetting, String> transportationCol;
    @FXML private TableColumn<TemperatureSetting, String> chillingCol;
    @FXML private TableColumn<TemperatureSetting, String> cuttingCol;
    @FXML private TableColumn<TemperatureSetting, String> packagingCol;
    @FXML private TableColumn<TemperatureSetting, String> chilledStorageCol;
    @FXML private TableColumn<TemperatureSetting, String> blastFreezingCol;
    @FXML private TableColumn<TemperatureSetting, String> truckCol;
    @FXML private TableColumn<TemperatureSetting, String> reeferConCol;
    @FXML private TableColumn<TemperatureSetting, String> frozenCol;

    @FXML private TableView<TemperatureSetting> temperatureTableView;

    @FXML private TextField livestockTransportation;
    @FXML private TextField carcassChilling;
    @FXML private TextField cuttingRoom;
    @FXML private TextField packagingRoom;
    @FXML private TextField chilledStorage;
    @FXML private TextField blastFreezing;
    @FXML private TextField coldTruck;
    @FXML private TextField reeferContainer;
    @FXML private TextField frozenStorage;

    @FXML private Label label;

    private ObservableList<TemperatureSetting> temperatureList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Correct column bindings
        transportationCol.setCellValueFactory(new PropertyValueFactory<>("transportation"));
        chillingCol.setCellValueFactory(new PropertyValueFactory<>("chilling"));
        cuttingCol.setCellValueFactory(new PropertyValueFactory<>("cutting"));
        packagingCol.setCellValueFactory(new PropertyValueFactory<>("packaging"));
        chilledStorageCol.setCellValueFactory(new PropertyValueFactory<>("chilledStorage"));
        blastFreezingCol.setCellValueFactory(new PropertyValueFactory<>("blastFreezing"));
        truckCol.setCellValueFactory(new PropertyValueFactory<>("truck"));
        reeferConCol.setCellValueFactory(new PropertyValueFactory<>("reeferCon"));
        frozenCol.setCellValueFactory(new PropertyValueFactory<>("frozen"));

        loadFromFile();
        temperatureTableView.setItems(temperatureList);
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(temperatureList));
            label.setText("Temperature data saved.");
        } catch (Exception e) {
            label.setText("Error saving data.");
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);

        // Prevent corrupted file error
        if (!file.exists() || file.length() == 0) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<TemperatureSetting> data = (ArrayList<TemperatureSetting>) ois.readObject();
            temperatureList.addAll(data);
        } catch (Exception e) {
            label.setText("Error loading data.");
            e.printStackTrace();
        }
    }

    @FXML
    public void updateTemperatureButton(ActionEvent actionEvent) {

        TemperatureSetting ts = new TemperatureSetting(
                livestockTransportation.getText(),
                carcassChilling.getText(),
                cuttingRoom.getText(),
                packagingRoom.getText(),
                chilledStorage.getText(),
                blastFreezing.getText(),
                coldTruck.getText(),
                reeferContainer.getText(),
                frozenStorage.getText()
        );

        temperatureList.add(ts);
        temperatureTableView.refresh();
        saveToFile();

        label.setText("Temperature updated and saved.");
    }

    @FXML
    public void nextButton(ActionEvent event) {
        // FIX for FXML error
        System.out.println("Next button clicked");
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml", actionEvent);
    }
}
