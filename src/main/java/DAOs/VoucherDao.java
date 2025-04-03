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

    /**
     * Lấy danh sách tất cả voucher từ cơ sở dữ liệu.
     *
     * @return danh sách các đối tượng Voucher
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public List<Voucher> getAllVouchers() throws ClassNotFoundException {
        List<Voucher> list = new ArrayList<>();
        String query = "SELECT * FROM voucher";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Voucher(
                        rs.getInt("voucherID"), // Gán voucherID từ cơ sở dữ liệu
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

    /**
     * Thêm voucher mới vào cơ sở dữ liệu.
     *
     * @param voucher đối tượng Voucher cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public boolean insertVoucher(Voucher voucher) throws SQLException, ClassNotFoundException {
        if (voucherExists(voucher.getVoucherCode())) {
            return false; // Không thêm nếu mã voucher đã tồn tại
        }

        String query = "INSERT INTO voucher (voucherCode, startDate, endDate, percentDiscount, quantity, usedTime) VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

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

    /**
     * Cập nhật thông tin voucher trong cơ sở dữ liệu dựa trên voucherID.
     *
     * @param voucher đối tượng Voucher cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public boolean updateVoucher(Voucher voucher) throws SQLException, ClassNotFoundException {
        String query = "UPDATE voucher SET voucherCode = ?, startDate = ?, endDate = ?, percentDiscount = ?, quantity = ?, usedTime = ? WHERE voucherID = ?";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, voucher.getVoucherCode());
            ps.setDate(2, java.sql.Date.valueOf(voucher.getStartDate()));
            ps.setDate(3, java.sql.Date.valueOf(voucher.getEndDate()));
            ps.setInt(4, voucher.getPercentDiscount());
            ps.setInt(5, voucher.getQuantity());
            ps.setInt(6, voucher.getUsedTime());
            ps.setInt(7, voucher.getVoucherID()); // Sử dụng voucherID để xác định bản ghi

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Xóa voucher khỏi cơ sở dữ liệu dựa trên voucherID.
     *
     * @param voucher đối tượng Voucher cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public boolean deleteVoucher(Voucher voucher) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM voucher WHERE voucherID = ?";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, voucher.getVoucherID()); // Sử dụng voucherID để xóa

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra xem voucher có tồn tại dựa trên voucherCode.
     *
     * @param voucherCode mã voucher cần kiểm tra
     * @return true nếu voucher tồn tại, false nếu không
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public boolean voucherExists(String voucherCode) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM voucher WHERE voucherCode = ?";
        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

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

    /**
     * Lấy voucher dựa trên voucherCode.
     *
     * @param voucherCode mã voucher cần tìm
     * @return đối tượng Voucher nếu tìm thấy, null nếu không
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public Voucher getVoucherByCode(String voucherCode) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM voucher WHERE voucherCode = ?";
        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, voucherCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Voucher(
                        rs.getInt("voucherID"),
                        rs.getString("voucherCode"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate(),
                        rs.getInt("percentDiscount"),
                        rs.getInt("quantity"),
                        rs.getInt("usedTime")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm kiếm voucher theo từ khóa trong voucherCode.
     *
     * @param searchKeyword từ khóa tìm kiếm
     * @return danh sách các đối tượng Voucher khớp với từ khóa
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public List<Voucher> searchVouchers(String searchKeyword) throws SQLException, ClassNotFoundException {
        List<Voucher> list = new ArrayList<>();
        String query = "SELECT * FROM voucher WHERE voucherCode LIKE ?";

        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

            String searchPattern = "%" + searchKeyword + "%";
            ps.setString(1, searchPattern);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Voucher(
                            rs.getInt("voucherID"),
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

    /**
     * Lấy voucher dựa trên voucherID.
     *
     * @param voucherId ID của voucher cần tìm
     * @return đối tượng Voucher nếu tìm thấy, null nếu không
     * @throws SQLException nếu có lỗi SQL
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public Voucher getVoucherById(int voucherId) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM voucher WHERE voucherID = ?";
        try ( Connection conn = DBConnection.connect();  PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, voucherId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Voucher(
                        rs.getInt("voucherID"),
                        rs.getString("voucherCode"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getDate("endDate").toLocalDate(),
                        rs.getInt("percentDiscount"),
                        rs.getInt("quantity"),
                        rs.getInt("usedTime")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
