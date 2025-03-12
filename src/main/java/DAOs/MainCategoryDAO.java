/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

/**
 *
 * @author Nguyễn Trường Vinh _ vinhntca181278
 */


import DB.DBConnection;
import Model.MainCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainCategoryDAO {
    Connection conn;

    public MainCategoryDAO() {
        try {
            conn = DBConnection.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MainCategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Lấy tất cả danh mục chính từ database
    public List<MainCategory> getAllMainCategories() {
        List<MainCategory> mainCategories = new ArrayList<>();
        String sql = "SELECT * FROM mainCategory";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mainCategories.add(new MainCategory(rs.getInt("mainCategoryID"), rs.getString("mainCateName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mainCategories;
    }
}
