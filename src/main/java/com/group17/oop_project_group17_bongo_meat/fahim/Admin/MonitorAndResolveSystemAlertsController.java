package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class MonitorAndResolveSystemAlertsController {

    private final String FILE_PATH = "alerts.bin";


    private List<SystemAlert> alertDataList = new ArrayList<>();


    private ObservableList<SystemAlert> alertObservableList = FXCollections.observableArrayList();

    @FXML
    private TextArea alertTextArea;

    @FXML
    private TableColumn<SystemAlert, LocalDate> dateCol;

    @FXML
    private TableColumn<SystemAlert, String> alertMessageCol;

    @FXML
    private TableView<SystemAlert> alertTableView;

    @FXML
    private DatePicker alertDate;


    @FXML
    public void initialize() {
        // Bind Table Columns
        dateCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("date"));
        alertMessageCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("message"));

        // Load alerts from file
        loadFromFile();

        // Add loaded data to TableView
        alertObservableList.addAll(alertDataList);
        alertTableView.setItems(alertObservableList);
    }


    private void loadFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return; // No file yet
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            alertDataList = (List<SystemAlert>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveToFile() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {

            oos.writeObject(alertDataList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void showAlertButton(ActionEvent actionEvent) {

        String msg = alertTextArea.getText();
        LocalDate date = alertDate.getValue();

        if (msg.isEmpty() || date == null) {
            new Alert(Alert.AlertType.WARNING, "Please enter a message and select a date!").show();
            return;
        }

        // Create new alert
        SystemAlert alert = new SystemAlert(date, msg);

        // Add to data list
        alertDataList.add(alert);

        // Save to file
        saveToFile();

        // Update TableView
        alertObservableList.add(alert);

        // Clear input fields
        alertTextArea.clear();
        alertDate.setValue(null);
    }

    @FXML
    public void nextButton(ActionEvent actionEvent) {}

    @FXML
    public void backButton(ActionEvent actionEvent)throws IOException {
        switchTo ("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml",actionEvent);
    }
}
