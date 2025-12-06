package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.stage.FileChooser;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Month;
import java.util.Arrays;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class GenerateSystemWidePerformanceReportsController
{
    @javafx.fxml.FXML
    private TableView<ReportData> reportTableView;
    @javafx.fxml.FXML
    private TableColumn<ReportData, Double> profitCol;
    @javafx.fxml.FXML
    private ComboBox monthCB;
    @javafx.fxml.FXML
    private ComboBox yearCB;
    @javafx.fxml.FXML
    private TableColumn<ReportData, Double> expensesCol;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private TableColumn<ReportData, Double> revenueCol;
    @javafx.fxml.FXML
    private PieChart pieChart;


    public static class ReportData {
        private double revenue;
        private double expenses;
        private double profit;

        public ReportData(double revenue, double expenses) {
            this.revenue = revenue;
            this.expenses = expenses;
            this.profit = revenue - expenses;
        }

        public double getRevenue() { return revenue; }
        public double getExpenses() { return expenses; }
        public double getProfit() { return profit; }
    }


    @javafx.fxml.FXML
    public void initialize() {
        // Populate months
        monthCB.setItems(FXCollections.observableArrayList(
                Arrays.stream(Month.values()).map(Month::name).toList()
        ));

        // Years
        yearCB.setItems(FXCollections.observableArrayList("2023", "2024", "2025", "2026"));

        // TableView Columns
        revenueCol.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        expensesCol.setCellValueFactory(new PropertyValueFactory<>("expenses"));
        profitCol.setCellValueFactory(new PropertyValueFactory<>("profit"));
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo ("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generateReportButtom(ActionEvent actionEvent) {
        if (yearCB.getValue() == null || monthCB.getValue() == null) {
            label.setText("Select year and month!");
            return;
        }

        String year = yearCB.getValue().toString();
        String month = monthCB.getValue().toString();

        label.setText("Showing report for: " + month + " " + year);


        double revenue = 12000 + Math.random() * 4000;
        double expenses = 5000 + Math.random() * 3000;

        ReportData data = new ReportData(revenue, expenses);

        reportTableView.setItems(FXCollections.observableArrayList(data));


        pieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Revenue", revenue),
                new PieChart.Data("Expenses", expenses),
                new PieChart.Data("Profit", data.getProfit())
        ));
    }

    @javafx.fxml.FXML
    public void exportPDFButton(ActionEvent actionEvent) {
        if (reportTableView.getItems().isEmpty()) {
            label.setText("Generate report first!");
            return;
        }

        try {


            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report as PDF");

            fileChooser.setInitialFileName("SystemWidePerformanceReport.pdf");


            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf")
            );


            File file = fileChooser.showSaveDialog(reportTableView.getScene().getWindow());

            if (file == null) {
                label.setText("PDF export cancelled!");
                return;
            }

            ReportData data = reportTableView.getItems().get(0);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            document.add(new Paragraph("SYSTEM WIDE PERFORMANCE REPORT\n\n"));
            document.add(new Paragraph("Year   : " + yearCB.getValue()));
            document.add(new Paragraph("Month  : " + monthCB.getValue() + "\n\n"));
            document.add(new Paragraph("Revenue : " + data.getRevenue()));
            document.add(new Paragraph("Expenses: " + data.getExpenses()));
            document.add(new Paragraph("Profit  : " + data.getProfit() + "\n\n"));
            document.add(new Paragraph("Generated using iTextPDF"));

            document.close();

            label.setText("PDF Saved Successfully:\n" + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            label.setText("PDF Export Failed!");
        }
    }
}