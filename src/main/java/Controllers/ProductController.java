/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CategoryDAO;
import DAOs.ProductDAO;
import Model.Category;
import Model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author tiend
 */
@MultipartConfig
public class ProductController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
            /* TODO output your page here. You may use following sample code. */
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String action = request.getParameter("action");

        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        List<Product> productList = productDAO.getAllProducts();
        List<Category> category = categoryDAO.getAllCategories(); // Lấy tất cả danh mục

        if (path.endsWith("/UpdateQuantity")) {

            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
        } else if (path.startsWith("/ProductController/DetailProductCustomer")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.getProductById(id);
            request.setAttribute("category", category);
            request.setAttribute("product", product);
            request.getRequestDispatcher("/web/GuessAndCustomer/DetailProduct.jsp").forward(request, response);

        } else if (path.endsWith("/ProductManagement")) {
            request.setAttribute("category", category);
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);
        } else if (path.endsWith("/UpdateQuantity")) {
            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
        }

        if ("deleteProduct".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("productID"));

                productDAO.deleteProduct(id);
                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi xóa sản phẩm: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("addProduct".equalsIgnoreCase(action)) {
            try {
                // Lấy dữ liệu từ form
                String name = request.getParameter("productName");
                int quantity = Integer.parseInt(request.getParameter("proQuantity"));
                double price = Double.parseDouble(request.getParameter("proPrice"));
                boolean state = Integer.parseInt(request.getParameter("proState")) == 1;
                String description = request.getParameter("proDescription");
                int categoryID = Integer.parseInt(request.getParameter("proCategory"));

//                Part filePart = request.getPart("proImg");
//                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
//                String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
//
//                File uploadDir = new File(uploadPath);
//                if (!uploadDir.exists()) {
//                    uploadDir.mkdir();
//                }
//                filePart.write(uploadPath + File.separator + fileName);
                Part filePart = request.getPart("proImg"); // Lấy file từ form
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Lấy tên file gốc

                // **Lấy phần mở rộng của file**
                String extension = "";
                int dotIndex = originalFileName.lastIndexOf(".");
                if (dotIndex > 0) {
                    extension = originalFileName.substring(dotIndex); // VD: ".png", ".jpg"
                }

                // **Xác định thư mục lưu ảnh**
                String uploadPath = getServletContext().getRealPath("") + File.separator + "link" + File.separator + "img";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // **Tạo thư mục nếu chưa tồn tại**
                }

                // **Kiểm tra xem ảnh đã tồn tại chưa**
                File file = new File(uploadPath + File.separator + originalFileName);
                if (!file.exists()) {
                    // **Chỉ lưu file nếu nó chưa tồn tại**
                    filePart.write(uploadPath + File.separator + originalFileName);
                }

                // **Gửi tên file để lưu vào database (dùng tên cũ nếu ảnh đã tồn tại)**
                request.setAttribute("uploadedFileName", originalFileName);

                ProductDAO productDAO = new ProductDAO();
                productDAO.addProduct(name, quantity, price, state, uploadPath, description, categoryID);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi thêm sản phẩm: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }
        }

        if ("editProduct".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("productID"));
                String name = request.getParameter("productName");
                int quantity = Integer.parseInt(request.getParameter("proQuantity"));
                double price = Double.parseDouble(request.getParameter("proPrice"));
                boolean state = Integer.parseInt(request.getParameter("proState")) == 1;
                String description = request.getParameter("proDescription");
                int categoryID = Integer.parseInt(request.getParameter("proCategory"));

                Part filePart = request.getPart("proImg"); // Lấy file từ form
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Lấy tên file gốc

                // **Lấy phần mở rộng của file**
                String extension = "";
                int dotIndex = originalFileName.lastIndexOf(".");
                if (dotIndex > 0) {
                    extension = originalFileName.substring(dotIndex); // VD: ".png", ".jpg"
                }

                // **Xác định thư mục lưu ảnh**
                // Xác định thư mục lưu ảnh trong src/main/webapp/link/img
                String uploadPath = System.getProperty("user.dir") + File.separator
                        + "src" + File.separator + "main" + File.separator
                        + "webapp" + File.separator + "link" + File.separator + "img";

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // **Tạo thư mục nếu chưa tồn tại**
                }

                // **Kiểm tra xem ảnh đã tồn tại chưa**
                File file = new File(uploadPath + File.separator + originalFileName);
                if (!file.exists()) {
                    // **Chỉ lưu file nếu nó chưa tồn tại**
                    filePart.write(uploadPath + File.separator + originalFileName);
                }

                // **Gửi tên file để lưu vào database (dùng tên cũ nếu ảnh đã tồn tại)**
                request.setAttribute("uploadedFileName", originalFileName);

                ProductDAO productDAO = new ProductDAO();
                productDAO.updateProduct(id, name, quantity, price, state, uploadPath, description, categoryID);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
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
    }// </editor-fold>

}
