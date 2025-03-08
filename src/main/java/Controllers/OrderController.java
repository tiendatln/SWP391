/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.OrderDAO;
import Model.Order;
import Model.OrderTotal;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tiend
 */

public class OrderController extends HttpServlet {

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
            out.println("<title>Servlet OrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderController at " + request.getContextPath() + "</h1>");
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
        if (path.endsWith("/OrderController/OrderManagement")) {
            // Giả lập danh sách đơn hàng (có thể lấy từ database)
            List<Order> order = new ArrayList<>();
            OrderDAO oDAO = new OrderDAO();
            order = oDAO.getAllOrderTotal();

            // Gửi danh sách đơn hàng đến JSP
            HttpSession session = request.getSession();
            session.setAttribute("orderList", order);
            request.getRequestDispatcher("/web/Staff/DisplayOrder.jsp").forward(request, response);
        } else if (path.endsWith("/CustomerOrder")) {

            List<Order> order = new ArrayList<>();
            OrderDAO oDAO = new OrderDAO();
            order = oDAO.getAllOrderTotal();

            // Gửi danh sách đơn hàng đến JSP
            HttpSession session = request.getSession();
            session.setAttribute("orderList", order);
            request.getRequestDispatcher("/web/GuessAndCustomer/show.jsp").forward(request, response);
        } else if (path.startsWith("/OrderController/OrderDetail")) {
            try {
                String[] id = path.split("/");
                int orderID = Integer.parseInt(id[id.length - 1]);
                OrderDAO orderDAO = new OrderDAO();
                List<Order> orderDetails = orderDAO.getOrderDetails(orderID);
                request.setAttribute("orderDetails", orderDetails);

                request.getRequestDispatcher("/web/Staff/orderDetail.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect("/OrderController/OrderManagement");
            }
        }else if (path.startsWith("/OrderController/orderProduct")){
            request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);
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
        if (path.endsWith("/UpdateOrder")) {
            try {
                int status = Integer.parseInt(request.getParameter("status"));
            int id = Integer.parseInt(request.getParameter("orderID"));

            List<Order> order = new ArrayList<>();
            OrderDAO oDAO = new OrderDAO();
            order = oDAO.UpdateStatusAndGetAllOrder(id, status);

            // Gửi danh sách đơn hàng đến JSP
            HttpSession session = request.getSession();
            session.setAttribute("orderList", order);
            request.getRequestDispatcher("/web/Staff/DisplayOrder.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendRedirect("/OrderController/OrderManagement");
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
