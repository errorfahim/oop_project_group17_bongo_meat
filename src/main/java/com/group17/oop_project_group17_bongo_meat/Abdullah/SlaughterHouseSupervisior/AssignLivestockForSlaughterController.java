package com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class AssignLivestockForSlaughterController {

    @FXML private TableView<AssignLivestockForSlaughter> tableView;
    @FXML private TableColumn<AssignLivestockForSlaughter, String> colBatchId;
    @FXML private TableColumn<AssignLivestockForSlaughter, String> colType;
    @FXML private TableColumn<AssignLivestockForSlaughter, Integer> colTotalQty;
    @FXML private TableColumn<AssignLivestockForSlaughter, String> vetAndQAColumn;
    @FXML private TableColumn<AssignLivestockForSlaughter, String> colAssignedTime;
    @FXML private TableColumn<AssignLivestockForSlaughter, String> colStaff;
    @FXML private TableColumn<AssignLivestockForSlaughter, Integer> colSlaughterAmount;
    @FXML private TableColumn<AssignLivestockForSlaughter, Integer> colRemaining;

    @FXML private TextField txtAssignedTime;
    @FXML private ComboBox<String> cbStaff;
    @FXML private TextField txtSlaughterAmount;
    @FXML private Label lblMessage;

    private final ObservableList<AssignLivestockForSlaughter> data = FXCollections.observableArrayList();

    // FILE NAMES
    private static final String QA_FILE = "incomingQARequests.dat";
    private static final String VET_FILE = "incomingVetRequests.dat";
    private static final String ASSIGNED_FILE = "assignedLivestock.dat";

    @FXML
    public void initialize() {
        colBatchId.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().getBatchId()));
        colType.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().getType()));
        colTotalQty.setCellValueFactory(x -> new javafx.beans.property.SimpleIntegerProperty(x.getValue().getTotalQuantity()).asObject());
        vetAndQAColumn.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().getVetApproved() + " / " + x.getValue().getQaApproved()));
        colAssignedTime.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().getAssignedTime()));
        colStaff.setCellValueFactory(x -> new javafx.beans.property.SimpleStringProperty(x.getValue().getStaffName()));
        colSlaughterAmount.setCellValueFactory(x -> new javafx.beans.property.SimpleIntegerProperty(x.getValue().getSlaughterAmount()).asObject());
        colRemaining.setCellValueFactory(x -> new javafx.beans.property.SimpleIntegerProperty(x.getValue().getRemainingQuantity()).asObject());

        cbStaff.setItems(FXCollections.observableArrayList("A", "B", "C", "D", "E"));
        tableView.setItems(data);

        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1) {
                AssignLivestockForSlaughter sel = tableView.getSelectionModel().getSelectedItem();
                if (sel != null) {
                    txtAssignedTime.setText(sel.getAssignedTime());
                    cbStaff.setValue(sel.getStaffName());
                    txtSlaughterAmount.setText(sel.getSlaughterAmount() > 0 ? String.valueOf(sel.getSlaughterAmount()) : "");
                }
            }
        });

        lblMessage.setText("");
    }

    /** LOAD PENDING LIVESTOCK ONLY (Vet + QA) */
    @FXML
    void handleShow() {
        data.clear();
        int totalLoaded = 0;

        // 1) Load QA pending records
        File qaFile = new File(QA_FILE);
        if (qaFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(qaFile))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    for (Object item : (ArrayList<?>) obj) {
                        if (item instanceof AssignLivestockForSlaughter) {
                            AssignLivestockForSlaughter rec = (AssignLivestockForSlaughter) item;
                            data.add(rec);
                            totalLoaded++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                lblMessage.setText("Error reading QA file!");
            }
        }

        // 2) Load Vet approved (Pending QA)
        File vetFile = new File(VET_FILE);
        if (vetFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(vetFile))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    for (Object item : (ArrayList<?>) obj) {
                        if (item instanceof IncomingLivestockVetInspectionRequest) {
                            IncomingLivestockVetInspectionRequest v = (IncomingLivestockVetInspectionRequest) item;

                            AssignLivestockForSlaughter livestock =
                                    new AssignLivestockForSlaughter(v.getBatchId(), v.getType(), v.getQuantity());

                            livestock.setVetApproved("Approved");
                            livestock.setQaApproved("");
                            livestock.setAssignedTime("");
                            livestock.setStaffName("");
                            livestock.setSlaughterAmount(0);
                            livestock.setRemainingQuantity(v.getQuantity());

                            // avoid duplicates (if QA already added same batch)
                            boolean exists = data.stream().anyMatch(d -> d.getBatchId().equals(v.getBatchId()));
                            if (!exists) {
                                data.add(livestock);
                                totalLoaded++;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                lblMessage.setText("Error reading Vet file!");
            }
        }

        tableView.setItems(data);
        lblMessage.setText("Loaded " + totalLoaded + " unassigned record(s).");
    }

    /** ASSIGN SELECTED LIVESTOCK AND SAVE IT INTO assignedLivestock.dat */
    @FXML
    private void handleAssign() {
        AssignLivestockForSlaughter selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("Select a batch!");
            return;
        }

        String time = txtAssignedTime.getText().trim();
        String staff = cbStaff.getValue();
        String amtText = txtSlaughterAmount.getText().trim();

        if (time.isEmpty()) {
            lblMessage.setText("Enter assigned time.");
            return;
        }
        if (staff == null) {
            lblMessage.setText("Select a staff.");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amtText);
        } catch (Exception e) {
            lblMessage.setText("Invalid slaughter amount!");
            return;
        }

        if (amount < 0 || amount > 20) {
            lblMessage.setText("Amount must be 0-20.");
            return;
        }

        if (amount > selected.getRemainingQuantity()) {
            lblMessage.setText("Amount exceeds remaining quantity.");
            return;
        }

        // update selected
        selected.setAssignedTime(time);
        selected.setStaffName(staff);
        selected.setSlaughterAmount(amount);
        selected.setRemainingQuantity(selected.getTotalQuantity() - amount);
        selected.setQaApproved("Approved");

        // Save to assigned file
        saveToAssignedFile(selected);

        // Remove from QA file & save remaining to QA file
        removeFromQaFile(selected.getBatchId());

        // remove from table
        data.remove(selected);
        tableView.refresh();

        lblMessage.setText("Assigned & saved into assignedLivestock.dat");
    }

    /** APPEND SELECTED RECORD INTO assignedLivestock.dat */
    private void saveToAssignedFile(AssignLivestockForSlaughter selected) {
        ArrayList<AssignLivestockForSlaughter> list = new ArrayList<>();

        // load existing
        File assigned = new File(ASSIGNED_FILE);
        if (assigned.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(assigned))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    for (Object item : (ArrayList<?>) obj) {
                        list.add((AssignLivestockForSlaughter) item);
                    }
                }
            } catch (Exception ignored) {}
        }

        list.add(selected);

        // save back
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(assigned))) {
            oos.writeObject(list);
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Error saving assigned livestock!");
        }
    }

    /** REMOVE ITEM FROM QA FILE AFTER ASSIGNING */
    private void removeFromQaFile(String batchId) {
        ArrayList<AssignLivestockForSlaughter> newList = new ArrayList<>();

        File qaFile = new File(QA_FILE);
        if (qaFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(qaFile))) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    for (Object item : (ArrayList<?>) obj) {
                        AssignLivestockForSlaughter rec = (AssignLivestockForSlaughter) item;
                        if (!rec.getBatchId().equals(batchId)) {
                            newList.add(rec);
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        // save updated list back
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(qaFile))) {
            oos.writeObject(newList);
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Error updating QA file!");
        }
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/Abdullah/SlaughterHouseSupervisior/SlaughterHouseSupervisiorDashboard.fxml", actionEvent);
    }
}
