package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;



public class ManageUserAccountsController
{
    @javafx.fxml.FXML
    private ComboBox userRoleCBFilter;
    @javafx.fxml.FXML
    private TableView userTableView;
    @javafx.fxml.FXML
    private TableColumn userIdCol;
    @javafx.fxml.FXML
    private ComboBox userRoleCB;
    @javafx.fxml.FXML
    private TableColumn userStatusCol;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private TableColumn userRoleCol;
    @javafx.fxml.FXML
    private TextField userName;
    @javafx.fxml.FXML
    private TextField userId;
    @javafx.fxml.FXML
    private TableColumn userNameCol;
    @javafx.fxml.FXML
    private TableColumn passwordCol;
    @javafx.fxml.FXML
    private ComboBox userStatusCB;
    @javafx.fxml.FXML
    private ComboBox userStatusCBFilter;
    @javafx.fxml.FXML
    private TextField password;

    private ArrayList<UserManagement> userList = new ArrayList<>();
    private final String FILE_NAME = "users_data.bin";

    @javafx.fxml.FXML
    public void initialize() {
        userRoleCB.setItems(FXCollections.observableArrayList("Admin", "Manager", "Worker"));
        userStatusCB.setItems(FXCollections.observableArrayList("Active", "Inactive"));

        userRoleCBFilter.setItems(FXCollections.observableArrayList("All", "Admin", "Manager", "Worker"));
        userStatusCBFilter.setItems(FXCollections.observableArrayList("All", "Active", "Inactive"));

        userRoleCBFilter.setValue("All");
        userStatusCBFilter.setValue("All");

        // Bind Table Columns
        userIdCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userId"));
        userNameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userName"));
        userRoleCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userRole"));
        userStatusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userStatus"));
        passwordCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("password"));


        loadData();
        userTableView.setItems(FXCollections.observableArrayList(userList));
    }

    private String generateUserId() {
        return "U-" + UUID.randomUUID().toString().substring(0, 8);
    }


    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) throws IOException{
        switchTo("", actionEvent);
    }


    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws  IOException{
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void addUserButton(ActionEvent actionEvent) {
        try {
            String id = generateUserId();
            String name = userName.getText();
            String role = userRoleCB.getValue().toString();
            String status = userStatusCB.getValue().toString();
            String pass = password.getText();

            if (name.isEmpty() || role == null || status == null || pass.isEmpty()) {
                label.setText("Please fill all fields!");
                return;
            }

            UserManagement user = new UserManagement(id, name, role, status, pass);
            userList.add(user);

            saveData();

            userTableView.setItems(FXCollections.observableArrayList(userList));

            label.setText("User added successfully! ID: " + id);

            clearForm();

        } catch (Exception e) {
            label.setText("Error adding user!");
        }
    }

    private void clearForm() {
        userName.clear();
        password.clear();
        userRoleCB.setValue(null);
        userStatusCB.setValue(null);
    }


    @javafx.fxml.FXML
    public void filterButton(ActionEvent actionEvent) {

        String selectedRole = userRoleCBFilter.getValue().toString();
        String selectedStatus = userStatusCBFilter.getValue().toString();

        ObservableList<UserManagement> filteredList = FXCollections.observableArrayList();

        for (UserManagement u : userList) {
            boolean roleMatch = selectedRole.equals("All") || u.getUserRole().equals(selectedRole);
            boolean statusMatch = selectedStatus.equals("All") || u.getUserStatus().equals(selectedStatus);

            if (roleMatch && statusMatch) {
                filteredList.add(u);
            }
        }

        userTableView.setItems(filteredList);
        label.setText("Filter applied.");
    }
    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(userList);
        } catch (IOException e) {
            System.out.println("Save Error: " + e.getMessage());
        }
    }

    private void loadData() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            userList = (ArrayList<UserManagement>) in.readObject();
        } catch (Exception e) {
            System.out.println("Load Error: " + e.getMessage());
        }
    }
}