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
        Sheet sheet = connection.getSheet("Producto");
        List<Product> products = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String categoryName = row.getCell(2).getStringCellValue();
            String brandName = row.getCell(3).getStringCellValue();
            int stock = (int) row.getCell(4).getNumericCellValue();
            Double price = (Double) row.getCell(5).getNumericCellValue();
            String location = row.getCell(6).getStringCellValue();
            products.add(new Product(id, name, categoryName, brandName, stock, price,location));
        }
        return products;
    }

    @Override
    public void add(Product product) throws IOException {
        Sheet sheet = connection.getSheet("Producto");
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
        newRow.createCell(2).setCellValue(product.getCategoryName()); // Category Name
        newRow.createCell(3).setCellValue(product.getBrandName()); // Brand Name
        newRow.createCell(4).setCellValue(product.getStock()); // Stock
        newRow.createCell(5).setCellValue(product.getPrice()); // Price
        newRow.createCell(6).setCellValue(product.getLocation()); // Price
        
        connection.save();
    }

    @Override
    public void update(int id, String newName, String newCategoryName, String newBrandName, int newStock, Double newPrice, String newLocation) throws IOException {
        Sheet sheet = connection.getSheet("Producto");
        boolean updated = false;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                row.getCell(1).setCellValue(newName);
                row.getCell(2).setCellValue(newCategoryName);
                row.getCell(3).setCellValue(newBrandName);
                row.getCell(4).setCellValue(newStock);
                row.getCell(5).setCellValue(newPrice);
                row.getCell(6).setCellValue(newLocation);
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
        Sheet sheet = connection.getSheet("Producto");
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
        Sheet sheet = connection.getSheet("Producto");
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                String name = row.getCell(1).getStringCellValue();
                String categoryName = row.getCell(2).getStringCellValue();
                String brandName = row.getCell(3).getStringCellValue();
                int stock = (int) row.getCell(4).getNumericCellValue();
                Double price = (Double) row.getCell(5).getNumericCellValue();
                String location = row.getCell(6).getStringCellValue();
                return new Product(id, name, categoryName, brandName, stock, price,location);
            }
        }
        throw new IllegalArgumentException("Product with ID " + id + " not found.");
    }
}
