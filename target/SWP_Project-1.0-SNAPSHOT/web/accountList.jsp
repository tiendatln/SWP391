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
                margin: 0;
                padding: 0;
                font-family: Arial, sans-serif;
                display: flex;
            }

            /* Sidebar styling */
            .sidebar {
                width: 250px;
                background: #FF9900;
                color: white;
                padding: 20px;
                height: 100vh;
                font-family: Arial, sans-serif;
                position: fixed;
                top: 0;
                left: 0;
                border-right: 2px solid #FFD700;
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            .dashboard {
                font-size: 20px;
                font-weight: bold;
                margin-bottom: 10px; /* Reduced from 20px to 10px */
                text-align: center;
            }

            .sidebar ul {
                list-style: none;
                padding: 0;
                width: 100%;
            }

            .sidebar ul li {
                margin: 5px 0; /* Reduced from 15px to 5px */
            }

            .sidebar ul li a {
                display: block;
                text-decoration: none;
                color: white;
                padding: 10px;
                background: #34495e;
                border-radius: 5px;
                text-align: center;
                transition: 0.3s;
            }

            .sidebar ul li a:hover {
                background: #1abc9c;
            }

            /* Main content container */
            .account-list-container {
                margin-left: 250px;
                background-color: #fff;
                margin-top: 20px;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                width: calc(100% - 250px);
            }

            .account-list-container h2 {
                font-size: 24px;
                font-weight: bold;
                color: #333;
                margin-bottom: 20px;
                text-align: left;
            }

            /* Search box styling */
            .account-list-search-box {
                margin-bottom: 20px;
                display: flex;
                justify-content: flex-end;
            }

            .account-list-search-box input {
                width: 200px;
                border: 1px solid #ccc;
                border-radius: 4px;
                padding: 5px 10px;
            }

            .account-list-search-box button {
                background-color: #007bff;
                border: none;
                border-radius: 4px;
                padding: 5px 15px;
                margin-left: 10px;
            }

            /* Table styling */
            .account-list-container table {
                width: 100%;
                border-collapse: collapse;
                background-color: #fff;
            }

            .account-list-container th {
                background-color: #f8f9fa;
                color: #333;
                font-weight: bold;
                padding: 10px;
                text-align: left;
                border-bottom: 2px solid #dee2e6;
            }

            .account-list-container td {
                padding: 10px;
                border-bottom: 1px solid #dee2e6;
                text-align: left;
            }

            .account-list-container tr:hover {
                background-color: #f1f1f1;
            }

            /* Role select styling */
            .account-list-container .account-list-role-select {
                padding: 5px;
                border-radius: 4px;
                border: 1px solid #ccc;
                background: #fff;
                width: 100px;
            }

            /* Plain text for admin role */
            .account-list-container .admin-role-text {
                padding: 5px;
                width: 100px;
                display: inline-block;
            }

            /* Message styling */
            .account-list-container .text-danger {
                color: #dc3545;
                text-align: center;
                margin: 20px 0;
            }
        </style>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    </head>
    <body>
        <jsp:include page="/AdminLayout.jsp" />

        <div class="account-list-container">
            <h2>Account List</h2>

            <!-- Search Box -->
            <div class="account-list-search-box">
                <input type="text" id="searchInput" class="form-control" placeholder="Search accounts...">
                <button class="btn btn-primary" onclick="searchAccount()">Search</button>
            </div>

            <!-- Display message if no accounts found -->
            <c:if test="${not empty message}">
                <p class="text-danger">${message}</p>
            </c:if>

            <c:choose>
                <c:when test="${not empty accounts}">
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
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
                                            <c:choose>
                                                <c:when test="${account.role == 'admin'}">
                                                    <span class="admin-role-text">Admin</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <select class="account-list-role-select" data-account-id="${account.id}">
                                                        <option value="user" ${account.role == 'user' ? 'selected' : ''}>User</option>
                                                        <option value="admin" ${account.role == 'admin' ? 'selected' : ''}>Admin</option>
                                                        <option value="staff" ${account.role == 'staff' ? 'selected' : ''}>Staff</option>
                                                    </select>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="text-danger">Không tìm thấy tài khoản nào.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <script>
            function searchAccount() {
                let keyword = document.getElementById("searchInput").value.trim();
                window.location.href = "<%= request.getContextPath()%>/AccountController/AccountList?search=" + encodeURIComponent(keyword);
            }

            $(document).ready(function () {
                $(".account-list-role-select").change(function () {
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
