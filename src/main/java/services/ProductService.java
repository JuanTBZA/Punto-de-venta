/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services;

import java.io.IOException;
import java.util.List;
import models.Product;

public interface ProductService {

    public List<Product> getAllProducts() throws IOException;
    public void addProduct(Product product) throws IOException ;
    public void updateProduct(int id, String newName, int newCategoryId, int newBrandId, int newStock, int newPrice) throws IOException ;
    public void deleteProduct(int id) throws IOException ;
    public Product findById(int id) throws IOException ;

}
