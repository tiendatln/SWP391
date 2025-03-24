/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Category;
import Model.MainCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyễn Trường Vinh _ vinhntca181278
 */
public class CategoryDAO {

    Connection conn;

    public CategoryDAO() {
        try {
            conn = DBConnection.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT c.categoryID, c.type, m.mainCategoryID, m.mainCateName "
                + "FROM category c "
                + "JOIN mainCategory m ON c.mainCategoryID = m.mainCategoryID "
                + "WHERE c.categoryID = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MainCategory mainCategory = new MainCategory(
                            rs.getInt("mainCategoryID"),
                            rs.getString("mainCateName")
                    );

                    return new Category(
                            rs.getInt("categoryID"),
                            rs.getString("type"),
                            mainCategory
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy danh sách tất cả danh mục con cùng với danh mục chính
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT c.categoryID, c.type, m.mainCategoryID, m.mainCateName FROM category c JOIN mainCategory m ON c.mainCategoryID = m.mainCategoryID";

        try ( PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MainCategory mainCategory = new MainCategory(rs.getInt("mainCategoryID"), rs.getString("mainCateName"));
                categories.add(new Category(rs.getInt("categoryID"), rs.getString("type"), mainCategory));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

   public Category getNormalCategoryByID(int categoryID) {
        String sql = "SELECT c.categoryID, c.type, m.mainCategoryID, m.mainCateName " +
                     "FROM category c " +
                     "JOIN mainCategory m ON c.mainCategoryID = m.mainCategoryID " +
                     "WHERE c.categoryID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int mainCategoryID = rs.getInt("mainCategoryID");
                    String mainCategoryName = rs.getString("mainCateName");

                    MainCategory mainCategory = new MainCategory(mainCategoryID, mainCategoryName);
                    return new Category(rs.getInt("categoryID"), rs.getString("type"), mainCategory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
