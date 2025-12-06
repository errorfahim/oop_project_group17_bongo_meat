package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.event.ActionEvent;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class SlaughterHouseSupervisiorDashboardController {

    @javafx.fxml.FXML
    public void initialize() {
        // Optional initialization
    }

    @javafx.fxml.FXML
    public void incomingLivestockVetInspectionRequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/IncomingLivestockVetInspectionRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void recordIncomingLivestockButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/RecordIncomingLivestock.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void requestQAForIncomingLivestockButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/RequestQAForIncomingLivestock.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void logOutButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void assignLivestockForSlaughterButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/AssignLivestockForSlaughter.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void slaughterDailyReport(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterDailyReport.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void slaughterOutputReportButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterOutputReport.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void meatTransferRequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/MeatTransferRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void farmDeliveryButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/FarmDelivery.fxml", actionEvent);
    }
}
