<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="utf-8" />
        <title>Order Manager</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f1f1f1;
            }

            .order-container {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
                gap: 20px;
                margin-top: 20px;
            }

            .order-item {
                background: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }

            .badge {
                padding: 6px 12px;
                border-radius: 20px;
                font-weight: 500;
            }

            .nav-tabs .nav-link.active {
                background-color: #0d6efd;
                color: white;
            }

            h1 {
                text-align: center;
                margin-top: 40px;
                font-weight: bold;
                color: #2c3e50;
            }
        </style>
    </head>
    <body>
        <%@include file="../../Header.jsp" %>

        <div class="container mt-4">
            <h1>Order Manager</h1>

            <!-- Tab navigation -->
            <ul class="nav nav-tabs mt-4" id="orderTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="all-tab" data-bs-toggle="tab" data-bs-target="#all" type="button" role="tab">All</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="pending-tab" data-bs-toggle="tab" data-bs-target="#pending" type="button" role="tab">Pending</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="completed-tab" data-bs-toggle="tab" data-bs-target="#completed" type="button" role="tab">Completed</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="cancelled-tab" data-bs-toggle="tab" data-bs-target="#cancelled" type="button" role="tab">Cancelled</button>
                </li>
            </ul>

            <!-- Tab content -->
            <div class="tab-content" id="orderTabsContent">
                <!-- All -->
                <div class="tab-pane fade show active" id="all" role="tabpanel">
                    <div class="order-container">
                        <c:forEach var="order" items="${sessionScope.orderList}">
                            <div class="order-item">
                                <div><strong>Order ID:</strong> ${order.orderTotal.orderID}</div>
                                <div><strong>Product:</strong> ${order.productNames}</div>
                                <div><strong>Quantity:</strong> ${order.quantity}</div>
                                <div><strong>Total:</strong> 
                                    <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/>
                                </div>
                                <div><strong>Date:</strong> 
                                    <fmt:formatDate value="${order.orderTotal.date}" pattern="dd/MM/yyyy HH:mm"/>
                                </div>
                                <div><strong>Status:</strong>
                                    <c:choose>
                                        <c:when test="${order.orderTotal.orderState == 0}">
                                            <span class="badge bg-warning">Pending</span>
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
                                <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-primary mt-2">View Details</a>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Pending -->
                <!-- Pending -->
                <div class="tab-pane fade" id="pending" role="tabpanel">
                    <div class="order-container">
                        <c:forEach var="order" items="${sessionScope.orderList}">
                            <c:if test="${order.orderTotal.orderState == 0}">
                                <form action="/OrderController/CusUpdateOrder" method="post" class="order-item">
                                    <div><strong>Order ID:</strong> ${order.orderTotal.orderID}</div>
                                    <div><strong>Product:</strong> ${order.productNames}</div>
                                    <div><strong>Quantity:</strong> ${order.quantity}</div>
                                    <div><strong>Total:</strong> 
                                        <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/>
                                    </div>
                                    <div><strong>Date:</strong> 
                                        <fmt:formatDate value="${order.orderTotal.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </div>
                                    <div><span class="badge bg-warning">Pending</span></div>

                                    <!-- Nút Cancel -->
                                    <input type="hidden" name="status" value="2" />
                                    <input type="hidden" name="orderID" value="${order.orderTotal.orderID}" />
                                    <button type="submit" class="btn btn-danger mt-2 me-2">Cancel</button>

                                    <!-- Nút View Details -->
                                    <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-warning mt-2">View Details</a>
                                </form>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>


                <!-- Completed -->
                <div class="tab-pane fade" id="completed" role="tabpanel">
                    <div class="order-container">
                        <c:forEach var="order" items="${sessionScope.orderList}">
                            <c:if test="${order.orderTotal.orderState == 1}">
                                <div class="order-item">
                                    <div><strong>Order ID:</strong> ${order.orderTotal.orderID}</div>
                                    <div><strong>Product:</strong> ${order.productNames}</div>
                                    <div><strong>Quantity:</strong> ${order.quantity}</div>
                                    <div><strong>Total:</strong> 
                                        <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/>
                                    </div>
                                    <div><strong>Date:</strong> 
                                        <fmt:formatDate value="${order.orderTotal.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </div>
                                    <div><span class="badge bg-success">Completed</span></div>
                                    <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-success mt-2">View Details</a>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>

                <!-- Cancelled -->
                <div class="tab-pane fade" id="cancelled" role="tabpanel">
                    <div class="order-container">
                        <c:forEach var="order" items="${sessionScope.orderList}">
                            <c:if test="${order.orderTotal.orderState == 2}">
                                <div class="order-item">
                                    <div><strong>Order ID:</strong> ${order.orderTotal.orderID}</div>
                                    <div><strong>Product:</strong> ${order.productNames}</div>
                                    <div><strong>Quantity:</strong> ${order.quantity}</div>
                                    <div><strong>Total:</strong> 
                                        <fmt:formatNumber value="${order.orderTotal.totalPrice}" type="currency" currencySymbol="đ"/>
                                    </div>
                                    <div><strong>Date:</strong> 
                                        <fmt:formatDate value="${order.orderTotal.date}" pattern="dd/MM/yyyy HH:mm"/>
                                    </div>
                                    <div><span class="badge bg-danger">Cancelled</span></div>
                                    <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-danger mt-2">View Details</a>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="../../Footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
