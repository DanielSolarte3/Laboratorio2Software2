package co.edu.unicauca.openmarket.access;

import co.edu.unicauca.openmarket.domain.Category;
import co.edu.unicauca.openmarket.domain.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Es una implementación que tiene libertad de hacer una implementación del
 * contrato. Lo puede hacer con Sqlite, postgres, mysql, u otra tecnología
 *
 * @author Libardo, Julio
 */
public class ProductRepository implements IProductRepository {

    private Connection conn;

    public ProductRepository() {
        initDatabase();
    }

    @Override
    public boolean save(Product newProduct) {

        try {
            //Validate product
            if (newProduct == null || newProduct.getName().isBlank()) {
                return false;
            }
            //this.connect();

            String sql = "INSERT INTO products ( name, description, categoryId ) "
                    + "VALUES ( ?, ?, ? )";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newProduct.getName());
            pstmt.setString(2, newProduct.getDescription());
            pstmt.setLong(3, newProduct.getCategory().getCategoryId());
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {

            String sql = "SELECT * FROM products INNER JOIN categorys "+
                         "ON products.categoryId = categorys.categoryId";
            //this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getLong("categoryId"));
                category.setName(rs.getString("categoryName"));
                Product newProduct = new Product();
                newProduct.setProductId(rs.getLong("productId"));
                newProduct.setName(rs.getString("name"));
                newProduct.setDescription(rs.getString("description"));
                newProduct.setCategory(category);
                products.add(newProduct);
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    private void initDatabase() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS categorys (\n"
                + "     categoryId integer PRIMARY KEY AUTOINCREMENT,\n"
                + "     categoryName text NOT NULL\n"
                + ");";
                
        String sql2 = "CREATE TABLE IF NOT EXISTS products (\n"
                + "	productId integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	name text NOT NULL,\n"
                + "	description text NULL,\n"
                + "     categoryId integer REFERENCES categorys(categoryId) ON DELETE CASCADE\n"
                + ");";
        try {
            this.connect();
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            stmt.execute(sql);
            stmt2.execute(sql2);
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        // SQLite connection string
        //String url = "jdbc:sqlite:./myDatabase.db"; //Para Linux/Mac
        //String url = "jdbc:sqlite:C:/sqlite/db/myDatabase.db"; //Para Windows
        String url = "jdbc:sqlite::memory:";

        try {
            conn = DriverManager.getConnection(url);

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public boolean edit(Long id, Product product) {
        try {
            //Validate product
            if (id <= 0 || product == null) {
                return false;
            }
            //this.connect();

            String sql = "UPDATE  products "
                    + "SET name=?, description=?, categoryId=?"
                    + "WHERE productId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setLong(3, product.getCategory().getCategoryId());
            pstmt.setLong(4, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        try {
            //Validate product
            if (id <= 0) {
                return false;
            }
            //this.connect();

            String sql = "DELETE FROM products "
                    + "WHERE productId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Product findById(Long id) {
        try {

            String sql = "SELECT * FROM products INNER JOIN categorys ON products.categoryId = categorys.categoryId "
                    + "WHERE productId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                Category cat = new Category();
                cat.setCategoryId(res.getLong("categoryId"));
                cat.setName(res.getString("categoryName"));
                Product prod = new Product();
                prod.setProductId(res.getLong("productId"));
                prod.setName(res.getString("name"));
                prod.setDescription(res.getString("description"));
                prod.setCategory(cat);
                return prod;
            } else {
                return null;
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Product findByName(String name) {
        try {

            String sql = "SELECT * FROM products INNER JOIN categorys ON products.categoryId = categorys.categoryId "
                    + "WHERE name = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                Category cat = new Category();
                cat.setCategoryId(res.getLong("categoryId"));
                cat.setName(res.getString("categoryName"));
                Product prod = new Product();
                prod.setProductId(res.getLong("productId"));
                prod.setName(res.getString("name"));
                prod.setDescription(res.getString("description"));
                prod.setCategory(cat);
                return prod;
            } else {
                return null;
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean saveCategory(Category category) {
        try {
            //Validate product
            if (category == null || category.getName().isBlank()) {
                return false;
            }
            //this.connect();

            String sql = "INSERT INTO categorys (categoryName) "
                    + "VALUES (?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getName());
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Category> findAllCategorys() {
        List<Category> categorys = new ArrayList<>();
        try {

            String sql = "SELECT * FROM categorys";
            //this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getLong("categoryId"));
                category.setName(rs.getString("categoryName"));

                categorys.add(category);
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categorys;
    }
    
    
    @Override
    public List<Product> findByCategory(Long id) {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products INNER JOIN categorys ON "+
                         "products.categoryId = categorys.categoryId WHERE categorys.categoryId = ?";
            //this.connect();
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            
            ResultSet rs = pstmt.executeQuery(); 
            
            while (rs.next()) {
                Product product = new Product();
                Category category = new Category();
                category.setCategoryId(rs.getLong("categoryId"));
                category.setName(rs.getString("categoryName"));
                product.setProductId(rs.getLong("productId"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setCategory(category);
                products.add(product);
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    @Override
    public boolean editCategory(Long id, Category category) {
        try {
            //Validate product
            if (id <= 0 || category == null) {
                return false;
            }
            //this.connect();

            String sql = "UPDATE  categorys "
                    + "SET categoryName = ?"
                    + "WHERE categoryId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getName());
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Category findCategory(Long id) {
        try {

            String sql = "SELECT * FROM categorys  "
                    + "WHERE categoryId = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            ResultSet res = pstmt.executeQuery();

            if (res.next()) {
                Category cat = new Category();
                cat.setCategoryId(res.getLong("categoryId"));
                cat.setName(res.getString("categoryName"));
                return cat;
            } else {
                return null;
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean deleteCategory(Long id) {
       try {
            //Validate product
            if (id <= 0) {
                return false;
            }
            //this.connect();

            String sql = "DELETE FROM categorys "
                    + "WHERE categoryId = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
