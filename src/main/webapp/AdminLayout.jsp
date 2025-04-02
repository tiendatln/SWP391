<%-- 
    Document   : AdminLayout
    Created on : Feb 17, 2025, 12:39:58 AM
    Author     : tiend
--%>
<%
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

                    if (!userName.isEmpty() && "customer".equals(userRole)) {
                      response.sendRedirect("/web/index.jsp");  
                    } else if ("admin".equals(userRole)) {
                        
                    }
                }
            }
        }
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            .sidebar {
                width: 250px;
                background: #FF9900;
                color: white;
                padding: 20px;
                height: 100vh;
                width: 250px;
                font-family: Arial, sans-serif;
                position: fixed;

                border-right: 2px solid #FFD700;

                flex-direction: column;
                align-items: center;
            }
            .dashboard {

                font-size: 20px;
                font-weight: bold;
                margin-bottom: 20px;
                text-align: center;
            }
            .sidebar ul {
                list-style: none;
                padding: 0;
            }
            .sidebar ul li {
                margin: 15px 0;
            }
            .sidebar ul li a {
                display: block;
                text-decoration: none;
                color: white;
                padding: 10px;
                background: #34495e;
                border-radius: 5px;
                text-align: center;
                transition: 0.3s;
            }
            .sidebar ul li a:hover {
                background: #1abc9c;
            }
        </style>
    </head>
    <body>
        <div class="sidebar">
            <div class="dashboard"><a href="/web/dashboard.jsp">Dashboard</a></div>
            <ul>
                <li><a href="/AccountController/AccountList" class="btn">Account</a></li>
                <li><a href="/OrderController/OrderManagement" class="btn">Order</a></li>
                <li><a href="/ProductController/ProductManagement" class="btn">Products</a></li>                
                <li><a href="/VoucherController/Voucher" class="btn">Voucher</a></li>
                <li><a href="/LoginController/Logout" class="btn">Logout</a></li>
            </ul>
        </div>
    </body>
</html>
