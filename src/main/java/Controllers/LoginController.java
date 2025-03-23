/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.AccountDAO;
import Model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author tiend
 */
public class LoginController extends HttpServlet {

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
            out.println("<title>Servlet LoginController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
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
        if (path.endsWith("/LoginController/Login")) {
            Cookie[] cookies = request.getCookies();
            String userName = null;
            String userRole = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("user".equals(cookie.getName())) {
                        String[] values = cookie.getValue().split("\\|");
                        if (values.length == 2) {
                            userName = values[0];
                            userRole = values[1];
                            if (!userName.isEmpty() && "customer".equals(userRole)) {
                                response.sendRedirect("/web/index.jsp");
                            } else if ("admin".equals(userRole)) {
                                request.getRequestDispatcher("/OrderController/OrderManagement").forward(request, response);
                            }
                        }
                    }
                }
            }
            request.getRequestDispatcher("/web/login.jsp").forward(request, response);
        } else if (path.endsWith("/LoginController/ForgotPassword")) {
            request.getRequestDispatcher("/web/forget-password.jsp").forward(request, response);
        } else if (path.endsWith("/LoginController/Register")) {
            request.getRequestDispatcher("/web/register.jsp").forward(request, response);
        } else if (path.endsWith("/LoginController/SendLink")) {
            request.getRequestDispatcher("/web/reset_password.jsp").forward(request, response);
        } else if (path.endsWith("/LoginController/Logout")) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) {
                        cookie.setMaxAge(0);
                        cookie.setValue("");
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
            HttpSession session = request.getSession();
            session.invalidate();
            request.getRequestDispatcher("/web/index.jsp").forward(request, response);
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
        String path = request.getRequestURI();
        if (path.endsWith("/LoginController/Login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            AccountDAO userDAO = new AccountDAO();
            Account user = userDAO.validateUser(username, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId()); // Lưu userId vào session

                Cookie userCookie = new Cookie("user", user.getUsername() + "|" + user.getRole());
                userCookie.setMaxAge(60 * 60 * 24);
                userCookie.setSecure(false);
                userCookie.setHttpOnly(false);
                userCookie.setPath("/");
                response.addCookie(userCookie);

                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("/OrderController/OrderManagement");
                } else {
                    response.sendRedirect("/web/index.jsp");
                }
            } else {
                response.sendRedirect("/web/login.jsp?error=invalid");
            }
        } else if (path.endsWith("/LoginController/Register")) {
            // Lấy dữ liệu từ form đăng ký
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String retypePassword = request.getParameter("confirmPassword");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            AccountDAO userDAO = new AccountDAO();

            // Kiểm tra username đã tồn tại chưa
            if (userDAO.getAccountByUsername(username) != null) {
                response.sendRedirect("/web/register.jsp?error=Username already exists");
                return;
            }

            // Tạo tài khoản mới
            Account newUser = new Account(username, email, password, phone, address, "customer"); // Mặc định role là "customer"
            boolean success = userDAO.addNewAccount(newUser);
            if (success) {
                response.sendRedirect("/web/login.jsp?message=Registration successful");
            } else {
                response.sendRedirect("/web/register.jsp?error=Registration failed");
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
