package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class QAOfficerDashboardController {

    @javafx.fxml.FXML
    public void initialize() {
        // Optional initialization
    }

    @javafx.fxml.FXML
    public void exportQualityCertificationButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/ExportQualityCertification.fxml", actionEvent);
    }



    @javafx.fxml.FXML
    public void logOutButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void incomingQARequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/IncomingQARequest.fxml", actionEvent);  // Added /Abdullah/QAOfficer/
    }

    @javafx.fxml.FXML
    public void meatTransferRequestButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/ConfirmMeatTestingRequest.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void requestLogisticForTransferMeatButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/RequestLogisticForTransferMeat.fxml", actionEvent);
    }

    @FXML
    public void dailyQaReportButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/DailyQAReport.fxml", actionEvent);
    }


    @FXML
    public void qaScheduleCheckButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/QAScheduleCheck.fxml", actionEvent);
    }

    @FXML
    public void multiDepartmentAuditButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/ConductMultiDepartmentQualityAuditandReporttoAdmin.fxml", actionEvent);
    }

    @FXML
    public void equipmentInspectionButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/EquipmentInspection.fxml", actionEvent);

    }
}
