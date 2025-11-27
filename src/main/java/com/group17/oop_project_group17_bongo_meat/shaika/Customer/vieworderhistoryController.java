package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;
import com.group17.oop_project_group17_bongo_meat.LoginController;

public class vieworderhistoryController {

    @FXML
    private TableView<Order> orderHistorytable;
    @FXML
    private TableColumn<Order, String> orderIDcol;
    @FXML
    private TableColumn<Order, Double> subtotalcol;
    @FXML
    private TableColumn<Order, String> StatusCol;

    @FXML
    private Label outputdetailsLabel;

    private final String ORDER_FILE = "orders.dat";
    private ObservableList<Order> ordersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        orderIDcol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrderId()));
        subtotalcol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());
        StatusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        loadOrders();

        // Selection listener: show details when a row is selected
        orderHistorytable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showOrderDetails(newSelection);
            }
        });
    }

    private void loadOrders() {
        ordersList.clear();
        ArrayList<Order> allOrders = new ArrayList<>();

        // Load orders from file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            allOrders = (ArrayList<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File might not exist yet
        }

        // Filter only orders for logged-in customer
        for (Order o : allOrders) {
            if (LoginController.loggedInEmail != null && LoginController.loggedInEmail.equals(o.getCustomerEmail())) {
                ordersList.add(o);
            }
        }

        orderHistorytable.setItems(ordersList);
    }

    private void showOrderDetails(Order o) {
        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(o.getOrderId()).append("\n")
                .append("Status: ").append(o.getStatus()).append("\n")
                .append("Total: ").append(o.getSubtotal()).append(" BDT\n")
                .append("Items:\n");

        for (var item : o.getItems()) {
            details.append(" • ").append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = ").append(item.getTotalPrice()).append(" BDT\n");
        }

        outputdetailsLabel.setText(details.toString());
    }

    @FXML
    private void backOA(javafx.event.ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", actionEvent);
    }

    @FXML
    private void logoutOA(javafx.event.ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }
}
