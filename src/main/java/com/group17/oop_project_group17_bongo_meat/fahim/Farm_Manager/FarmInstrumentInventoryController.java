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

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class FarmInstrumentInventoryController
{
    @javafx.fxml.FXML
    private TextField instrumentName;
    @javafx.fxml.FXML
    private TextField quantity;
    @javafx.fxml.FXML
    private TableColumn instrumentIdCol;
    @javafx.fxml.FXML
    private TextField instrumentId;
    @javafx.fxml.FXML
    private TableColumn instrumentCatCol;
    @javafx.fxml.FXML
    private TableColumn instrumentEntryDate;
    @javafx.fxml.FXML
    private TableColumn quantityCol;
    @javafx.fxml.FXML
    private TableColumn instrumentNameCol;
    @javafx.fxml.FXML
    private ComboBox instrumentCatCB;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private DatePicker instrumentDate;
    @javafx.fxml.FXML
    private TableView instrumentTableView;

    private ArrayList<InstrumentInventory> inventoryList = new ArrayList<>();
    private final String FILE_NAME = "instrument_inventory.bin";

    @javafx.fxml.FXML
    public void initialize() {
        // Fill dummy categories
        instrumentCatCB.setItems(FXCollections.observableArrayList(
                "Cutting Tools", "Measuring Tools", "Electrical Tools", "Safety Equipment", "Farm Tools"
        ));

        // Bind table columns
        instrumentCatCol.setCellValueFactory(new PropertyValueFactory<>("instrumentCategory"));
        instrumentNameCol.setCellValueFactory(new PropertyValueFactory<>("instrumentName"));
        instrumentIdCol.setCellValueFactory(new PropertyValueFactory<>("instrumentId"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        instrumentEntryDate.setCellValueFactory(new PropertyValueFactory<>("entryDate"));

        // Load existing data
        loadData();

        instrumentTableView.setItems(FXCollections.observableArrayList(inventoryList));
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void cancelButton(ActionEvent actionEvent) {
        clearForm(); label.setText("");
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException{
        switchTo ("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void saveInventoryButton(ActionEvent actionEvent) {
        try {
            String category = instrumentCatCB.getValue().toString();
            String name = instrumentName.getText();
            String id = instrumentId.getText();
            int qty = Integer.parseInt(quantity.getText());
            LocalDate date = instrumentDate.getValue();

            if (category == null || name.isEmpty() || id.isEmpty() || date == null) {
                label.setText("Please fill all the fields!");
                return;
            }

            InstrumentInventory inventory = new InstrumentInventory(category, name, id, qty, date);
            inventoryList.add(inventory);

            saveData();

            instrumentTableView.setItems(FXCollections.observableArrayList(inventoryList));

            label.setText("Instrument saved in inventory successfully!");

            clearForm();

        } catch (Exception e) {
            label.setText("Invalid input! Please check your fields.");
        }
    }
    private void clearForm() {
        instrumentName.clear();
        instrumentId.clear();
        quantity.clear();
        instrumentCatCB.setValue(null);
        instrumentDate.setValue(null);
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(inventoryList);
        } catch (IOException e) {
            System.out.println("Save Error: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            inventoryList = (ArrayList<InstrumentInventory>) in.readObject();
        } catch (Exception e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }
}