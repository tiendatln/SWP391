<%@ page contentType="text/html; charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="Manager Order Page" />
        <meta name="author" content="Admin" />
        <title>Manager Order</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <style>
/*            body {
                display: flex;
                min-height: 100vh;
                margin: 0;
                font-family: Arial, sans-serif;
            }
            .content {
                margin-left: 250px;
                padding: 20px;
                flex-grow: 1;
                width: calc(100% - 250px);
            }

            .filter-bar select,
            .filter-bar button {
                margin-right: 10px;
            }

            .filter-bar {
                margin-bottom: 20px;
            }*/
        </style>
    </head>

    <body>

        <%@include file="../../Header.jsp" %>

        <!-- Main Content -->
        <div class="content">
            <div class="p-4">
                <h1 class="mb-4 mt-4 text-center" style="font-weight: bold;">Manage Order</h1>

                <div class="mt-5">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <div class="d-flex">
                                <h3>Table Orders</h3>
                            </div>

                            <hr />
                            <table class="table table-bordered table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>User</th>
                                        <th>Total Price</th>
                                        <th>Date</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="order" items="${sessionScope.orderList}">
                                        <tr>
                                            <td>${order.id}</td>
                                            <td>${order.user}</td>
                                            <td>${order.totalPrice} đ</td>
                                            <td>${order.date}</td>
                                            <td>${order.status}</td>
                                            <td>
                                                <button onclick="showOrderDetails('${order.id}', '${order.user}', '${order.totalPrice}', '${order.date}', '${order.status}')" class="btn btn-success">View</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>


            </div>
        </div>
        <!-- Popup Modal (Sử dụng Bootstrap Modal chuẩn) -->
        <div class="modal fade" id="orderModal" tabindex="-1" aria-labelledby="orderModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="orderModalLabel">Order Details</h4>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><b>ID:</b> <span id="orderId"></span></p>
                        <p><b>User:</b> <span id="orderUser"></span></p>
                        <p><b>Total Price:</b> <span id="orderTotalPrice"></span></p>
                        <p><b>Date:</b> <span id="orderDate"></span></p>
                        <p><b>Status:</b> <span id="orderStatus"></span></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- JavaScript sửa lỗi popup -->
        <script>
            function showOrderDetails(orderId, user, totalPrice, date, status) {
                document.getElementById("orderId").innerText = orderId;
                document.getElementById("orderUser").innerText = user;
                document.getElementById("orderTotalPrice").innerText = totalPrice;
                document.getElementById("orderDate").innerText = date;
                document.getElementById("orderStatus").innerText = status;

                // Hiển thị modal bằng Bootstrap
                var modal = new bootstrap.Modal(document.getElementById('orderModal'));
                modal.show();
            }
        </script>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
        <%@include file="../../Footer.jsp" %>
    </body>

</html>