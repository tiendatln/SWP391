<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="Manager Order Page" />
    <meta name="author" content="Admin" />
    <title>Manager Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
        }
        
        .content-Order {
            padding: 30px;
            margin: 20px auto;
            max-width: 1200px;
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
            backdrop-filter: blur(4px);
            border: 1px solid rgba(255, 255, 255, 0.18);
        }

        h1 {
            color: #2c3e50;
            font-weight: 600;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
        }

        .order-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            padding: 20px 0;
        }

        .order-item {
            background: #ffffff;
            border: none;
            border-radius: 12px;
            padding: 20px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .order-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
        }

        .mb-2 {
            padding: 8px;
            border-radius: 5px;
            background: #f8f9fa;
            transition: all 0.2s ease;
        }

        .mb-2 strong {
            color: #34495e;
            margin-right: 5px;
        }

        .mb-2:hover {
            background: #e9ecef;
        }

        .btn {
            padding: 8px 20px;
            border-radius: 25px;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .btn-success {
            background: #27ae60;
            border: none;
        }

        .btn-success:hover {
            background: #219653;
            transform: scale(1.05);
        }

        .btn-danger {
            background: #e74c3c;
            border: none;
        }

        .btn-danger:hover {
            background: #c0392b;
            transform: scale(1.05);
        }

        .badge {
            padding: 6px 12px;
            font-size: 0.9em;
            border-radius: 20px;
            font-weight: 500;
        }

        .bg-warning {
            background: #f1c40f;
            color: #fff;
        }

        .bg-success {
            background: #2ecc71;
            color: #fff;
        }

        .bg-danger {
            background: #e74c3c;
            color: #fff;
        }

        .bg-secondary {
            background: #7f8c8d;
            color: #fff;
        }

        hr {
            border: 0;
            height: 2px;
            background: linear-gradient(to right, transparent, #3498db, transparent);
            margin: 30px 0;
        }

        @media (max-width: 768px) {
            .order-item {
                margin: 0 10px;
            }
            .content-Order {
                margin: 10px;
                padding: 15px;
            }
        }
    </style>
</head>

<body>
    <%@include file="../../Header.jsp" %>

    <div class="content-Order">
        <div class="p-4">
            <h1 class="mb-4 mt-4 text-center">Orders</h1>
            <div class="mt-5">
                <div class="row">
                    <div class="col-12 mx-auto">
                        <hr />
                        <c:choose>
                            <c:when test="${empty sessionScope.orderList}">
                                <p class="text-center" style="color: #7f8c8d; font-size: 1.2em;">No orders found</p>
                            </c:when>
                            <c:otherwise>
                                <div class="order-container">
                                    <c:forEach var="order" items="${sessionScope.orderList}" varStatus="status">
                                        <form action="/generateQR" method="post" class="order-item">
                                            <input type="hidden" name="orderID" value="${order.orderTotal.orderID}" />
                                            <div class="mb-2"><strong>Order ID:</strong> ${order.orderTotal.orderID}</div>
                                            <div class="mb-2">
                                                <strong>Product Name:</strong>
                                                ${order.productNames}
                                            </div>
                                            <div class="mb-2"><strong>Quantity:</strong> ${order.quantity}</div>
                                            <div class="mb-2"><strong>Total Price:</strong> 
                                                <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ" />
                                            </div>
                                            <div class="mb-2"><strong>Date:</strong> 
                                                <fmt:formatDate value="${order.orderTotal.date}" pattern="dd/MM/yyyy HH:mm" />
                                            </div>
                                            <div class="mb-2">
                                                <strong>Status:</strong>
                                                <c:choose>
                                                    <c:when test="${order.orderTotal.orderState == 0}">
                                                        <span class="badge bg-warning">Pending</span>
                                                        <button type="submit" class="btn btn-danger mt-2">Cancel</button>
                                                    </c:when>
                                                    <c:when test="${order.orderTotal.orderState == 1}">
                                                        <span class="badge bg-success">Completed</span>
                                                    </c:when>
                                                    <c:when test="${order.orderTotal.orderState == 2}">
                                                        <span class="badge bg-danger">Cancelled</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">Unknown</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div>
                                                <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-success">View Details</a>
                                            </div>
                                        </form>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%@include file="../../Footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>