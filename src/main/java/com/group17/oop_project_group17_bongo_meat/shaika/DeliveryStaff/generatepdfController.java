package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import com.group17.oop_project_group17_bongo_meat.shaika.Customer.Order;
import com.group17.oop_project_group17_bongo_meat.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Table;
import org.openpdf.text.pdf.PdfWriter;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Table;
import org.openpdf.text.pdf.PdfWriter;


import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class generatepdfController {

    // ---------------- UI Elements ----------------
    @FXML private TableView<DeliveredRow> pdftable;
    @FXML private TableColumn<DeliveredRow, String> deliveryidcol;
    @FXML private TableColumn<DeliveredRow, String> orderidcol;
    @FXML private TableColumn<DeliveredRow, String> deliverytimecol;
    @FXML private TableColumn<DeliveredRow, String> customernamecol;

    @FXML private Label outputLabel;

    // ---------------- File Names ----------------
    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String ORDER_FILE = "orders.dat";
    private static final String USER_FILE = "users.bin";

    private ArrayList<DeliveryAssignment> allAssignments;
    private ArrayList<Order> allOrders;
    private ArrayList<User> allUsers;

    // =============================================================
    // INITIALIZE
    // =============================================================
    @FXML
    public void initialize() {
        deliveryidcol.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        orderidcol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        deliverytimecol.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));
        customernamecol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        loadDataFromFiles();
        loadDeliveredOrdersIntoTable();
    }

    // =============================================================
    // LOAD ALL FILE DATA
    // =============================================================
    private void loadDataFromFiles() {
        allAssignments = loadDeliveryAssignments();
        allOrders = loadOrders();
        allUsers = loadUsers();
    }

    private ArrayList<DeliveryAssignment> loadDeliveryAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELIVERY_FILE))) {
            return (ArrayList<DeliveryAssignment>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<Order> loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_FILE))) {
            return (ArrayList<Order>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            return (ArrayList<User>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // =============================================================
    // LOAD DELIVERED INTO TABLE
    // =============================================================
    private void loadDeliveredOrdersIntoTable() {

        ObservableList<DeliveredRow> rows = FXCollections.observableArrayList();

        for (DeliveryAssignment da : allAssignments) {

            if (da.getStatus().equalsIgnoreCase("Delivered")) {

                // Find Order
                Order order = allOrders.stream()
                        .filter(o -> o.getOrderId().equals(da.getOrderId()))
                        .findFirst()
                        .orElse(null);

                // Find Customer
                User customer = null;
                if (order != null) {
                    customer = allUsers.stream()
                            .filter(u -> u.getEmail().equals(order.getCustomerEmail()))
                            .findFirst()
                            .orElse(null);
                }

                String customerName =
                        (customer != null) ? customer.getName() : "Unknown Customer";

                rows.add(new DeliveredRow(
                        da.getDeliveryId(),
                        da.getOrderId(),
                        da.getDeliveryTime(),
                        customerName
                ));
            }
        }

        pdftable.setItems(rows);

        outputLabel.setText("Delivered orders loaded successfully.");
    }

    // =============================================================
    // GENERATE PDF BUTTON
    // =============================================================
    @FXML
    public void generatepdfOA(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Delivered Orders PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Document (*.pdf)", "*.pdf")
        );

        File chosenFile = fileChooser.showSaveDialog(outputLabel.getScene().getWindow());
        if (chosenFile == null) return;

        try {

            Document pdfDoc = new Document();
            PdfWriter.getInstance(pdfDoc, new FileOutputStream(chosenFile));

            pdfDoc.open();
            pdfDoc.add(new Paragraph("Delivered Orders Report"));
            pdfDoc.add(new Paragraph("Generated on: " + LocalDate.now()));
            pdfDoc.add(new Paragraph(" "));
            pdfDoc.add(new Paragraph("Total Deliveries: " + pdftable.getItems().size()));
            pdfDoc.add(new Paragraph(" "));

            Table pdfTable = new Table(4);
            pdfTable.addCell("Delivery ID");
            pdfTable.addCell("Order ID");
            pdfTable.addCell("Delivery Time");
            pdfTable.addCell("Customer Name");

            for (DeliveredRow row : pdftable.getItems()) {
                pdfTable.addCell(row.getDeliveryId());
                pdfTable.addCell(row.getOrderId());
                pdfTable.addCell(row.getDeliveryTime());
                pdfTable.addCell(row.getCustomerName());
            }

            pdfDoc.add(pdfTable);
            pdfDoc.close();

            outputLabel.setText("PDF generated successfully.");

        } catch (IOException | DocumentException e) {
            outputLabel.setText("Error generating PDF.");
        }
    }

    // =============================================================
    // NAVIGATION
    // =============================================================
    @FXML
    public void backOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/deliveryStaffDashboard.fxml", event);
    }

    @FXML
    public void LogoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }

    // =============================================================
    // INNER CLASS FOR TABLE ROWS
    // =============================================================
    public static class DeliveredRow {

        private final String deliveryId;
        private final String orderId;
        private final String deliveryTime;
        private final String customerName;

        public DeliveredRow(String deliveryId, String orderId, String deliveryTime, String customerName) {
            this.deliveryId = deliveryId;
            this.orderId = orderId;
            this.deliveryTime = deliveryTime;
            this.customerName = customerName;
        }

        public String getDeliveryId() { return deliveryId; }
        public String getOrderId() { return orderId; }
        public String getDeliveryTime() { return deliveryTime; }
        public String getCustomerName() { return customerName; }
    }
}
