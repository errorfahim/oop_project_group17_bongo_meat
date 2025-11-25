package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class IncomingLivestockVetInspectionRequestController
{
    @javafx.fxml.FXML
    private TextField detailsTextField;
    @javafx.fxml.FXML
    private DatePicker arrivalDatePicker;
    @javafx.fxml.FXML
    private ComboBox typeComboBox;
    @javafx.fxml.FXML
    private CheckBox noCheckBox;
    @javafx.fxml.FXML
    private CheckBox yesCheckBox;

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void sendRequestButton(ActionEvent actionEvent) {
    }
}