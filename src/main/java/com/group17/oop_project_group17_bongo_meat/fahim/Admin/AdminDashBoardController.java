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
}