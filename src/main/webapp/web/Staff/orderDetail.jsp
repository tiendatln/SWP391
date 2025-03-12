<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Order Details</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: Arial, sans-serif;
            }

            .container {
                margin-top: 30px;
                max-width: 800px;
                background: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            }

            .order-status {
                font-weight: bold;
                font-size: 18px;
            }

            .status-completed {
                color: green;
            }

            .status-pending {
                color: orange;
            }

            .status-cancelled {
                color: red;
            }

            .info-box {
                background: #f8f9fa;
                padding: 10px;
                border-radius: 8px;
                margin-bottom: 10px;
            }

            .product-img {
                width: 80px;
                height: auto;
                border-radius: 5px;
            }

            .btn-custom {
                background: #ff5722;
                color: white;
                font-weight: bold;
                border: none;
                padding: 10px;
                border-radius: 5px;
                text-align: center;
                width: 100%;
                margin-top: 20px;
                display: block;
                text-decoration: none;
            }

            .btn-custom:hover {
                background: #e64a19;
                color: white;
            }

            .price {
                font-weight: bold;
                font-size: 20px;
                color: red;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <c:forEach var="order" items="${orderDetails}">
                <h4 class="text-center">
                    Order Details #${order.product.productName}
                    <span class="order-status
                          <c:choose>
                              <c:when test="${order.orderTotal.orderState == 1}"> status-pending">Processing</c:when>
                              <c:when test="${order.orderTotal.orderState == 2}"> status-completed">Delivered</c:when>
                              <c:otherwise> status-cancelled">Cancelled</c:otherwise>
                          </c:choose>
                    </span>
                </h4>

                <p class="text-muted">Order Time: <fmt:formatDate value="${order.orderTotal.date}" pattern="HH:mm dd/MM/yyyy"/></p>

                <!-- Shipping Information -->
                <div class="info-box">
                    <h6>📦 SHIPPING INFORMATION</h6>
                    <p><strong>Recipient:</strong> ${order.orderTotal.account.username} - ${order.orderTotal.phoneNumber}</p>
                    <p><strong>Address:</strong> ${order.orderTotal.address}</p>
                </div>

                <!-- Product Information -->
                <div class="info-box">
                    <h6>🛒 PRODUCT INFORMATION</h6>
                    <div class="d-flex">
                        <img src="images/${order.product.proImg}" alt="Product" class="product-img me-3">
                        <div>
                            <p><strong>${order.product.productName}</strong></p>
                            <p>Quantity: ${order.quantity}</p>
                            <p><del class="text-muted"><fmt:formatNumber value="${order.orderPrice}" type="currency" currencySymbol="đ"/></del> 
                                <span class="text-danger"><fmt:formatNumber value="${order.orderPrice}" type="currency" currencySymbol="đ"/></span></p>
                        </div>
                    </div>
                </div>

                <!-- Total Price -->
                <div class="info-box">
                    <h6>💰 TOTAL PRICE</h6>
                    <p>Subtotal: <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/></p>
                    <p><strong class="price">Amount Paid: <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/></strong></p>
                </div>
            </c:forEach>
            <!-- Back Button -->
            <a href="orderList.jsp" class="btn btn-custom">BACK TO ORDER LIST</a>

        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
