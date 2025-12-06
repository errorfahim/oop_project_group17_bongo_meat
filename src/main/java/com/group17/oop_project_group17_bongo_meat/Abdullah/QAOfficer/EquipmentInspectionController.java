package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class EquipmentInspectionController {

    @FXML private ComboBox<String> cbDepartment, cbEquipment, cbStatus;
    @FXML private TextArea txtNotes;
    @FXML private TableView<EquipmentInspection> tableInspections;
    @FXML private TableColumn<EquipmentInspection, String> colDate, colDept, colEquip, colStatus, colNotes;
    @FXML private Label lblMessage;

    private ObservableList<EquipmentInspection> data = FXCollections.observableArrayList();
    private static final String FILE_NAME = "equipmentInspections.dat";

    @FXML
    public void initialize() {
        // Initialize ComboBoxes
        cbDepartment.getItems().addAll("Farm", "Slaughterhouse", "Packaging", "Delivery");
        cbEquipment.getItems().addAll("Knives", "Machinery", "Refrigerators", "Vehicles", "Trays");
        cbStatus.getItems().addAll("Good", "Needs Repair", "Out of Order");

        // Initialize TableView columns
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDate().toString()));
        colDept.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDepartment()));
        colEquip.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEquipment()));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));
        colNotes.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNotes()));

        // Bind TableView
        tableInspections.setItems(data);

        // Load previous inspections from file
        data.addAll(loadInspections());
    }

    // Handle Submit Button
    @FXML
    private void handleSubmit(ActionEvent event) {
        if (cbDepartment.getValue() == null || cbEquipment.getValue() == null || cbStatus.getValue() == null) {
            lblMessage.setText("Please select department, equipment, and status.");
            return;
        }

        EquipmentInspection inspection = new EquipmentInspection(
                cbDepartment.getValue(),
                cbEquipment.getValue(),
                cbStatus.getValue(),
                txtNotes.getText().trim(),
                LocalDate.now()
        );

        data.add(inspection);
        saveInspections();
        lblMessage.setText("Inspection logged successfully!");

        // Clear selections
        cbDepartment.setValue(null);
        cbEquipment.setValue(null);
        cbStatus.setValue(null);
        txtNotes.clear();
    }

    // Load previous inspections
    private ArrayList<EquipmentInspection> loadInspections() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (ArrayList<EquipmentInspection>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save inspections to file
    private void saveInspections() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(new ArrayList<>(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle Back Button
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAOfficerDashboard.fxml", event);
    }
}


