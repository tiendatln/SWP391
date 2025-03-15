/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CartDAO;
import DAOs.CategoryDAO;
import DAOs.ProductDAO;
import Model.Category;
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
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

//        if (path.endsWith("/UpdateQuantity")) {
//
//            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
//        } else 
        if (path.startsWith("/ProductController/DetailProductCustomer")) {
            int id = Integer.parseInt(request.getParameter("id"));

            Product product = productDAO.getProductById(id);
            ProductDetail productDetail = productDAO.getProductDetailById(id);
            request.setAttribute("product", product);
            request.setAttribute("productDetail", productDetail);
            request.getRequestDispatcher("/web/GuessAndCustomer/DetailProduct.jsp").forward(request, response);

        } else if (path.endsWith("/ProductManagement")) {
            List<Product> productList = productDAO.getAllProducts();
            List<Category> category = categoryDAO.getAllCategories();
            request.setAttribute("productList", productList);
            request.setAttribute("category", category);
            request.getRequestDispatcher("/web/Staff/productManagement.jsp").forward(request, response);
        } else if (path.endsWith("/UpdateQuantity")) {
            request.getRequestDispatcher("/web/Staff/updateQuantity.jsp").forward(request, response);
        }

        if ("deleteProduct".equalsIgnoreCase(action)) {
            CartDAO cartDAO = new CartDAO();

            int productID = Integer.parseInt(request.getParameter("productID"));

            try {
                if (cartDAO.checkProductInCart(productID)) {
                    // Thay vì setAttribute, ta sẽ dùng session và xóa ngay sau khi hiển thị
                    request.getSession().setAttribute("errorMessage", "Cannot delete product because it is in cart!");
                    response.sendRedirect("/ProductController/ProductManagement");
                } else {
                    productDAO.deleteProductDetail(productID);
                    productDAO.deleteProduct(productID);
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
        fileImg.substring(0, fileImg.length() - 1);
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

                Part part = request.getPart("proImg");
                Path fileName = Paths.get(part.getSubmittedFileName());
                if (!Files.exists(Paths.get(fileImg))) {
                    Files.createDirectories(Paths.get(fileImg));
                }
                String picture = fileName.getFileName().toString();

                part.write(fileImg + "/" + fileName);

                ProductDAO productDAO = new ProductDAO();
                productDAO.addProduct(name, quantity, price, state, picture, description, categoryID);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error adding product: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }
        }

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

                // **Nhận file ảnh từ request**
                Part filePart = request.getPart("proImg"); // Lấy ảnh từ input có id="proImg"
                Path fileName = Paths.get(filePart.getSubmittedFileName()); // Lấy tên file gốc
                if (!Files.exists(Paths.get(fileImg))) {
                    Files.createDirectories(Paths.get(fileImg));
                }
                String picture = fileName.getFileName().toString();
                if (!picture.isEmpty()) {
                    filePart.write(fileImg + "/" + fileName);

                    File filePic = new File(fileImg + "/" + productDAO.getProductImgById(id));
                    if (filePic.exists()) {
                        filePic.delete();
                    }
                }

                productDAO.updateProduct(id, name, quantity, price, state, picture, description, categoryID);

                response.sendRedirect("/ProductController/ProductManagement");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error while updating product: " + e.getMessage());
                response.sendRedirect("/ProductController/ProductManagement");
            }
        }

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
