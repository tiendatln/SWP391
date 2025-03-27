<%@page import="Model.Order"%>
<%@page import="Model.OrderTotal"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>Order Confirmation</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <jsp:include page="../../Header.jsp" />
    <div class="cart-container">
        <h2 class="cart-title">Order Confirmation</h2>
        
        <%
            OrderTotal orderTotal = (OrderTotal) session.getAttribute("newOrderTotal");
            List<Order> orderItems = (List<Order>) session.getAttribute("newOrderItems");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            if (orderTotal != null && orderItems != null) {
        %>
        <div>
            <h4>Order Information</h4>
            <p><strong>Order ID:</strong> <%= orderTotal.getOrderID() %></p>
            <p><strong>Phone Number:</strong> <%= orderTotal.getPhoneNumber() %></p>
            <p><strong>Address:</strong> <%= orderTotal.getAddress() %></p>
            <p><strong>Note:</strong> <%= orderTotal.getNote() != null ? orderTotal.getNote() : "None" %></p>
            <p><strong>Order Date:</strong> <%= sdf.format(orderTotal.getDate()) %></p>
            <p><strong>Total Amount:</strong> <span class="cart-item-total"><%= String.format("%,d", orderTotal.getTotalPrice()) %> đ</span></p>
        </div>

        <table class="cart-table">
            <thead class="cart-table-dark">
                <tr>
                    <th>Product Name</th>
                    <th>Image</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <% for (Order item : orderItems) { %>
                <tr>
                    <td><%= item.getProduct().getProductName() %></td>
                    <td><img src="/link/img/<%= item.getProduct().getProImg() %>" alt="<%= item.getProduct().getProductName() %>" width="100px" height="100px"></td>
                    <td><%= item.getQuantity() %></td>
                    <td class="cart-item-price"><%= String.format("%,d", item.getProduct().getProPrice()) %> đ</td>
                    <td class="cart-item-total"><%= String.format("%,d", item.getOrderPrice()) %> đ</td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <div class="cart-text-end">
            <a href="/index.jsp" class="cart-btn cart-btn-success">Back to Homepage</a>
        </div>
        <% 
            session.removeAttribute("newOrderTotal");
            session.removeAttribute("newOrderItems");
            session.removeAttribute("selectedItems");
            session.removeAttribute("totalPrice");
            } else { 
        %>
        <p class="cart-alert cart-alert-warning">No order information to display!</p>
        <% } %>
    </div>
    <div style="background-color: rgb(31, 41, 55)">
        <jsp:include page="../../Footer.jsp" />
    </div>
</body>
</html>