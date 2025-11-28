package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import com.group17.oop_project_group17_bongo_meat.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class SlaughterHouseSupervisiorDashboardController {

    @javafx.fxml.FXML
    public void initialize() {
        // Any initialization logic here
    }

    @javafx.fxml.FXML
    public void recordIncomingLivestockButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/RecordIncomingLivestock.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void vetCheckRequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/VetCheckRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void confirmFarmDeliveriesButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/ConfirmFarmDeliveries.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void qaInspectionRequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/QAInspectionRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generateDailyReport(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/DailyReport.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void assignSlaughterBatchButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/AssignSlaughterBatch.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void dispatchRequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/DispatchRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void recordSlaughterOperationsButton(ActionEvent actionEvent) throws IOException {
        switchTo("com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/RecordSlaughterOperations.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void logOutButton(ActionEvent actionEvent) throws IOException {
        LoginController.loggedInEmail = null;
        switchTo("com/group17/oop_project_group17_bongo_meat/Login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
        // Placeholder for future functionality
    }
}