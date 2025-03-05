<%-- 
    Document   : AdminLayout
    Created on : Feb 17, 2025, 12:39:58 AM
    Author     : tiend
--%>

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
            <div class="dashboard">Dashboard</div>
            <ul>
                <li><a href="/OrderController/OrderManagement" class="btn">Order</a></li>
                <li><a href="/ProductController/ProductManagement" class="btn">Products</a></li>
                <li><a href="/ProductController/UpdateQuantity" class="btn">Update Quantity</a></li>
                <li><a href="/VoucherController/Voucher" class="btn">Voucher</a></li>
                <li><a href="/LoginController/Logout" class="btn">Logout</a></li>
            </ul>
        </div>
    </body>
</html>
