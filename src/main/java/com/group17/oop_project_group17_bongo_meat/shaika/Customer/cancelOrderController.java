package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.CartItem;
import com.group17.oop_project_group17_bongo_meat.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class cancelOrderController {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> orderidcol;
    @FXML
    private TableColumn<Order, String> productcol;
    @FXML
    private TableColumn<Order, Integer> qtycol;
    @FXML
    private TableColumn<Order, Double> pricecol;

    @FXML
    private TextField newqtyTF;
    @FXML
    private ComboBox<CartItem> orderItemCB;
    @FXML
    private Label outputmsgLabel;

    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private final String ORDER_FILE = "orders.dat";

    @FXML
    public void initialize() {

        // Table column setup
        orderidcol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrderId()));

        productcol.setCellValueFactory(cellData -> {
            StringBuilder products = new StringBuilder();
            for (CartItem item : cellData.getValue().getItems()) {
                products.append(item.getProduct().getName()).append("\n");
            }
            return new javafx.beans.property.SimpleStringProperty(products.toString());
        });

        qtycol.setCellValueFactory(cellData -> {
            int totalQty = 0;
            for (CartItem item : cellData.getValue().getItems()) {
                totalQty += item.getQuantity();
            }
            return new javafx.beans.property.SimpleIntegerProperty(totalQty).asObject();
        });

        pricecol.setCellValueFactory(cellData -> {
            double totalPrice = 0;
            for (CartItem item : cellData.getValue().getItems()) {
                totalPrice += item.getTotalPrice();
            }
            return new javafx.beans.property.SimpleDoubleProperty(totalPrice).asObject();
        });

        // Load only THIS customer's orders
        loadOrders();

        // Update orderItemCB when an order is selected
        orderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldOrder, newOrder) -> {
            orderItemCB.getItems().clear();
            if (newOrder != null) {
                orderItemCB.getItems().addAll(newOrder.getItems());
            }
        });
    }

    // Load ONLY orders belonging to logged in customer
    private void loadOrders() {
        orders.clear();
        ArrayList<Order> loadedOrders = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            loadedOrders = (ArrayList<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // file might not exist yet
        }

        String loggedInEmail = LoginController.loggedInEmail;

        for (Order o : loadedOrders) {
            if (o.getCustomerEmail() != null &&
                    LoginController.loggedInEmail.equals(o.getCustomerEmail()) &&
                    !o.getStatus().equalsIgnoreCase("Packed") &&
                    !o.getStatus().equalsIgnoreCase("Delivered") &&
                    !o.getStatus().equalsIgnoreCase("Cancelled")) {

                orders.add(o);
            }
        }

        orderTable.setItems(orders);
    }




    @FXML
    private void modifyOA(ActionEvent event) {

        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        CartItem selectedItem = orderItemCB.getValue();

        if (selectedOrder == null || selectedItem == null) {
            outputmsgLabel.setText("Select an order and item to modify!");
            return;
        }

        int newQty;
        try {
            newQty = Integer.parseInt(newqtyTF.getText());
        } catch (Exception e) {
            outputmsgLabel.setText("Enter a valid quantity!");
            return;
        }

        if (newQty <= 0) {
            outputmsgLabel.setText("Quantity must be greater than 0!");
            return;
        }

        // Update quantity
        selectedItem.setQuantity(newQty);

        // Recalculate subtotal
        double subtotal = 0;
        for (CartItem item : selectedOrder.getItems()) {
            subtotal += item.getTotalPrice();
        }
        selectedOrder.setSubtotal(subtotal);

        orderTable.refresh();
        saveOrders();

        outputmsgLabel.setText(
                "Updated " + selectedItem.getProduct().getName() +
                        " in Order " + selectedOrder.getOrderId()
        );

        newqtyTF.clear();
    }

    @FXML
    private void cancelorderOA(ActionEvent event) {

        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        CartItem selectedItem = orderItemCB.getValue();

        if (selectedOrder == null || selectedItem == null) {
            outputmsgLabel.setText("Select an order and item to cancel!");
            return;
        }

        selectedOrder.getItems().remove(selectedItem);

        // Recalculate subtotal
        double subtotal = 0;
        for (CartItem item : selectedOrder.getItems()) {
            subtotal += item.getTotalPrice();
        }
        selectedOrder.setSubtotal(subtotal);

        // If no items left → cancel whole order
        if (selectedOrder.getItems().isEmpty()) {
            selectedOrder.setStatus("Cancelled");
            orders.remove(selectedOrder);
        }

        orderTable.refresh();
        orderItemCB.getItems().clear();
        saveOrders();

        outputmsgLabel.setText(
                "Removed " + selectedItem.getProduct().getName() +
                        " from Order " + selectedOrder.getOrderId()
        );
    }

    // Save all orders back to file
    private void saveOrders() {

        ArrayList<Order> allOrders;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            allOrders = (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            allOrders = new ArrayList<>();
        }

        // Update matching orders
        for (Order updated : orders) {
            boolean found = false;

            for (int i = 0; i < allOrders.size(); i++) {
                if (allOrders.get(i).getOrderId().equals(updated.getOrderId())) {
                    allOrders.set(i, updated);
                    found = true;
                    break;
                }
            }

            if (!found) allOrders.add(updated);
        }

        // Save to file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(allOrders);
        } catch (IOException e) {
            outputmsgLabel.setText("Error saving orders!");
        }
    }

    @FXML
    private void backOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", actionEvent);
    }

    @FXML
    private void logoutOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }
}
