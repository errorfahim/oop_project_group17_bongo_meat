package com.group17.oop_project_group17_bongo_meat;

import java.io.*;
import java.util.ArrayList;

public class ProductService {

    private static final String PRODUCT_FILE = "products.dat"; // binary file path

    // Load products from binary file
    public static ArrayList<ProductDetails> loadProducts() {
        ArrayList<ProductDetails> list = new ArrayList<>();

        File file = new File(PRODUCT_FILE);
        if (!file.exists()) {
            // If file doesn't exist, create sample products
            list.add(new ProductDetails("Beef Steak", "Beef", 500, 10));
            list.add(new ProductDetails("Chicken Breast", "Chicken", 200, 20));
            list.add(new ProductDetails("Mutton Chops", "Mutton", 450, 15));
            list.add(new ProductDetails("Hilsa Fish", "Fish", 600, 8));

            saveProducts(list); // save to file
            return list;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            list = (ArrayList<ProductDetails>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Save products to binary file
    public static void saveProducts(ArrayList<ProductDetails> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE))) {
            oos.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

