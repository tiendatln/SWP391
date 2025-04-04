/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CartDAO;
import DAOs.CategoryDAO;
import DAOs.CommentDAO;
import DAOs.OrderDAO; // Thêm import cho OrderDAO
import DAOs.ProductDAO;
import Model.Account; // Thêm import cho Account
import Model.Category;
import Model.Comment;
import Model.Product;
import Model.ProductDetail;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Thêm import cho HttpSession
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException; // Thêm import cho SQLException
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Thêm import cho logging
import java.util.logging.Logger; // Thêm import cho Logger

/**
 * Servlet xử lý các yêu cầu liên quan đến sản phẩm, bao gồm hiển thị chi tiết
 * sản phẩm, quản lý sản phẩm, thêm/sửa/xóa sản phẩm và bình luận.
 *
 * @author tiend
 */
@MultipartConfig
public class ProductController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Hiển thị trang HTML mặc định nếu không có logic cụ thể được gọi.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            // Hiển thị trang HTML mẫu mặc định
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method. Xử lý các yêu cầu lấy thông tin
     * sản phẩm, chi tiết sản phẩm, quản lý sản phẩm và xóa sản phẩm.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI(); // Đường dẫn URI của yêu cầu
        String action = request.getParameter("action"); // Tham số action từ query string

        // Ghi log để debug yêu cầu GET
        System.out.println("Received GET request for URI: " + request.getRequestURI() + ", Query: " + request.getQueryString());

        // Khởi tạo các DAO để tương tác với cơ sở dữ liệu
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        CommentDAO commentDAO = new CommentDAO();
        OrderDAO orderDAO = new OrderDAO(); // Thêm OrderDAO

        // Xử lý yêu cầu hiển thị chi tiết sản phẩm cho khách hàng
        if (path.startsWith("/ProductController/DetailProductCustomer")) {
            int id;
            try {
                id = Integer.parseInt(request.getParameter("id")); // Lấy ID sản phẩm từ tham số
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "ID sản phẩm không hợp lệ: " + e.getMessage());
                request.getRequestDispatcher("/web/GuessAndCustomer/DetailProduct.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin sản phẩm, chi tiết sản phẩm và danh sách bình luận
            Product product = productDAO.getProductById(id);
            ProductDetail productDetail = productDAO.getProductDetailById(id);
            List<Comment> comments = commentDAO.getCommentsByProductID(id);
            System.out.println("Danh sách bình luận cho productID " + id + ": " + comments);

            // Kiểm tra nếu sản phẩm không tồn tại
            if (product == null) {
                request.setAttribute("errorMessage", "Sản phẩm không tồn tại cho ID: " + id);
                request.getRequestDispatcher("/web/GuessAndCustomer/DetailProduct.jsp").forward(request, response);
                return;
            }

            // Nếu không có bình luận, khởi tạo danh sách rỗng
            if (comments == null) {
                comments = new ArrayList<>();
            }

            // Thêm kiểm tra quyền bình luận
            boolean canComment = false;
            HttpSession session = request.getSession();
            Account user = (Account) session.getAttribute("user");
            if (user != null) {
                try {
                    canComment = orderDAO.hasUserPurchasedProduct(user.getId(), id);
                } catch (SQLException e) {
                    Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, "Lỗi khi kiểm tra quyền bình luận", e);
                }
            }

            // Thêm tính trung bình sao
            double averageRating = 0.0;
            if (!comments.isEmpty()) {
                int totalRating = 0;
                for (Comment comment : comments) {
                    totalRating += comment.getRate();
                }
                averageRating = (double) totalRating / comments.size();
            }

            // Thêm tổng số bình luận mà không thay đổi logic hiện tại
            int totalComments = comments.size(); // Tính tổng số bình luận từ danh sách comments

            // Thêm logic định dạng totalComments (e.g., 24,400 -> "24.4 N")
            String formattedTotalComments;
            if (totalComments >= 1000) {
                double commentsInThousands = totalComments / 1000.0;
                formattedTotalComments = String.format("%.1f N", commentsInThousands);
            } else {
                formattedTotalComments = String.valueOf(totalComments);
            }

            // Đặt các thuộc tính để truyền sang JSP
            request.setAttribute("product", product);
            request.setAttribute("productDetail", productDetail);
            request.setAttribute("productId", id);
            request.setAttribute("comments", comments);
            request.setAttribute("canComment", canComment); // Thêm canComment
            request.setAttribute("averageRating", averageRating); // Thêm averageRating
            request.setAttribute("totalComments", totalComments); // Giữ nguyên totalComments gốc
            request.setAttribute("formattedTotalComments", formattedTotalComments); // Thêm formattedTotalComments
            System.out.println("Forwarding to DetailProduct.jsp with productId: " + id + ", comments size: " + comments.size());
            request.getRequestDispatcher("/web/GuessAndCustomer/DetailProduct.jsp").forward(request, response);

            // Xử lý yêu cầu hiển thị trang quản lý sản phẩm
        } else if (path.endsWith("/ProductManagement")) {
            List<Product> productList = productDAO.getAllProducts(); // Lấy danh sách tất cả sản phẩm
            List<Category> category = categoryDAO.getAllCategories(); // Lấy danh sách danh mục
            request.setAttribute("productList", productList);
            request.setAttribute("category", category);
            request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);

            // Xử lý yêu cầu hiển thị trang cập nhật số lượng sản phẩm
        }

        // Xử lý yêu cầu xóa sản phẩm
        if ("deleteProduct".equalsIgnoreCase(action)) {
            CartDAO cartDAO = new CartDAO();
            int productID = Integer.parseInt(request.getParameter("productID")); // Lấy ID sản phẩm cần xóa

            try {
                // Kiểm tra xem sản phẩm có trong giỏ hàng không
                if (cartDAO.checkProductInCart(productID)) {
                    request.getSession().setAttribute("errorMessage", "Cannot delete product because it is in cart!");
                    response.sendRedirect("/ProductController/ProductManagement");
                } else {
                    commentDAO.deleteCommentByProduct(productID);//xóa comment liên quan đến sp
                    productDAO.deleteProductDetail(productID); // Xóa chi tiết sản phẩm
                    productDAO.deleteProduct(productID); // Xóa sản phẩm
                    response.sendRedirect("/ProductController/ProductManagement");
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error while deleting product: " + e.getMessage());
                request.getRequestDispatcher("/ProductController/ProductManagement").forward(request, response);
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method. Xử lý các yêu cầu thêm, sửa,
     * xóa sản phẩm, thêm bình luận, xóa bình luận và cập nhật bình luận từ
     * modal.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đặt mã hóa ký tự UTF-8 cho request
        response.setCharacterEncoding("UTF-8"); // Đặt mã hóa ký tự UTF-8 cho response

        // Xác định thư mục lưu trữ hình ảnh sản phẩm
        String file = request.getSession().getServletContext().getRealPath("/link/img");
        String[] s = file.split("\\\\");
        String fileImg = "";
        for (int i = 0; i < s.length; i++) {
            if (!s[i].equals("build")) {
                fileImg += s[i];
                if (i < s.length - 1) {
                    fileImg += "\\";
                }
            }
        }
        String action = request.getParameter("action"); // Lấy tham số action từ request

        OrderDAO orderDAO = new OrderDAO(); // Thêm OrderDAO

        // Xử lý yêu cầu thêm sản phẩm mới
        if ("addProduct".equalsIgnoreCase(action)) {
            try {
                String name = request.getParameter("productName");
                int quantity = Integer.parseInt(request.getParameter("proQuantity"));
                double price = Double.parseDouble(request.getParameter("proPrice"));
                boolean state = Integer.parseInt(request.getParameter("proState")) == 1;
                String description = request.getParameter("proDescription");
                int categoryID = Integer.parseInt(request.getParameter("proCategory"));

                // Xử lý tải lên hình ảnh sản phẩm
                Part part = request.getPart("proImg");
                Path fileName = Paths.get(part.getSubmittedFileName());
                if (!Files.exists(Paths.get(fileImg))) {
                    Files.createDirectories(Paths.get(fileImg));
                }
                String picture = fileName.getFileName().toString();
                part.write(fileImg + "/" + fileName);

                // Thêm sản phẩm vào cơ sở dữ liệu
                ProductDAO productDAO = new ProductDAO();
                productDAO.addProduct(name, quantity, price, state, picture, description, categoryID);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error adding product: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }
        }

        // Xử lý yêu cầu chỉnh sửa sản phẩm
        if ("editProduct".equalsIgnoreCase(action)) {
            ProductDAO productDAO = new ProductDAO();
            try {
                int id = Integer.parseInt(request.getParameter("productID"));
                String name = request.getParameter("productName");
                int quantity = Integer.parseInt(request.getParameter("proQuantity"));
                double price = Double.parseDouble(request.getParameter("proPrice"));
                boolean state = Integer.parseInt(request.getParameter("proState")) == 1;
                String description = request.getParameter("proDescription");
                int categoryID = Integer.parseInt(request.getParameter("proCategory"));

                // Xử lý tải lên hình ảnh mới (nếu có)
                Part filePart = request.getPart("proImg");
                Path fileName = Paths.get(filePart.getSubmittedFileName());
                if (!Files.exists(Paths.get(fileImg))) {
                    Files.createDirectories(Paths.get(fileImg));
                }

                String picture = fileName.getFileName().toString();

                if (!picture.isEmpty()) {
                    // Lấy ảnh cũ của sản phẩm hiện tại
                    String oldImage = productDAO.getProductImgById(id);

                    // Kiểm tra nếu ảnh cũ không rỗng và không null
                    if (oldImage != null && !oldImage.isEmpty()) {
                        // Kiểm tra xem ảnh này có được sử dụng ở sản phẩm khác không
                        boolean isUsedByOtherProducts = productDAO.checkImageUsedByOtherProducts(oldImage, id);

                        // Nếu ảnh không được dùng ở sản phẩm khác, mới thực hiện xóa
                        if (!isUsedByOtherProducts) {
                            File filePic = new File(fileImg + "/" + oldImage);
                            if (filePic.exists()) {
                                filePic.delete();
                            }
                        }
                    }

                    // Lưu ảnh mới
                    filePart.write(fileImg + "/" + picture);
                }

                // Cập nhật thông tin sản phẩm
                productDAO.updateProduct(id, name, quantity, price, state, picture, description, categoryID);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error while updating product: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }
        }

        // Xử lý yêu cầu cập nhật chi tiết sản phẩm
        if ("updateProductDetail".equalsIgnoreCase(action)) {
            try {
                int productID = Integer.parseInt(request.getParameter("productID"));
                String operatingSystem = request.getParameter("operatingSystem");
                String cpuTechnology = request.getParameter("cpuTechnology");
                int coreCount = Integer.parseInt(request.getParameter("coreCount"));
                int threadCount = Integer.parseInt(request.getParameter("threadCount"));
                String cpuSpeed = request.getParameter("cpuSpeed");
                String gpu = request.getParameter("gpu");
                int ram = Integer.parseInt(request.getParameter("ram"));
                String ramType = request.getParameter("ramType");
                String ramBusSpeed = request.getParameter("ramBusSpeed");
                int maxRam = Integer.parseInt(request.getParameter("maxRam"));
                String storage = request.getParameter("storage");
                String memoryCard = request.getParameter("memoryCard");
                String screen = request.getParameter("screen");
                String resolution = request.getParameter("resolution");
                String refreshRate = request.getParameter("refreshRate");
                String batteryCapacity = request.getParameter("batteryCapacity");
                String batteryType = request.getParameter("batteryType");
                String maxChargingSupport = request.getParameter("maxChargingSupport");
                String releaseDate = request.getParameter("releaseDate");
                String origin = request.getParameter("origin");

                // Tạo đối tượng ProductDetail và cập nhật vào cơ sở dữ liệu
                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductById(productID);
                ProductDetail productDetail = new ProductDetail(product, operatingSystem, cpuTechnology, coreCount,
                        threadCount, cpuSpeed, gpu, ram, ramType, ramBusSpeed, maxRam, storage, memoryCard, screen,
                        resolution, refreshRate, batteryCapacity, batteryType, maxChargingSupport, releaseDate, origin);

                productDAO.updateProductDetail(productDetail);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error updating product details: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }
        }

        // Xử lý yêu cầu thêm bình luận từ biểu mẫu
        if (request.getParameter("rate") != null && request.getParameter("comment") != null && request.getParameter("action") == null) {
            try {
                int productID = Integer.parseInt(request.getParameter("productId"));
                int rate = Integer.parseInt(request.getParameter("rate"));
                String commentContent = request.getParameter("comment");
                String userIDParam = request.getParameter("id");

                // Ghi log để debug yêu cầu thêm bình luận
                System.out.println("Nhận yêu cầu thêm bình luận - ProductID: " + productID + ", Rate: " + rate + ", Comment: " + commentContent + ", UserID: " + userIDParam);

                // Kiểm tra xem người dùng đã đăng nhập chưa
                if (userIDParam == null || userIDParam.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Bạn cần đăng nhập để bình luận.");
                    request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
                    return;
                }

                int userID = Integer.parseInt(userIDParam);

                // Thêm kiểm tra xem người dùng đã mua sản phẩm chưa
                if (!orderDAO.hasUserPurchasedProduct(userID, productID)) {
                    request.setAttribute("errorMessage", "Bạn chỉ có thể bình luận về sản phẩm đã mua.");
                    request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
                    return;
                }

                // Kiểm tra dữ liệu đầu vào
                if (rate < 1 || rate > 5) {
                    request.setAttribute("errorMessage", "Điểm đánh giá phải từ 1 đến 5.");
                    request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
                    return;
                }
                if (commentContent == null || commentContent.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Nội dung bình luận không được để trống.");
                    request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
                    return;
                }

                // Thêm bình luận vào cơ sở dữ liệu
                CommentDAO commentDAO = new CommentDAO();
                commentDAO.addComment(productID, rate, commentContent, userID);

                response.sendRedirect("/ProductController/DetailProductCustomer?id=" + productID);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage());
                String productID = request.getParameter("productId");
                request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi thêm bình luận: " + e.getMessage());
                String productID = request.getParameter("productId");
                request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
            }
        }

        // Xử lý yêu cầu xóa bình luận
        if ("deleteComment".equalsIgnoreCase(action)) {
            try {
                int commentID = Integer.parseInt(request.getParameter("commentID"));
                int productID = Integer.parseInt(request.getParameter("productId"));

                // Ghi log để debug yêu cầu xóa bình luận
                System.out.println("Nhận yêu cầu deleteComment - commentID: " + commentID + ", ProductID: " + productID);

                // Xóa bình luận khỏi cơ sở dữ liệu
                CommentDAO commentDAO = new CommentDAO();
                boolean success = commentDAO.deleteComment(commentID);

                if (success) {
                    response.sendRedirect("/ProductController/DetailProductCustomer?id=" + productID);
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa bình luận.");
                    request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage());
                String productID = request.getParameter("productId");
                request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi xóa bình luận: " + e.getMessage());
                String productID = request.getParameter("productId");
                request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
            }
        }

        // Xử lý yêu cầu cập nhật bình luận từ modal
        if ("updateComment".equalsIgnoreCase(action)) {
            try {
                // Lấy dữ liệu từ form trong modal
                int commentID = Integer.parseInt(request.getParameter("commentID"));
                int rate = Integer.parseInt(request.getParameter("rate"));
                String commentContent = request.getParameter("comment");
                int productID = Integer.parseInt(request.getParameter("productId"));

                // Ghi log để debug yêu cầu cập nhật bình luận
                System.out.println("Nhận yêu cầu updateComment - commentID: " + commentID + ", Rate: " + rate + ", Comment: " + commentContent + ", ProductID: " + productID);

                // Cập nhật bình luận trong cơ sở dữ liệu
                CommentDAO commentDAO = new CommentDAO();
                boolean success = commentDAO.updateComment(commentID, rate, commentContent);

                if (success) {
                    // Nếu cập nhật thành công, chuyển hướng về trang chi tiết sản phẩm
                    response.sendRedirect("/ProductController/DetailProductCustomer?id=" + productID);
                } else {
                    // Nếu thất bại, hiển thị thông báo lỗi
                    request.setAttribute("errorMessage", "Không thể cập nhật bình luận.");
                    request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
                }
            } catch (NumberFormatException e) {
                // Xử lý lỗi khi dữ liệu không đúng định dạng số
                e.printStackTrace();
                request.setAttribute("errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage());
                String productID = request.getParameter("productId");
                request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
            } catch (Exception e) {
                // Xử lý các lỗi khác khi cập nhật bình luận
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi cập nhật bình luận: " + e.getMessage());
                String productID = request.getParameter("productId");
                request.getRequestDispatcher("/ProductController/DetailProductCustomer?id=" + productID).forward(request, response);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
