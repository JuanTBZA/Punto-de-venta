
package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Brand;
import models.Category;
import models.Product;
import services.ProductServiceImpl;

public class ProductController {

    private final ProductServiceImpl service;

    public ProductController(ProductServiceImpl service) {
        this.service = service;
    }

    public List<Product> listProducts() throws IOException {
        return service.getAllProducts();
    }

    public void addProduct(String name, String categoryName, String brandName, int stock, int price, String location) throws IOException {
        Product newProduct = new Product(0, name, categoryName, brandName, stock, price, location); // El ID se asignará automáticamente
        service.addProduct(newProduct);
    }

    public void updateProduct(int id, String newName, String categoryName, String brandName, int newStock, int newPrice, String newLocation) throws IOException {
        service.updateProduct(id, newName, categoryName, brandName, newStock, newPrice, newLocation);
        System.out.println("Product updated successfully!");
    }

    public void deleteProduct(int id) throws IOException {
        service.deleteProduct(id);
        System.out.println("Product deleted successfully!");
    }

    public Product findById(int id) throws IOException {
        return service.findById(id);
    }
    
}
