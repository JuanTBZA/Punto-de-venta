package services;

import repositories.*;

import java.io.IOException;
import java.util.List;
import models.Product;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProducts() throws IOException {
        return repository.findAll();
    }

    @Override
    public void addProduct(Product product) throws IOException {
        repository.add(product);
    }

    @Override
    public void updateProduct(int id, String newName, String newCategoryName, String newBrandName, int newStock, int newPrice,String newLocation) throws IOException {
        repository.update(id, newName, newCategoryName, newBrandName, newStock, newPrice,newLocation);
    }

    @Override
    public void deleteProduct(int id) throws IOException {
        repository.delete(id);
    }

    @Override
    public Product findById(int id) throws IOException {
        return repository.findById(id);
    }
}
