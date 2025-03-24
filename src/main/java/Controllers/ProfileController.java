/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.ProfileDAO;
import Model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author tiend
 */
public class ProfileController extends HttpServlet {

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
            out.println("<title>Servlet ProfileController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    private ProfileDAO profileDAO; // Khai báo instance variable

    @Override
    public void init() throws ServletException {
        profileDAO = new ProfileDAO(); // Khởi tạo instance trong init()
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
        if (path.endsWith("/ProfileController/Profile")) {
            Cookie[] cookies = request.getCookies();
            String userName = null;
            String userRole = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("user".equals(cookie.getName())) {
                        String[] values = cookie.getValue().split("\\|");
                        if (values.length == 2) {  // Kiểm tra xem có đủ phần tử không
                            userName = values[0];
                            userRole = values[1];
                        }
                    }
                }
            }
            ProfileDAO profileDAO = new ProfileDAO();
            try {
                Account account = profileDAO.getAccount(userName); // Gọi trực tiếp qua tên class
                if (account != null) {
                    request.setAttribute("account", account);
                    request.getRequestDispatcher("/web/GuessAndCustomer/Profile.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving profile");
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
        String path = request.getRequestURI();
        ProfileDAO profileDAO = new ProfileDAO();

        if (path.endsWith("/ProfileController/Profile")) {
            Cookie[] cookies = request.getCookies();
            String userName = null;
            String userRole = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("user".equals(cookie.getName())) {
                        String[] values = cookie.getValue().split("\\|");
                        if (values.length == 2) {  // Kiểm tra xem có đủ phần tử không
                            userName = values[0];
                            userRole = values[1];
                        }
                    }
                }
            }
            Account account = profileDAO.getAccount(userName);
            if (account == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }
            int userId= account.getId();
            try {
                // Kiểm tra hành động: cập nhật profile hay đổi mật khẩu
                String action = request.getParameter("action");

                if ("changePassword".equals(action)) {
                    // Xử lý thay đổi mật khẩu
                    String oldPassword = request.getParameter("oldPassword");
                    String newPassword = request.getParameter("newPassword");
                    String confirmPassword = request.getParameter("confirmPassword");

                    // Kiểm tra mật khẩu cũ
                    if (!profileDAO.checkOldPassword(userId, oldPassword)) {
                        request.setAttribute("errorMessage", "Old password is incorrect.");
                        request.setAttribute("account", account);
                        request.getRequestDispatcher("/web/GuessAndCustomer/Profile.jsp").forward(request, response);
                        return;
                    }
                    // Kiểm tra mật khẩu mới trùng mật khẩu cũ
                    if (newPassword.equals(oldPassword)) {
                        request.setAttribute("errorMessage", "New password is similar to old password.");
                        request.setAttribute("account", account);
                        request.getRequestDispatcher("/web/GuessAndCustomer/Profile.jsp").forward(request, response);
                        return;
                    }

                    // Kiểm tra mật khẩu mới và xác nhận mật khẩu
                    if (!newPassword.equals(confirmPassword)) {
                        request.setAttribute("errorMessage", "New password and confirmation do not match.");
                        request.setAttribute("account", account);
                        request.getRequestDispatcher("/web/GuessAndCustomer/Profile.jsp").forward(request, response);
                        return;
                    }

                    // Kiểm tra độ dài mật khẩu (tùy chọn)
                    if (newPassword.length() < 1) {
                        request.setAttribute("errorMessage", "New password must be at least 1 characters long.");
                        request.setAttribute("account", account);
                        request.getRequestDispatcher("/web/GuessAndCustomer/Profile.jsp").forward(request, response);
                        return;
                    }

                    // Cập nhật mật khẩu mới
                    boolean isUpdated = profileDAO.updatePassword(userId, newPassword);
                    if (isUpdated) {
                        request.setAttribute("successMessage", "Password changed successfully!");
                    } else {
                        request.setAttribute("errorMessage", "Failed to change password.");
                    }
                } else {
                    // Xử lý cập nhật thông tin profile (logic hiện tại)
                    String username = request.getParameter("username");
                    String email = request.getParameter("email");
                    String phoneNumber = request.getParameter("phoneNumber");
                    String address = request.getParameter("address");

                    account.setUsername(username);
                    account.setEmail(email);
                    account.setPhoneNumber(phoneNumber);
                    account.setAddress(address);

                    boolean isUpdated = profileDAO.updateAccount(account);
                    if (isUpdated) {
                        request.setAttribute("successMessage", "Profile updated successfully!");
                    } else {
                        request.setAttribute("errorMessage", "Failed to update profile.");
                    }
                }

                request.setAttribute("account", account);
                request.getRequestDispatcher("/web/GuessAndCustomer/Profile.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
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
