package controllers;

import java.io.IOException;
import java.util.List;
import models.Category;
import services.CategoryServiceImpl;

public class CategoryController {

    private final CategoryServiceImpl service;

    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }

    public List<Category> listCategories() throws IOException {
        return service.getAllCategories();
    }

    public void addCategory(String name, String description) throws IOException {
        Category newCategory = new Category(0, name, description); // El ID se asignará automáticamente
        service.addCategory(newCategory);
    }

    public void updateCategory(int id, String newName, String newDescription) throws IOException {
        service.updateCategory(id, newName, newDescription);
        System.out.println("Category updated successfully!");
    }

    public void deleteCategory(int id) throws IOException {
        service.deleteCategory(id);
        System.out.println("Category deleted successfully!");
    }

    public Category findById(int id) throws IOException {
        return service.findById(id);
    }
}
