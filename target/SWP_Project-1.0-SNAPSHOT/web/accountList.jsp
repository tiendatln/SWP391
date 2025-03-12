<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Account List</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                margin-top: 40px;
            }
            table {
                border-radius: 8px;
                overflow: hidden;
            }
            th {
                background-color: #007bff;
                color: white;
                text-align: center;
            }
            tr:hover {
                background-color: #f1f1f1;
            }
            .roleSelect {
                padding: 5px;
                border-radius: 5px;
                border: 1px solid #ced4da;
                background: #fff;
            }
            .search-box {
                margin-bottom: 20px;
                display: flex;
                justify-content: center;
            }
        </style>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <jsp:include page="/AdminLayout.jsp" />


        <div class="container">
            <h2 class="text-center text-primary">Account List</h2>

            <!-- Ô tìm kiếm -->
            <div class="search-box">
                <input type="text" id="searchInput" class="form-control w-50" placeholder="Tìm kiếm username hoặc email">
                <button class="btn btn-primary ms-2" onclick="searchAccount()">Tìm kiếm</button>
            </div>

            <!-- Hiển thị thông báo nếu không tìm thấy tài khoản -->
            <c:if test="${not empty message}">
                <p class="text-danger text-center">${message}</p>
            </c:if>

            <c:choose>
                <c:when test="${not empty accounts}">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover text-center">
                            <thead class="table-primary">
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Address</th>
                                    <th>Role</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="account" items="${accounts}">
                                    <tr>
                                        <td>${account.id}</td>
                                        <td>${account.username}</td>
                                        <td>${account.email}</td>
                                        <td>${account.phoneNumber}</td>
                                        <td>${account.address}</td>
                                        <td>
                                            <select class="form-select roleSelect" data-account-id="${account.id}" ${account.role == 'admin' ? 'disabled' : ''}>
                                                <option value="user" ${account.role == 'user' ? 'selected' : ''}>User</option>
                                                <option value="admin" ${account.role == 'admin' ? 'selected' : ''}>Admin</option>
                                                <option value="staff" ${account.role == 'staff' ? 'selected' : ''}>Staff</option>
                                            </select>
                                        
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-danger text-center">Không tìm thấy tài khoản nào.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <script>
            function searchAccount() {
                let keyword = document.getElementById("searchInput").value.trim();
                window.location.href = "<%= request.getContextPath()%>/AccountController/AccountList?search=" + encodeURIComponent(keyword);
            }

            $(document).ready(function () {
                $(".roleSelect").change(function () {
                    var accountId = $(this).data("account-id");
                    var newRole = $(this).val();

                    $.post("/AccountController/UpdateRole", {accountId: accountId, newRole: newRole}, function (response) {
                        if (response === "success") {
                            alert("Role updated successfully!");
                        } else {
                            alert("Failed to update role.");
                        }
                    });
                });
            });
        </script>

    </body>
</html>
