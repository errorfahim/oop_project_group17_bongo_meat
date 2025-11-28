package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;
import com.group17.oop_project_group17_bongo_meat.LoginController;

public class providefeedbackController {

    @FXML private TableView<Order> deliveredOrdersTable;
    @FXML private TableColumn<Order, String> orderIdCol;
    @FXML private TableColumn<Order, String> productcol;
    @FXML private TableColumn<Order, Integer> qtycol;
    @FXML private TableColumn<Order, Double> totalcol;

    @FXML private TextField orderIdTF;
    @FXML private ComboBox<Integer> ratingCB;
    @FXML private TextArea commentTA;
    @FXML private Label outputLabel;

    private final String ORDER_FILE = "orders.dat";
    private final String FEEDBACK_FILE = "feedback.dat";
    private ObservableList<Order> deliveredOrdersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // ------------------ Setup Table Columns -------------------
        orderIdCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getOrderId()));
        productcol.setCellValueFactory(cellData -> {
            String products = "";
            for (var item : cellData.getValue().getItems()) {
                products += item.getProduct().getName() + "\n";
            }
            return new javafx.beans.property.SimpleStringProperty(products);
        });
        qtycol.setCellValueFactory(cellData -> {
            int totalQty = 0;
            for (var item : cellData.getValue().getItems()) totalQty += item.getQuantity();
            return new javafx.beans.property.SimpleIntegerProperty(totalQty).asObject();
        });
        totalcol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());

        // ------------------ Populate Rating ComboBox -------------------
        ratingCB.getItems().addAll(1,2,3,4,5);

        // ------------------ Load Delivered Orders -------------------
        loadDeliveredOrders();

        // ------------------ Table Selection Listener -------------------
        deliveredOrdersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldOrder, newOrder) -> {
            if (newOrder != null) {
                orderIdTF.setText(newOrder.getOrderId());
            }
        });
    }

    private void loadDeliveredOrders() {
        deliveredOrdersList.clear();
        ArrayList<Order> allOrders = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            allOrders = (ArrayList<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // ignore if file does not exist
        }

        // Filter delivered orders for current customer
        for (Order o : allOrders) {
            if ("Delivered".equalsIgnoreCase(o.getStatus()) &&
                    LoginController.loggedInEmail.equals(o.getCustomerEmail())) {
                deliveredOrdersList.add(o);
            }
        }

        deliveredOrdersTable.setItems(deliveredOrdersList);
    }

    @FXML
    private void saveFeedbackOA() {
        String orderId = orderIdTF.getText().trim();
        Integer rating = ratingCB.getValue();
        String comment = commentTA.getText().trim();

        if (orderId.isEmpty() || rating == null || comment.isEmpty()) {
            outputLabel.setText("Please fill all fields!");
            return;
        }

        Feedback feedback = new Feedback(orderId, LoginController.loggedInEmail, rating, comment);

        // Save feedback to file
        ArrayList<Feedback> allFeedback = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FEEDBACK_FILE))) {
            allFeedback = (ArrayList<Feedback>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // first feedback
        }

        allFeedback.add(feedback);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FEEDBACK_FILE))) {
            oos.writeObject(allFeedback);
            outputLabel.setText("Thank you for your feedback!");
            orderIdTF.clear();
            commentTA.clear();
            ratingCB.getSelectionModel().clearSelection();
        } catch (IOException e) {
            outputLabel.setText("Error saving feedback!");
            e.printStackTrace();
        }
    }

    @FXML
    private void backOA(javafx.event.ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", event);
    }

    @FXML
    private void logoutOA(javafx.event.ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }
}
