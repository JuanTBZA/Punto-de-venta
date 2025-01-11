package services;
import java.io.IOException;
import java.util.List;
import models.Category;
import repositories.CategoryRepositoryImpl;
import services.CategoryService;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepositoryImpl repository;

    public CategoryServiceImpl(CategoryRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        return repository.findAll();
    }

    @Override
    public void addCategory(Category category) throws IOException {
        repository.add(category);
    }

    @Override
    public void updateCategory(int id, String newName, String newDescription) throws IOException {
        repository.update(id, newName, newDescription);
    }

    @Override
    public void deleteCategory(int id) throws IOException {
        repository.delete(id);
    }

    @Override
    public Category findById(int id) throws IOException {
        return repository.findById(id);
    }
}
