package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.SceneSwitcher;
import com.group17.oop_project_group17_bongo_meat.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class registeraccountController {

    @FXML private TextField nameTF;
    @FXML private TextField phoneTF;
    @FXML private TextField emailTF;
    @FXML private TextField PasswordTF;
    @FXML private TextField confirmPasswordTF;
    @FXML private TextField deliveryAddressTF;
    @FXML private Label outputlabel;

    private static final String USER_FILE = "users.bin";
    private List<User> userList = new ArrayList<>();

    @FXML
    private void initialize() {
        loadUsers(); // Load users from binary file on start
    }

    @FXML
    private void registerOA() {
        String name = nameTF.getText().trim();
        String phone = phoneTF.getText().trim();
        String email = emailTF.getText().trim();
        String password = PasswordTF.getText();
        String confirmPassword = confirmPasswordTF.getText();
        String address = deliveryAddressTF.getText().trim();

        // 1. Validate all fields
        if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || address.isEmpty()) {
            outputlabel.setTextFill(Color.RED);
            outputlabel.setText("Please fill all the fields!");
            return;
        }

        // 2. Check password match
        if(!password.equals(confirmPassword)) {
            outputlabel.setTextFill(Color.RED);
            outputlabel.setText("Passwords do not match!");
            return;
        }

        // 3. Check existing user
        for(User user : userList) {
            if(user.getEmail().equals(email)) {
                outputlabel.setTextFill(Color.RED);
                outputlabel.setText("Email already registered!");
                return;
            }
            if(user.getPhone().equals(phone)) {
                outputlabel.setTextFill(Color.RED);
                outputlabel.setText("Phone already registered!");
                return;
            }
        }

        // 4. Create and save new user
        User newUser = new User(name, email, phone, password, address);
        userList.add(newUser);
        saveUsers();

        outputlabel.setTextFill(Color.GREEN);
        outputlabel.setText("Registration Successful!");

        // Clear fields
        nameTF.clear();
        phoneTF.clear();
        emailTF.clear();
        PasswordTF.clear();
        confirmPasswordTF.clear();
        deliveryAddressTF.clear();
    }

    // Load users from binary file
    private void loadUsers() {
        File file = new File(USER_FILE);
        if(!file.exists()) return;

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            userList = (List<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Save users to binary file
    private void saveUsers() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            out.writeObject(userList);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backbuttonOA(ActionEvent actionEvent) throws IOException {
        SceneSwitcher.switchTo("/com/group17/oop_project_group17_bongo_meat/Login.fxml",actionEvent);
    }
}
