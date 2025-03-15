/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.AccountDAO;
import DAOs.CartDAO;
import Model.Account;
import Model.Cart;
import Model.Product;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author tiend
 */
public class CartController extends HttpServlet {

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
            out.println("<title>Servlet CartController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartController at " + request.getContextPath() + "</h1>");
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
    if (path.endsWith("/CartController/Cart")) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Nếu userId không có trong session, kiểm tra cookie
        if (userId == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("user".equals(cookie.getName())) {
                        String[] values = cookie.getValue().split("\\|");
                        if (values.length == 2) {
                            String username = values[0];
                            String role = values[1];
                            CartDAO userDAO = new CartDAO();
                            Account user = null;
                            try {
                                user = userDAO.getAccountByUsername(username); // Giả sử có phương thức này
                            } catch (SQLException ex) {
                                Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (user != null && user.getRole().equals(role)) {
                                userId = user.getId();
                                session.setAttribute("userId", userId);
                                session.setAttribute("user", user);
                            }
                            break;
                        }
                    }
                }
            }
        }

        // Nếu vẫn không có userId, chuyển hướng đến login
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        CartDAO dao = new CartDAO();
        Cart cart = (Cart) session.getAttribute("cart");
        try {
            cart = dao.loadCartFromDB(userId);
            if (cart == null) {
                cart = new Cart(userId);
            }
            session.setAttribute("cart", cart);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CartController.class.getName()).log(Level.SEVERE, "Lỗi khi tải giỏ hàng từ DB", ex);
            cart = new Cart(userId);
            session.setAttribute("cart", cart);
        }
        request.getRequestDispatcher("/web/GuessAndCustomer/cart.jsp").forward(request, response);
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
       HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
       Cart cart = (Cart) session.getAttribute("cart");

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        String action = request.getParameter("action");
        if (action == null) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Thiếu tham số action!");
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            CartDAO dao = new CartDAO();

            if ("add".equals(action)) {
                if (userId == null) {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
                    out.print(jsonResponse.toString());
                    out.flush();
                    return;
                }
                if (cart == null) {
                    cart = new Cart(userId);
                    session.setAttribute("cart", cart);
                }
                int quantity = request.getParameter("quantity") != null
                        ? Integer.parseInt(request.getParameter("quantity"))
                        : 1;
                Product product = dao.getProductById(productId);
                if (product != null && product.getProState() == 1 && product.getProQuantity() >= quantity) {
                    cart.addItem(product, quantity);
                    dao.addToCart(userId, productId, quantity);
                    session.setAttribute("cart", cart);
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Sản phẩm đã được thêm vào giỏ hàng.");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Sản phẩm không tồn tại hoặc không đủ số lượng!");
                }
            } else if (userId != null && cart != null) {
                switch (action) {
                    case "update": {
                        int quantity = Integer.parseInt(request.getParameter("quantity"));
                        cart.updateQuantity(productId, quantity);
                        dao.updateCart(userId, productId, quantity);
                        session.setAttribute("cart", cart);
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Đã cập nhật số lượng.");
                        break;
                    }
                    case "delete": {
                        cart.removeItem(productId);
                        dao.removeFromCart(userId, productId);
                        session.setAttribute("cart", cart);
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Đã xóa sản phẩm.");
                        break;
                    }
                    default: {
                        jsonResponse.put("status", "error");
                        jsonResponse.put("message", "Hành động không hợp lệ!");
                        break;
                    }
                }
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Vui lòng đăng nhập để thực hiện hành động này!");
            }

            out.print(jsonResponse.toString());
            out.flush();
        } catch (NumberFormatException e) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Dữ liệu không hợp lệ!");
            out.print(jsonResponse.toString());
            out.flush();
        } catch (SQLException | ClassNotFoundException ex) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Lỗi cơ sở dữ liệu!");
            out.print(jsonResponse.toString());
            out.flush();
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