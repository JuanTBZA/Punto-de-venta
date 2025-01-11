/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories;

import java.io.IOException;
import java.util.List;
import models.Category;

/**
 *
 * @author usuario
 */
public interface CategoryRepository {
    public List<Category> findAll() throws IOException;
    public void add(Category category) throws IOException;
    public void update(int id, String newName, String newDescription) throws IOException;
    public void delete(int id) throws IOException;
    public Category findById(int id) throws IOException;
}
