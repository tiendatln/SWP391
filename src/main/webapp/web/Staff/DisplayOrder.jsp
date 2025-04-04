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
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h3>Manage Orders</h3>
                                        <div class="input-group" style="max-width: 300px;">
                                            <input type="text" id="orderSearch" class="form-control" placeholder="Search orders...">
                                            <button class="btn btn-primary" onclick="searchOrders()"><i class="bi bi-search"></i></button>
                                        </div>
                                    </div>
                                    <hr />

                                    <!-- Bootstrap Tabs -->
                                    <ul class="nav nav-tabs" id="orderTabs" role="tablist">
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

                                    <!-- Tab Content -->
                                    <div class="tab-content" id="orderTabContent">
                                        <!-- All Orders Tab -->
                                        <div class="tab-pane fade show active" id="all" role="tabpanel">
                                            <table class="table table-bordered" id="orderTableAll">
                                                <thead>
                                                    <tr>
                                                        <th>Order ID</th>
                                                        <th>Product Name</th>
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
                                                                <td class="searchable">${order.orderTotal.orderID}</td>
                                                                <td class="searchable">${order.productNames}</td>
                                                                <td class="searchable">${order.quantity}</td>
                                                                <td class="searchable">${order.orderTotal.totalPrice} đ</td>
                                                                <td class="searchable">${order.orderTotal.date}</td>
                                                                <td class="searchable">
                                                                    <c:choose>
                                                                        <c:when test="${order.orderTotal.orderState == 0}">
                                                                            <span class="badge bg-warning">Pending</span>
                                                                            <input type="radio" id="statusCancelled_${order.orderTotal.orderID}" name="status" value="2">
                                                                            <label for="statusCancelled_${order.orderTotal.orderID}">Cancel</label>
                                                                            <input type="radio" id="statusCompleted_${order.orderTotal.orderID}" name="status" value="1">
                                                                            <label for="statusCompleted_${order.orderTotal.orderID}">Complete</label>
                                                                            <button type="submit" class="btn btn-primary btn-sm">Update</button>
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

                                        <!-- Pending Orders Tab -->
                                        <div class="tab-pane fade" id="pending" role="tabpanel">
                                            <table class="table table-bordered" id="orderTablePending">
                                                <thead>
                                                    <tr>
                                                        <th>Order ID</th>
                                                        <th>Product Name</th>
                                                        <th>Quantity</th>
                                                        <th>Total Price</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="order" items="${sessionScope.orderList}">
                                                        <c:if test="${order.orderTotal.orderState == 0}">
                                                            <tr>
                                                        <form action="/OrderController/UpdateOrder" method="post">
                                                            <input type="hidden" name="orderID" value="${order.orderTotal.orderID}" />
                                                            <td class="searchable">${order.orderTotal.orderID}</td>
                                                            <td class="searchable">${order.productNames}</td>
                                                            <td class="searchable">${order.quantity}</td>
                                                            <td class="searchable">${order.orderTotal.totalPrice} đ</td>
                                                            <td class="searchable">${order.orderTotal.date}</td>
                                                            <td class="searchable">
                                                                <span class="badge bg-warning">Pending</span>
                                                                <input type="radio" id="statusCancelled_${order.orderTotal.orderID}" name="status" value="2">
                                                                <label for="statusCancelled_${order.orderTotal.orderID}">Cancel</label>
                                                                <input type="radio" id="statusCompleted_${order.orderTotal.orderID}" name="status" value="1">
                                                                <label for="statusCompleted_${order.orderTotal.orderID}">Complete</label>
                                                                <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                                            </td>
                                                            <td>
                                                                <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-success btn-sm">View</a>
                                                            </td>
                                                        </form>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                        <!-- Completed Orders Tab -->
                                        <div class="tab-pane fade" id="completed" role="tabpanel">
                                            <table class="table table-bordered" id="orderTableCompleted">
                                                <thead>
                                                    <tr>
                                                        <th>Order ID</th>
                                                        <th>Product Name</th>
                                                        <th>Quantity</th>
                                                        <th>Total Price</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="order" items="${sessionScope.orderList}">
                                                        <c:if test="${order.orderTotal.orderState == 1}">
                                                            <tr>
                                                        <form action="/OrderController/UpdateOrder" method="post">
                                                            <input type="hidden" name="orderID" value="${order.orderTotal.orderID}" />
                                                            <td class="searchable">${order.orderTotal.orderID}</td>
                                                            <td class="searchable">${order.productNames}</td>
                                                            <td class="searchable">${order.quantity}</td>
                                                            <td class="searchable">${order.orderTotal.totalPrice} đ</td>
                                                            <td class="searchable">${order.orderTotal.date}</td>
                                                            <td class="searchable">
                                                                <span class="badge bg-success">Completed</span>
                                                            </td>
                                                            <td>
                                                                <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-success btn-sm">View</a>
                                                            </td>
                                                        </form>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                        <!-- Cancelled Orders Tab -->
                                        <div class="tab-pane fade" id="cancelled" role="tabpanel">
                                            <table class="table table-bordered" id="orderTableCancelled">
                                                <thead>
                                                    <tr>
                                                        <th>Order ID</th>
                                                        <th>Product Name</th>
                                                        <th>Quantity</th>
                                                        <th>Total Price</th>
                                                        <th>Date</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="order" items="${sessionScope.orderList}">
                                                        <c:if test="${order.orderTotal.orderState == 2}">
                                                            <tr>
                                                        <form action="/OrderController/UpdateOrder" method="post">
                                                            <input type="hidden" name="orderID" value="${order.orderTotal.orderID}" />
                                                            <td class="searchable">${order.orderTotal.orderID}</td>
                                                            <td class="searchable">${order.productNames}</td>
                                                            <td class="searchable">${order.quantity}</td>
                                                            <td class="searchable">${order.orderTotal.totalPrice} đ</td>
                                                            <td class="searchable">${order.orderTotal.date}</td>
                                                            <td class="searchable">
                                                                <span class="badge bg-danger">Cancelled</span>
                                                            </td>
                                                            <td>
                                                                <a href="/OrderController/OrderDetail/${order.orderTotal.orderID}" class="btn btn-success btn-sm">View</a>
                                                            </td>
                                                        </form>
                                                        </tr>
                                                    </c:if>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <script>
                                function searchOrders() {
                                    let input = document.getElementById("orderSearch").value.toLowerCase();
                                    let tables = [
                                        'orderTableAll',
                                        'orderTablePending',
                                        'orderTableCompleted',
                                        'orderTableCancelled'
                                    ];

                                    tables.forEach(tableId => {
                                        let table = document.getElementById(tableId);
                                        if (table) {
                                            let rows = table.querySelectorAll("tbody tr");
                                            rows.forEach(row => {
                                                let searchableFields = row.querySelectorAll(".searchable");
                                                let found = false;

                                                searchableFields.forEach(field => {
                                                    if (field.textContent.toLowerCase().includes(input)) {
                                                        found = true;
                                                    }
                                                });

                                                // Sửa lỗi cú pháp: đảm bảo hiển thị/ẩn row chính xác
                                                row.style.display = found ? "" : "none";
                                            });
                                        }
                                    });
                                }

                                // Optional: Thêm sự kiện để search khi nhấn Enter
                                document.getElementById("orderSearch").addEventListener("keypress", function (event) {
                                    if (event.key === "Enter") {
                                        searchOrders();
                                    }
                                });
                            </script>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>