/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Category;
import Model.Order;
import Model.OrderTotal;
import Model.Product;
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
 * @author tiend
 */
public class OrderDAO {

    Connection conn;

    public OrderDAO() {
        try {
            conn = DBConnection.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Order> GetAllOrderTotal() {
        List<Order> orderList = new ArrayList<>();
        ResultSet rs = null;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT \n"
                    + "    ot.orderID,\n"
                    + "    ot.phoneNumber,\n"
                    + "    ot.[address],\n"
                    + "    ot.note,\n"
                    + "    ot.totalPrice,\n"
                    + "    ot.[date],\n"
                    + "    ot.orderState,\n"
                    + "    ot.voucherCode,\n"
                    + "    a.username,\n"
                    + "    pFirst.proImg, \n"
                    + "    STRING_AGG(p.productName, ', ') AS productNames,\n"
                    + "    MIN(p.productID) AS productID, \n"
                    + "    MIN(o.oderQuantity) AS oderQuantity, \n"
                    + "    MIN(o.oderPrice) AS oderPrice \n"
                    + "FROM \n"
                    + "    orderTotal ot\n"
                    + "LEFT JOIN \n"
                    + "    account a ON ot.id = a.id\n"
                    + "LEFT JOIN \n"
                    + "    [order] o ON ot.orderID = o.orderID\n"
                    + "LEFT JOIN \n"
                    + "    [product] p ON o.productID = p.productID\n"
                    + "OUTER APPLY \n"
                    + "    (SELECT TOP 1 p2.proImg \n"
                    + "     FROM [order] o2 \n"
                    + "     JOIN [product] p2 ON o2.productID = p2.productID \n"
                    + "     WHERE o2.orderID = ot.orderID \n"
                    + "     ORDER BY o2.productID) AS pFirst\n"
                    + "GROUP BY \n"
                    + "    ot.orderID, ot.phoneNumber, ot.[address], ot.note, \n"
                    + "    ot.totalPrice, ot.[date], ot.orderState, ot.voucherCode, a.username, pFirst.proImg;"); // Đặt SQL đã sửa vào đây
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderTotal ot = new OrderTotal(
                        rs.getInt("orderID"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("note"),
                        rs.getLong("totalPrice"),
                        rs.getDate("date"),
                        rs.getInt("orderState"),
                        rs.getString("voucherCode")
                );

                Product p = new Product(
                        rs.getInt("productID"),
                        rs.getString("productNames"), // Vì đã gộp sản phẩm lại
                        0, // proQuantity (không có trong SQL)
                        0L, // proPrice (không có trong SQL)
                        (byte) 0, // proState (không có trong SQL)
                        rs.getString("proImg"),
                        "", // proDes (không có trong SQL)
                        null
                );

                orderList.add(new Order(p, ot, rs.getInt("oderQuantity"), rs.getLong("oderPrice")));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return orderList;

    }

    public List<Order> getOrderDetails(int orderID) {
        List<Order> orderDetails = new ArrayList<>();
        String sql = "SELECT *"
                + "FROM orderTotal ot\n"
                + "JOIN account a ON ot.id = a.id\n"
                + "JOIN [order] o ON ot.orderID = o.orderID\n"
                + "JOIN [product] p ON o.productID = p.productID\n"
                + "WHERE ot.orderID = ?;";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderID); // Sửa lỗi setInt(0, orderID) thành setInt(1, orderID)
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderTotal ot = new OrderTotal(
                            rs.getInt("orderID"), // Không có "o." vì orderID lấy từ orderTotal
                            rs.getString("phoneNumber"), // Không có "ot."
                            rs.getString("address"),
                            rs.getString("note"),
                            rs.getLong("totalPrice"),
                            rs.getDate("date"), // Không có "ot."
                            rs.getInt("orderState"),
                            rs.getString("voucherCode") != null ? rs.getString("voucherCode") : ""
                    );

                    Product p = new Product(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getInt("proQuantity"),
                            rs.getLong("proPrice"),
                            rs.getByte("proState"),
                            rs.getString("proImg"),
                            rs.getString("proDes"),
                            null // Chưa lấy category
                    );

                    orderDetails.add(new Order(p, ot, rs.getInt("oderQuantity"), rs.getLong("oderPrice")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public List<Order> UpdateStatusAndGetAllOrder(int id, int newStatus) {

        String sql = "UPDATE OrderTotal SET orderState = ? WHERE orderID = ?";
        int rowsUpdated;
        List<Order> newOrder = new ArrayList<>();
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newStatus);
            stmt.setInt(2, id);

            rowsUpdated = stmt.executeUpdate();
            newOrder = rowsUpdated > 0 ? GetAllOrderTotal() : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newOrder;
    }
}
