package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import com.group17.oop_project_group17_bongo_meat.shaika.Customer.DeliveryRating;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class viewRatingController {

    @FXML
    private TableView<DeliveryRating> showratingtable;

    @FXML
    private TableColumn<DeliveryRating, String> deliveryidcol;

    @FXML
    private TableColumn<DeliveryRating, Integer> ratingcol;

    // If you want to show Order ID in future add:
    // @FXML private TableColumn<DeliveryRating, String> orderidcol;

    @FXML
    private Label outputLabel;

    private final String RATING_FILE = "deliveryRatings.dat";

    @FXML
    public void initialize() {

        // Set table columns
        deliveryidcol.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        ratingcol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        loadRatings();
    }

    /**
     * Loads all ratings from file (for now only 1 delivery staff).
     */
    private void loadRatings() {
        ArrayList<DeliveryRating> list = loadRatingList();

        if (list.isEmpty()) {
            outputLabel.setText("You have no ratings yet.");
            showratingtable.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<DeliveryRating> observableList = FXCollections.observableArrayList(list);
        showratingtable.setItems(observableList);

        outputLabel.setText("Ratings loaded successfully.");
    }

    /**
     * Reads rating list from binary file.
     */
    private ArrayList<DeliveryRating> loadRatingList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RATING_FILE))) {
            return (ArrayList<DeliveryRating>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @FXML
    public void logoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }

    @FXML
    public void backOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/deliveryStaffDashboard.fxml", event);
    }
}
