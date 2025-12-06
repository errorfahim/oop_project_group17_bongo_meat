
package com.group17.oop_project_group17_bongo_meat.shaika.Customer;

import com.group17.oop_project_group17_bongo_meat.CartItem;
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

public class browseproductController implements Initializable {

    //viewing products
    @FXML private TableView<ProductDetails> productTable;
    @FXML private TableColumn<ProductDetails, String> nameCol;
    @FXML private TableColumn<ProductDetails, String> categoryCol;
    @FXML private TableColumn<ProductDetails, Double> priceCol;
    @FXML private TableColumn<ProductDetails, Integer> stockCol;

    @FXML private ComboBox<String> filterCategoryCB;
    @FXML private TextField maxPriceTF;

    //Adding to cart
    @FXML private TextField productNameTF;
    @FXML private TextField quantityTF;

    //for cart table
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> cartNameCol;
    @FXML private TableColumn<CartItem, Integer> cartQtyCol;
    @FXML private TableColumn<CartItem, Double> cartTotalCol;

    @FXML private Label subtotalLbl;

    private ObservableList<ProductDetails> productList;
    private ObservableList<CartItem> cartList = FXCollections.observableArrayList();

    private double subtotal = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //loading products from file
        ArrayList<ProductDetails> loaded = ProductService.loadProducts();
        productList = FXCollections.observableArrayList(loaded);
        productTable.setItems(productList);

        //for product table
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //for cart table
        cartNameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getProduct().getName())
        );
        cartQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartTotalCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        cartTable.setItems(cartList);

        //combobox
        filterCategoryCB.getItems().addAll("All", "Beef", "Chicken", "Mutton", "Fish");
        filterCategoryCB.setValue("All");

        updateSubtotal();
    }

    //filter
    @FXML
    void applyFilter(ActionEvent event) {

        String selectedCategory = filterCategoryCB.getValue();
        String priceText = maxPriceTF.getText().trim();

        productList.clear();
        ArrayList<ProductDetails> all = ProductService.loadProducts();

        for (ProductDetails p : all) {
            boolean match = true;

            if (!selectedCategory.equals("All") &&
                    !p.getCategory().equalsIgnoreCase(selectedCategory))
                match = false;

            if (!priceText.isEmpty()) {
                try {
                    double maxPrice = Double.parseDouble(priceText);
                    if (maxPrice < 0 || p.getPrice() > maxPrice)
                        match = false;
                } catch (Exception e) {
                    showAlert("Invalid Price", "Price must be a number.");
                    return;
                }
            }

            if (match) productList.add(p);
        }
    }

    private void updateSubtotal() {

        subtotalLbl.setText("Subtotal: " + subtotal + " Tk");
    }
    @FXML
    private void addToCart(ActionEvent event) {
        String productName = productNameTF.getText().trim();
        int qty;

        //validation
        try {
            qty = Integer.parseInt(quantityTF.getText());
        } catch (NumberFormatException e) {
            subtotalLbl.setText("Enter a valid quantity!");
            return;
        }

        if (productName.isEmpty() || qty <= 0) {
            subtotalLbl.setText("Enter product name and quantity!");
            return;
        }

        //Finding product
        ProductDetails selectedProduct = null;
        for (ProductDetails p : productTable.getItems()) {
            if (p.getName().equalsIgnoreCase(productName)) {
                selectedProduct = p;
                break;
            }
        }

        if (selectedProduct == null) {
            subtotalLbl.setText("Product not found in table!");
            return;
        }

        //adding to the cart
        CartItem item = new CartItem(selectedProduct, qty);
        Cart.addItem(item);

        // Updating cartTable
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList(Cart.getCartItems());
        cartTable.setItems(cartItems);

        // Setting the cartTable columns if not set yet
        cartNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getProduct().getName()));
        cartQtyCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(
                cellData.getValue().getQuantity()).asObject());
        cartTotalCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
                cellData.getValue().getTotalPrice()).asObject());

        // Update subtotal
        double subtotal = 0.0;
        for (CartItem c : Cart.getCartItems()) {
            subtotal += c.getTotalPrice();
        }
        subtotalLbl.setText("Subtotal: " + subtotal + " Tk");

        // Clear input fields
        productNameTF.clear();
        quantityTF.clear();
    }

    //for checkout
    @FXML
    void checkout(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/PlaceOrder.fxml", actionEvent);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }

    @FXML
    public void logoutOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/login.fxml", actionEvent);
    }

    @FXML
    public void backOA(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/shaika/Customer/CustomerDashboard.fxml", actionEvent);
    }
}
