package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class AuditSystemLogsAndUserActivitiesController {

    @FXML
    private ComboBox<String> userTypeCB;
    @FXML
    private ComboBox<String> monthCB;
    @FXML
    private ComboBox<String> yearCB;

    @FXML
    private TableColumn<Map, String> actionCol;
    @FXML
    private TableColumn<Map, String> deatilsCol;
    @FXML
    private TableColumn<Map, String> timeCol;
    @FXML
    private TableColumn<Map, String> userCol;

    @FXML
    private TableView<Map<String, String>> logInfoTableView;

    @FXML
    public void initialize() {

        // Year ComboBox
        yearCB.setItems(FXCollections.observableArrayList("2025", "2024"));

        // Month ComboBox
        monthCB.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));

        // User Type ComboBox
        userTypeCB.setItems(FXCollections.observableArrayList(
                "All", "Customer", "Deliveryman", "Admin", "Farm Manager",
                "Vet Officer", "Slaughter House Manager", "Logistics Officer"
        ));

        // Map column keys
        timeCol.setCellValueFactory(new MapValueFactory<>("time"));
        userCol.setCellValueFactory(new MapValueFactory<>("user"));
        actionCol.setCellValueFactory(new MapValueFactory<>("action"));
        deatilsCol.setCellValueFactory(new MapValueFactory<>("details"));
    }

    @FXML
    public void searchLogInfoButton(ActionEvent actionEvent) {

        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();

        // Dummy log row #1
        Map<String, String> log1 = new HashMap<>();
        log1.put("time", "2025-01-12 10:34 AM");
        log1.put("user", "Admin");
        log1.put("action", "Logged In");
        log1.put("details", "Admin accessed the system dashboard");

        // Dummy log row #2
        Map<String, String> log2 = new HashMap<>();
        log2.put("time", "2025-01-12 11:00 AM");
        log2.put("user", "Customer");
        log2.put("action", "Placed Order");
        log2.put("details", "Order ID #BM-1122 placed successfully");

        // Dummy log row #3
        Map<String, String> log3 = new HashMap<>();
        log3.put("time", "2025-01-13 09:12 AM");
        log3.put("user", "Deliveryman");
        log3.put("action", "Order Pickup");
        log3.put("details", "Picked up Order ID #BM-1122");

        data.addAll(log1, log2, log3);

        // Load dummy data into table
        logInfoTableView.setItems(data);
    }

    @FXML
    public void nextButton(ActionEvent actionEvent) {
        // Navigation code here if needed
    }

    @FXML
    public void backButton(ActionEvent actionEvent)throws IOException {
        switchTo ("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml",actionEvent);
    }
}
