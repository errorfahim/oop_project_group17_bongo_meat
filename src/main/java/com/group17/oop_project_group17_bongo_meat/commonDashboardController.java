package com.group17.oop_project_group17_bongo_meat;

import javafx.event.ActionEvent;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;
public class commonDashboardController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void admin(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void farmManger(ActionEvent actionEvent) throws  IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml", actionEvent);
    }
}