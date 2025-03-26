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

    public List<Order> getAllOrderTotalByUserID(int userID) {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT \n"
                + "    ot.orderID, \n"
                + "    ot.phoneNumber, \n"
                + "    ot.address, \n"
                + "    ot.note, \n"
                + "    ot.totalPrice, \n"
                + "    ot.date, \n"
                + "    ot.orderState, \n"
                + "    ot.voucherID, \n"
                + "    STRING_AGG(p.productName, ', ') AS productNames, \n"
                + "    SUM(o.orderQuantity) AS totalQuantity, \n"
                + "    SUM(o.orderPrice) AS totalOrderPrice \n"
                + "FROM orderTotal ot \n"
                + "LEFT JOIN account a ON ot.id = a.id \n"
                + "LEFT JOIN [order] o ON ot.orderID = o.orderID \n"
                + "LEFT JOIN product p ON o.productID = p.productID \n"
                + "WHERE a.id = ? \n"
                + "GROUP BY ot.orderID, ot.phoneNumber, ot.address, ot.note, \n"
                + "         ot.totalPrice, ot.date, ot.orderState, ot.voucherID \n"
                + "HAVING SUM(o.orderQuantity) > 0;"; // Đảm bảo chỉ lấy đơn có sản phẩm

        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userID); // Đặt tham số userID
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderTotal ot = new OrderTotal(
                            rs.getInt("orderID"),
                            rs.getString("phoneNumber"),
                            rs.getString("address"),
                            rs.getString("note"),
                            rs.getLong("totalPrice"),
                            rs.getDate("date"),
                            rs.getInt("orderState"),
                            rs.getInt("voucherID")
                    );

                    // Lấy danh sách sản phẩm dưới dạng chuỗi
                    String productNames = rs.getString("productNames");

                    // Tạo Order với danh sách sản phẩm gộp
                    Order order = new Order(
                            ot,
                            productNames, // Danh sách sản phẩm dạng String
                            rs.getInt("totalQuantity"),
                            rs.getLong("totalOrderPrice")
                    );

                    orderList.add(order);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderList;
    }


    public List<Order> getAllOrderTotal() throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT \n"
                + "    ot.orderID, \n"
                + "    ot.phoneNumber, \n"
                + "    ot.address, \n"
                + "    ot.note, \n"
                + "    ot.totalPrice, \n"
                + "    ot.date, \n"
                + "    ot.orderState, \n"
                + "    ot.voucherID, \n"
                + "    STRING_AGG(p.productName, ', ') AS productNames, \n"
                + "    SUM(o.orderQuantity) AS totalQuantity, \n"
                + "    SUM(o.orderPrice) AS totalOrderPrice \n"
                + "FROM orderTotal ot \n"
                + "LEFT JOIN [order] o ON ot.orderID = o.orderID \n"
                + "LEFT JOIN product p ON o.productID = p.productID \n"
                + "GROUP BY \n"
                + "    ot.orderID, ot.phoneNumber, ot.address, ot.note, \n"
                + "    ot.totalPrice, ot.date, ot.orderState, ot.voucherID;";


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
                        rs.getInt("voucherID")
                );


                // Lấy danh sách sản phẩm dưới dạng chuỗi
                String productNames = rs.getString("productNames");

                // Tạo Order với danh sách sản phẩm gộp
                Order order = new Order(
                        ot,
                        productNames, // Danh sách sản phẩm dạng String
                        rs.getInt("totalQuantity"),
                        rs.getLong("totalOrderPrice")
                );

                orderList.add(order);
            }
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
        PreparedStatement pstmtDeleteCart = null;
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
            pstmtOrderTotal.setObject(7, orderTotal.getVoucherCode() == 0 ? null : orderTotal.getVoucherCode(), java.sql.Types.INTEGER);
            pstmtOrderTotal.setInt(8, orderTotal.getAccount().getId());

            int affectedRows = pstmtOrderTotal.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Không thể thêm orderTotal.");
            }

            // Lấy orderID vừa được tạo
            generatedKeys = pstmtOrderTotal.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Không thể lấy orderID.");
            }
            int orderID = generatedKeys.getInt(1);
            orderTotal.setOrderID(orderID);

            // 2. Thêm chi tiết đơn hàng vào bảng order
            String insertOrderSQL = "INSERT INTO [order] (productID, orderID, orderQuantity, orderPrice) VALUES (?, ?, ?, ?)";
            pstmtOrder = conn.prepareStatement(insertOrderSQL);

            for (Order order : orderDetails) {
                pstmtOrder.setInt(1, order.getProduct().getProductID());
                pstmtOrder.setInt(2, orderTotal.getOrderID());
                pstmtOrder.setInt(3, order.getQuantity());
                pstmtOrder.setLong(4, order.getOrderPrice());
                pstmtOrder.addBatch();
            }

            int[] batchResults = pstmtOrder.executeBatch();
            for (int result : batchResults) {
                if (result < 0) { // Kiểm tra lỗi chính xác hơn
                    throw new SQLException("Lỗi khi thêm chi tiết đơn hàng.");
                }
            }

            // 3. Xóa sản phẩm khỏi giỏ hàng
            String deleteCartSQL = "DELETE FROM cart WHERE id = ? AND productID = ?";
            pstmtDeleteCart = conn.prepareStatement(deleteCartSQL);

            for (Order order : orderDetails) {
                pstmtDeleteCart.setInt(1, orderTotal.getAccount().getId());
                pstmtDeleteCart.setInt(2, order.getProduct().getProductID());
                pstmtDeleteCart.addBatch();
            }

            int[] deleteResults = pstmtDeleteCart.executeBatch();
            for (int result : deleteResults) {
                if (result < 0) {
                    throw new SQLException("Lỗi khi xóa giỏ hàng.");
                }
            }

            // Commit transaction nếu mọi thứ thành công
            conn.commit();
            System.out.println("Thêm đơn hàng mới thành công và xóa giỏ hàng!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Transaction đã bị rollback!");
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
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
                if (pstmtDeleteCart != null) {
                    pstmtDeleteCart.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Khôi phục trạng thái auto-commit
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }
    }

    public Account getAccountByID(int ID) {
        String sql = "SELECT * FROM [dbo].[account] WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {  // Nếu có dữ liệu thì mới tạo đối tượng Account
                return new Account(
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
        return null;  // Trả về null nếu không tìm thấy user
    }

    public int getVoucherID(String voucherCode) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT voucherID FROM voucher WHERE voucherCode = ?");
            ps.setString(1, voucherCode);
            ResultSet rs = ps.executeQuery();  // Sử dụng executeQuery() thay vì executeUpdate()
            if (rs.next()) {
                return rs.getInt("voucherID");  // Lấy giá trị thực tế
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Trả về -1 nếu không tìm thấy
    }

}
