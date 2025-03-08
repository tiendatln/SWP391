/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Account;
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

    public List<Order> getAllOrderTotal() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT "
                + "    ot.orderID, ot.phoneNumber, ot.address, ot.note, "
                + "    ot.totalPrice, ot.date, ot.orderState, ot.voucherID, "
                + "    v.voucherCode, v.startDate, v.endDate, v.percentDiscount, v.quantity, v.usedTime, "
                + "    a.id AS accountID, a.username, a.email, a.password, a.phone_number AS accPhone, a.address AS accAddress, a.role, "
                + "    p.productID, p.productName, o.orderQuantity, o.orderPrice, "
                + "    p.proState, p.proImg, p.proDes, p.categoryID "
                + "FROM orderTotal ot "
                + "LEFT JOIN account a ON ot.id = a.id "
                + "LEFT JOIN voucher v ON ot.voucherID = v.voucherID "
                + "LEFT JOIN [order] o ON ot.orderID = o.orderID "
                + "LEFT JOIN product p ON o.productID = p.productID";

        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrderTotal ot = new OrderTotal(
                        rs.getInt("orderID"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("note"),
                        rs.getLong("totalPrice"),
                        rs.getDate("date"),
                        rs.getInt("orderState"),
                        rs.getInt("voucherID"),
                        new Account(
                                rs.getInt("accountID"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("accPhone"),
                                rs.getString("accAddress"),
                                rs.getString("role")
                        )
                );

                Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getInt("orderQuantity"),
                        rs.getLong("orderPrice"),
                        rs.getByte("proState"),
                        rs.getString("proImg"),
                        rs.getString("proDes"),
                        new Category(rs.getInt("categoryID")) // Cần điều chỉnh nếu Category có nhiều hơn 1 tham số
                );

                Order order = new Order(
                        product,
                        ot,
                        rs.getInt("orderQuantity"),
                        rs.getLong("orderPrice")
                );

                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Order> getOrderDetails(int orderID) {
    List<Order> orderDetails = new ArrayList<>();
    String sql = "SELECT "
            + "    ot.orderID, ot.phoneNumber, ot.address, ot.note, "
            + "    ot.totalPrice, ot.date, ot.orderState, ot.voucherID, "
            + "    v.voucherCode, v.startDate, v.endDate, v.percentDiscount, v.quantity, v.usedTime, "
            + "    a.id AS accountID, a.username, a.email, a.password, a.phone_number AS accPhone, a.address AS accAddress, a.role, "
            + "    p.productID, p.productName, o.orderQuantity, o.orderPrice, "
            + "    p.proState, p.proImg, p.proDes, p.categoryID "
            + "FROM orderTotal ot "
            + "LEFT JOIN account a ON ot.id = a.id "
            + "LEFT JOIN voucher v ON ot.voucherID = v.voucherID "
            + "LEFT JOIN [order] o ON ot.orderID = o.orderID "
            + "LEFT JOIN product p ON o.productID = p.productID "
            + "WHERE ot.orderID = ?;";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, orderID);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OrderTotal ot = new OrderTotal(
                        rs.getInt("orderID"),
                        rs.getString("phoneNumber"),
                        rs.getString("address"),
                        rs.getString("note"),
                        rs.getLong("totalPrice"),
                        rs.getDate("date"),
                        rs.getInt("orderState"),
                        rs.getInt("voucherID"),
                        new Account(
                                rs.getInt("accountID"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getString("accPhone"),
                                rs.getString("accAddress"),
                                rs.getString("role")
                        )
                );

                Product p = new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getInt("orderQuantity"),
                        rs.getLong("orderPrice"),
                        rs.getByte("proState"),
                        rs.getString("proImg"),
                        rs.getString("proDes"),
                        new Category(rs.getInt("categoryID"))
                );

                orderDetails.add(new Order(p, ot, rs.getInt("orderQuantity"), rs.getLong("orderPrice")));
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
            newOrder = rowsUpdated > 0 ? getAllOrderTotal() : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newOrder;
    }
}
