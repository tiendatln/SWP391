/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.AccountDAO;
import DAOs.OrderDAO;
import DAOs.ProductDAO;
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
        }else if (path.startsWith("/OrderController/PrepareOrder")){
            List<CartForOrder> proList = new ArrayList<>();
            ProductDAO proDAO = new ProductDAO();
            OrderDAO oDAO = new OrderDAO();
            CartForOrder cartFO = new CartForOrder();
            Voucher voucher = new Voucher();
            voucher = oDAO.getVOucherID();
            String[] _productID = request.getParameterValues("productId/Quantity");
            String _userID = request.getParameter("userID");
            for(int i = 0; i < _productID.length; i++){
                String[] parts = _productID[i].split("/");
                String[] _quantity = new String[_productID.length];
                _productID[i] = parts[0];
                _quantity[i] = parts[1];
                cartFO.setProduct(proDAO.getProductById(Integer.parseInt(_productID[i])));
                cartFO.setQuantity(Integer.parseInt(_quantity[i]));
                cartFO.setAccount(oDAO.getAccountByID(Integer.parseInt(_userID)));
                proList.add(cartFO);
            }
            HttpSession session = request.getSession();
            session.setAttribute("cartOrder", proList);
            session.setAttribute("voucher", voucher);
            request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);
        }
        else if (path.startsWith("/OrderController/ConfirmOrder")){
            List<CartForOrder> proList = new ArrayList<>();
            HttpSession session = request.getSession();
            proList = (List<CartForOrder>) session.getAttribute("cartOrder");
            OrderDAO oDAO = new OrderDAO();
            ProductDAO proDAO = new ProductDAO();
            List<Order> List = new ArrayList<>();
            OrderTotal ot = new OrderTotal();
            Order o = new Order();
            
            
            
            for (int i = 0; i < proList.size(); i++) {
                o = new Order(proList.get(i).getProduct(), ot, i, i);
                   
                }
            
            
            if(oDAO.addNewOrder(ot, List)){
                for (int i = 0; i < proList.size(); i++) {
                    if(!oDAO.deleteCart(proList.get(i).getAccount().getId(), proList.get(i).getProduct().getProductID())){
                        
                    }
                }
            }
            
            request.getRequestDispatcher("/web/GuessAndCustomer/orderConfirmation.jsp").forward(request, response);
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
