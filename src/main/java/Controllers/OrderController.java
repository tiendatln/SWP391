package Controllers;

import DAOs.AccountDAO;
import DAOs.OrderDAO;
import DAOs.ProductDAO;
import DAOs.VoucherDao;
import Model.Account;
import Model.CartForOrder;
import Model.Order;
import Model.OrderProduct;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.endsWith("/OrderController/OrderManagement")) {
            List<OrderProduct> order = new ArrayList<>();
            OrderDAO oDAO = new OrderDAO();
            try {
                order = oDAO.getAllOrderTotal();
            } catch (SQLException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("error", "Error loading order list: " + ex.getMessage());
            }

            HttpSession session = request.getSession();
            session.setAttribute("orderList", order);
            request.getRequestDispatcher("/web/Staff/DisplayOrder.jsp").forward(request, response);
        } else if (path.startsWith("/OrderController/CustomerOrder")) {
            String[] id = path.split("/");
            List<OrderProduct> order = new ArrayList<>();
            OrderDAO oDAO = new OrderDAO();
            order = oDAO.getAllOrderTotalByUserID(Integer.parseInt(id[id.length - 1]));

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.endsWith("/UpdateOrder")) {
            try {
                int status = Integer.parseInt(request.getParameter("status"));
                int id = Integer.parseInt(request.getParameter("orderID"));

                List<OrderProduct> order = new ArrayList<>();
                OrderDAO oDAO = new OrderDAO();
                order = oDAO.UpdateStatusAndGetAllOrder(id, status);
                
                if (order == null) {
                    request.setAttribute("error", "Failed to update order status.");
                } else {
                    request.setAttribute("message", "Order status updated successfully!");
                }

                Account a = oDAO.getAccountByID(order.get(0).getOrderTotal().getAccount().getId());
                
                HttpSession session = request.getSession();
                session.setAttribute("orderList", order);
                if (a.getRole().equalsIgnoreCase("customer")) {
                    request.getRequestDispatcher("/OrderController/CustomerOrder/" + a.getId()).forward(request, response);
                } else {
                    request.getRequestDispatcher("/web/Staff/DisplayOrder.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                request.setAttribute("error", "Error updating order status: " + e.getMessage());
                response.sendRedirect("/OrderController/OrderManagement");
            }
        } else if (path.startsWith("/OrderController/PrepareOrder")) {
            List<CartForOrder> proList = new ArrayList<>();
            ProductDAO proDAO = new ProductDAO();
            OrderDAO oDAO = new OrderDAO();
            VoucherDao vDAO = new VoucherDao();
            List<Voucher> voucherList = new ArrayList<>();

            try {
                List<Voucher> allVouchers = vDAO.getAllVouchers();
                if (allVouchers == null) {
                    allVouchers = new ArrayList<>();
                }

                LocalDate today = LocalDate.now();
                for (Voucher voucher : allVouchers) {
                    boolean isDateValid = !today.isBefore(voucher.getStartDate())
                            && !today.isAfter(voucher.getEndDate());
                    boolean isQuantityValid = voucher.getUsedTime() < voucher.getQuantity();

                    if (isDateValid && isQuantityValid) {
                        voucherList.add(voucher);
                    }
                }

                if (voucherList.isEmpty()) {
                    request.setAttribute("message", "No valid vouchers available.");
                }

            } catch (Exception ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, "Error fetching vouchers", ex);
                request.setAttribute("error", "Unable to load voucher list.");
            }

            String[] productAndQuantity = request.getParameterValues("productId/Quantity");
            String userIDStr = request.getParameter("userID");

            if (productAndQuantity == null || productAndQuantity.length == 0 || userIDStr == null) {
                request.setAttribute("error", "Invalid cart or user data.");
                request.getRequestDispatcher("/web/error.jsp").forward(request, response);
                return;
            }

            try {
                int userID = Integer.parseInt(userIDStr);
                Account account = oDAO.getAccountByID(userID);
                if (account == null) {
                    request.setAttribute("error", "User not found.");
                    request.getRequestDispatcher("/web/error.jsp").forward(request, response);
                    return;
                }

                for (String item : productAndQuantity) {
                    String[] parts = item.split("/");
                    if (parts.length != 2) {
                        Logger.getLogger(OrderController.class.getName()).log(Level.WARNING, "Invalid productId/Quantity format: " + item);
                        continue;
                    }

                    try {
                        int productID = Integer.parseInt(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);

                        Product product = proDAO.getProductById(productID);
                        if (product == null || quantity <= 0) {
                            Logger.getLogger(OrderController.class.getName()).log(Level.WARNING, "Invalid product or quantity: " + item);
                            continue;
                        }

                        CartForOrder cartFO = new CartForOrder();
                        cartFO.setProduct(product);
                        cartFO.setQuantity(quantity);
                        cartFO.setAccount(account);
                        proList.add(cartFO);
                    } catch (NumberFormatException ex) {
                        Logger.getLogger(OrderController.class.getName()).log(Level.WARNING, "Invalid product ID or quantity: " + item, ex);
                    }
                }

                if (proList.isEmpty()) {
                    request.setAttribute("error", "Cart is empty or invalid.");
                    request.getRequestDispatcher("/web/error.jsp").forward(request, response);
                    return;
                }

                HttpSession session = request.getSession();
                session.setAttribute("cartOrder", proList);
                session.setAttribute("voucher", voucherList);

                request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);

            } catch (NumberFormatException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, "Invalid userID: " + userIDStr, ex);
                request.setAttribute("error", "Invalid user ID.");
                request.getRequestDispatcher("/web/error.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
                request.setAttribute("error", "System error occurred.");
                request.getRequestDispatcher("/web/error.jsp").forward(request, response);
            }
        } else if (path.startsWith("/OrderController/ConfirmOrder")) {
            HttpSession session = request.getSession();
            List<CartForOrder> proList = (List<CartForOrder>) session.getAttribute("cartOrder");

            if (proList == null || proList.isEmpty()) {
                session.setAttribute("message1", "Cart is empty!");
                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
                return;
            }

            String _phone = request.getParameter("phone");
            String _address = request.getParameter("address");
            String _note = request.getParameter("note");
            String _voucherCode = request.getParameter("voucher");
            VoucherDao vDAO = new VoucherDao();

            try {
                int voucherID = 0;
                if (_voucherCode != null && !_voucherCode.isEmpty()) {
                    Voucher voucher = vDAO.getVoucherByCode(_voucherCode);
                    if (voucher == null) {
                        session.setAttribute("message1", "Invalid voucher code!");
                        request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);
                        return;
                    }

                    LocalDate today = LocalDate.now();
                    boolean isDateValid = !today.isBefore(voucher.getStartDate()) && !today.isAfter(voucher.getEndDate());
                    boolean isQuantityValid = voucher.getUsedTime() < voucher.getQuantity();

                    if (!isDateValid) {
                        session.setAttribute("message1", "Voucher is not valid for the current date!");
                        request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);
                        return;
                    }
                    if (!isQuantityValid) {
                        session.setAttribute("message1", "Voucher has been used up!");
                        request.getRequestDispatcher("/web/orderProduct.jsp").forward(request, response);
                        return;
                    }
                    voucherID = voucher.getVoucherID();
                }

                String price = request.getParameter("priceTotal");
                int _priceTotal = 0;
                if (price.contains(".")) {
                    double value = Double.parseDouble(price);
                    _priceTotal = (int) value;
                } else {
                    _priceTotal = Integer.parseInt(price);
                }

                OrderDAO oDAO = new OrderDAO();
                java.util.Date today = new java.util.Date();
                java.sql.Date sqlDate = new java.sql.Date(today.getTime());
                OrderTotal ot = new OrderTotal(_phone, _address, _note, _priceTotal, sqlDate, 0, voucherID, proList.get(0).getAccount());
                List<Order> orderList = new ArrayList<>();

                for (CartForOrder cartItem : proList) {
                    Order o = new Order(cartItem.getProduct(), ot, cartItem.getQuantity(), cartItem.getProduct().getProPrice());
                    orderList.add(o);
                }

                if (oDAO.addNewOrder(ot, orderList)) {
                    session.setAttribute("message1", "https://img.vietqr.io/image/MB-0939303405-print.png?amount=" + String.valueOf(_priceTotal) + "&addInfo=" + proList.get(0).getAccount().getUsername() + "%20Price%20" + _priceTotal + "&accountName=LE%20NGUYEN%20TIEN%20DAT");
                    request.getRequestDispatcher("/payment.jsp").forward(request, response);
                } else {
                    session.setAttribute("message1", "Order placement failed! Please check product availability or voucher validity.");
                    request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
                }

            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("message1", "System error while processing order: " + ex.getMessage());
                request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            }
        } else if (path.endsWith("/CusUpdateOrder")) {
            try {
                int status = Integer.parseInt(request.getParameter("status"));
                int id = Integer.parseInt(request.getParameter("orderID"));

                List<OrderProduct> order = new ArrayList<>();
                OrderDAO oDAO = new OrderDAO();
                order = oDAO.UpdateStatusAndGetAllOrder(id, status);
                
                if (order == null) {
                    request.setAttribute("error", "Failed to update order status.");
                } else {
                    request.setAttribute("message", "Order status updated successfully!");
                }

                Account a = oDAO.getAccountByOrderID(id);
                
                HttpSession session = request.getSession();
                session.setAttribute("orderList", order);
                if (a.getRole().equalsIgnoreCase("customer")) {
                    response.sendRedirect("/OrderController/CustomerOrder/" + a.getId());
                } else {
                    request.getRequestDispatcher("/web/Staff/DisplayOrder.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                request.setAttribute("error", "Error updating order status: " + e.getMessage());
                response.sendRedirect("/OrderController/OrderManagement");
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}