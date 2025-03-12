package DAOs;

import Model.Comments;
import DB.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsDAO {

    // Lấy danh sách comment cho một sản phẩm
    public List<Comments> getCommentsByProduct(int productID) throws ClassNotFoundException {
        List<Comments> list = new ArrayList<>();
        String query = "SELECT * FROM comment WHERE productID = ?";

        try (Connection conn = DBConnection.connect(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Comments(
                            rs.getInt("commentID"),
                            rs.getInt("rate"),
                            rs.getString("comment"),
                            rs.getInt("productID"),
                            rs.getInt("id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm comment mới vào bảng comment
    public boolean addComment(int rate, String commentText, int productID, int userID) throws ClassNotFoundException {
        if (isCustomer(userID)) {  // Kiểm tra xem người dùng có phải là customer không
            String query = "INSERT INTO comment (rate, comment, productID, id) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.connect(); 
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, rate);
                ps.setString(2, commentText);
                ps.setInt(3, productID);
                ps.setInt(4, userID);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false; // Người dùng không đủ điều kiện để thêm comment
    }

    // Cập nhật một comment
    public boolean updateComment(int commentID, int rate, String commentText, int userID) throws ClassNotFoundException {
        if (isCustomer(userID)) {  // Kiểm tra xem người dùng có phải là customer không
            String checkUserSQL = "SELECT id FROM comment WHERE commentID = ?";
            try (Connection conn = DBConnection.connect(); 
                 PreparedStatement ps = conn.prepareStatement(checkUserSQL)) {
                ps.setInt(1, commentID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int commentUserID = rs.getInt("id");
                        // Nếu người dùng hiện tại là chủ của comment
                        if (commentUserID == userID) {
                            String updateSQL = "UPDATE comment SET rate = ?, comment = ? WHERE commentID = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                                updateStmt.setInt(1, rate);
                                updateStmt.setString(2, commentText);
                                updateStmt.setInt(3, commentID);
                                int rowsAffected = updateStmt.executeUpdate();
                                return rowsAffected > 0;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false; // Người dùng không đủ quyền hoặc không phải chủ của comment
    }

    // Xóa comment
    public boolean deleteComment(int commentID, int userID) throws ClassNotFoundException {
        if (isCustomer(userID)) {  // Kiểm tra xem người dùng có phải là customer không
            String checkUserSQL = "SELECT id FROM comment WHERE commentID = ?";
            try (Connection conn = DBConnection.connect(); 
                 PreparedStatement ps = conn.prepareStatement(checkUserSQL)) {
                ps.setInt(1, commentID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int commentUserID = rs.getInt("id");
                        // Nếu người dùng hiện tại là chủ của comment
                        if (commentUserID == userID) {
                            String deleteSQL = "DELETE FROM comment WHERE commentID = ?";
                            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                                deleteStmt.setInt(1, commentID);
                                int rowsAffected = deleteStmt.executeUpdate();
                                return rowsAffected > 0;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false; // Người dùng không đủ quyền hoặc không phải chủ của comment
    }

    // Kiểm tra xem người dùng có phải là customer không
    private boolean isCustomer(int userID) throws ClassNotFoundException {
        String sql = "SELECT role FROM account WHERE id = ?";
        try (Connection conn = DBConnection.connect(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    return "customer".equalsIgnoreCase(role);  // Kiểm tra role là 'customer'
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Người dùng không phải customer
    }
//    public static void main(String[] args) {
//    // Khởi tạo kết nối cơ sở dữ liệu
//    try (Connection conn = DBConnection.connect()) {
//
//        // Tạo đối tượng CommentsDAO
//        CommentsDAO commentsDAO = new CommentsDAO();
//
//        // Kiểm tra lấy danh sách comment của sản phẩm có productID = 1
//        List<Comments> commentsList = commentsDAO.getCommentsByProduct(1);
//        System.out.println("List of comments for product ID 1:");
//        for (Comments comment : commentsList) {
//            System.out.println("Comment ID: " + comment.getCommentID() + ", Rate: " + comment.getRate() + ", Comment: " + comment.getCommentText() + ", User ID: " + comment.getUserID());
//        }
//
//        // Kiểm tra thêm comment mới
//        boolean addSuccess = commentsDAO.addComment(5, "Great product!", 1, 1);
//        System.out.println("\nAdd comment: " + (addSuccess ? "Success" : "Failed"));
//
//        // Kiểm tra cập nhật comment (commentID = 11)
//        boolean updateSuccess = commentsDAO.updateComment(11, 4, "Updated comment text for commentID = 11", 1);
//        System.out.println("\nUpdate comment with commentID = 11: " + (updateSuccess ? "Success" : "Failed"));
//
//        // Kiểm tra xóa comment (commentID = 11)
//        boolean deleteSuccess = commentsDAO.deleteComment(11, 1);
//        System.out.println("\nDelete comment with commentID = 11: " + (deleteSuccess ? "Success" : "Failed"));
//
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}

}
