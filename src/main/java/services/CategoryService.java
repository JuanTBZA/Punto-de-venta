/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services;

import java.io.IOException;
import java.util.List;
import models.Category;

/**
 *
 * @author usuario
 */
public interface CategoryService {
 
    public List<Category> getAllCategories() throws IOException;


    public void addCategory(Category category) throws IOException;

  
    public void updateCategory(int id, String newName, String newDescription)  throws IOException ;

    public void deleteCategory(int id) throws IOException ;
    
    public Category findById(int id) throws IOException;
}
