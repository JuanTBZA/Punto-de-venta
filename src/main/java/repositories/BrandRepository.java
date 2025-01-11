/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositories;

import java.io.IOException;
import java.util.List;
import models.Brand;

/**
 *
 * @author usuario
 */
public interface BrandRepository {
    public List<Brand> findAll()throws IOException;
    public void add(Brand brand) throws IOException;
    public void update(int id, String newName) throws IOException;
    public void delete(int id) throws IOException;
    public Brand findById(int id) throws IOException;
}
