package DAOs;

import DB.DBConnection;
import Model.Comment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    // Lấy danh sách bình luận theo productID
    public List<Comment> getCommentsByProductID(int id) {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comment WHERE productID = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("commentID"),
                        rs.getInt("rate"),
                        rs.getString("comment"),
                        rs.getInt("productID"),
                        rs.getInt("id")
                );
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi SQL khi lấy bình luận cho productID " + id + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khác khi lấy bình luận cho productID " + id + ": " + e.getMessage());
        }
        return comments; // Trả về danh sách (có thể rỗng)
    }

    // Thêm bình luận mới vào cơ sở dữ liệu
    public void addComment(int productID, int rate, String commentContent, int userID) {
        String query = "INSERT INTO comment (rate, comment, productID, id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, rate);
            ps.setString(2, commentContent);
            ps.setInt(3, productID);
            ps.setInt(4, userID);

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Thêm bình luận thành công.");
            } else {
                System.out.println("Không thể thêm bình luận.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi SQL khi thêm bình luận: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khác khi thêm bình luận: " + e.getMessage());
        }
    }

    // Sửa bình luận trong cơ sở dữ liệu
    public boolean updateComment(int commentID, int rate, String commentContent) {
        String query = "UPDATE comment SET rate = ?, comment = ? WHERE commentID = ?";
        boolean success = false;

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, rate);
            ps.setString(2, commentContent);
            ps.setInt(3, commentID);

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Cập nhật bình luận thành công cho commentID " + commentID);
                success = true;
            } else {
                System.out.println("Không tìm thấy bình luận với commentID " + commentID + " để cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi SQL khi cập nhật bình luận: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khác khi cập nhật bình luận: " + e.getMessage());
        }
        return success;
    }

    // Xóa bình luận từ cơ sở dữ liệu
    public boolean deleteComment(int commentID) {
        String query = "DELETE FROM comment WHERE commentID = ?";
        boolean success = false;

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, commentID);

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("Xóa bình luận thành công cho commentID " + commentID);
                success = true;
            } else {
                System.out.println("Không tìm thấy bình luận với commentID " + commentID + " để xóa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi SQL khi xóa bình luận: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khác khi xóa bình luận: " + e.getMessage());
        }
        return success;
    }
    public boolean deleteCommentByProduct(int productID){
        String sql = "DELETE FROM comment WHERE productID = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql))  {
            ps.setInt(1, productID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức main để kiểm tra
    public static void main(String[] args) {
        CommentDAO commentDAO = new CommentDAO();

        // Kiểm tra phương thức lấy bình luận
        int productId = 1;
        List<Comment> comments = commentDAO.getCommentsByProductID(productId);
        for (Comment comment : comments) {
            System.out.println(comment);
        }

        // Kiểm tra thêm bình luận
        commentDAO.addComment(1, 5, "Sản phẩm tuyệt vời, tôi rất thích!", 1); // Thêm bình luận mẫu

        // Kiểm tra sửa bình luận (giả sử commentID = 1)
        boolean updated = commentDAO.updateComment(1, 4, "Sản phẩm tốt nhưng giao hàng hơi chậm.");
        if (updated) {
            System.out.println("Kiểm tra cập nhật: Thành công.");
        } else {
            System.out.println("Kiểm tra cập nhật: Thất bại.");
        }

        // Kiểm tra xóa bình luận (giả sử commentID = 1)
        boolean deleted = commentDAO.deleteComment(1);
        if (deleted) {
            System.out.println("Kiểm tra xóa: Thành công.");
        } else {
            System.out.println("Kiểm tra xóa: Thất bại.");
        }

        // Kiểm tra lại danh sách bình luận sau khi xóa
        comments = commentDAO.getCommentsByProductID(productId);
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }
    

}