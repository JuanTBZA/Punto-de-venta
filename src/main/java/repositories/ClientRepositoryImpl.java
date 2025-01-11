/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositories;


import models.Client;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import utilities.ExcelDatabaseConnection;

public class ClientRepositoryImpl implements ClientRepository {
    
    private final ExcelDatabaseConnection connection;

    public ClientRepositoryImpl(ExcelDatabaseConnection connection) {
        this.connection = connection;
    }

    @Override
    public List<Client> findAll() throws IOException {
        Sheet sheet = connection.getSheet("Client");
        List<Client> clients = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String dni = row.getCell(2).getStringCellValue();
            String nickname = row.getCell(3).getStringCellValue();
            clients.add(new Client(id, name, dni, nickname));
        }
        return clients;
    }

    @Override
    public void add(Client client) throws IOException {
        Sheet sheet = connection.getSheet("Client");
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
        newRow.createCell(1).setCellValue(client.getName()); // Client Name
        newRow.createCell(2).setCellValue(client.getDni()); // Client DNI
        newRow.createCell(3).setCellValue(client.getNickname()); // Client Nickname

        connection.save();
    }

    @Override
    public void update(int id, String newName, String newDni, String newNickname) throws IOException {
        Sheet sheet = connection.getSheet("Client");
        boolean updated = false;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                row.getCell(1).setCellValue(newName);
                row.getCell(2).setCellValue(newDni);
                row.getCell(3).setCellValue(newNickname);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("Client with ID " + id + " not found.");
        }
        connection.save();
    }

    @Override
    public void delete(int id) throws IOException {
        Sheet sheet = connection.getSheet("Client");
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
        throw new IllegalArgumentException("Client with ID " + id + " not found.");
    }

    @Override
    public Client findById(int id) throws IOException {
        Sheet sheet = connection.getSheet("Client");
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header
            if ((int) row.getCell(0).getNumericCellValue() == id) {
                String name = row.getCell(1).getStringCellValue();
                String dni = row.getCell(2).getStringCellValue();
                String nickname = row.getCell(3).getStringCellValue();
                return new Client(id, name, dni, nickname);
            }
        }
        throw new IllegalArgumentException("Client with ID " + id + " not found.");
    }
}

