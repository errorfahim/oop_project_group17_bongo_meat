package com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

import javafx.event.ActionEvent;
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
    public void customerQualityComplaintsButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/CustomerQualityComplaints.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void deliveryQualityMonitoringButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/DeliveryQualityMonitoring.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generateReport(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/GenerateReport.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void preSlaughterBatchApprovalButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/PreSlaughterBatchApproval.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void veterinaryDocumentVerificationButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/VeterinaryDocumentVerification.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/NextScene.fxml", actionEvent);
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
    public void backButton(ActionEvent actionEvent) {

    }

    @javafx.fxml.FXML
    public void overseeMeatQualityTestingButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/QAOfficer/OverseeMeatQualityTesting", actionEvent);
    }
}
