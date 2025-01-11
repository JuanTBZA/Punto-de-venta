/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import controllers.BrandController;
import java.io.IOException;
import repositories.BrandRepositoryImpl;
import services.BrandServiceImpl;
import utilities.ExcelDatabaseConnection;

public class Main {
    public static void main(String[] args) throws IOException {
     
        ExcelDatabaseConnection conection = new ExcelDatabaseConnection();
        BrandRepositoryImpl brandRepository = new BrandRepositoryImpl(conection);
        BrandServiceImpl brandService =new BrandServiceImpl(brandRepository);
        BrandController brandController = new BrandController(brandService);

        // Agregar marcas
        brandController.addBrand("funa");
      //  brandController.addBrand("Adidas");

        // Listar marcas
        System.out.println("Lista de marcas:");
        brandController.listBrands();

        // Buscar por nombre
        //System.out.println("Buscar marcas con nombre 'Nike':");
        //brandController.searchBrandByName("Nike");
        
        //buscar por id
        //System.out.println("Buscar marcas con id 'Nike':1");
        //brandController.findById(1);

        // Modificar marca
        //brandController.updateBrand(1, "Nike Updated");

        // Eliminar marca
        //brandController.deleteBrand(2);

        // Listar marcas actualizadas
       // System.out.println("Lista actualizada de marcas:");
      //  brandController.listBrands();

        conection.close();
    }
}
