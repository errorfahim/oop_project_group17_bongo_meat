package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.io.*;
import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class TrackLivestockInventoryController {
    @javafx.fxml.FXML
    private TableColumn locationCol;
    @javafx.fxml.FXML
    private ComboBox availabiltyCB;
    @javafx.fxml.FXML
    private TableColumn ageCol;
    @javafx.fxml.FXML
    private TableColumn animalTypeCol;
    @javafx.fxml.FXML
    private TableColumn availabilityCol;
    @javafx.fxml.FXML
    private TableView animalInfoTableView;
    @javafx.fxml.FXML
    private TableColumn sourceCol;
    @javafx.fxml.FXML
    private TableColumn animalIdCol;
    @javafx.fxml.FXML
    private ComboBox animalTypeCB;
    @javafx.fxml.FXML
    private TableColumn additionalNotesCol;
    @javafx.fxml.FXML
    private TableColumn weightCol;
    private ObservableList<Livestock> livestockData = FXCollections.observableArrayList();
    private FilteredList<Livestock> filteredLivestockData;
    private final String DATA_FILE = "livestock_data.bin";

    @javafx.fxml.FXML
    public void initialize() {
        // Initialize ComboBoxes with filter options
        animalTypeCB.getItems().addAll("All", "Cow", "Sheep", "Goat", "Chicken", "Pig", "Duck", "Turkey");
        availabiltyCB.getItems().addAll("All", "Available", "Sold", "Deceased", "Transferred");

        // Set default selection
        animalTypeCB.setValue("All");
        availabiltyCB.setValue("All");

        // Initialize table columns
        initializeTableColumns();

        // Load data from binary file
        loadLivestockData();

        // Set up filtered list
        filteredLivestockData = new FilteredList<>(livestockData, p -> true);
        animalInfoTableView.setItems(filteredLivestockData);

        // Apply initial filter (show all)
        applyFilter();
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
    }

    private void loadLivestockData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                ArrayList<Livestock> loadedList = (ArrayList<Livestock>) ois.readObject();
                livestockData.clear();
                livestockData.addAll(loadedList);
                System.out.println("Data loaded from " + DATA_FILE + ". Total records: " + livestockData.size());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
                livestockData.clear();
            }
        } else {
            System.out.println("Data file not found. Starting with empty list.");
            livestockData.clear();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadLivestockDataFromText() {
        // Alternative method if binary serialization fails - use text file
        File file = new File("livestock_data.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader("livestock_data.txt"))) {
                livestockData.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 9) {
                        Livestock livestock = new Livestock(
                                parts[0], parts[1], parts[2], parts[3], parts[4],
                                parts[5], parts[6], parts[7], parts[8]
                        );
                        livestockData.add(livestock);
                    }
                }
                System.out.println("Data loaded from text file. Total records: " + livestockData.size());
            } catch (IOException e) {
                System.err.println("Error loading text data: " + e.getMessage());
                livestockData.clear();
            }
        }
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void applyFilterButton(ActionEvent actionEvent) {
        applyFilter();
    }

    private void applyFilter() {
        String selectedAnimalType = (String) animalTypeCB.getValue();
        String selectedAvailability = (String) availabiltyCB.getValue();

        filteredLivestockData.setPredicate(livestock -> {
            boolean matchesAnimalType = selectedAnimalType == null ||
                    selectedAnimalType.equals("All") ||
                    livestock.getAnimalType().equals(selectedAnimalType);

            boolean matchesAvailability = selectedAvailability == null ||
                    selectedAvailability.equals("All") ||
                    livestock.getAvailability().equals(selectedAvailability);

            return matchesAnimalType && matchesAvailability;
        });

        // Update table
        animalInfoTableView.refresh();

        // Show filter status
        System.out.println("Filter applied - Animal Type: " + selectedAnimalType +
                ", Availability: " + selectedAvailability +
                ", Showing: " + filteredLivestockData.size() + " records");
    }

    @javafx.fxml.FXML
    public void clearFilterButton(ActionEvent actionEvent) {
        // Reset comboboxes to "All"
        animalTypeCB.setValue("All");
        availabiltyCB.setValue("All");

        // Apply filter to show all
        applyFilter();

    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml", actionEvent);
    }


}