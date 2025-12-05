package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.event.ActionEvent;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class AdminDashBoardController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void manageproductOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/ManageProductCatalogAndPricing.fxml", actionEvent);

    }

    @javafx.fxml.FXML
    public void configSystemSettingsButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/ConfigureSystemSettings.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void financialTransactionButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/OverseeFinancialTransactionsAndInvoicing.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void monitoreandResolveButton(ActionEvent actionEvent)  throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/MonitorAndResolveSystemAlerts.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generateReportButton(ActionEvent actionEvent) throws  IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/GenerateSystemWidePerformanceReports.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void approveSupplierReqButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/ApproveSupplierRequests.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void manageUserAccountsButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/ManageUserAccounts.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void auditSystemLogButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AuditSystemLogsAndUserActivities.fxml", actionEvent);
    }
}