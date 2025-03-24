/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Account;
import Model.Cart;
import Model.Category;
import Model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author hnpt6504
 */
public class CartDAO {

    public Product getProductById(int productId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT p.*, c.categoryID, c.[type] "
                + "FROM product p "
                + "JOIN category c ON p.categoryID = c.categoryID "
                + "WHERE p.productID = ?";
        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(rs.getInt("categoryID"), rs.getString("type"));
                    return new Product(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getInt("proQuantity"),
                            rs.getLong("proPrice"),
                            rs.getByte("proState"),
                            rs.getString("proImg"),
                            rs.getString("proDes"),
                            category
                    );
                }
            }
        }
        return null;
    }

    public void addToCart(int userId, int productId, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "MERGE INTO cart AS target "
                + "USING (SELECT ? AS id, ? AS productID, ? AS quantity) AS source "
                + "ON target.id = source.id AND target.productID = source.productID "
                + "WHEN MATCHED THEN UPDATE SET target.quantity = target.quantity + source.quantity "
                + "WHEN NOT MATCHED THEN INSERT (id, productID, quantity) VALUES (source.id, source.productID, source.quantity);";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public void removeFromCart(int userId, int productId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM cart WHERE id = ? AND productID = ?";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void updateCart(int userId, int productId, int quantity) throws SQLException, ClassNotFoundException {
        if (quantity > 0) {
            String sql = "UPDATE cart SET quantity = ? WHERE id = ? AND productID = ?";
            try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, quantity);
                ps.setInt(2, userId);
                ps.setInt(3, productId);
                ps.executeUpdate();
            }
        } else {
            removeFromCart(userId, productId);
        }
    }

    public Cart loadCartFromDB(int userId) throws SQLException, ClassNotFoundException {
        Cart cart = new Cart(userId);
        String sql = "SELECT c.productID, c.quantity, p.productName, p.proQuantity, p.proPrice, p.proState, p.proImg, p.proDes, cat.categoryID, cat.type "
                + "FROM cart c "
                + "JOIN product p ON c.productID = p.productID "
                + "JOIN category cat ON p.categoryID = cat.categoryID "
                + "WHERE c.id = ?";
        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(rs.getInt("categoryID"), rs.getString("type"));
                    Product product = new Product(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getInt("proQuantity"),
                            rs.getLong("proPrice"),
                            rs.getByte("proState"),
                            rs.getString("proImg"),
                            rs.getString("proDes"),
                            category
                    );
                    cart.addItem(product, rs.getInt("quantity"));
                }
            }
        }
        return cart;
    }

    public Account getAccountByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Account WHERE username = ?";
        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setUsername(rs.getString("username"));
                    account.setPassword(rs.getString("password"));
                    account.setRole(rs.getString("role"));
                    // Thêm các trường khác nếu cần
                    return account;
                }
            }
        }
        return null;
    }

    public boolean checkProductInCart(int productId) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.connect();
        String query = "SELECT * FROM cart WHERE productID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();

        // Nếu tìm thấy sản phẩm trong giỏ hàng, trả về true
        return rs.next();
    }

}
