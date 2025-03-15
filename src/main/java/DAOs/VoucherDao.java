package DAOs;

import DB.DBConnection;
import Model.Voucher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kim Chi Khang _ CE180324
 */
public class VoucherDao {

    /*
     * Show danh sách voucher
     */
    public List<Voucher> getAllVouchers() throws ClassNotFoundException {
        List<Voucher> list = new ArrayList<>();
        String query = "SELECT * FROM voucher";

        try (Connection conn = DBConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(query); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Voucher(
                        rs.getString("voucherCode"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate(),
                        rs.getInt("percentDiscount"),
                        rs.getInt("quantity"),
                        rs.getInt("usedTime")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     * Thêm voucher mới
     */
    public boolean insertVoucher(Voucher voucher) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO voucher (voucherCode, startDate, endDate, percentDiscount, quantity, usedTime) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, voucher.getVoucherCode());
            ps.setDate(2, java.sql.Date.valueOf(voucher.getStartDate()));
            ps.setDate(3, java.sql.Date.valueOf(voucher.getEndDate()));
            ps.setInt(4, voucher.getPercentDiscount());
            ps.setInt(5, voucher.getQuantity());
            ps.setInt(6, voucher.getUsedTime());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /*
     * Cập nhật voucher
     */
    public boolean updateVoucher(Voucher voucher) throws SQLException, ClassNotFoundException {
        String query = "UPDATE voucher SET startDate = ?, endDate = ?, percentDiscount = ?, quantity = ?, usedTime = ? WHERE voucherCode = ?";

        try (Connection conn = DBConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, java.sql.Date.valueOf(voucher.getStartDate()));
            ps.setDate(2, java.sql.Date.valueOf(voucher.getEndDate()));
            ps.setInt(3, voucher.getPercentDiscount());
            ps.setInt(4, voucher.getQuantity());
            ps.setInt(5, voucher.getUsedTime());
            ps.setString(6, voucher.getVoucherCode());  // Điều kiện WHERE để tìm đúng voucher

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /*
     * Xóa voucher
     */
    public boolean deleteVoucher(Voucher voucher) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM voucher WHERE voucherCode = ?";

        try (Connection conn = DBConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Chỉ cần set voucherCode vào câu lệnh SQL
            ps.setString(1, voucher.getVoucherCode());  // Điều kiện WHERE để tìm đúng voucher

            // Thực thi câu lệnh xóa và kiểm tra số dòng bị ảnh hưởng
            int rowsAffected = ps.executeUpdate();

            // Nếu có ít nhất 1 bản ghi bị xóa, trả về true
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     * Kiểm tra voucher có tồn tại không
     */
    public boolean voucherExists(String voucherCode) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM voucher WHERE voucherCode = ?";
        try (Connection conn = DBConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, voucherCode);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /*
 * Tìm kiếm voucher theo từ khóa
 */
    
     public List<Voucher> searchVouchers(String searchKeyword) throws SQLException, ClassNotFoundException {
    List<Voucher> list = new ArrayList<>();
    String query = "SELECT * FROM voucher WHERE voucherCode LIKE ? OR startDate LIKE ? OR endDate LIKE ?";

    try (Connection conn = DBConnection.connect(); 
         PreparedStatement ps = conn.prepareStatement(query)) {
        
        String searchPattern = "%" + searchKeyword + "%";
        ps.setString(1, searchPattern); // voucherCode
        ps.setString(2, searchPattern); // startDate
        ps.setString(3, searchPattern); // endDate
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Voucher(
                        rs.getString("voucherCode"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate(),
                        rs.getInt("percentDiscount"),
                        rs.getInt("quantity"),
                        rs.getInt("usedTime")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

   

   
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Tạo đối tượng VoucherDao để gọi phương thức tìm kiếm
        VoucherDao voucherDao = new VoucherDao();
        
        // Từ khóa tìm kiếm
        String searchKeyword = "cv";  // Ví dụ tìm kiếm các voucher có "2023" trong mã voucher hoặc ngày

        // Gọi phương thức tìm kiếm
        List<Voucher> voucherList = voucherDao.searchVouchers(searchKeyword);

        // In kết quả tìm kiếm
        if (voucherList.isEmpty()) {
            System.out.println("Không tìm thấy voucher nào với từ khóa: " + searchKeyword);
        } else {
            System.out.println("Danh sách voucher tìm được:");
            for (Voucher voucher : voucherList) {
                System.out.println("Voucher Code: " + voucher.getVoucherCode() +
                        ", Start Date: " + voucher.getStartDate() +
                        ", End Date: " + voucher.getEndDate() +
                        ", Percent Discount: " + voucher.getPercentDiscount() +
                        ", Quantity: " + voucher.getQuantity() +
                        ", Used Time: " + voucher.getUsedTime());
            }
        }
    }
}


