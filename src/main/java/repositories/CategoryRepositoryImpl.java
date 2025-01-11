/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;

/**
 *
 * @author usuario
 */
import models.Category;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import utilities.ExcelDatabaseConnection;

public class CategoryRepositoryImpl implements CategoryRepository {
    
    private final ExcelDatabaseConnection connection;

    public CategoryRepositoryImpl(ExcelDatabaseConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<Category> findAll() throws IOException {
        Sheet sheet = connection.getSheet("Category");
        List<Category> categories = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String description = row.getCell(2).getStringCellValue();
            categories.add(new Category(id, name, description));
        }
        return categories;
    }

    @Override
    public void add(Category category) throws IOException {
        Sheet sheet = connection.getSheet("Category");
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
        newRow.createCell(1).setCellValue(category.getName()); // Category Name
        newRow.createCell(2).setCellValue(category.getDescription()); // Category Description

        connection.save();
    }

    @Override
    public void update(int id, String newName, String newDescription) throws IOException {
        Sheet sheet = connection.getSheet("Category");
        boolean updated = false;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                row.getCell(1).setCellValue(newName);
                row.getCell(2).setCellValue(newDescription);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("Category with ID " + id + " not found.");
        }
        connection.save();
    }

    @Override
    public void delete(int id) throws IOException {
        Sheet sheet = connection.getSheet("Category");
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
        throw new IllegalArgumentException("Category with ID " + id + " not found.");
    }

    @Override
    public Category findById(int id) throws IOException {
        Sheet sheet = connection.getSheet("Category");
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                String name = row.getCell(1).getStringCellValue();
                String description = row.getCell(2).getStringCellValue();
                return new Category(id, name, description);
            }
        }
        throw new IllegalArgumentException("Category with ID " + id + " not found.");
    }
}

