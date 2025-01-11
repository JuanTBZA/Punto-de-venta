/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;


import models.Product;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import utilities.ExcelDatabaseConnection;

public class ProductRepositoryImpl implements ProductRepository {
    
    private final ExcelDatabaseConnection connection;

    public ProductRepositoryImpl(ExcelDatabaseConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<Product> findAll() throws IOException {
        Sheet sheet = connection.getSheet("Product");
        List<Product> products = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            int idCategory = (int) row.getCell(2).getNumericCellValue();
            int idBrand = (int) row.getCell(3).getNumericCellValue();
            int stock = (int) row.getCell(4).getNumericCellValue();
            int price = (int) row.getCell(5).getNumericCellValue();
            products.add(new Product(id, name, idCategory, idBrand, stock, price));
        }
        return products;
    }

    @Override
    public void add(Product product) throws IOException {
        Sheet sheet = connection.getSheet("Product");
        int newId = 1; // Default for the first record

        // Find the last ID in column 0 (ID)
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            int currentId = (int) row.getCell(0).getNumericCellValue();
            if (currentId >= newId) {
                newId = currentId + 1; // Increment the highest ID found
            }
        }

        // Create a new row at the end of the sheet
        Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        newRow.createCell(0).setCellValue(newId); // Generated ID
        newRow.createCell(1).setCellValue(product.getName()); // Product Name
        newRow.createCell(2).setCellValue(product.getIdCategory()); // Category ID
        newRow.createCell(3).setCellValue(product.getIdBrand()); // Brand ID
        newRow.createCell(4).setCellValue(product.getStock()); // Stock
        newRow.createCell(5).setCellValue(product.getPrice()); // Price

        connection.save();
    }

    @Override
    public void update(int id, String newName, int newCategoryId, int newBrandId, int newStock, int newPrice) throws IOException {
        Sheet sheet = connection.getSheet("Product");
        boolean updated = false;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                row.getCell(1).setCellValue(newName);
                row.getCell(2).setCellValue(newCategoryId);
                row.getCell(3).setCellValue(newBrandId);
                row.getCell(4).setCellValue(newStock);
                row.getCell(5).setCellValue(newPrice);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("Product with ID " + id + " not found.");
        }
        connection.save();
    }

    @Override
    public void delete(int id) throws IOException {
        Sheet sheet = connection.getSheet("Product");
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                sheet.removeRow(row);
                connection.save();
                return;
            }
        }
        throw new IllegalArgumentException("Product with ID " + id + " not found.");
    }

    @Override
    public Product findById(int id) throws IOException {
        Sheet sheet = connection.getSheet("Product");
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                String name = row.getCell(1).getStringCellValue();
                int idCategory = (int) row.getCell(2).getNumericCellValue();
                int idBrand = (int) row.getCell(3).getNumericCellValue();
                int stock = (int) row.getCell(4).getNumericCellValue();
                int price = (int) row.getCell(5).getNumericCellValue();
                return new Product(id, name, idCategory, idBrand, stock, price);
            }
        }
        throw new IllegalArgumentException("Product with ID " + id + " not found.");
    }
}