<%@page contentType="text/html" pageEncoding="UTF-8" %>
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
        <style>
            body {
        display: flex;
        min-height: 100vh;
        margin: 0;
        font-family: "Poppins", sans-serif;
        background-color: #f8f9fa;
    }
    .content {
        margin-left: 250px;
        padding: 20px;
        flex-grow: 1;
        width: calc(100% - 250px);
        background: white;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
    }
    .filter-bar {
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 10px;
        background: white;
        border-radius: 5px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        margin-bottom: 20px;
    }
    .filter-bar select,
    .filter-bar button {
        padding: 8px 12px;
        border: 1px solid #ced4da;
        border-radius: 5px;
        font-size: 14px;
        transition: all 0.3s;
    }
    .filter-bar select:focus,
    .filter-bar button:hover {
        border-color: #007bff;
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }
    .order-container {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
    }
    .order-item {
        flex: 1 1 calc(50% - 15px);
        border: 1px solid #ddd;
        padding: 15px;
        background: white;
        border-radius: 10px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }
        </style>
    </head>

    <body>

        <%@include file="../../AdminLayout.jsp" %>

        <!-- Main Content -->
        <div class="content">
            <div class="p-4">
                <h1 class="mb-4 mt-4 text-center" style="font-weight: bold;">Manage Order</h1>

                <div class="mt-5">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <hr />
                            
                            
                            
                            
       <div class="row">
    <div class="col-12 mx-auto">
        <div class="d-flex">
            <h3>Manage Orders</h3>
        </div>
        <hr />
        <table class="table table-bordered">
    <thead>
        <tr>
            <th>Order ID</th>
            <th>Image</th>
            <th>Quantity</th>
            <th>Total Price</th>
            <th>Date</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty sessionScope.orderList}">
                <tr>
                    <td colspan="7" class="text-center">No orders found</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="order" items="${sessionScope.orderList}">
                    <tr>
                        <form action="/OrderController/UpdateOrder" method="post">
                            <input type="hidden" name="orderID" value="${order.orderTotal.orderID}" />
                            <td>${order.orderTotal.orderID}</td>
                            <td>
                                <img src="/link/img/${order.product.proImg}" width="50" height="50" class="rounded" />
                            </td>
                            <td>${order.quantity}</td>
                            <td>${order.orderTotal.totalPrice} đ</td>
                            <td>${order.orderTotal.date}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.orderTotal.orderState == 0}">
                                        <span class="badge bg-warning">Pending</span>
                                        <div>
                                            <input type="radio" id="statusCancelled_${order.orderTotal.orderID}" name="status" value="2">
                                            <label for="statusCancelled_${order.orderTotal.orderID}">Cancel</label>
                                            <input type="radio" id="statusCompleted_${order.orderTotal.orderID}" name="status" value="1">
                                            <label for="statusCompleted_${order.orderTotal.orderID}">Complete</label>
                                        </div>
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
                            </td>
                            <td>
                                <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-success btn-sm">View</a>
                            </td>
                        </form>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

    </div>
</div>

                            
                            
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>
