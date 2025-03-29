/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Account;
import Model.Category;
import Model.Order;
import Model.OrderProduct;
import Model.OrderTotal;
import Model.Product;
import Model.Voucher;
import java.math.BigDecimal;
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

    public List<OrderProduct> getAllOrderTotalByUserID(int userID) {
        List<OrderProduct> orderList = new ArrayList<>();
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
                    OrderProduct order = new OrderProduct(
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

    public List<OrderProduct> getAllOrderTotal() throws SQLException {
        List<OrderProduct> orderList = new ArrayList<>();

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
                OrderProduct order = new OrderProduct(
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

    public List<OrderProduct> UpdateStatusAndGetAllOrder(int id, int newStatus) {

        String sql = "UPDATE OrderTotal SET orderState = ? WHERE orderID = ?";
        int rowsUpdated;
        List<OrderProduct> newOrder = new ArrayList<>();
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, newStatus);
            stmt.setInt(2, id);

            rowsUpdated = stmt.executeUpdate();
            newOrder = rowsUpdated > 0 ? getAllOrderTotal() : null;

            //
            if (newStatus == 2) {
                List<Order> add = new ArrayList<>();
                add = getOrderDetails(id);
                for (int i = 0; i < add.size(); i++) {
                    PreparedStatement ps = conn.prepareStatement("UPDATE product SET proQuantity = proQuantity + ? WHERE productID = ?");
                    ps.setInt(1, add.get(i).getQuantity());
                    ps.setInt(2, add.get(i).getProduct().getProductID());

                    ps.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newOrder;
    }

    public boolean addNewOrder(OrderTotal orderTotal, List<Order> orderDetails) {
        PreparedStatement pstmtOrderTotal = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtUpdateProduct = null;
        PreparedStatement pstmtUpdateVoucher = null; // New PreparedStatement for updating voucher quantity
        PreparedStatement pstmtDeleteCart = null;
        ResultSet generatedKeys = null;

        try {
            // Kiểm tra kết nối cơ sở dữ liệu
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Kết nối cơ sở dữ liệu không hợp lệ hoặc đã đóng.");
            }

            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Kiểm tra và cập nhật số lượng voucher (nếu có voucher được sử dụng)
            Integer voucherID = orderTotal.getVoucherCode() == 0 ? null : orderTotal.getVoucherCode();
            if (voucherID != null) {
                

                // Cập nhật số lượng voucher
                String updateVoucherSQL = "UPDATE voucher SET quantity = quantity - 1 WHERE voucherID = ? AND quantity > 0";
                pstmtUpdateVoucher = conn.prepareStatement(updateVoucherSQL);
                pstmtUpdateVoucher.setInt(1, voucherID);
                int voucherUpdated = pstmtUpdateVoucher.executeUpdate();
                if (voucherUpdated == 0) {
                    throw new SQLException("Không thể cập nhật số lượng voucher, có thể voucher đã hết.");
                }
            }

            // 2. Thêm vào bảng orderTotal
            String insertOrderTotalSQL = "INSERT INTO orderTotal (phoneNumber, [address], note, totalPrice, [date], orderState, voucherID, id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtOrderTotal = conn.prepareStatement(insertOrderTotalSQL, Statement.RETURN_GENERATED_KEYS);

            // Gán giá trị cho PreparedStatement
            pstmtOrderTotal.setString(1, orderTotal.getPhoneNumber());
            pstmtOrderTotal.setString(2, orderTotal.getAddress());
            pstmtOrderTotal.setString(3, orderTotal.getNote());
            pstmtOrderTotal.setBigDecimal(4, BigDecimal.valueOf(orderTotal.getTotalPrice()));
            pstmtOrderTotal.setDate(5, orderTotal.getDate());
            pstmtOrderTotal.setInt(6, orderTotal.getOrderState());
            pstmtOrderTotal.setInt(7, voucherID);
            pstmtOrderTotal.setInt(8, orderTotal.getAccount().getId());

            // Thực thi và kiểm tra kết quả
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

            // 3. Thêm chi tiết đơn hàng vào bảng order
            String insertOrderSQL = "INSERT INTO [order] (productID, orderID, orderQuantity, orderPrice) VALUES (?, ?, ?, ?)";
            pstmtOrder = conn.prepareStatement(insertOrderSQL);

            for (Order order : orderDetails) {
                pstmtOrder.setInt(1, order.getProduct().getProductID());
                pstmtOrder.setInt(2, orderTotal.getOrderID());
                pstmtOrder.setInt(3, order.getQuantity());
                pstmtOrder.setBigDecimal(4, BigDecimal.valueOf(order.getOrderPrice()));
                pstmtOrder.addBatch();
            }

            int[] batchResults = pstmtOrder.executeBatch();
            for (int result : batchResults) {
                if (result < 0 && result != Statement.SUCCESS_NO_INFO) {
                    throw new SQLException("Lỗi khi thêm chi tiết đơn hàng.");
                }
            }

            // 4. Cập nhật số lượng sản phẩm trong bảng product
            String updateProductSQL = "UPDATE product SET proQuantity = proQuantity - ? WHERE productID = ? AND proQuantity >= ?";
            pstmtUpdateProduct = conn.prepareStatement(updateProductSQL);

            for (Order order : orderDetails) {
                int quantityOrdered = order.getQuantity();
                int productID = order.getProduct().getProductID();

                pstmtUpdateProduct.setInt(1, quantityOrdered);
                pstmtUpdateProduct.setInt(2, productID);
                pstmtUpdateProduct.setInt(3, quantityOrdered);
                pstmtUpdateProduct.addBatch();
            }

            int[] updateResults = pstmtUpdateProduct.executeBatch();
            for (int i = 0; i < updateResults.length; i++) {
                if (updateResults[i] == 0) {
                    throw new SQLException("Số lượng tồn kho không đủ hoặc sản phẩm không tồn tại cho productID: " + orderDetails.get(i).getProduct().getProductID());
                }
                if (updateResults[i] < 0 && updateResults[i] != Statement.SUCCESS_NO_INFO) {
                    throw new SQLException("Lỗi khi cập nhật số lượng sản phẩm.");
                }
            }

            // 5. Xóa sản phẩm khỏi giỏ hàng
            String deleteCartSQL = "DELETE FROM cart WHERE id = ? AND productID = ?";
            pstmtDeleteCart = conn.prepareStatement(deleteCartSQL);

            for (Order order : orderDetails) {
                pstmtDeleteCart.setInt(1, orderTotal.getAccount().getId());
                pstmtDeleteCart.setInt(2, order.getProduct().getProductID());
                pstmtDeleteCart.addBatch();
            }

            int[] deleteResults = pstmtDeleteCart.executeBatch();
            for (int result : deleteResults) {
                if (result < 0 && result != Statement.SUCCESS_NO_INFO) {
                    throw new SQLException("Lỗi khi xóa giỏ hàng.");
                }
            }

            // Commit transaction nếu mọi thứ thành công
            conn.commit();
            System.out.println("Thêm đơn hàng mới thành công, cập nhật số lượng voucher, sản phẩm và xóa giỏ hàng!");
            return true;

        } catch (SQLException e) {
            // Ghi log lỗi chi tiết
            System.err.println("Lỗi SQL: " + e.getMessage());
            System.err.println("Mã lỗi: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();

            // Rollback transaction nếu có lỗi
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Transaction đã bị rollback!");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                rollbackEx.printStackTrace();
            }
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
                if (pstmtUpdateProduct != null) {
                    pstmtUpdateProduct.close();
                }
                if (pstmtUpdateVoucher != null) {
                    pstmtUpdateVoucher.close();
                }
                if (pstmtDeleteCart != null) {
                    pstmtDeleteCart.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true); // Khôi phục trạng thái auto-commit
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                e.printStackTrace();
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
    
    public Account getAccountByOrderID(int orderID) {
        Account account = null;
        
        ResultSet rs = null;

        try {
            // Get database connection (assuming DBConnection is a utility class)

            // SQL query to join orderTotal and account tables
            String sql = " SELECT a.id, a.username, a.email, a.password, a.phone_number, a.address, a.role " +
                         "FROM account a " +
                         "INNER JOIN orderTotal ot ON a.id = ot.id " +
                         "WHERE ot.orderID = ? ";

            // Prepare the statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderID);

            // Execute the query
            rs = ps.executeQuery();

            // If a record is found, populate the Account object
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setEmail(rs.getString("email"));
                account.setPassword(rs.getString("password"));
                account.setPhoneNumber(rs.getString("phone_number"));
                account.setAddress(rs.getString("address"));
                account.setRole(rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return account;
    }

}
