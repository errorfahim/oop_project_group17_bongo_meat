package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import com.group17.oop_project_group17_bongo_meat.ProductDetails;
import com.group17.oop_project_group17_bongo_meat.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class ManageProductCatalogAndPricingController implements Initializable {

    @FXML private TableView<ProductDetails> productTable;
    @FXML private TableColumn<ProductDetails, String> nameCol;
    @FXML private TableColumn<ProductDetails, String> categoryCol;
    @FXML private TableColumn<ProductDetails, Double> priceCol;
    @FXML private TableColumn<ProductDetails, Integer> stockCol;

    @FXML private TextField productNameTF;
    @FXML private TextField priceTF;
    @FXML private TextField stockTF;
    @FXML private ComboBox<String> catagoryCombobox;

    private ObservableList<ProductDetails> productList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Table column mapping using PropertyValueFactory -> uses getName(), getCategory(), getPrice(), getStock()
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Populate category combobox (adjust categories as needed)
        catagoryCombobox.setItems(FXCollections.observableArrayList("Beef", "Chicken", "Mutton", "Fish", "Other"));

        // Load products from file via ProductService
        ArrayList<ProductDetails> loaded = ProductService.loadProducts();
        productList = FXCollections.observableArrayList(loaded);
        productTable.setItems(productList);

        // When admin clicks a row, populate input fields
        productTable.setOnMouseClicked(e -> fillFieldsFromTable());
    }

    // Fill text fields when a table row is selected
    private void fillFieldsFromTable() {
        ProductDetails selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        productNameTF.setText(selected.getName());
        priceTF.setText(String.valueOf(selected.getPrice()));
        stockTF.setText(String.valueOf(selected.getStock()));
        catagoryCombobox.setValue(selected.getCategory());
    }

    // Add new product
    @FXML
    public void addtoproductOA(ActionEvent event) {
        String name = productNameTF.getText().trim();
        String category = catagoryCombobox.getValue();
        String priceText = priceTF.getText().trim();
        String stockText = stockTF.getText().trim();

        if (name.isEmpty() || category == null || priceText.isEmpty() || stockText.isEmpty()) {
            showAlert("Input Error", "All fields must be filled!");
            return;
        }

        double price;
        int stock;
        try {
            price = Double.parseDouble(priceText);
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Price must be a number and Stock must be an integer.");
            return;
        }

        if (price <= 0) {
            showAlert("Validation Error", "Price must be greater than 0.");
            return;
        }
        if (stock < 0) {
            showAlert("Validation Error", "Stock cannot be negative.");
            return;
        }

        ProductDetails newProduct = new ProductDetails(name, category, price, stock);
        productList.add(newProduct);
        saveProducts();
        clearInputs();
        showInfo("Product Added", "New product added successfully.");
    }

    // Update selected product
    @FXML
    public void updateproductlistOA(ActionEvent event) {
        ProductDetails selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a product to update.");
            return;
        }

        String name = productNameTF.getText().trim();
        String category = catagoryCombobox.getValue();
        String priceText = priceTF.getText().trim();
        String stockText = stockTF.getText().trim();

        if (name.isEmpty() || category == null || priceText.isEmpty() || stockText.isEmpty()) {
            showAlert("Input Error", "All fields must be filled!");
            return;
        }

        double price;
        int stock;
        try {
            price = Double.parseDouble(priceText);
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Price must be numeric and stock must be integer.");
            return;
        }

        if (price <= 0) {
            showAlert("Validation Error", "Price must be greater than 0.");
            return;
        }
        if (stock < 0) {
            showAlert("Validation Error", "Stock cannot be negative.");
            return;
        }

        selected.setName(name);
        selected.setCategory(category);
        selected.setPrice(price);
        selected.setStock(stock);

        productTable.refresh();
        saveProducts();
        showInfo("Product Updated", "Product updated successfully.");
    }

    // Delete selected product
    @FXML
    public void deleteproductOA(ActionEvent event) {
        ProductDetails selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a product to delete.");
            return;
        }

        productList.remove(selected);
        saveProducts();
        clearInputs();
        showInfo("Product Deleted", "Product deleted successfully.");
    }

    // Go back (use your SceneSwitcher or FXML path)
    @FXML
    public void backOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashboard.fxml", event);
    }

    @FXML
    public void logoutOA(ActionEvent event) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Login.fxml", event);
    }

    // Save current productList to file using ProductService
    private void saveProducts() {
        ArrayList<ProductDetails> temp = new ArrayList<>(productList);
        ProductService.saveProducts(temp);
    }

    // Utility: clear input fields
    private void clearInputs() {
        productNameTF.clear();
        priceTF.clear();
        stockTF.clear();
        catagoryCombobox.getSelectionModel().clearSelection();
    }

    // Utility: show error alert
    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    // Utility: show info alert
    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
