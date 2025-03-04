<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Chi tiết đơn hàng</title>

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
                    Chi tiết đơn hàng #${order.product.productName}
                    <span class="order-status
                          <c:choose>
                              <c:when test="${order.orderTotal.orderState == 1}"> status-pending">Đang xử lý</c:when>
                              <c:when test="${order.orderTotal.orderState == 2}"> status-completed">Đã nhận hàng</c:when>
                              <c:otherwise> status-cancelled">Đã hủy</c:otherwise>
                          </c:choose>
                    </span>
                </h4>

                <p class="text-muted">Đặt lúc: <fmt:formatDate value="${order.orderTotal.date}" pattern="HH:mm dd/MM/yyyy"/></p>

                <!-- Thông tin nhận hàng -->
                <div class="info-box">
                    <h6>📦 THÔNG TIN NHẬN HÀNG</h6>
                    <p><strong>Người nhận:</strong> ${order.orderTotal.account.username} - ${order.orderTotal.phoneNumber}</p>
                    <p><strong>Nhận tại:</strong> ${order.orderTotal.address}</p>
                    <p><strong>Nhận lúc:</strong> Trước <fmt:formatDate value="${order.orderTotal.date}" pattern="HH:mm"/> - <fmt:formatDate value="${order.orderTotal.date}" pattern="EEEE (dd/MM)"/></p>
                </div>

                <!-- Hình thức thanh toán -->
                <div class="info-box">
                    <h6>💳 HÌNH THỨC THANH TOÁN</h6>
                    <p>Thanh toán khi nhận hàng</p>
                </div>

                <!-- Thông tin sản phẩm -->
                <div class="info-box">
                    <h6>🛒 THÔNG TIN SẢN PHẨM</h6>
                    <div class="d-flex">
                        <img src="images/${order.product.proImg}" alt="Sản phẩm" class="product-img me-3">
                        <div>
                            <p><strong>${order.product.productName}</strong></p>
                            <p class="text-success">📅 Bảo hành: Còn BH đến <fmt:formatDate value="${order.orderTotal.date}" pattern="dd/MM/yyyy"/></p>
                            <p>Số lượng: ${order.quantity}</p>
                            <p><del class="text-muted"><fmt:formatNumber value="${order.orderPrice}" type="currency" currencySymbol="đ"/></del> 
                                <span class="text-danger"><fmt:formatNumber value="${order.orderPrice}" type="currency" currencySymbol="đ"/></span></p>
                        </div>
                    </div>
                </div>

                <!-- Tổng tiền -->
                <div class="info-box">
                    <h6>💰 TỔNG TIỀN</h6>
                    <p>Tạm tính: <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/></p>
                    <p><strong class="price">Số tiền đã thanh toán: <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/></strong></p>
                </div>
            </c:forEach>
            <!-- Nút trở về -->
            <a href="orderList.jsp" class="btn btn-custom">VỀ TRANG DANH SÁCH ĐƠN HÀNG</a>

        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
