/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;
import DB.DBConnection;
import Model.Product;
import Model.Category;
import DAOs.CategoryDAO;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Nguyễn Trường Vinh _ vinhntca181278
 */
public class ProductDAO {
    Connection conn;
    
     public ProductDAO() {
        try {
            conn = DBConnection.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     // Add product
    public boolean addProduct(String name, int quantity, double price, boolean state, String img, String description, int categoryID) {
        String sql = "INSERT INTO product (productName, proQuantity, proPrice, proState, proImg, proDes, categoryID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, state);
            stmt.setString(5, img);
            stmt.setString(6, description);
            stmt.setInt(7, categoryID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update product
    public boolean updateProduct(int id, String name, int quantity, double price, boolean state, String img, String description, int categoryID) {
        String sql = "UPDATE product SET productName=?, proQuantity=?, proPrice=?, proState=?, proImg=?, proDes=?, categoryID=? WHERE productID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, state);
            stmt.setString(5, img);
            stmt.setString(6, description);
            stmt.setInt(7, categoryID);
            stmt.setInt(8, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete product
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE productID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
        // Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        CategoryDAO categoryDAO = new CategoryDAO();
      

        String sql = "SELECT * FROM product";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category category;
                category = categoryDAO.getCategoryById(rs.getInt("categoryID"));
                Product product = new Product(
                    rs.getInt("productID"),
                    rs.getString("productName"),
                    rs.getInt("proQuantity"),
                    rs.getInt("proPrice"),
                    rs.getByte("proState"),
                    rs.getString("proImg"),
                    rs.getString("proDes"),
                    category
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    
    public Product getProductById(int id) {
    Product product = null;
    CategoryDAO categoryDAO = new CategoryDAO();
    String sql = "SELECT * FROM Products WHERE productID = ?";
    
    try (Connection conn = DBConnection.connect();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            Category category;
                category = categoryDAO.getCategoryById(rs.getInt("categoryID"));
            product = new Product(
                rs.getInt("productID"),
                rs.getString("productName"),
                rs.getInt("proQuantity"),
                rs.getLong("proPrice"),
                rs.getByte("proState"),
                rs.getString("proImg"),
                rs.getString("proDescription"),
                 category
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return product;
}

}
