package repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import models.Brand;
import org.apache.poi.ss.usermodel.*;
import utilities.ExcelDatabaseConnection;


public class BrandRepositoryImpl implements BrandRepository{
   
    private final  ExcelDatabaseConnection connection;

    public BrandRepositoryImpl(ExcelDatabaseConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<Brand> findAll() throws IOException {
        Sheet sheet = connection.getSheet("Brand");
        List<Brand> brands = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            brands.add(new Brand(id, name));
        }
        return brands;
    }

    @Override
    public void add(Brand brand) throws IOException {
        Sheet sheet = connection.getSheet("Brand");
        int newId = 1; // Default para el primer registro

        // Encontrar el último ID en la columna 0 (ID)
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Saltar cabecera
            int currentId = (int) row.getCell(0).getNumericCellValue();
            if (currentId >= newId) {
                newId = currentId + 1; // Incrementar el ID más alto encontrado
            }
        }

        // Crear una nueva fila al final del archivo
        Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
        newRow.createCell(0).setCellValue(newId); // ID generado
        newRow.createCell(1).setCellValue(brand.getName()); // Nombre de la marca

        connection.save();
    }

    @Override
    public void update(int id, String newName) throws IOException {
        Sheet sheet = connection.getSheet("Brand");
        boolean updated = false;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Saltar cabecera
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                row.getCell(1).setCellValue(newName);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("Brand with ID " + id + " not found.");
        }
        connection.save();
    }

    @Override
    public void delete(int id) throws IOException {
        Sheet sheet = connection.getSheet("Brand");
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
        throw new IllegalArgumentException("Brand with ID " + id + " not found.");
    }

    
    @Override
    public Brand findById(int id) throws IOException {
    Sheet sheet = connection.getSheet("Brand");
    for (Row row : sheet) {
        if (row.getRowNum() == 0) continue; // Skip header
        if ((int) row.getCell(0).getNumericCellValue() == id) {
            String name = row.getCell(1).getStringCellValue();
            return new Brand(id, name);
        }
    }
    throw new IllegalArgumentException("Brand with ID " + id + " not found.");
}

}
