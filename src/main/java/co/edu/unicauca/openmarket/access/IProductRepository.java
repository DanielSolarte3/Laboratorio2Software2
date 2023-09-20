
package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Product;
import co.edu.unicauca.openmarket.domain.Category;
import java.util.List;

/**
 *
 * @author Libardo, Julio
 */
public interface IProductRepository {
    boolean save(Product newProduct);
    
    boolean saveCategory(Category category);
    
    boolean edit(Long id, Product product);
    
    boolean editCategory(Long id, Category category);
    
    boolean delete(Long id);
    
    boolean deleteCategory(Long id);

    Product findById(Long id);
    
    Product findByName(String name);
    
    Category findCategory(Long id);
    
    List<Product> findByCategory(Long id);
    
    List<Product> findAll();
    
    List<Category> findAllCategorys();
    
}
