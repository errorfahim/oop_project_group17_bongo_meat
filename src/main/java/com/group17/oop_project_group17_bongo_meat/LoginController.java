package com.group17.oop_project_group17_bongo_meat;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class LoginController
{
    @javafx.fxml.FXML
    private TextField passwordTextField;
    @javafx.fxml.FXML
    private TextField emailOrPhoneNoTextField;
    @javafx.fxml.FXML
    private ComboBox userTypeCB;

    // ArrayList to store credentials
    private ArrayList<Login> users = new ArrayList<>();
    @javafx.fxml.FXML
    public void initialize() {
        // Add default user types
        userTypeCB.getItems().addAll("Admin", "Customer", "Seller");

        // Add some demo accounts to ArrayList
        users.add(new Login("admin@gmail.com", "12345", "Admin"));
        users.add(new Login("customer@gmail.com", "11111", "Customer"));
        users.add(new Login("seller@gmail.com", "22222", "Seller"));
    }

    @javafx.fxml.FXML
    public void loginButton(ActionEvent actionEvent) {
        String inputEmail = emailOrPhoneNoTextField.getText();
        String inputPassword = passwordTextField.getText();
        String inputUserType = userTypeCB.getValue().toString();
        if (inputEmail.isEmpty() || inputPassword.isEmpty() || inputUserType == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            return;
        }

        // Verify credentials
        for (Login user : users) {
            if (user.getEmailOrPhone().equals(inputEmail) &&
                    user.getPassword().equals(inputPassword) &&
                    user.getUserType().equals(inputUserType)) {

                showAlert(Alert.AlertType.INFORMATION, "Login Status", "Login Successful!");
                return;
            }
        }

        showAlert(Alert.AlertType.ERROR, "Login Failed", "Wrong credentials! Try again.");

    }

    private void showAlert(Alert.AlertType alertType, String error, String s) {
    }

    @javafx.fxml.FXML
    public void forgetPasswordButton(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, "Info", "Forget Password feature coming soon...");
    }

    @javafx.fxml.FXML
    public void registerHereButton(ActionEvent actionEvent) {

    }
}