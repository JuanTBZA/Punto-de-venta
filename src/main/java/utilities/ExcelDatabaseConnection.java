
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;



public class ExcelDatabaseConnection {
    private static final String FILE_PATH = "database.xlsx";
    private Workbook workbook;

    public ExcelDatabaseConnection() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            workbook = new XSSFWorkbook();
            createDefaultSheetsWithHeaders();
            save();
        } else {
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            fis.close();
        }
    }

    public Sheet getSheet(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);
            setDefaultHeaders(sheet);
        }
        return sheet;
    }

    private void setDefaultHeaders(Sheet sheet) {
        // Establecer encabezados predeterminados dependiendo del nombre de la hoja
        String[] headers;
        switch (sheet.getSheetName()) {
          
            case "Cliente":
                headers = new String[]{"ID", "Nombre", "DNI", "Apodo"};
                break;
            case "Categoria":
                headers = new String[]{"ID", "Nombre", "Descripcion"};
                break;
            case "Producto":
                headers = new String[]{"ID", "Nombre", "Categoria", "Marca", "Stock", "Precio", "Ubicacion"};
                break;
            case "Venta":
                headers = new String[]{"ID", "Fecha", "Hora", "Cliente", "Cantidad", "Producto", "Precio", "Subtotal"};
                break;
            default:
                return; // Si no se encuentra la hoja, no hace nada
        }

        Row headerRow = sheet.createRow(0); // Crea la primera fila para los encabezados
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    private void createDefaultSheetsWithHeaders() {
        // Crea las hojas con los encabezados predeterminados
       
        getSheet("Cliente");
        getSheet("Categoria");
        getSheet("Producto");
        getSheet("Venta");
    }

    public void save() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
            workbook.write(fos);
        }
    }

    public void close() throws IOException {
        workbook.close();
    }
} 