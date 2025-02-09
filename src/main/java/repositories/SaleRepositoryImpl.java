package repositories;

import models.Sale;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import utilities.ExcelDatabaseConnection;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.time.LocalTime;

public class SaleRepositoryImpl {

    private final ExcelDatabaseConnection connection;

    public SaleRepositoryImpl(ExcelDatabaseConnection connection) {
        this.connection = connection;
    }

    public List<Sale> findAll() throws IOException {
        Sheet sheet = connection.getSheet("Venta");
        List<Sale> sales = new ArrayList<>();

        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            // Read the sale details (ID, Date, Time, Client)
            Cell idCell = row.getCell(0);
            Cell dateCell = row.getCell(1);
            Cell timeCell = row.getCell(2);
            Cell clientCell = row.getCell(3);

            if (idCell == null || dateCell == null || timeCell == null || clientCell == null) {
                continue;
            }

            int id = (int) idCell.getNumericCellValue();
            LocalDate date = LocalDate.parse(dateCell.getStringCellValue());
            LocalTime time = LocalTime.parse(timeCell.getStringCellValue());
            String client = clientCell.getStringCellValue();

            // Retrieve the products associated with the sale
            List<String[]> productData = new ArrayList<>();
            while (i <= lastRowNum) {
                Row productRow = sheet.getRow(i);

                Cell quantityCell = productRow.getCell(4);
                Cell productCell = productRow.getCell(5);
                Cell priceCell = productRow.getCell(6);

                if (quantityCell == null || productCell == null || priceCell == null) {
                    break;
                }

                String quantity = String.valueOf((int) quantityCell.getNumericCellValue());
                String product = productCell.getStringCellValue();
                String price = String.valueOf(priceCell.getNumericCellValue());

                productData.add(new String[]{quantity, product, price});

                // Check if the next row starts a new sale (based on merged cells for ID, Date, etc.)
                Row nextRow = sheet.getRow(i + 1);
                if (nextRow != null && nextRow.getCell(0) != null) {
                    break;
                }

                i++;
            }

            // Calculate total
            double total = productData.stream()
                    .mapToDouble(p -> Integer.parseInt(p[0]) * Double.parseDouble(p[2]))
                    .sum();

            // Add the sale object to the list
            sales.add(new Sale(id, date, time, client, productData, total));
        }

        return sales;
    }

    public void add(Sale sale) throws IOException {
        Sheet sheet = connection.getSheet("Venta");

        // Create cell styles
        CellStyle styleRight = sheet.getWorkbook().createCellStyle();
        styleRight.setAlignment(HorizontalAlignment.RIGHT);
        styleRight.setVerticalAlignment(VerticalAlignment.CENTER);
        styleRight.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleRight.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleCenter = sheet.getWorkbook().createCellStyle();
        styleCenter.setAlignment(HorizontalAlignment.CENTER);
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCenter.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleTotal = sheet.getWorkbook().createCellStyle();
        styleTotal.setAlignment(HorizontalAlignment.RIGHT);
        styleTotal.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTotal.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleTotal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int startingRowNum = sheet.getLastRowNum() + 1;
        int newRowNum = startingRowNum;

        int newId = 1; // Default for the first record

// Find the last ID in column 0 (ID)
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header
            }

            Cell idCell = row.getCell(0);
            if (idCell == null || idCell.getCellType() != CellType.NUMERIC) {
                continue; // Skip rows without a valid numeric ID
            }

            int currentId = (int) idCell.getNumericCellValue();
            if (currentId >= newId) {
                newId = currentId + 1; // Increment the highest ID found
            }
        }

        // Add sale details row
        Row saleRow = sheet.createRow(newRowNum);
        Cell idCell = saleRow.createCell(0);
        idCell.setCellValue(newId);
        idCell.setCellStyle(styleCenter);

        Cell dateCell = saleRow.createCell(1);
        dateCell.setCellValue(sale.getDate().toString());
        dateCell.setCellStyle(styleCenter);

        Cell timeCell = saleRow.createCell(2);
        timeCell.setCellValue(sale.getHora().toString());
        timeCell.setCellStyle(styleCenter);

        Cell clientCell = saleRow.createCell(3);
        clientCell.setCellValue(sale.getClient());
        clientCell.setCellStyle(styleCenter);

        double total = 0;
        boolean isFirstProduct = true; // Flag to track the first product

        for (String[] product : sale.getProductData()) {
            if (isFirstProduct) {
                // Add the first product in the same row as the sale
                Cell quantityCell = saleRow.createCell(4);  //CANTIDAD
                quantityCell.setCellValue(Integer.parseInt(product[0]));
                quantityCell.setCellStyle(styleRight);

                Cell productCell = saleRow.createCell(5);   // PRODUCTO
                productCell.setCellValue(product[1]);
                productCell.setCellStyle(styleRight);

                Cell priceCell = saleRow.createCell(6); //PRECIO
                priceCell.setCellValue(Double.parseDouble(product[2]));
                priceCell.setCellStyle(styleRight);

                double subtotal = Integer.parseInt(product[0]) * Double.parseDouble(product[2]);  //SUBTOTAL
                Cell subtotalCell = saleRow.createCell(7);
                subtotalCell.setCellValue(subtotal);
                subtotalCell.setCellStyle(styleRight);

                total += subtotal;
                isFirstProduct = false;
            } else {
                // For subsequent products, add them on new rows
                newRowNum++;
                Row productRow = sheet.createRow(newRowNum);

                Cell quantityCell = productRow.createCell(4);
                quantityCell.setCellValue(Integer.parseInt(product[0]));  //CANTIDAD
                quantityCell.setCellStyle(styleRight);

                Cell productCell = productRow.createCell(5);
                productCell.setCellValue(product[1]);
                productCell.setCellStyle(styleRight);

                Cell priceCell = productRow.createCell(6);
                priceCell.setCellValue(Double.parseDouble(product[2]));  //SUBTOTAL
                priceCell.setCellStyle(styleRight);

                double subtotal = Integer.parseInt(product[0]) * Double.parseDouble(product[2]);
                Cell subtotalCell = productRow.createCell(7);
                subtotalCell.setCellValue(subtotal);
                subtotalCell.setCellStyle(styleRight);

                total += subtotal;
            }
        }

        // Add total row
        newRowNum++;
        Row totalRow = sheet.createRow(newRowNum);
        Cell totalLabelCell = totalRow.createCell(0);
        totalLabelCell.setCellValue("TOTAL:");
        totalLabelCell.setCellStyle(styleTotal);

        // Merge cells from column 0 to column 6 for "TOTAL:"
        sheet.addMergedRegion(new CellRangeAddress(newRowNum, newRowNum, 0, 6));

        Cell totalValueCell = totalRow.createCell(7);
        totalValueCell.setCellValue(total);
        totalValueCell.setCellStyle(styleTotal);

        // Merge rows for the sale details
        sheet.addMergedRegion(new CellRangeAddress(startingRowNum, newRowNum - 1, 0, 0)); // ID
        sheet.addMergedRegion(new CellRangeAddress(startingRowNum, newRowNum - 1, 1, 1)); // Date
        sheet.addMergedRegion(new CellRangeAddress(startingRowNum, newRowNum - 1, 2, 2)); // Time
        sheet.addMergedRegion(new CellRangeAddress(startingRowNum, newRowNum - 1, 3, 3)); // Client

        // Save changes to the connection
        connection.save();
    }

    public Sale findById(int saleId) throws IOException {
        Sheet sheet = connection.getSheet("Venta");

        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            // Verificar si la fila tiene un ID
            Cell idCell = row.getCell(0);
            if (idCell == null || idCell.getCellType() != CellType.NUMERIC) {
                continue;
            }

            int currentId = (int) idCell.getNumericCellValue();
            if (currentId == saleId) {
                // Leer los detalles de la venta (ID, Fecha, Hora, Cliente)
                Cell dateCell = row.getCell(1);
                Cell timeCell = row.getCell(2);
                Cell clientCell = row.getCell(3);

                LocalDate date = LocalDate.parse(dateCell.getStringCellValue());
                LocalTime time = LocalTime.parse(timeCell.getStringCellValue());
                String client = clientCell.getStringCellValue();

                // Obtener los productos asociados con la venta
                List<String[]> productData = new ArrayList<>();
                while (i <= lastRowNum) {
                    Row productRow = sheet.getRow(i);

                    Cell quantityCell = productRow.getCell(4);
                    Cell productCell = productRow.getCell(5);
                    Cell priceCell = productRow.getCell(6);

                    if (quantityCell == null || productCell == null || priceCell == null) {
                        break;
                    }

                    String quantity = String.valueOf((int) quantityCell.getNumericCellValue());
                    String product = productCell.getStringCellValue();
                    String price = String.valueOf(priceCell.getNumericCellValue());

                    productData.add(new String[]{quantity, product, price});

                    // Verificar si la siguiente fila inicia una nueva venta (basado en celdas fusionadas en ID)
                    Row nextRow = sheet.getRow(i + 1);
                    if (nextRow != null && nextRow.getCell(0) != null) {
                        break;
                    }

                    i++;
                }

                // Calcular el total
                double total = productData.stream()
                        .mapToDouble(p -> Integer.parseInt(p[0]) * Double.parseDouble(p[2]))
                        .sum();

                // Retornar la venta encontrada
                return new Sale(saleId, date, time, client, productData, total);
            }
        }

        // Retornar null si no se encuentra la venta
        return null;
    }

    public void delete(int id) throws IOException {

    }
}
