package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.group17.oop_project_group17_bongo_meat.SceneSwitcher;

import java.io.*;
import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class RegisterNewLivestockController
{
    @javafx.fxml.FXML
    private Label sucessMessageLabel;
    @javafx.fxml.FXML
    private TableColumn locationCol;
    @javafx.fxml.FXML
    private TableColumn ageCol;
    @javafx.fxml.FXML
    private TextField ageField;
    @javafx.fxml.FXML
    private TableView animalInfoTableView;
    @javafx.fxml.FXML
    private TableColumn sourceCol;
    @javafx.fxml.FXML
    private TableColumn animalIdCol;
    @javafx.fxml.FXML
    private ComboBox animalTypeCB;
    @javafx.fxml.FXML
    private ComboBox sourceCB;
    @javafx.fxml.FXML
    private TextField loactionField;
    @javafx.fxml.FXML
    private TableColumn weightCol;
    @javafx.fxml.FXML
    private TextField animalIdField;
    @javafx.fxml.FXML
    private TableColumn animalTypeCol;
    @javafx.fxml.FXML
    private TextField additioanlNotesField;
    @javafx.fxml.FXML
    private TextField weightField;
    @javafx.fxml.FXML
    private TableColumn additionalNotesCol;
    @javafx.fxml.FXML
    private TableColumn availabilityCol;
    @javafx.fxml.FXML
    private ComboBox availabilityCB;
    @javafx.fxml.FXML
    private TableColumn healthStatusCol;
    @javafx.fxml.FXML
    private ComboBox healthStatusCB;

    private ArrayList<Livestock> livestockList = new ArrayList<>();
    private final String DATA_FILE = "livestock_data.bin";

    @javafx.fxml.FXML
    public void initialize() {

        animalTypeCB.getItems().addAll("Cow", "Sheep", "Goat", "Chicken", "Duck", "Turkey");
        sourceCB.getItems().addAll("Purchase", "Birth", "Donation", "Transfer");
        availabilityCB.getItems().addAll("Available", "Sold", "Deceased", "Transferred");
        healthStatusCB.getItems().addAll("Healthy", "Sick", "Under Treatment", "Recovering");

        initializeTableColumns();


        loadLivestockData();


        updateTableView();

    }
    private void initializeTableColumns() {
        animalIdCol.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        animalTypeCol.setCellValueFactory(new PropertyValueFactory<>("animalType"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weightKg"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("source"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        additionalNotesCol.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        healthStatusCol.setCellValueFactory(new PropertyValueFactory<>("healthStatus"));
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/TrackLivestockInventory.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void cancelButton(ActionEvent actionEvent) {
        // Clear all input fields
        clearInputFields();
        sucessMessageLabel.setText("");
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void saveLivestockButton(ActionEvent actionEvent) {
        try {
            // Validate input fields
            if (!validateInput()) {
                return;
            }

            // Create new Livestock object
            Livestock livestock = new Livestock(
                    animalIdField.getText(),                         // animalId
                    ageField.getText(),                               // age (String)
                    animalTypeCB.getValue().toString(),               // animalType
                    availabilityCB.getValue().toString(),             // availability
                    weightField.getText(),                            // weightKg
                    sourceCB.getValue().toString(),                   // source
                    healthStatusCB.getValue().toString(),             // healthStatus
                    loactionField.getText(),                          // location
                    additioanlNotesField.getText()

            );

            // Add to list
            livestockList.add(livestock);

            // Save to binary file
            saveLivestockData();

            // Update table view
            updateTableView();

            // Show success message
            sucessMessageLabel.setText("Livestock registered successfully!");
            sucessMessageLabel.setStyle("-fx-text-fill: green;");

            // Clear input fields
            clearInputFields();

        } catch (Exception e) {
            sucessMessageLabel.setText("Error: " + e.getMessage());
            sucessMessageLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }

    }
    private boolean validateInput() {
        // Check if all required fields are filled
        if (animalIdField.getText().isEmpty()) {
            showError("Animal ID is required");
            return false;
        }
        if (animalTypeCB.getValue() == null) {
            showError("Animal Type is required");
            return false;
        }
        if (weightField.getText().isEmpty()) {
            showError("Weight is required");
            return false;
        }
        if (ageField.getText().isEmpty()) {
            showError("Age is required");
            return false;
        }
        if (availabilityCB.getValue() == null) {
            showError("Availability is required");
            return false;
        }
        if (sourceCB.getValue() == null) {
            showError("Source is required");
            return false;
        }
        if (loactionField.getText().isEmpty()) {
            showError("Location is required");
            return false;
        }
        if (healthStatusCB.getValue() == null) {
            showError("Health Status is required");
            return false;
        }

        // Validate numeric fields
        try {
            double weight = Double.parseDouble(weightField.getText());
            if (weight <= 0) {
                showError("Weight must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Weight must be a number");
            return false;
        }

        try {
            int age = Integer.parseInt(ageField.getText());
            if (age < 0) {
                showError("Age cannot be negative");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Age must be a whole number");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        sucessMessageLabel.setText(message);
        sucessMessageLabel.setStyle("-fx-text-fill: red;");
    }

    private void clearInputFields() {
        animalIdField.clear();
        animalTypeCB.setValue(null);
        weightField.clear();
        ageField.clear();
        availabilityCB.setValue(null);
        sourceCB.setValue(null);
        loactionField.clear();
        additioanlNotesField.clear();
        healthStatusCB.setValue(null);
    }

    private void saveLivestockData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(livestockList);
            System.out.println("Data saved to " + DATA_FILE + ". Total records: " + livestockList.size());
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            throw new RuntimeException("Failed to save livestock data", e);
        }
    }

    private void loadLivestockData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                livestockList = (ArrayList<Livestock>) ois.readObject();
                System.out.println("Data loaded from " + DATA_FILE + ". Total records: " + livestockList.size());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
                // Start with empty list if file can't be read
                livestockList = new ArrayList<>();
            }
        } else {
            System.out.println("Data file not found. Starting with empty list.");
            livestockList = new ArrayList<>();
        }
    }

    private void updateTableView() {
        animalInfoTableView.getItems().clear();
        animalInfoTableView.getItems().addAll(livestockList);
    }
}