/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.openmarket.domain.service;

import co.edu.unicauca.openmarket.access.IProductRepository;
import co.edu.unicauca.openmarket.domain.Category;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cesar
 */
public class CategoryService {
    
    private IProductRepository repository;
    
    public CategoryService(IProductRepository repository) {
        this.repository = repository;
    }
    
    public Category findCategory(Long id){
        return repository.findCategory(id);
    }
    
    public boolean deleteCategory(Long id){
        return repository.deleteCategory(id);
    }
    
    public boolean editCategory(Long categoryId, Category category){
        if(category == null || category.getName().isBlank()){
            return false;
        }
        return repository.editCategory(categoryId, category);
    }
    
    
    public boolean saveCategory(String name){
        Category category = new Category();
        category.setName(name);
        
        //Validate product
        if (category.getName().isBlank() ) {
            return false;
        }

        return repository.saveCategory(category);
    }
    
    public List<Category> findAllCategorys() {
        List<Category> categorys = new ArrayList<>();
        categorys = repository.findAllCategorys();;

        return categorys;
    }
}
