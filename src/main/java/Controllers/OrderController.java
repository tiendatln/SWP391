/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.AccountDAO;
import DAOs.OrderDAO;
import DAOs.ProductDAO;
import DAOs.VoucherDao;
import Model.CartForOrder;
import Model.Order;
import Model.OrderTotal;
import Model.Product;
import Model.Voucher;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        } else if (path.startsWith("/OrderController/CustomerOrder")) {
            String[] id = path.split("/");
            List<Order> order = new ArrayList<>();
            OrderDAO oDAO = new OrderDAO();
            order = oDAO.getAllOrderTotalByUserID(Integer.parseInt(id[id.length - 1]));

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
        } else if (path.startsWith("/OrderController/PrepareOrder")) {
            List<CartForOrder> proList = new ArrayList<>();
            ProductDAO proDAO = new ProductDAO();
            OrderDAO oDAO = new OrderDAO();
            VoucherDao vDAO = new VoucherDao();
            List<Voucher> voucher = new ArrayList<>();
            try {
                voucher = vDAO.getAllVouchers();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] _productAndQuantity = request.getParameterValues("productId/Quantity");
            String _userID = request.getParameter("userID");
            for (int i = 0; i < _productAndQuantity.length; i++) {
                String[] parts = _productAndQuantity[i].split("/");
                String _productID = parts[0];
                String _quantity = parts[1];
                // Create a new CartForOrder object for each iteration
                CartForOrder cartFO = new CartForOrder();
                cartFO.setProduct(proDAO.getProductById(Integer.parseInt(_productID)));
                cartFO.setQuantity(Integer.parseInt(_quantity));
                cartFO.setAccount(oDAO.getAccountByID(Integer.parseInt(_userID)));
                proList.add(cartFO);
            }
            HttpSession session = request.getSession();
            session.setAttribute("cartOrder", proList);
            session.setAttribute("voucher", voucher);
            request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);
        } else if (path.startsWith("/OrderController/ConfirmOrder")) {
            // Lấy session từ request

            HttpSession session = request.getSession();

            // Xử lý logic của ConfirmOrder
            List<CartForOrder> proList = new ArrayList<>();
            proList = (List<CartForOrder>) session.getAttribute("cartOrder");

            // Kiểm tra giỏ hàng rỗng
            if (proList == null || proList.isEmpty()) {
                session.setAttribute("message1", "Giỏ hàng trống!");
                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
                return;
            }

            String _phone = request.getParameter("phone");
            String _address = request.getParameter("address");
            String _note = request.getParameter("note");
            String _voucherCode = request.getParameter("voucher");
            VoucherDao vDAO = new VoucherDao();

            try {
                if (!vDAO.voucherExists(_voucherCode)) {
                    session.setAttribute("message1", "Mã voucher không hợp lệ!");
                    request.getRequestDispatcher("/checkout.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("message1", "Lỗi hệ thống khi kiểm tra voucher!");
                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
                return;
            }

            String price = request.getParameter("priceTotal");
            int _priceTotal = 0;
            if (price.contains(".")) {
                double value = Double.parseDouble(price);
                _priceTotal = (int) value; // Hoặc Math.round(value) nếu muốn làm tròn
            } else {
                _priceTotal = Integer.parseInt(price);
            }

            OrderDAO oDAO = new OrderDAO();
            java.util.Date today = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(today.getTime());
            int voucherID = oDAO.getVoucherID(_voucherCode);
            OrderTotal ot = new OrderTotal(_phone, _address, _note, _priceTotal, sqlDate, 0, voucherID, proList.get(0).getAccount());
            List<Order> orderList = new ArrayList<>();

            // Tạo danh sách các Order
            for (CartForOrder cartItem : proList) {
                Order o = new Order(cartItem.getProduct(), ot, cartItem.getQuantity(), cartItem.getProduct().getProPrice());
                orderList.add(o);
            }

            // Add order and clear cart
            if (oDAO.addNewOrder(ot, orderList)) {
                session.setAttribute("message1", "Total amount: " + _priceTotal + "$");
                session.setAttribute("message2", "Transfer note: " + proList.get(0).getAccount().getUsername() + "_Price_" + _priceTotal);
                request.setAttribute("amount", String.valueOf(_priceTotal));
                request.setAttribute("description:", proList.get(0).getAccount().getUsername() + "_Price_" + _priceTotal);
                request.getRequestDispatcher("/generateQR").forward(request, response);
            } else {
                session.setAttribute("message1", "Order placement failed!");
                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
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
