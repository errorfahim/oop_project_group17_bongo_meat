package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ApproveSupplierRequestsController
{
    @javafx.fxml.FXML
    private TextField supplierId;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> dateCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> statusCol;
    @javafx.fxml.FXML
    private ComboBox statusComboBox;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> quantityCol;
    @javafx.fxml.FXML
    private TableView<ArrayList> supplierTableView;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private DatePicker datepicker;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> supplierNameCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> productCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> supplierIdCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> priceCol;

    // Dummy data list
    private ObservableList<ArrayList> supplierData = FXCollections.observableArrayList();
    private ObservableList<ArrayList> filteredData = FXCollections.observableArrayList();

    @javafx.fxml.FXML
    public void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList(
                "All", "Pending", "Accepted", "Rejected"
        ));
        statusComboBox.setValue("All");

        supplierIdCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(0).toString()));
        supplierNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(1).toString()));
        productCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(2).toString()));
        quantityCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(3).toString()));
        priceCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(4).toString()));
        dateCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(5).toString()));
        statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(6).toString()));

        loadDummyData();
        supplierTableView.setItems(supplierData);
    }
    private void loadDummyData() {
        supplierData.clear();

        supplierData.add(new ArrayList<>(FXCollections.observableArrayList(
                "S001", "Rahim Traders", "Rice", "100", "20000", "2025-01-01", "Pending"
        )));
        supplierData.add(new ArrayList<>(FXCollections.observableArrayList(
                "S002", "Karim Supply", "Sugar", "50", "15000", "2025-01-03", "Accepted"
        )));
        supplierData.add(new ArrayList<>(FXCollections.observableArrayList(
                "S003", "Mina Agro", "Oil", "70", "35000", "2025-01-05", "Rejected"
        )));
        supplierData.add(new ArrayList<>(FXCollections.observableArrayList(
                "S004", "Bismillah Food", "Wheat", "120", "18000", "2025-01-06", "Pending"
        )));
    }


    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void approveSelected(ActionEvent actionEvent) {
        ArrayList selected = (ArrayList) supplierTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            label.setText("Please select a row.");
            return;
        }

        selected.set(6, "Accepted");

        supplierTableView.refresh();
        label.setText("Selected request Approved.");
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void rejectSelected(ActionEvent actionEvent) {
        ArrayList selected = (ArrayList) supplierTableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            label.setText("Please select a row.");
            return;
        }

        selected.set(6, "Rejected");

        supplierTableView.refresh();
        label.setText("Selected request Rejected.");
    }

    @javafx.fxml.FXML
    public void filterButton(ActionEvent actionEvent) {
        String idFilter = supplierId.getText().trim();
        String selectedStatus = statusComboBox.getValue().toString();
        LocalDate selectedDate = datepicker.getValue();

        filteredData.clear();

        for (ArrayList row : supplierData) {

            boolean match = true;

            if (!idFilter.isEmpty() && !row.get(0).toString().equalsIgnoreCase(idFilter)) {
                match = false;
            }

            if (!selectedStatus.equals("All") && !row.get(6).toString().equalsIgnoreCase(selectedStatus)) {
                match = false;
            }


            if (selectedDate != null && !row.get(5).toString().equals(selectedDate.toString())) {
                match = false;
            }

            if (match) filteredData.add(row);
        }

        supplierTableView.setItems(filteredData);

        label.setText("Filter applied.");
    }
}