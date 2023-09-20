package co.edu.unicauca.openmarket.domain.service;

import co.edu.unicauca.openmarket.access.Factory;
import co.edu.unicauca.openmarket.access.IProductRepository;
import co.edu.unicauca.openmarket.domain.Category;
import co.edu.unicauca.openmarket.domain.Product;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServiceTest {
    
    IProductRepository repository = Factory.getInstance().getRepository("default");
    ProductService productService = new ProductService(repository);
    CategoryService categoryService = new CategoryService(repository);
   
    String productNameExpResult = "Papas";
    String productDescriptionExpResult = "Fritas";
    String categoryNameExpResult = "Snacks";
    String categoryNameExpResult2 = "Bebidas";
    
    Product p1 = new Product(2L, "Jugo", "Sabor limon", 1000);
    Product p2 = new Product(3L, "Papel", "Higienico", 2000);
    Product p3 = new Product(2L, "Barra", "Hecha de chocolate", 1500);
    Product p4 = new Product(1L, productNameExpResult, productDescriptionExpResult, 2000);
    Category c1 = new Category(1L, categoryNameExpResult);
    Category c2 = new Category(2L, categoryNameExpResult2);
    
    public ServiceTest() {
        this.categoryService.saveCategory(categoryNameExpResult);
        this.categoryService.saveCategory(categoryNameExpResult2);
        this.productService.saveProduct(productNameExpResult, productDescriptionExpResult, 1L);
        this.p1.setCategory(c1);
        this.p2.setCategory(c1);
    }

    
    //TEST DE FUNCIONES PARA LOS PRODUCTOS
    @Test
    public void testSaveProduct() {
        System.out.println("saveProduct");
        String name = "Leche";
        String description = "De vaca"; 
        boolean expResult = true;
        boolean result = productService.saveProduct(name, description, 1L);
        assertEquals(expResult, result);
    }

    @Test
    public void testFindAllProducts() {
        System.out.println("findAllProducts");
        productService.saveProduct("Jugo", "Sabor limon", 1L);
        productService.saveProduct("Papel", "Higienico", 1L);
        List<Product> expResult = new ArrayList<>();
        expResult.add(p1);
        expResult.add(p2);
        List<Product> result = productService.findAllProducts();
        for(int i=0; i<expResult.size(); i++){
            assertEquals(expResult.get(i).getName(), result.get(i+1).getName());
            assertEquals(expResult.get(i).getDescription(), result.get(i+1).getDescription());
        }
    }

    @Test
    public void testFindProductById() {
        Long idProduct = 1L;
        System.out.println("findProductById");
        Product result = productService.findProductById(idProduct);
        assertEquals(productNameExpResult, result.getName());
        assertEquals(productDescriptionExpResult, result.getDescription());
    }

    @Test
    public void testFindProductByName() {
        System.out.println("findProductByName");
        Product result = productService.findProductByName(productNameExpResult);
        assertEquals(productNameExpResult, result.getName());
        assertEquals(productDescriptionExpResult, result.getDescription());
    }

    @Test
    public void testFindByCategory() {
        Long idCategory = 1L;
        System.out.println("findByCategory");
        
        p4.setCategory(c1);
        p3.setCategory(c1);
        List<Product> expResult = new ArrayList<>();
        
        expResult.add(p4); //Snack 1
        expResult.add(p3); //Snack 2
        
        productService.saveProduct("Barra", "Hecha de chocolate", 1L);
        
        List<Product> result = productService.findByCategory(idCategory);
        
        for(int i=0; i<expResult.size(); i++){
            assertEquals(expResult.get(i).getName(), result.get(i).getName());
            assertEquals(expResult.get(i).getDescription(), result.get(i).getDescription());
        }
    }

    @Test
    public void testDeleteProduct() {
        System.out.println("deleteProduct");
        Long id = 1L;
        boolean expResult = true;
        boolean result = productService.deleteProduct(id);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEditProduct() {
        Long id = 1L;
        System.out.println("editProduct");
        boolean expResult = true;
        boolean result = productService.editProduct(id, p1);
        assertEquals(expResult, result);
        assertEquals(p1.getName(), productService.findProductById(id).getName());
        assertEquals(p1.getDescription(), productService.findProductById(id).getDescription());
    }
    
    //TEST DE FUNCIONES PARA LAS CATEGORIAS
    @Test
    public void testFindCategory() {
        Long idCategory = 1L;
        System.out.println("findCategory");
        Category result = categoryService.findCategory(idCategory);
        assertEquals(categoryNameExpResult, result.getName());
    }

    
    @Test
    public void testDeleteCategory() {
        System.out.println("deleteCategory");
        Long idCategory = 1L;
        boolean expResult = true;
        boolean result = categoryService.deleteCategory(idCategory);
        assertEquals(expResult, result);
    }


    @Test
    public void testEditCategory() {
        System.out.println("editCategory");
        Long idCategory = 1L;
        boolean expResult = true;
        boolean result = categoryService.editCategory(idCategory, c1);
        assertEquals(expResult, result);
        assertEquals(c1.getName(), categoryService.findCategory(idCategory).getName());
    }

    @Test
    public void testSaveCategory() {
        System.out.println("saveCategory");
        String name = "Cocina";
        boolean expResult = true;
        boolean result = categoryService.saveCategory(name);
        assertEquals(expResult, result);
    }

    @Test
    public void testFindAllCategorys() {
        System.out.println("findAllCategorys");
        List<Category> expResult = new ArrayList<>();
        expResult.add(c1);
        expResult.add(c2);
        List<Category> result = categoryService.findAllCategorys();
        for(int i=0; i<expResult.size(); i++){
            assertEquals(expResult.get(i).getName(), result.get(i).getName());
        }
    }
    
    
    
}
