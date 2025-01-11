/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import repositories.*;

import java.io.IOException;
import java.util.List;
import models.Brand;

public class BrandServiceImpl implements BrandService {

    private final BrandRepositoryImpl repository;

    public BrandServiceImpl(BrandRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public List<Brand> getAllBrands() throws IOException {
        return repository.findAll();
    }

    @Override
    public void addBrand(Brand brand) throws IOException {
        repository.add(brand);
    }

    @Override
    public void updateBrand(int id, String newName) throws IOException {
        repository.update(id, newName);
    }

    @Override
    public void deleteBrand(int id) throws IOException {
        repository.delete(id);
    }

    @Override
    public Brand findById(int id) throws IOException {
        return repository.findById(id);
    }
}
