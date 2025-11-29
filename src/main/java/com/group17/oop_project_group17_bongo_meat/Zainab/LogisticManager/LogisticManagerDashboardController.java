package com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;
import com.group17.oop_project_group17_bongo_meat.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class LogisticManagerDashboardController {
    @javafx.fxml.FXML
    private VBox buttonsVBox;
    @javafx.fxml.FXML
    private Label iDLabel;
    @javafx.fxml.FXML
    private Label nameLabel;

    @javafx.fxml.FXML
    public void exportContainerAllocationButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/exportContainerAllocationForm.fxml", actionEvent);
    }


    @javafx.fxml.FXML
    public void transportScheduleButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/transportScheduleForm.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void packagingInventoryButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/packagingInventoryView.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void coldChainButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/coldChainMonitorView.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void exportClearanceButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/exportClearanceForm.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void fleetMaintenanceButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/fleetMaintenanceView.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void packagingCoordinationButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/packagingCoordinationForm.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void warehouseDispatchButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/LogisticManager/warehouseDispatchView.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void backOutButtonOnAction(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }
}


