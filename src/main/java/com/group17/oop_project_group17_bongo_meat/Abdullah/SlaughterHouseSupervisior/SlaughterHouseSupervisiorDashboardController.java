package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.event.ActionEvent;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class SlaughterHouseSupervisiorDashboardController {

    @javafx.fxml.FXML
    public void initialize() {
        // Optional initialization
    }

    @javafx.fxml.FXML
    public void recordIncomingLivestockButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/RecordIncomingLivestock.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void vetCheckRequestButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/VetCheckRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void confirmFarmDeliveriesButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/ConfirmFarmDeliveries.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void qaInspectionRequestButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/QAInspectionRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generateDailyReport(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/DailyReport.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void assignSlaughterBatchButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/AssignSlaughterBatch.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void dispatchRequestButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/DispatchRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void recordSlaughterOperationsButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/RecordSlaughterOperations.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void logOutButton(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
        // Future features
    }
}
