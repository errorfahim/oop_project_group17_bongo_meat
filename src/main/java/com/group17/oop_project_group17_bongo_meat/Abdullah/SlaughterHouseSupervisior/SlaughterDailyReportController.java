package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class SlaughterDailyReportController
{
    @javafx.fxml.FXML
    private ComboBox filterByComboBox;
    @javafx.fxml.FXML
    private Label outputLabel;
    @javafx.fxml.FXML
    private TableColumn batchIDColumn;
    @javafx.fxml.FXML
    private TableColumn qaAndVertinaryColumn;
    @javafx.fxml.FXML
    private TableColumn arrivalTime;
    @javafx.fxml.FXML
    private TableColumn statusColumn;
    @javafx.fxml.FXML
    private DatePicker dailyReportDatePicker;
    @javafx.fxml.FXML
    private TableColumn slaughterTimeColumn;
    @javafx.fxml.FXML
    private TableColumn typeColumn;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void downloadPDFButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void refreshButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) {
    }
}