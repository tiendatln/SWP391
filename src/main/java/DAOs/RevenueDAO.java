/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.RevenueReport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Huynh Nguyen Phu Thanh - CE180094
 */
public class RevenueDAO {

    public List<RevenueReport> getRevenueReport(String startDate, String endDate, String groupBy)
            throws SQLException, ClassNotFoundException {
        List<RevenueReport> reports = new ArrayList<>();
        Connection conn = DBConnection.connect();

        // Câu truy vấn đảm bảo tất cả cột không tổng hợp đều trong GROUP BY
        // Thêm điều kiện orderState = 1 để chỉ lấy đơn hàng đã hoàn thành
        String sql = "SELECT ot.date,"
                + "FORMAT(ot.date, ?) AS period, p.productID, p.productName, "
                + "SUM(p.proPrice * o.orderQuantity * (100 - v.percentDiscount)/100) AS totalRevenue,"
                + "SUM(o.orderQuantity) AS totalQuantity  "
                + "FROM orderTotal ot "
                + "JOIN [order] o ON ot.orderID = o.orderID "
                + "JOIN product p ON o.productID = p.productID "
                + "JOIN voucher v ON ot.voucherID = v.voucherID "
                + "JOIN (SELECT orderID, SUM(orderQuantity) AS totalQty "
                + "FROM [order] GROUP BY orderID) totalOrderQty ON ot.orderID = totalOrderQty.orderID "
                + "WHERE ot.date BETWEEN ? AND DATEADD(MILLISECOND, -3, DATEADD(DAY, 1, ?)) "
                + "AND ot.orderState = 1 GROUP BY ot.date, "
                + "FORMAT(ot.date, ?), p.productID, p.productName ORDER BY period, p.productID; ";

        String dateFormat;
        switch (groupBy.toLowerCase()) {
            case "day":
                dateFormat = "yyyy-MM-dd";
                break;
            case "month":
                dateFormat = "yyyy-MM";
                break;
            case "year":
                dateFormat = "yyyy";
                break;
            default:
                dateFormat = "yyyy-MM-dd";
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, dateFormat); // FORMAT trong SELECT
        ps.setString(2, startDate);  // WHERE ot.date BETWEEN
        ps.setString(3, endDate);    // WHERE ot.date BETWEEN
        ps.setString(4, dateFormat); // FORMAT trong GROUP BY

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            RevenueReport report = new RevenueReport(
                    rs.getString("period"),
                    rs.getInt("productID"),
                    rs.getString("productName"),
                    rs.getDouble("totalRevenue"),
                    rs.getInt("totalQuantity")
            );
            reports.add(report);
        }

        rs.close();
        ps.close();
        conn.close();
        return reports;
    }
    public Map<String, Integer> getOrderStatusCount(String startDate, String endDate)
            throws SQLException, ClassNotFoundException {
        Map<String, Integer> statusCount = new HashMap<>();
        Connection conn = DBConnection.connect();

        String sql = "SELECT orderState, COUNT(*) AS count " +
                     "FROM orderTotal " +
                     "WHERE [date] BETWEEN ? AND DATEADD(MILLISECOND, -3, DATEADD(DAY, 1, ?)) " +
                     "GROUP BY orderState";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, startDate);
        ps.setString(2, endDate);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int state = rs.getInt("orderState");
            int count = rs.getInt("count");
            String statusLabel;
            switch (state) {
                case 0:
                    statusLabel = "Pending";
                    break;
                case 1:
                    statusLabel = "Complete";
                    break;
                case 2:
                    statusLabel = "Cancel";
                    break;
                default:
                    statusLabel = "Unknown";
            }
            statusCount.put(statusLabel, count);
        }

        rs.close();
        ps.close();
        conn.close();
        return statusCount;
    }
}
