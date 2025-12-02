package com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class MedicineInventoryController {

    // ---------- FXML NODES ----------

    @FXML private TableView<MedicineInventoryItem> medicineListTableView;
    @FXML private TableColumn<MedicineInventoryItem, String> medicineNameCol;
    @FXML private TableColumn<MedicineInventoryItem, String> batchNoCol;
    @FXML private TableColumn<MedicineInventoryItem, Integer> quantityCol;
    @FXML private TableColumn<MedicineInventoryItem, String> dosageCol;
    @FXML private TableColumn<MedicineInventoryItem, String> expiryDateCol;
    @FXML private TableColumn<MedicineInventoryItem, String> statusCol;

    // Add new stock
    @FXML private TextField medicineNameTextField;
    @FXML private TextField batchNumberTextField;
    @FXML private TextField quantityTextField;
    @FXML private DatePicker expiryDatePicker;
    @FXML private TextField dosageTextField;

    // Record medicine usage
    @FXML private ComboBox<String> selectMedicineComboBox;
    @FXML private TextField usedQuantityTextField;      // must match fx:id of TextField in FXML
    @FXML private TextField batchIDButtonOnAction;
    @FXML private TextField dateUsedButtonOnAction;

    // Filter
    @FXML private ComboBox<String> filterComboBox;

    // Labels
    @FXML private Label selectedMeatBatchMessageLabel;
    @FXML private Label validationSuccessMessageLabel;
    @FXML private Label validationSuccessMessageLabel1;

    // ---------- DATA STRUCTURES ----------

    private final ObservableList<MedicineInventoryItem> masterInventoryList =
            FXCollections.observableArrayList();

    private final ObservableList<MedicineInventoryItem> displayedInventoryList =
            FXCollections.observableArrayList();

    private static final String INVENTORY_FILE = "medicineInventory.dat";
    private static final String PURCHASE_REQUEST_FILE = "medicinePurchaseRequests.dat";

    private MedicineInventoryItem selectedItem;

    @FXML
    public void initialize() {

        medicineNameCol.setCellValueFactory(data -> data.getValue().medicineNameProperty());
        batchNoCol.setCellValueFactory(data -> data.getValue().batchNoProperty());
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        dosageCol.setCellValueFactory(data -> data.getValue().dosageProperty());
        expiryDateCol.setCellValueFactory(data -> data.getValue().expiryDateProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());

        // TableView uses the displayed list (starts empty)
        medicineListTableView.setItems(displayedInventoryList);

        // color rows for low/expiring/expired
        medicineListTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(MedicineInventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    String st = item.getStatus();
                    if (!"OK".equalsIgnoreCase(st)) {
                        setStyle("-fx-background-color: #ffdddd;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Filter options
        if (filterComboBox != null) {
            filterComboBox.getItems().setAll(
                    "Low Stock",
                    "Quantity (Ascending)",
                    "Expiry (Soonest First)",
                    "All"
            );
        }

        selectedMeatBatchMessageLabel.setText("Observing the Medicine List");
        validationSuccessMessageLabel.setText("*Success/Error messages*");
        validationSuccessMessageLabel1.setText("*Success/Error messages*");

        // Selection listener
        medicineListTableView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    selectedItem = newSel;
                    if (newSel != null) {
                        selectedMeatBatchMessageLabel.setText(
                                "Selected -> " + newSel.getMedicineName() +
                                        " | Batch: " + newSel.getBatchNo() +
                                        " | Qty: " + newSel.getQuantity() +
                                        " | Exp: " + newSel.getExpiryDate() +
                                        " | Status: " + newSel.getStatus()
                        );
                        if (selectMedicineComboBox != null) {
                            selectMedicineComboBox.setValue(newSel.getMedicineName());
                        }
                        batchIDButtonOnAction.setText(newSel.getBatchNo());
                    } else {
                        selectedMeatBatchMessageLabel.setText("Observing the Medicine List");
                    }
                });

        // 🔹 IMPORTANT: Do NOT auto-load data here.
        // masterInventoryList stays empty until user clicks "Load Inventory Table".
        displayedInventoryList.clear();
    }

    // ================== BUTTONS ==================

    @FXML
    public void loadInvertoryTableButtonOnAction(ActionEvent actionEvent) {
        File file = new File(INVENTORY_FILE);
        if (file.exists()) {
            loadInventoryFromFileIfExists();
        } else {
            masterInventoryList.clear();
            LocalDate today = LocalDate.now();

            masterInventoryList.add(createItemWithStatus(
                    "Antibiotic A", "BA-101", 50, "10 mg/kg",
                    today.plusDays(60)));
            masterInventoryList.add(createItemWithStatus(
                    "Painkiller B", "PB-222", 8, "5 ml",
                    today.plusDays(3)));
            masterInventoryList.add(createItemWithStatus(
                    "Vitamin C", "VC-501", 20, "2 tablets",
                    today.plusDays(10)));
            masterInventoryList.add(createItemWithStatus(
                    "Dewormer D", "DW-700", 3, "1 dose",
                    today.minusDays(1)));
            masterInventoryList.add(createItemWithStatus(
                    "Vaccine E", "VE-330", 100, "1 ml",
                    today.plusDays(180)));

            saveInventoryToFile();
        }

        // Now show data *after* button click
        displayedInventoryList.setAll(masterInventoryList);
        medicineListTableView.refresh();
        refreshSelectMedicineComboBox();

        validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel.setText("Inventory table loaded.");
    }

    @FXML
    public void validateEntryButtonOnAction(ActionEvent actionEvent) {
        validateAddStockForm();
    }

    @FXML
    public void addStockButtonOnAction(ActionEvent actionEvent) {
        if (!validateAddStockForm()) {
            return;
        }

        String name = medicineNameTextField.getText().trim();
        String batchNo = batchNumberTextField.getText().trim();
        int qty = Integer.parseInt(quantityTextField.getText().trim());
        LocalDate expDate = expiryDatePicker.getValue();
        String dosage = dosageTextField.getText().trim();
        String expDateStr = expDate.toString();

        MedicineInventoryItem existing = null;
        for (MedicineInventoryItem item : masterInventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(name)
                    && item.getBatchNo().equalsIgnoreCase(batchNo)) {
                existing = item;
                break;
            }
        }

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            existing.setExpiryDate(expDateStr);
            existing.setDosage(dosage);
            updateStatus(existing);
        } else {
            MedicineInventoryItem newItem =
                    createItemWithStatus(name, batchNo, qty, dosage, expDate);
            masterInventoryList.add(newItem);
        }

        displayedInventoryList.setAll(masterInventoryList);
        medicineListTableView.refresh();
        saveInventoryToFile();
        refreshSelectMedicineComboBox();

        // keep fields as you requested
        validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel.setText("Stock updated successfully.");
    }

    @FXML
    public void useMedicineButtonOnAction(ActionEvent actionEvent) {
        StringBuilder errors = new StringBuilder();

        if (selectMedicineComboBox == null || selectMedicineComboBox.getValue() == null) {
            errors.append("Select a medicine from the combo box.\n");
        }

        int usedQty = 0;
        String usedText = usedQuantityTextField.getText().trim();
        if (usedText.isEmpty()) {
            errors.append("Used Quantity cannot be empty.\n");
        } else {
            try {
                usedQty = Integer.parseInt(usedText);
                if (usedQty <= 0) {
                    errors.append("Used Quantity must be positive.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Used Quantity must be an integer.\n");
            }
        }

        String batchId = batchIDButtonOnAction.getText().trim();
        String dateUsed = dateUsedButtonOnAction.getText().trim();
        if (dateUsed.isEmpty()) {
            errors.append("Date Used cannot be empty.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return;
        }

        String medName = selectMedicineComboBox.getValue();

        MedicineInventoryItem target = null;
        for (MedicineInventoryItem item : masterInventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medName)
                    && (batchId.isEmpty()
                    || item.getBatchNo().equalsIgnoreCase(batchId))) {
                target = item;
                break;
            }
        }

        if (target == null) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("No matching medicine/batch found in inventory.");
            return;
        }

        if (usedQty > target.getQuantity()) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText("Not enough stock. Available: " + target.getQuantity());
            return;
        }

        target.setQuantity(target.getQuantity() - usedQty);
        updateStatus(target);

        displayedInventoryList.setAll(masterInventoryList);
        medicineListTableView.refresh();
        saveInventoryToFile();

        validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel.setText(
                "Usage recorded on " + dateUsed + ". Remaining quantity: " + target.getQuantity() + ".");
    }

    @FXML
    public void filterComboBoxOnAction(ActionEvent actionEvent) {
        // real filtering happens on Apply Filter
    }

    @FXML
    public void applyFilterButtonOnAction(ActionEvent actionEvent) {
        if (filterComboBox == null || filterComboBox.getValue() == null) {
            validationSuccessMessageLabel1.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel1.setText("Please choose a filter option first.");
            return;
        }

        String choice = filterComboBox.getValue();
        List<MedicineInventoryItem> base = new ArrayList<>(masterInventoryList);
        List<MedicineInventoryItem> result;

        switch (choice) {
            case "Low Stock":
                result = new ArrayList<>();
                for (MedicineInventoryItem item : base) {
                    if (!"OK".equalsIgnoreCase(item.getStatus())) {
                        result.add(item);
                    }
                }
                break;

            case "Quantity (Ascending)":
                base.sort(Comparator.comparingInt(MedicineInventoryItem::getQuantity));
                result = base;
                break;

            case "Expiry (Soonest First)":
                base.sort(Comparator.comparing(item ->
                        LocalDate.parse(item.getExpiryDate())));
                result = base;
                break;

            case "All":
            default:
                result = base;
                break;
        }

        displayedInventoryList.setAll(result);
        medicineListTableView.refresh();

        validationSuccessMessageLabel1.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel1.setText("Filter applied: " + choice);
    }

    @FXML
    public void resetButtonOnAction(ActionEvent actionEvent) {
        displayedInventoryList.setAll(masterInventoryList);
        medicineListTableView.refresh();

        if (filterComboBox != null) {
            filterComboBox.setValue(null);
        }

        validationSuccessMessageLabel1.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel1.setText("Filter reset. Showing full inventory.");
    }

    @FXML
    public void submitButtonOnAction(ActionEvent actionEvent) {
        ArrayList<MedicineInventoryItem> lowList = new ArrayList<>();
        for (MedicineInventoryItem item : masterInventoryList) {
            if (!"OK".equalsIgnoreCase(item.getStatus())) {
                lowList.add(item);
            }
        }

        if (lowList.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "No low stock or expiring medicines. Purchase request not generated.").show();
            return;
        }

        ArrayList<MedicineInventoryItem> existing = new ArrayList<>();
        File file = new File(PURCHASE_REQUEST_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                existing = (ArrayList<MedicineInventoryItem>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        existing.addAll(lowList);

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(PURCHASE_REQUEST_FILE))) {
            oos.writeObject(existing);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase Request Submitted");
        alert.setHeaderText("Request sent to Farm Manager");
        alert.setContentText("Purchase request generated for "
                + lowList.size() + " low/expiring medicine items.");
        alert.show();

        validationSuccessMessageLabel1.setStyle("-fx-text-fill: green;");
        validationSuccessMessageLabel1.setText("Purchase request submitted to Farm Manager.");
    }

    @FXML
    public void returnButtonOnAction(ActionEvent actionEvent) throws Exception {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Zainab/VeterinaryOfficer/VeterinaryOfficerDashboard.fxml",
                actionEvent);
    }

    // ================== VALIDATION HELPERS ==================

    private boolean validateAddStockForm() {
        StringBuilder errors = new StringBuilder();

        if (medicineNameTextField.getText().trim().isEmpty()) {
            errors.append("Medicine name cannot be empty.\n");
        }
        if (batchNumberTextField.getText().trim().isEmpty()) {
            errors.append("Batch number cannot be empty.\n");
        }

        String qtyText = quantityTextField.getText().trim();
        if (qtyText.isEmpty()) {
            errors.append("Quantity cannot be empty.\n");
        } else {
            try {
                int qty = Integer.parseInt(qtyText);
                if (qty <= 0) {
                    errors.append("Quantity must be a positive integer.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Quantity must be an integer.\n");
            }
        }

        LocalDate expDate = expiryDatePicker.getValue();
        if (expDate == null) {
            errors.append("Expiry date must be selected.\n");
        } else if (expDate.isBefore(LocalDate.now())) {
            errors.append("Expiry date cannot be in the past.\n");
        }

        if (dosageTextField.getText().trim().isEmpty()) {
            errors.append("Dosage information cannot be empty.\n");
        }

        if (errors.length() > 0) {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: red;");
            validationSuccessMessageLabel.setText(errors.toString().trim());
            return false;
        } else {
            validationSuccessMessageLabel.setStyle("-fx-text-fill: green;");
            validationSuccessMessageLabel.setText("Validation successful. You can add stock.");
            return true;
        }
    }

    // ================== INVENTORY HELPERS ==================

    private MedicineInventoryItem createItemWithStatus(String name,
                                                       String batchNo,
                                                       int qty,
                                                       String dosage,
                                                       LocalDate expiry) {
        MedicineInventoryItem item = new MedicineInventoryItem(
                name, batchNo, qty, dosage, expiry.toString(), "OK");
        updateStatus(item);
        return item;
    }

    private void updateStatus(MedicineInventoryItem item) {
        LocalDate today = LocalDate.now();
        LocalDate expDate = LocalDate.parse(item.getExpiryDate());

        String status;
        if (expDate.isBefore(today)) {
            status = "Expired";
        } else if (item.getQuantity() <= 5) {
            status = "Low Stock";
        } else if (!expDate.isAfter(today.plusDays(7))) {
            status = "Expiring Soon";
        } else {
            status = "OK";
        }
        item.setStatus(status);
    }

    private void saveInventoryToFile() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(new ArrayList<>(masterInventoryList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadInventoryFromFileIfExists() {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<MedicineInventoryItem> list =
                    (ArrayList<MedicineInventoryItem>) ois.readObject();
            masterInventoryList.clear();
            masterInventoryList.addAll(list);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void refreshSelectMedicineComboBox() {
        if (selectMedicineComboBox == null) return;
        selectMedicineComboBox.getItems().clear();
        for (MedicineInventoryItem item : masterInventoryList) {
            if (!selectMedicineComboBox.getItems().contains(item.getMedicineName())) {
                selectMedicineComboBox.getItems().add(item.getMedicineName());
            }
        }
    }

}
