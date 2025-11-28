package com.group17.oop_project_group17_bongo_meat;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javax.imageio.IIOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class LoginController
{
    @javafx.fxml.FXML
    private TextField passwordTextField;
    @javafx.fxml.FXML
    private TextField emailOrPhoneNoTextField;
    @javafx.fxml.FXML
    private ComboBox<String> userTypeCB;
    public static String loggedInEmail;  // store currently logged-in customer email


    // ArrayList to store credentials
    private ArrayList<Login> users = new ArrayList<>();
    private ArrayList<User> customers = new ArrayList<>();


    @javafx.fxml.FXML
    public void initialize() {
        // Add default user types
        userTypeCB.getItems().addAll("Admin","FarmManager", "Customer", "Delivery staff","QAOfficer","SlaughterHouseSupervisior","vaterinaryOfficer","Logistic");

        // Add some demo accounts to ArrayList
        users.add(new Login("admin@gmail.com", "12345", "Admin"));
        users.add(new Login("customer@gmail.com", "123", "Customer"));
        users.add(new Login("deliveryman@gmail.com", "456", "Delivery staff"));
        users.add(new Login("supervisior@gmail.com", "0123", "SlaughterHouseSupervisor"));
        loadCustomersFromFile();
    }

    private void loadCustomersFromFile() {
        File file = new File("users.bin");

        if (!file.exists()) {
            System.out.println("users.bin not found → Creating new file later.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            customers = (ArrayList<User>) in.readObject();
            System.out.println("Loaded " + customers.size() + " customers.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error reading users.bin");
        }
    }
    @javafx.fxml.FXML
    public void loginButton(ActionEvent actionEvent) throws IOException {
        String inputEmail = emailOrPhoneNoTextField.getText();
        String inputPassword = passwordTextField.getText();
        if (userTypeCB.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a user type!");
            return;
        }
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
                if (inputUserType.equals("Customer")) {
                    switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", actionEvent);
                } else if (inputUserType.equals("Delivery staff")) {
                    switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/DeliveryStaff/DeliverystaffDashboard.fxml", actionEvent);
                } else if (inputUserType.equals("Admin")) {
                    switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashboard.fxml", actionEvent);}
                else if (inputUserType.equals("SlaughterHouseSupervisor")) {
                    switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", actionEvent);}
                return;
            }
        }
        if (inputUserType.equals("Customer")) {
            for (User c : customers) {
                if ((c.getEmail().equals(inputEmail) || c.getPhone().equals(inputEmail)) &&
                        c.getPassword().equals(inputPassword)) {
                    loggedInEmail = c.getEmail();

                    showAlert(Alert.AlertType.INFORMATION, "Login Status", "Customer Login Successful!");
                    switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", actionEvent);
                    return;
                }
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
    public void registerHereButton(ActionEvent actionEvent)throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/RegisterAccount.fxml", actionEvent);


    }
}