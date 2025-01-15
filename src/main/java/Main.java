

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

            // Create a new sale object
            Sale newSale = new Sale(1, LocalDate.of(2024, 2, 1), LocalTime.of(11, 50, 11), "JORGE BALDERA", productData, 0.0);
          //  Sale news = new Sale(1, LocalDate.of(2024, 2, 1), LocalTime.of(11, 50, 11), "JORGE BALDERA", productData, 0.0);
            // Add the sale to the Excel sheet
            repository.add(newSale);
            System.out.println("Sale added successfully!");
          //  repository.add(news);
            
            
          
          

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}