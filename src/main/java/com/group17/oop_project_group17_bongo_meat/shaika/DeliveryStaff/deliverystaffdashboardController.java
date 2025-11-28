package com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

import javafx.event.ActionEvent;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class deliverystaffdashboardController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void contactcustomerOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/ContactCustomer.fxml", actionEvent);
    }


    @javafx.fxml.FXML
    public void collectorderOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/CollectOrderfromPackageunit.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generatepdfOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/GeneratePdf.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void viewAssignedvehicleOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/ViewAssignedVehicleDetails.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void confirmdeliveryOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/ConfirmDelivery.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void receivedeliveryOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/RecieveDeliveryAssignment.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void reportdeliveryissueOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/ReportDeliveryIssue.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void viewRatingsOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/ViewRating.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void logoutOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }
}