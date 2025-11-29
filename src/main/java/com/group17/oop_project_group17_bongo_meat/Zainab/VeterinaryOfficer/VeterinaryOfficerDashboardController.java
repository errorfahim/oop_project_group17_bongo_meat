package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;
import javafx.event.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class VeterinaryOfficerDashboardController {
    @javafx.fxml.FXML
    private VBox buttonsVBox;

    @javafx.fxml.FXML
    public void meatQualityTestButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/meatQualityTestForm.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void backOutButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void vaccinationRecordButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/vaccinationScheduleRecord.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void medicineInventoryButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/medicineInventory.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void preSlaughterInspectionButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/preSlaughterInspectionForm.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void veterinaryActivityReportsButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/vetActivityReport.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void feedingandNutritionDataRecordButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/nutritionFeedingRecord.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void animalHealthCheckupButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/animalHealthCheckForm.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void diseaseOutbreakAlertsButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/diseaseAlertForm.fxml", actionEvent);
    }
}
