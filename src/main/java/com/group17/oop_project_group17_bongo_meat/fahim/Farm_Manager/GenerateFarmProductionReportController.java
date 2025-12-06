package com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class GenerateFarmProductionReportController {

    @FXML private TableColumn<ProductionData, Integer> meatCol;
    @FXML private TableColumn<ProductionData, Integer> milkCol;
    @FXML private TableColumn<ProductionData, Integer> eggCol;
    @FXML private ComboBox<String> monthCB;
    @FXML private ComboBox<String> animalTypeCB;
    @FXML private Label label;
    @FXML private TableView<ProductionData> productionReportTableView;
    @FXML private PieChart pieChart;

    private ObservableList<ProductionData> productionList = FXCollections.observableArrayList();

    private final HashMap<String, ProductionData> dummyData = new HashMap<>();

    @FXML
    public void initialize() {


        animalTypeCB.getItems().addAll("Cow", "Goat", "Sheep", "All");
        monthCB.getItems().addAll("January", "February", "March", "April");


        dummyData.put("January", new ProductionData(120, 250, 310));
        dummyData.put("February", new ProductionData(90, 270, 290));
        dummyData.put("March", new ProductionData(130, 300, 400));
        dummyData.put("April", new ProductionData(100, 200, 250));


        meatCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getMeat()).asObject());
        milkCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getMilk()).asObject());
        eggCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getEgg()).asObject());

        productionReportTableView.setItems(productionList);
    }

    @FXML
    public void generateReportButton(ActionEvent event) {

        String type = animalTypeCB.getValue();
        String month = monthCB.getValue();

        if (type == null || month == null) {
            label.setText("⚠ Please select both animal type and month!");
            return;
        }


        ProductionData data = dummyData.get(month);

        productionList.clear();
        productionList.add(data);


        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Meat", data.getMeat()));
        pieChart.getData().add(new PieChart.Data("Milk", data.getMilk()));
        pieChart.getData().add(new PieChart.Data("Egg", data.getEgg()));

        label.setText("✔ Report generated successfully!");
    }

    @FXML
    public void exportPDFButton(ActionEvent event) {
        if (productionList.isEmpty()) {
            label.setText("⚠ No data to export!");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Report as PDF");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = chooser.showSaveDialog(null);

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                ProductionData data = productionList.get(0);

                document.add(new Paragraph("Farm Production Report\n\n"));
                document.add(new Paragraph("Animal Type: " + animalTypeCB.getValue()));
                document.add(new Paragraph("Month: " + monthCB.getValue() + "\n"));

                document.add(new Paragraph("Meat: " + data.getMeat() + " kg"));
                document.add(new Paragraph("Milk: " + data.getMilk() + " L"));
                document.add(new Paragraph("Egg: " + data.getEgg() + " units"));

                document.close();
                label.setText("✔ PDF exported successfully!");

            } catch (Exception e) {
                e.printStackTrace();
                label.setText("❌ Failed to export PDF!");
            }
        }
    }

    @FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo ("/com/group17/oop_project_group17_bongo_meat/fahim/Farm_Manager/FarmManagerDashBoard.fxml",actionEvent);

    }

    @FXML
    public void nextButton(ActionEvent actionEvent) {}
}
