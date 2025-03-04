<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Giả lập giỏ hàng nếu chưa có --%>
<c:if test="${empty sessionScope.cart}">
    <c:set var="cart" value="iPhone 15,999.99,1,1" scope="session"/>
</c:if>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Giỏ hàng</title>
        <style>
            /* Reset CSS */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: Arial, sans-serif;
            }

            /* Container */
            .main {
                width: 80%;
                margin: 20px auto;
                min-height: 62.7vh;
            }

            h2 {
                margin-bottom: 15px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: white;
            }

            table, th, td {
                border: 1px solid #ddd;
                text-align: center;
            }

            th {
                background: #f7c708;
                text-align: center;
                vertical-align: middle;
            }

            .delete-btn {
                background: red;
                color: white;
                padding: 5px 10px;
                text-decoration: none;
                border-radius: 5px;
            }

            .update-btn {
                background: green;
                color: white;
                padding: 5px 10px;
                text-decoration: none;
                border-radius: 5px;
            }

            .continue-btn {
                display: inline-block;
                margin-top: 20px;
                padding: 10px 15px;
                background: #007bff;
                color: white;
                text-decoration: none;
                border-radius: 5px;
            }

            input[type="number"] {
                width: 50px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div>
            <jsp:include page="../../Header.jsp"/>
        </div>
        <div class="container main">
            <h2>Giỏ hàng của bạn</h2>

            <c:choose>
                <c:when test="${empty sessionScope.cart}">
                    <p>Giỏ hàng trống.</p>
                </c:when>
                <c:otherwise>
                    <c:set var="cartItems" value="${fn:split(sessionScope.cart, ',')}" />
                    <table>
                        <tr>
                            <th>Sản phẩm</th>
                            <th>Giá</th>
                            <th>Số lượng</th>
                            <th>Thành tiền</th>
                            <th>Hành động</th>
                        </tr>
                        <tr>
                            <td>${cartItems[0]}</td> <!-- Tên sản phẩm -->
                            <td>$${cartItems[1]}</td> <!-- Giá -->
                            <td>
                                <form action="CartServlet" method="get">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="id" value="${cartItems[2]}">

                                    <c:choose>
                                        <c:when test="${empty cartItems[3] or cartItems[3] lt 1}">
                                            <c:set var="quantity" value="1"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="quantity" value="${cartItems[3]}"/>
                                        </c:otherwise>
                                    </c:choose>

                                    <input type="number" name="quantity" value="${quantity}" min="1">
                                    <button type="submit" class="update-btn">Cập nhật</button>
                                </form>
                            </td>
                            <td>$${cartItems[1] * quantity}</td> <!-- Thành tiền -->
                            <td>
                                <a class="delete-btn" href="CartServlet?action=remove&id=${cartItems[2]}">Xóa</a>
                            </td>
                        </tr>
                    </table>
                    <p><b>Tổng tiền:</b> $${cartItems[1] * quantity}</p>
                </c:otherwise>
            </c:choose>

            <a class="continue-btn" href="index.jsp">Back</a>
        </div>
        <div>
            <jsp:include page="../../Footer.jsp"/>
        </div>
    </body>
</html>
