<%-- 
    Document   : cart
    Created on : 25 Feb 2025, 09:51:38
    Author     : hnpt6504
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="Model.Cart" %>
<%@ page import="Model.CartItem" %>

<jsp:useBean id="cart" class="Model.Cart" scope="session"/>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

</head>
<body>
    <jsp:include page="../../Header.jsp" />
    <div class="container mt-4">
        <h2>Giỏ hàng của bạn</h2>
        
        <c:choose>
            <c:when test="${not empty cart.cartItems}">
                <table class="table table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>Tên sản phẩm</th>
                            <th>Hình ảnh</th>
                            <th>Số lượng</th>
                            <th>Giá</th>
                            <th>Tổng</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cart.cartItems}">
                            <tr>
                                <td>
                                    <input type="checkbox">
                                </td>
                                <td>${item.product.productName}</td>
                                <td>
                                    <img src="images/${item.product.proImg}" alt="${item.product.productName}" width="50">
                                </td>
                                <td>
                                    <form action="CartController" method="post" class="d-flex">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="productId" value="${item.product.productID}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" class="form-control w-50 me-2">
                                        <button type="submit" class="btn btn-primary">Cập nhật</button>
                                    </form>
                                </td>
                                <td><fmt:formatNumber value="${item.product.proPrice}" type="currency" currencySymbol="đ"/></td>
                                <td><fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="đ"/></td>
                                <td>
                                    <form action="CartController" method="post">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="productId" value="${item.product.productID}">
                                        <button type="submit" class="btn btn-danger">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="text-end">
                    <h4>Tổng tiền: 
                        <strong><fmt:formatNumber value="${cart.totalAmount}" type="currency" currencySymbol="đ"/></strong>
                    </h4>
                    <a href="checkout.jsp" class="btn btn-success">Thanh toán</a>
                </div>
            </c:when>
            <c:otherwise>
                <p class="alert alert-warning">Giỏ hàng của bạn đang trống!</p>
            </c:otherwise>
        </c:choose>
    </div>
    <div ><jsp:include page="../../Footer.jsp" />
</div>
</body>
</html>
