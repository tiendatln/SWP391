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
                margin: 0;
                padding: 0;
            }

            .order-container {
                max-width: 800px;
                margin: 30px auto;
                background: #ffffff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            .order-header {
                margin-bottom: 20px;
            }

            .status-badge {
                font-weight: bold;
                font-size: 18px;
            }

            .status-completed {
                color: #28a745; /* Màu xanh đậm hơn cho trạng thái hoàn thành */
            }

            .status-pending {
                color: #ff9800; /* Màu cam đậm hơn cho trạng thái đang xử lý */
            }

            .status-cancelled {
                color: #dc3545; /* Màu đỏ đậm hơn cho trạng thái hủy */
            }

            .section-card {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 15px;
            }

            .product-img {
                width: 80px;
                height: auto;
                border-radius: 5px;
                object-fit: cover; /* Đảm bảo ảnh không bị méo */
            }

            .btn-back {
                display: block;
                background: #ff5722;
                color: #ffffff;
                font-weight: bold;
                text-align: center;
                padding: 10px;
                border-radius: 5px;
                text-decoration: none;
                transition: background 0.3s ease; /* Thêm hiệu ứng chuyển màu mượt */
            }

            .btn-back:hover {
                background: #e64a19;
                color: #ffffff;
            }

            .price {
                font-weight: bold;
                font-size: 20px;
                color: #dc3545; /* Đồng nhất màu đỏ với status-cancelled */
            }

            .total-section {
                padding: 15px;
                background: #f8f9fa;
                border-radius: 8px;
            }

            .text-muted {
                font-size: 14px;
            }

            h6 {
                color: #333;
                margin-bottom: 15px;
            }

            /* Responsive adjustments */
            @media (max-width: 576px) {
                .order-container {
                    margin: 15px;
                    padding: 15px;
                }

                .product-img {
                    width: 60px;
                }

                .price {
                    font-size: 18px;
                }
            }
        </style>
    </head>
    <body>

        <div class="order-container">
            <c:forEach var="order" items="${orderDetails}" varStatus="loop">
                <c:if test="${loop.first}">
                    <c:set var="accountId" value="${order.orderTotal.account.id}" scope="request" />
                    <c:set var="role" value="${order.orderTotal.account.role}" scope="request" />
                    <div class="order-header">
                        <h4 class="d-flex justify-content-between align-items-center">
                            <span>Order #${order.orderTotal.orderID}</span>
                            <span class="status-badge
                                  <c:choose>
                                      <c:when test="${order.orderTotal.orderState == 0}">status-pending">Pending</c:when>
                                      <c:when test="${order.orderTotal.orderState == 1}">status-completed">Completed</c:when>
                                      <c:otherwise>status-cancelled">Cancelled</c:otherwise>
                                  </c:choose>
                            </span>
                        </h4>
                        <small class="text-muted">
                            Placed on: <fmt:formatDate value="${order.orderTotal.date}" pattern="dd MMM yyyy, HH:mm"/>
                        </small>
                    </div>
                    <!-- Shipping Information -->
                    <div class="section-card">
                        <h6 class="mb-3">📦 Shipping Details</h6>
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Recipient:</strong> ${order.orderTotal.account.username}</p>
                                <p><strong>Phone:</strong> ${order.orderTotal.phoneNumber}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Address:</strong> ${order.orderTotal.address}</p>
                            </div>
                        </div>
                    </div>
                </c:if>



                <!-- Product Information -->
                <div class="section-card">
                    <h6 class="mb-3">🛒 Items Ordered</h6>
                    <div class="d-flex align-items-center mb-3">
                        <img src="/link/img/${order.product.proImg}" alt="${order.product.productName}" class="product-img me-3">
                        <div class="flex-grow-1">
                            <p class="mb-1"><strong>${order.product.productName}</strong></p>
                            <p class="mb-1">Quantity: ${order.quantity}</p>
                            <p class="mb-0">
                                <span class="text-danger">
                                    <fmt:formatNumber value="${order.orderPrice}" type="currency" currencySymbol="đ"/>
                                </span>
                                <c:if test="${order.orderPrice < order.product.proPrice}">
                                    <del class="text-muted ms-2">
                                        <fmt:formatNumber value="${order.product.originalPrice}" type="currency" currencySymbol="đ"/>
                                    </del>
                                </c:if>
                            </p>
                        </div>
                    </div>
                </div>

                <c:if test="${loop.last}">
                    <!-- Total Price -->
                    <div class="total-section">
                        <h6 class="mb-3">💰 Order Summary</h6>
                        <div class="d-flex justify-content-between">
                            <span>Subtotal:</span>
                            <span><fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/></span>
                        </div>
                        <div class="d-flex justify-content-between mt-2 fw-bold">
                            <span>Total:</span>
                            <span class="text-primary">
                                <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/>
                            </span>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
            <c:choose>
                <c:when test="${role == 'customer'}">
                    <a href="/OrderController/CustomerOrder/${accountId}" class="btn-back mt-4">Back to Orders</a>
                </c:when>
                <c:when test="${role == 'admin'}">
                    <a href="/OrderController/OrderManagement/" class="btn-back mt-4">Back to Orders</a>
                </c:when>
            </c:choose>


        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
