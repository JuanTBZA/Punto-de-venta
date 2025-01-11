/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author usuario
 */
import java.io.IOException;
import java.util.List;
import models.Brand;
import services.BrandServiceImpl;

public class BrandController {
    
    private final  BrandServiceImpl service;

    public BrandController(BrandServiceImpl service) {
        this.service = service;
    }



    public List<Brand> listBrands() throws IOException {
        return service.getAllBrands();
     
    }

public void addBrand(String name) throws IOException {
    Brand newBrand = new Brand(0, name); // El ID se asignará automáticamente
    service.addBrand(newBrand);
}


    public void updateBrand(int id, String newName) throws IOException {
        service.updateBrand(id, newName);
        System.out.println("Brand updated successfully!");
    }

    public void deleteBrand(int id) throws IOException {
        service.deleteBrand(id);
        System.out.println("Brand deleted successfully!");
    }

    
        public Brand findById(int id) throws IOException {
        Brand brand = service.findById(id);
        return brand;
    }
}


/*
if (brand == null) {
            System.out.println("No brands found with id: " + id);
        } else {
          return brand;
        
            
        }*/