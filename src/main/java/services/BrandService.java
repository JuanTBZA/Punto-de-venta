/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services;

import java.io.IOException;
import java.util.List;
import models.Brand;


public interface BrandService {

    public List<Brand> getAllBrands() throws IOException;

    public void addBrand(Brand brand) throws IOException;

    public void updateBrand(int id, String newName) throws IOException;

    public void deleteBrand(int id) throws IOException;

  

    public Brand findById(int id) throws IOException;
}
