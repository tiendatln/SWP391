/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
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
        String sql = "SELECT p.*, c.categoryID, c.categoryName "
                + "FROM product p "
                + "JOIN category c ON p.categoryID = c.categoryID "
                + "WHERE p.productID = ?";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category(
                            rs.getInt("categoryID"),
                            rs.getString("categoryName")
                    );

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
}
