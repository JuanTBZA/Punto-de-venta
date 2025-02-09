

import java.io.File;
import models.Sale;
import repositories.SaleRepositoryImpl;
import utilities.ExcelDatabaseConnection;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            
            

        
            // Initialize the Excel connection
            ExcelDatabaseConnection conection = new ExcelDatabaseConnection();

            // Initialize the repository
            SaleRepositoryImpl repository = new SaleRepositoryImpl(conection);

            // Create sample product data
            List<String[]> productData = new ArrayList<>();
            productData.add(new String[]{"3", "CHEM-RISE", "10"});
            productData.add(new String[]{"2", "LEGADO 50 EC", "3"});
            productData.add(new String[]{"2", "BORO", "3"});
            productData.add(new String[]{"12", "BOROs", "10"});
            
            List<String[]> productData1 = new ArrayList<>();
            productData1.add(new String[]{"3", "CHEM-RISE", "10"});
            productData1.add(new String[]{"12", "BOROs", "10"});

            // Create a new sale object
            Sale newSale = new Sale(1, LocalDate.of(2024, 2, 1), LocalTime.of(11, 50, 11), "JORGE BALDERA", productData, 0.0);
            
             Sale newSale1 = new Sale(2, LocalDate.of(2025, 1, 2), LocalTime.of(10, 40, 10), "JORGE ", productData1, 0.0);
          //  Sale news = new Sale(1, LocalDate.of(2024, 2, 1), LocalTime.of(11, 50, 11), "JORGE BALDERA", productData, 0.0);
            // Add the sale to the Excel sheet
            repository.add(newSale);
            repository.add(newSale1);
            System.out.println("Sale added successfully!");
          //  repository.add(news);
            
            
          
           try {
            // Llama al método findAll para obtener todas las ventas
            List<Sale> sales = repository.findAll();

            // Muestra los resultados
            for (Sale sale : sales) {
                System.out.println("ID: " + sale.getId());
                System.out.println("Fecha: " + sale.getDate());
                System.out.println("Hora: " + sale.getHora());
                System.out.println("Cliente: " + sale.getClient());
                System.out.println("Productos:");
                for (String[] product : sale.getProductData()) {
                    System.out.println("  Cantidad: " + product[0] + ", Producto: " + product[1] + ", Precio: " + product[2]);
                }
                System.out.println("Total: " + sale.getTotalAmount());
                System.out.println();
            }

        } catch (IOException e) {
            System.out.println("Error al recuperar las ventas: " + e.getMessage());
        }
     

            // Create a new sale object
           //  new Sale(1, LocalDate.of(2025, 2, 1), LocalTime.of(12, 50, 11), "JORGE", productData1, 0.0);
            
           Sale newSale2 = repository.findById(2);
           
           System.out.println("ID: " + newSale2.getId());
                System.out.println("Fecha: " + newSale2.getDate());
                System.out.println("Hora: " + newSale2.getHora());
                System.out.println("Cliente: " + newSale2.getClient());
                System.out.println("Productos:");
                for (String[] product : newSale2.getProductData()) {
                    System.out.println("  Cantidad: " + product[0] + ", Producto: " + product[1] + ", Precio: " + product[2]);
                }
                System.out.println("Total: " + newSale2.getTotalAmount());
                System.out.println();
          
          

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}