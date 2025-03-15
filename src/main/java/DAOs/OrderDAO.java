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
import Model.Voucher;
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

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            try ( ResultSet rs = stmt.executeQuery()) {
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

    public boolean addNewOrder(OrderTotal orderTotal, List<Order> orderDetails) {
      
        PreparedStatement pstmtOrderTotal = null;
        PreparedStatement pstmtOrder = null;
        ResultSet generatedKeys = null;

        try {
           
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Thêm vào bảng orderTotal
            String insertOrderTotalSQL = "INSERT INTO orderTotal (phoneNumber, [address], note, totalPrice, [date], orderState, voucherID, id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtOrderTotal = conn.prepareStatement(insertOrderTotalSQL, Statement.RETURN_GENERATED_KEYS);
            pstmtOrderTotal.setString(1, orderTotal.getPhoneNumber());
            pstmtOrderTotal.setString(2, orderTotal.getAddress());
            pstmtOrderTotal.setString(3, orderTotal.getNote());
            pstmtOrderTotal.setLong(4, orderTotal.getTotalPrice());
            pstmtOrderTotal.setTimestamp(5, new java.sql.Timestamp(orderTotal.getDate().getTime()));
            pstmtOrderTotal.setInt(6, orderTotal.getOrderState());
            if (orderTotal.getVoucherCode() == 0) { // Giả sử 0 là giá trị không có voucher
                pstmtOrderTotal.setNull(7, java.sql.Types.INTEGER);
            } else {
                pstmtOrderTotal.setInt(7, orderTotal.getVoucherCode());
            }
            pstmtOrderTotal.setInt(8, orderTotal.getAccount().getId());

            int affectedRows = pstmtOrderTotal.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false; // Thêm OrderTotal thất bại
            }

            // Lấy orderID vừa được tạo
            generatedKeys = pstmtOrderTotal.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderID = generatedKeys.getInt(1);
                orderTotal.setOrderID(orderID); // Cập nhật orderID vào đối tượng OrderTotal
            } else {
                conn.rollback();
                return false; // Không lấy được orderID
            }

            // 2. Thêm các chi tiết đơn hàng vào bảng order
            String insertOrderSQL = "INSERT INTO [order] (productID, orderID, orderQuantity, orderPrice) "
                    + "VALUES (?, ?, ?, ?)";
            pstmtOrder = conn.prepareStatement(insertOrderSQL);

            for (Order order : orderDetails) {
                pstmtOrder.setInt(1, order.getProduct().getProductID());
                pstmtOrder.setInt(2, orderTotal.getOrderID());
                pstmtOrder.setInt(3, order.getQuantity());
                pstmtOrder.setLong(4, order.getOrderPrice());
                pstmtOrder.addBatch(); // Thêm vào batch
            }

            int[] batchResults = pstmtOrder.executeBatch();
            for (int result : batchResults) {
                if (result == Statement.EXECUTE_FAILED) {
                    conn.rollback();
                    return false; // Thêm chi tiết Order thất bại
                }
            }

            // Commit transaction nếu mọi thứ thành công
            conn.commit();
            System.out.println("Thêm đơn hàng mới thành công!");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Lỗi khi thêm đơn hàng: " + e.getMessage());
            return false;
        } finally {
            // Đóng các tài nguyên
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (pstmtOrderTotal != null) {
                    pstmtOrderTotal.close();
                }
                if (pstmtOrder != null) {
                    pstmtOrder.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }
    }
     public Account getAccountByID(int ID) {
        Account accounts = new Account();
        String sql = "SELECT * FROM [dbo].[account] where id = ?";
        
        try  {
                PreparedStatement stmt = conn.prepareStatement(sql); 
                stmt.setInt(1, ID);
                ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts = new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }
     
     public boolean deleteCart(int userID, int ProductID){
         int count = 0;
         try {
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cart WHERE id = ? AND productID = ?;");
             ps.setInt(1, userID);
             ps.setInt(2, ProductID);
             count = ps.executeUpdate();
             if(count > 0){
                 return true;
             }
         } catch (Exception e) {
         }
         return false;
     }
     public Voucher getVOucherID(){
         Voucher v = null;
         try {
             PreparedStatement ps = conn.prepareCall("");
             
         } catch (Exception e) {
         }
         return v;
     }
}
