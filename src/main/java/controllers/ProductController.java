
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

    public void addProduct(String name, int categoryId, int brandId, int stock, int price) throws IOException {
        Product newProduct = new Product(0, name, categoryId, brandId, stock, price); // El ID se asignará automáticamente
        service.addProduct(newProduct);
    }

    public void updateProduct(int id, String newName, int newCategoryId, int newBrandId, int newStock, int newPrice) throws IOException {
        service.updateProduct(id, newName, newCategoryId, newBrandId, newStock, newPrice);
        System.out.println("Product updated successfully!");
    }

    public void deleteProduct(int id) throws IOException {
        service.deleteProduct(id);
        System.out.println("Product deleted successfully!");
    }

    public Product findById(int id) throws IOException {
        return service.findById(id);
    }
    
     public Map<String, String> getProductDetailsWithNames(List<Product> products, List<Category> categories, List<Brand> brands) {
        Map<String, String> productDetailsMap = new HashMap<>();

        // Crear mapas para acceder a las categorías y marcas por ID
        Map<Integer, String> categoryMap = new HashMap<>();
        Map<Integer, String> brandMap = new HashMap<>();

        // Llenar el mapa de categorías con los ID de categorías y sus nombres
        for (Category category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }

        // Llenar el mapa de marcas con los ID de marcas y sus nombres
        for (Brand brand : brands) {
            brandMap.put(brand.getId(), brand.getName());
        }

        // Procesar la lista de productos y agregar la categoría y marca correspondientes
        for (Product product : products) {
            String categoryName = categoryMap.get(product.getIdCategory());
            String brandName = brandMap.get(product.getIdBrand());

            if (categoryName != null && brandName != null) {
                // Crear el mapa de detalles del producto con los nombres
                String details = "Category: " + categoryName + ", Brand: " + brandName;
                productDetailsMap.put(product.getName(), details);
            }
        }

        return productDetailsMap;
    }
}
