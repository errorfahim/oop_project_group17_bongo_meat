package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import com.group17.oop_project_group17_bongo_meat.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class reportdeliveryissueController {

    private static final String DELIVERY_FILE = "deliveryAssignments.dat";
    private static final String ISSUE_FILE = "deliveryIssues.dat";

    @FXML
    private TextField deliveryidTF;

    @FXML
    private DatePicker issuedatedatepicker;

    @FXML
    private TextArea descriptionTA;

    @FXML
    private Label outputLabel;

    @FXML
    private ListView<String> deliveryidListview;

    // ---------------- INITIALIZE ----------------
    @FXML
    public void initialize() {
        loadDeliveredDeliveries();
    }

    // Load only deliveries with status = Delivered
    private void loadDeliveredDeliveries() {
        ArrayList<DeliveryAssignment> list = loadDeliveryAssignments();
        deliveryidListview.getItems().clear();

        for (DeliveryAssignment da : list) {
            if (da.getStatus().equalsIgnoreCase("Accepted") ||
                    da.getStatus().equalsIgnoreCase("Out for Delivery")||da.getStatus().equalsIgnoreCase("Delivered")){
                deliveryidListview.getItems().add(da.getDeliveryId());
            }
        }
    }

    private ArrayList<DeliveryAssignment> loadDeliveryAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DELIVERY_FILE))) {
            return (ArrayList<DeliveryAssignment>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --------------- REPORT ISSUE BUTTON ----------------
    @FXML
    public void reportIssueOA(ActionEvent event) {

        String deliveryId = deliveryidTF.getText().trim();
        LocalDate issueDate = issuedatedatepicker.getValue();
        String description = descriptionTA.getText().trim();

        // VALIDATION CHECKS -------------------
        if (deliveryId.isEmpty() || description.isEmpty() || issueDate == null) {
            outputLabel.setText("⚠ Please fill all fields!");
            return;
        }

        if (issueDate.isAfter(LocalDate.now())) {
            outputLabel.setText("⚠ Issue date cannot be in the future!");
            return;
        }

        // Check if deliveryID exists AND is Delivered
        boolean isValid = false;
        for (String id : deliveryidListview.getItems()) {
            if (id.equalsIgnoreCase(deliveryId)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            outputLabel.setText("❌ Invalid Delivery ID!");
            return;
        }

        // SAVE ISSUE INTO FILE ----------------
        DeliveryIssue issue = new DeliveryIssue(deliveryId, issueDate, description);

        ArrayList<DeliveryIssue> issues = loadReportedIssues();
        issues.add(issue);
        saveReportedIssues(issues);

        outputLabel.setText("✔ Issue Reported Successfully!");

        // clear fields
        deliveryidTF.clear();
        issuedatedatepicker.setValue(null);
        descriptionTA.clear();
    }

    private ArrayList<DeliveryIssue> loadReportedIssues() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ISSUE_FILE))) {
            return (ArrayList<DeliveryIssue>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void saveReportedIssues(ArrayList<DeliveryIssue> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ISSUE_FILE))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------- BACK BUTTON ----------------
    @FXML
    public void backOA(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/deliveryStaffDashboard.fxml", event);
    }

    // ---------------- LOGOUT BUTTON ----------------
    @FXML
    public void logoutOA(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", event);
    }
}
