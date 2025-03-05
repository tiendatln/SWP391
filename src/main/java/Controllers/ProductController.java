/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CategoryDAO;
import DAOs.ProductDAO;
import DB.DBConnection;
import Model.Category;
import Model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiend
 */
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
        if (path.endsWith("/UpdateQuantity")) {

            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
        } else if (path.contains("/ProductController/DetailProductCustomer/")) {
            String idStr = path.substring(path.lastIndexOf("/") + 1);
            try {
                int id = Integer.parseInt(idStr);
                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductById(id);

                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/GuessAndCustomer/DetailProduct.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp"); // Nếu không tìm thấy sản phẩm
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }

        } else if (path.endsWith("/ProductManagement")) {
            Connection conn;
            try {
                conn = DBConnection.connect(); // Đảm bảo có class DatabaseConnection
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            List<Product> productList = productDAO.getAllProducts();
            List<Category> category = categoryDAO.getAllCategories(); // Lấy tất cả danh mục

            request.setAttribute("category", category);
            request.setAttribute("productList", productList);
            request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);
        } else if (path.endsWith("/UpdateQuantity")) {
            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
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

        if ("addProduct".equals(action)) {
            addProduct(request, response);
            request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);
        }
    }

    private static final String IMAGE_UPLOAD_DIR = "uploads";

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("productName");
            int quantity = Integer.parseInt(request.getParameter("proQuantity"));
            double price = Double.parseDouble(request.getParameter("proPrice"));
            boolean state = Integer.parseInt(request.getParameter("proState")) == 1;
            String description = request.getParameter("proDescription");
            int categoryID = Integer.parseInt(request.getParameter("proCategory"));

            // Xử lý file ảnh
            Part filePart = request.getPart("proImg");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + IMAGE_UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);
            String imagePath = IMAGE_UPLOAD_DIR + "/" + fileName;

            // Gọi DAO để thêm sản phẩm
            ProductDAO productDAO = new ProductDAO();
            boolean success = productDAO.addProduct(name, quantity, price, state, imagePath, description, categoryID);

            if (success) {
                // response.sendRedirect(request.getContextPath() + "/ProductManagement?success=1");
                request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Thêm sản phẩm thất bại!");
                request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);
            }
        } catch (IOException | NumberFormatException | ServletException e) {
            e.printStackTrace();
            //response.sendRedirect(request.getContextPath() + "/ProductManagement?error=1");

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
