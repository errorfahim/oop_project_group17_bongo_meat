package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ScheduleAnimalFeedingController
{
    @javafx.fxml.FXML
    private Label confirmMessage;
    @javafx.fxml.FXML
    private TextField quantity;
    @javafx.fxml.FXML
    private TableColumn feedTypeCol;
    @javafx.fxml.FXML
    private Label validateMessage;
    @javafx.fxml.FXML
    private ComboBox feedTypeCB;
    @javafx.fxml.FXML
    private TableColumn animalIdCol;
    @javafx.fxml.FXML
    private ComboBox animalTypeCB;
    @javafx.fxml.FXML
    private TableColumn feedingTimeCol;
    @javafx.fxml.FXML
    private TextField animalId;
    @javafx.fxml.FXML
    private DatePicker feedingDate;
    @javafx.fxml.FXML
    private TableColumn animalTypeCol;
    @javafx.fxml.FXML
    private TextField feedingTime;
    @javafx.fxml.FXML
    private TableColumn feedingDateCol;
    @javafx.fxml.FXML
    private TableView feedingTableView;
    @javafx.fxml.FXML
    private TableColumn quantityCol;


    // Dummy stock data
    private HashMap<String, Double> feedStock = new HashMap<>();

    private ArrayList<Feeding> feedingList = new ArrayList<>();
    private final String FILE_NAME = "feeding_records.bin";

    @javafx.fxml.FXML
    public void initialize() {

        feedStock.put("Hay", 100.0);
        feedStock.put("Grass", 200.0);
        feedStock.put("Corn", 150.0);
        feedStock.put("Pellet", 80.0);


        animalTypeCB.setItems(FXCollections.observableArrayList("Cow", "Goat", "Sheep"));
        feedTypeCB.setItems(FXCollections.observableArrayList("Hay", "Grass", "Corn", "Pellet"));


        animalIdCol.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        animalTypeCol.setCellValueFactory(new PropertyValueFactory<>("animalType"));
        feedTypeCol.setCellValueFactory(new PropertyValueFactory<>("feedType"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        feedingDateCol.setCellValueFactory(new PropertyValueFactory<>("feedingDate"));
        feedingTimeCol.setCellValueFactory(new PropertyValueFactory<>("feedingTime"));


        loadData();

        feedingTableView.setItems(FXCollections.observableArrayList(feedingList));
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void confirmAndSaveButton(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(animalId.getText());
            String type = animalTypeCB.getValue().toString();
            String feed = feedTypeCB.getValue().toString();
            double qty = Double.parseDouble(quantity.getText());
            LocalDate date = feedingDate.getValue();
            String time = feedingTime.getText();

            if (type == null || feed == null || date == null || time.isEmpty()) {
                confirmMessage.setText("Fill all fields!");
                return;
            }


            if (qty > feedStock.getOrDefault(feed, 0.0)) {
                confirmMessage.setText("Insufficient stock! Cannot save.");
                return;
            }

            // Create record
            Feeding record =
                    new Feeding(id, type, feed, qty, date, time);

            feedingList.add(record);

            // Update stock
            feedStock.put(feed, feedStock.get(feed) - qty);

            // Save binary file
            saveData();

            // Refresh table
            feedingTableView.setItems(FXCollections.observableArrayList(feedingList));

            confirmMessage.setText("✔ Feeding Scheduled Successfully!");

            clearInputs();

        } catch (Exception e) {
            confirmMessage.setText("Invalid Input!");

        }
    }
    private void clearInputs() {
        animalId.clear();
        quantity.clear();
        feedingTime.clear();
        feedingDate.setValue(null);
        animalTypeCB.setValue(null);
        feedTypeCB.setValue(null);
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(feedingList);
        } catch (IOException e) {
            System.out.println("Save Error: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            feedingList = (ArrayList<Feeding>) in.readObject();
        } catch (Exception e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void validateAndCheckStockButton(ActionEvent actionEvent) {
        try {
            String feed = feedTypeCB.getValue().toString();
            double qty = Double.parseDouble(quantity.getText());

            if (feed == null) {
                validateMessage.setText("Select a Feed Type!");
                return;
            }

            double available = feedStock.getOrDefault(feed, 0.0);

            if (qty <= available) {
                validateMessage.setText("Stock Available (" + available + " units)");
            } else {
                validateMessage.setText("❌ Insufficient Quantity! Available: " + available);
            }

        } catch (Exception e) {
            validateMessage.setText("Invalid Quantity!");
        }
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml",actionEvent);
    }
}