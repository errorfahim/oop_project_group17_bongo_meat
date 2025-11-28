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

public class LogisticManagerDashboardController {
    @javafx.fxml.FXML
    private VBox buttonsVBox;
    @javafx.fxml.FXML
    private Label iDLabel;
    @javafx.fxml.FXML
    private Label nameLabel;
    @javafx.fxml.FXML
    private Button backOutButtonOnAction;

    @javafx.fxml.FXML
    public void exportContainerAllocationButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/exportContainerAllocationForm.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Export Container Allocation Form");
        stage.show();
    }


    @javafx.fxml.FXML
    public void transportScheduleButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/transportScheduleForm.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Transport Schedule Form");
        stage.show();
    }

    @javafx.fxml.FXML
    public void packagingInventoryButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/packagingInventoryView.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Packaging Inventory View");
        stage.show();
    }

    @javafx.fxml.FXML
    public void coldChainButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/coldChainMonitorView.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Cold Chain Monitor View");
        stage.show();
    }

    @javafx.fxml.FXML
    public void exportClearanceButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/exportClearanceForm.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Export Clearance Form");
        stage.show();
    }

    @javafx.fxml.FXML
    public void fleetMaintenanceButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/fleetMaintenanceView.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Fleet Maintenance View");
        stage.show();
    }

    @javafx.fxml.FXML
    public void packagingCoordinationButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/packagingCoordinationForm.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Packaging Coordination Form");
        stage.show();
    }

    @javafx.fxml.FXML
    public void warehouseDispatchButtonOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Zainab/warehouseDispatchView.fxml"));
        root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Warehouse Dispatch");
        stage.show();
    }
}


