package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.LoginController;
import javafx.event.ActionEvent;

import java.io.IOException;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class customerdashboardController
{
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void browseandaddproductOA(ActionEvent actionEvent) throws IOException {
        Cart.setCustomerEmail(LoginController.loggedInEmail);
        Cart.clearCart();
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/Browse_and_add_products.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void providefeedbackOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/ProvideFeedback.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void cancelorderOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/Cancelormodifyorder.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void manageprofileOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/ManageCustomerProfile.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void vieworderOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/ViewOrderHistory.fxml", actionEvent);
    }

    @Deprecated
    public void placeorderOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/PlaceOrder.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void logoutOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }



    @javafx.fxml.FXML
    public void rateDeliverystaffOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/RateDeliveryStaff.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void gobackOA(ActionEvent actionEvent) {
    }
}