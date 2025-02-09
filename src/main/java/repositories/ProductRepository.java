/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories;

import java.io.IOException;
import java.util.List;
import models.Product;

/**
 *
 * @author usuario
 */
public interface ProductRepository {
        public List<Product> findAll() throws IOException;

    public void add(Product product) throws IOException;

    public void update(int id, String newName, String newCategoryName, String newBrandName, int newStock, Double newPrice, String newlocation) throws IOException;

    public void delete(int id) throws IOException;

    public Product findById(int id) throws IOException;
}
