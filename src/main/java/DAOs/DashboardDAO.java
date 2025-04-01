
import DB.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardDAO {

    private Connection conn;

    public DashboardDAO() throws SQLException {
        try {
            conn = DBConnection.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Fetch account counts by role
    public List<String> getAccountRoles() {
        List<String> roles = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT role FROM account GROUP BY role")) {
            while (rs.next()) {
                roles.add(rs.getString("role"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return roles;
    }

    public List<Integer> getAccountRoleCounts() {
        List<Integer> counts = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM account GROUP BY role")) {
            while (rs.next()) {
                counts.add(rs.getInt("count"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return counts;
    }

    // Fetch product names and quantities
    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT productName FROM product")) {
            while (rs.next()) {
                names.add(rs.getString("productName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return names;
    }

    public List<Integer> getProductQuantities() {
        List<Integer> quantities = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT proQuantity FROM product")) {
            while (rs.next()) {
                quantities.add(rs.getInt("proQuantity"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantities;
    }

    // Fetch voucher codes and quantities
    public List<String> getVoucherCodes() {
        List<String> codes = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT voucherCode FROM voucher")) {
            while (rs.next()) {
                codes.add(rs.getString("voucherCode"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codes;
    }

    public List<Integer> getVoucherQuantities() {
        List<Integer> quantities = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT quantity FROM voucher")) {
            while (rs.next()) {
                quantities.add(rs.getInt("quantity"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quantities;
    }

    // Fetch order dates and total prices
    public List<String> getOrderDates() {
        List<String> dates = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT CONVERT(VARCHAR(10), date, 120) as orderDate FROM orderTotal GROUP BY CONVERT(VARCHAR(10), date, 120)")) {
            while (rs.next()) {
                dates.add(rs.getString("orderDate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dates;
    }

    public List<Double> getOrderTotals() {
        List<Double> totals = new ArrayList<>();
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT SUM(totalPrice) as total FROM orderTotal GROUP BY CONVERT(VARCHAR(10), date, 120)")) {
            while (rs.next()) {
                totals.add(rs.getDouble("total"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totals;
    }

    // Close connection
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
