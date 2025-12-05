package com.group17.oop_project_group17_bongo_meat.fahim.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.DirectoryChooser;
import javafx.scene.Node;

// Apache POI imports for Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// iText imports for PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.group17.oop_project_group17_bongo_meat.SceneSwitcher.switchTo;

public class OverseeFinancialTransactionsAndInvoicingController {
    @javafx.fxml.FXML
    private ComboBox<String> transactionTypeCB;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> typeCol;
    @javafx.fxml.FXML
    private TableView<ArrayList> transactionTableView;
    @javafx.fxml.FXML
    private DatePicker trasactionDatePC;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> dateCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> amountCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> statusCol;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> partyCol;
    @javafx.fxml.FXML
    private Label label;
    @javafx.fxml.FXML
    private TextField transactionId;
    @javafx.fxml.FXML
    private TableColumn<ArrayList<String>, String> transactionIdCol;
    @javafx.fxml.FXML
    private Button exportExcelButton;
    @javafx.fxml.FXML
    private Button generateReportButton;
    @javafx.fxml.FXML
    private Button clearSearchButton;

    private ObservableList<ArrayList> dummyData = FXCollections.observableArrayList();
    private ObservableList<ArrayList> filteredData = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @javafx.fxml.FXML
    public void initialize() {
        // Dropdown values
        transactionTypeCB.setItems(FXCollections.observableArrayList("All", "Sale", "Purchase"));
        transactionTypeCB.setValue("All");

        // Set Table Column mappings
        transactionIdCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
        dateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(2)));
        partyCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
        amountCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));
        statusCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(5)));

        loadDummyData();
        transactionTableView.setItems(dummyData);
    }

    // Load sample data
    private void loadDummyData() {
        dummyData.add(new ArrayList<>(FXCollections.observableArrayList(
                "T001", "2025-01-01", "Sale", "Rahim", "12000", "Paid"
        )));
        dummyData.add(new ArrayList<>(FXCollections.observableArrayList(
                "T002", "2025-01-02", "Purchase", "Karim", "8000", "Pending"
        )));
        dummyData.add(new ArrayList<>(FXCollections.observableArrayList(
                "T003", "2025-01-03", "Sale", "Mina", "5000", "Paid"
        )));
        dummyData.add(new ArrayList<>(FXCollections.observableArrayList(
                "T004", "2025-01-05", "Purchase", "Bismillah Traders", "22000", "Unpaid"
        )));
    }

    @javafx.fxml.FXML
    public void nextButton(ActionEvent actionEvent) {
        // Implement next button functionality if needed
    }

    @javafx.fxml.FXML
    public void exportToExcelButton(ActionEvent actionEvent) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Directory to Save Excel File");
            File selectedDirectory = directoryChooser.showDialog(((Node) actionEvent.getSource()).getScene().getWindow());

            if (selectedDirectory == null) {
                label.setText("Export cancelled.");
                return;
            }

            // Use LocalDate for timestamp (not LocalDateTime with HH format)
            String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String fileName = "Financial_Report_" + timestamp + ".xlsx";
            File outputFile = new File(selectedDirectory, fileName);

            // Check if file already exists
            if (outputFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("File Exists");
                alert.setHeaderText("File already exists: " + fileName);
                alert.setContentText("Do you want to overwrite it?");

                if (alert.showAndWait().get() != ButtonType.OK) {
                    label.setText("Export cancelled.");
                    return;
                }
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Transactions");

            // Create header style using org.apache.poi.ss.usermodel.Font
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Create data style for currency
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            // Header Row
            Row header = sheet.createRow(0);
            String[] titles = {"Transaction ID", "Date", "Type", "Party", "Amount", "Status"};
            for (int i = 0; i < titles.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            ObservableList<ArrayList> data = transactionTableView.getItems();

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                ArrayList<String> entry = data.get(i);
                for (int j = 0; j < entry.size(); j++) {
                    Cell cell = row.createCell(j);
                    String value = entry.get(j);

                    // Apply currency format for amount column (index 4)
                    if (j == 4) {
                        try {
                            double amount = Double.parseDouble(value);
                            cell.setCellValue(amount);
                            cell.setCellStyle(currencyStyle);
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value);
                        }
                    } else {
                        cell.setCellValue(value);
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < titles.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Create summary row
            if (data.size() > 0) {
                Row summaryRow = sheet.createRow(data.size() + 2);
                summaryRow.createCell(0).setCellValue("Summary");
                summaryRow.createCell(1).setCellValue("Total Transactions: " + data.size());

                // Calculate total amount
                double totalAmount = 0;
                for (ArrayList<String> row : data) {
                    try {
                        totalAmount += Double.parseDouble(row.get(4));
                    } catch (NumberFormatException e) {
                        // Skip if amount is not a valid number
                    }
                }

                Row totalRow = sheet.createRow(data.size() + 3);
                totalRow.createCell(0).setCellValue("Total Amount:");
                Cell totalCell = totalRow.createCell(1);
                totalCell.setCellValue(totalAmount);
                totalCell.setCellStyle(currencyStyle);
            }

            // Write to file
            try (FileOutputStream output = new FileOutputStream(outputFile)) {
                workbook.write(output);
                workbook.close();
                label.setText("Excel file created successfully: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                label.setText("Error writing file: " + e.getMessage());
            }

        } catch (Exception e) {
            label.setText("Failed to export to Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void searchButton(ActionEvent actionEvent) {
        String searchId = transactionId.getText().trim();
        LocalDate selectedDate = trasactionDatePC.getValue();
        String type = transactionTypeCB.getValue();

        filteredData.clear();

        for (ArrayList<String> row : dummyData) {
            boolean match = true;

            // Search by ID (partial match)
            if (!searchId.isEmpty() && !row.get(0).toLowerCase().contains(searchId.toLowerCase())) {
                match = false;
            }

            // Search by date
            if (selectedDate != null && !row.get(1).equals(selectedDate.toString())) {
                match = false;
            }

            // Search by type
            if (!type.equals("All") && !row.get(2).equalsIgnoreCase(type)) {
                match = false;
            }

            if (match) filteredData.add(row);
        }

        transactionTableView.setItems(filteredData);
        label.setText("Found " + filteredData.size() + " transaction(s).");
    }

    @javafx.fxml.FXML
    public void backButton(ActionEvent actionEvent) throws IOException {
        switchTo("/com/group17/oop_project_group17_bongo_meat/fahim/Admin/AdminDashBoard.fxml", actionEvent);
    }

    @javafx.fxml.FXML
    public void generateReportButton(ActionEvent actionEvent) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Directory to Save PDF Report");
            File selectedDirectory = directoryChooser.showDialog(((Node) actionEvent.getSource()).getScene().getWindow());

            if (selectedDirectory == null) {
                label.setText("PDF generation cancelled.");
                return;
            }

            // Use LocalDateTime for timestamp with hours/minutes/seconds
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Financial_Report_" + timestamp + ".pdf";
            File outputFile = new File(selectedDirectory, fileName);

            // Check if file already exists
            if (outputFile.exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("File Exists");
                alert.setHeaderText("File already exists: " + fileName);
                alert.setContentText("Do you want to overwrite it?");

                if (alert.showAndWait().get() != ButtonType.OK) {
                    label.setText("PDF generation cancelled.");
                    return;
                }
            }

            Document document = new Document(PageSize.A4.rotate()); // Landscape for better table view

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                PdfWriter writer = PdfWriter.getInstance(document, fos);

                document.open();

                // Title using com.itextpdf.text.Font
                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
                Paragraph title = new Paragraph("Financial Transactions Report", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Report generation date
                com.itextpdf.text.Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
                // Use LocalDateTime.now() for timestamp with time
                String generationTime = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")
                );
                Paragraph dateInfo = new Paragraph("Generated on: " + generationTime, dateFont);
                dateInfo.setAlignment(Element.ALIGN_RIGHT);
                dateInfo.setSpacingAfter(20);
                document.add(dateInfo);

                // Summary information
                ObservableList<ArrayList> data = transactionTableView.getItems();
                Paragraph summary = new Paragraph();
                summary.setSpacingAfter(15);
                summary.add(new Chunk("Total Transactions: ",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                summary.add(new Chunk(String.valueOf(data.size()),
                        FontFactory.getFont(FontFactory.HELVETICA, 12)));

                if (data.isEmpty()) {
                    summary.add(new Chunk("\nNo transactions to display.",
                            FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED)));
                    document.add(summary);
                    document.close();
                    label.setText("PDF generated with empty data: " + outputFile.getAbsolutePath());
                    return;
                }

                double totalAmount = 0;
                int paidCount = 0;
                int unpaidCount = 0;
                int pendingCount = 0;

                for (ArrayList<String> row : data) {
                    try {
                        totalAmount += Double.parseDouble(row.get(4));
                    } catch (NumberFormatException e) {
                        // Skip if not a valid number
                    }

                    String status = row.get(5);
                    if (status.equalsIgnoreCase("Paid")) {
                        paidCount++;
                    } else if (status.equalsIgnoreCase("Unpaid")) {
                        unpaidCount++;
                    } else if (status.equalsIgnoreCase("Pending")) {
                        pendingCount++;
                    }
                }

                summary.add(new Chunk("\nTotal Amount: ",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                summary.add(new Chunk(String.format("BDT %,.2f", totalAmount),
                        FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED)));

                summary.add(new Chunk("\nPaid Transactions: ",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                summary.add(new Chunk(String.valueOf(paidCount),
                        FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GREEN)));

                summary.add(new Chunk("\nUnpaid Transactions: ",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                summary.add(new Chunk(String.valueOf(unpaidCount),
                        FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED)));

                summary.add(new Chunk("\nPending Transactions: ",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                summary.add(new Chunk(String.valueOf(pendingCount),
                        FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.ORANGE)));

                document.add(summary);
                document.add(new Paragraph("\n"));

                // Create table
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                // Set column widths (adjust as needed)
                float[] columnWidths = {1f, 1f, 1f, 2f, 1.5f, 1f};
                table.setWidths(columnWidths);

                // Table headers with styling
                String[] headers = {"ID", "Date", "Type", "Party", "Amount", "Status"};
                com.itextpdf.text.Font headerFontPdf = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);

                for (String headerText : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(headerText, headerFontPdf));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(5);
                    table.addCell(cell);
                }

                // Table data
                com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
                com.itextpdf.text.Font amountFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.BLACK);

                for (ArrayList<String> row : data) {
                    for (int i = 0; i < row.size(); i++) {
                        com.itextpdf.text.Font cellFont = dataFont;
                        String cellValue = row.get(i);

                        if (i == 4) { // Amount column
                            cellFont = amountFont;
                            // Format amount with commas
                            try {
                                double amount = Double.parseDouble(cellValue);
                                cellValue = String.format("%,.2f", amount);
                            } catch (NumberFormatException e) {
                                // Keep original value if not a number
                            }
                        } else if (i == 5) { // Status column
                            if (cellValue.equalsIgnoreCase("Paid")) {
                                cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.GREEN);
                            } else if (cellValue.equalsIgnoreCase("Unpaid")) {
                                cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.RED);
                            } else if (cellValue.equalsIgnoreCase("Pending")) {
                                cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.ORANGE);
                            }
                        }

                        PdfPCell cell = new PdfPCell(new Phrase(cellValue, cellFont));
                        cell.setPadding(5);
                        if (i == 4) { // Right align amount
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        } else if (i == 0 || i == 2 || i == 5) { // Center align ID, Type, Status
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        } else {
                            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        }
                        table.addCell(cell);
                    }
                }

                document.add(table);

                // Footer
                Paragraph footer = new Paragraph(
                        "This report is generated by Bongo Meat Management System\n" +
                                "Report saved to: " + outputFile.getAbsolutePath(),
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY)
                );
                footer.setAlignment(Element.ALIGN_CENTER);
                footer.setSpacingBefore(20);
                document.add(footer);

                document.close();

                label.setText("PDF report generated successfully: " + outputFile.getAbsolutePath());

            } catch (DocumentException | IOException e) {
                label.setText("Failed to generate PDF: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            label.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void clearSearchButton(ActionEvent actionEvent) {
        transactionId.clear();
        trasactionDatePC.setValue(null);
        transactionTypeCB.setValue("All");
        transactionTableView.setItems(dummyData);
        label.setText("Search cleared. Showing all transactions.");
    }
}