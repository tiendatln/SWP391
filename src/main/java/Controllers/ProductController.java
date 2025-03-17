/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CartDAO;
import DAOs.CategoryDAO;
import DAOs.CommentDAO;
import DAOs.ProductDAO;
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
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet xử lý các yêu cầu liên quan đến sản phẩm, bao gồm hiển thị chi tiết sản phẩm,
 * quản lý sản phẩm, thêm/sửa/xóa sản phẩm và bình luận.
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
        try (PrintWriter out = response.getWriter()) {
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
     * Handles the HTTP <code>GET</code> method. Xử lý các yêu cầu lấy thông tin sản phẩm,
     * chi tiết sản phẩm, quản lý sản phẩm và xóa sản phẩm.
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

            // Đặt các thuộc tính để truyền sang JSP
            request.setAttribute("product", product);
            request.setAttribute("productDetail", productDetail);
            request.setAttribute("productId", id);
            request.setAttribute("comments", comments);
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
        } else if (path.endsWith("/UpdateQuantity")) {
            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
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
     * Handles the HTTP <code>POST</code> method. Xử lý các yêu cầu thêm, sửa, xóa sản phẩm,
     * thêm bình luận, xóa bình luận và cập nhật bình luận từ modal.
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
        fileImg = fileImg.substring(0, fileImg.length() - 1); // Xóa ký tự thừa ở cuối
        String action = request.getParameter("action"); // Lấy tham số action từ request

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
                    filePart.write(fileImg + "/" + fileName);
                    File filePic = new File(fileImg + "/" + productDAO.getProductImgById(id));
                    if (filePic.exists()) {
                        filePic.delete(); // Xóa hình ảnh cũ
                    }
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