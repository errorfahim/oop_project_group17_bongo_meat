package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.LoginController;
import com.group17.oop_project_group17_bongo_meat.SceneSwitcher;
import com.group17.oop_project_group17_bongo_meat.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class manageCustomerProfileController {
    @javafx.fxml.FXML
    private TextField manageemailTF;
    @javafx.fxml.FXML
    private TextField managenameTF;
    @javafx.fxml.FXML
    private TextField managedeliveryaddressTF;
    @javafx.fxml.FXML
    private Label manageOutputLabel;
    @javafx.fxml.FXML
    private TextField managephoneTF;
    @javafx.fxml.FXML
    private TextField managepassTF;
    @javafx.fxml.FXML
    private TextField manageconfirmpassTF;
    private static final String USER_FILE = "users.bin";
    private List<User> userList = new ArrayList<>();
    private User currentUser;

    // Logged-in customer's email from LoginController
    private String loggedInEmail = LoginController.loggedInEmail;

    @javafx.fxml.FXML
    public void initialize() {
        loadUsers();
        loadCurrentUser();
    }
    /** Load all users from file */
    private void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            userList = (List<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Load the currently logged-in user's profile */
    private void loadCurrentUser() {
        if (userList.isEmpty() || loggedInEmail == null) return;

        for (User user : userList) {
            if (user.getEmail().equals(loggedInEmail)) {
                currentUser = user;
                break;
            }
        }

        if (currentUser == null) return;

        managenameTF.setText(currentUser.getName());
        managephoneTF.setText(currentUser.getPhone());
        manageemailTF.setText(currentUser.getEmail());
        managepassTF.setText(currentUser.getPassword());
        manageconfirmpassTF.setText(currentUser.getPassword());
        managedeliveryaddressTF.setText(currentUser.getAddress());

        // Email cant be editable
        manageemailTF.setEditable(false);
    }

    @FXML
    private void savechangesOA(ActionEvent event) {
        String name = managenameTF.getText().trim();
        String phone = managephoneTF.getText().trim();
        String password = managepassTF.getText();
        String confirmPassword = manageconfirmpassTF.getText();
        String address = managedeliveryaddressTF.getText().trim();

        // Validation
        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty()) {
            manageOutputLabel.setTextFill(Color.RED);
            manageOutputLabel.setText("Please fill all fields!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            manageOutputLabel.setTextFill(Color.RED);
            manageOutputLabel.setText("Passwords do not match!");
            return;
        }

        if (!phone.matches("\\d{11}")) { // Phone num 11 digits
            manageOutputLabel.setTextFill(Color.RED);
            manageOutputLabel.setText("Phone must be 11 digits!");
            return;
        }

        // Update user info
        currentUser.setName(name);
        currentUser.setPhone(phone);
        currentUser.setPassword(password);
        currentUser.setAddress(address);

        // Save updated users list
        saveUsers();

        manageOutputLabel.setTextFill(Color.GREEN);
        manageOutputLabel.setText("Profile Updated Successfully!");
    }


    private void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            out.writeObject(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void backOA(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", event);
    }


    @FXML
    private void logoutOA(ActionEvent event) throws IOException {
        SceneSwitcher.switchTo("/com/group17/oop_project_group17_bongo_meat/Login.fxml", event);
    }
}

