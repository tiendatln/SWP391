<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User Profile</title>
        <!-- Font Awesome CDN for icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <!-- Bootstrap CSS (optional, can be removed if using only custom CSS) -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
                margin: 0;
                padding: 0;
            }

            .profile-container {
                display: flex;
                max-width: 900px;
                margin: 20px auto;
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                overflow: hidden;
            }

            .profile-sidebar {
                width: 200px;
                background-color: #343a40;
                padding: 20px;
                color: #fff;
            }

            .profile-sidebar a {
                display: block;
                padding: 10px;
                margin-bottom: 10px;
                background-color: #495057;
                color: #fff;
                text-decoration: none;
                border-radius: 5px;
                text-align: center;
            }

            .profile-sidebar a:hover {
                background-color: #6c757d;
            }

            .profile-sidebar a.profile-active {
                background-color: #007bff;
            }

            .profile-sidebar a i {
                margin-right: 5px;
            }

            .profile-content {
                flex-grow: 1;
                padding: 20px;
            }

            .profile-content h2 {
                margin-top: 0;
                color: #343a40;
            }

            .profile-field {
                margin-bottom: 15px;
            }

            .profile-field label {
                display: block;
                font-weight: bold;
                margin-bottom: 5px;
                color: #495057;
            }

            .profile-field input {
                width: 100%;
                padding: 8px;
                border: 1px solid #ced4da;
                border-radius: 5px;
                box-sizing: border-box;
            }

            .profile-field input[readonly] {
                background-color: #e9ecef;
                border-color: #ced4da;
                color: #495057;
            }

            .profile-btn {
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
            }

            .profile-btn-primary {
                background-color: #007bff;
                color: #fff;
            }

            .profile-btn-primary:hover {
                background-color: #0056b3;
            }

            .profile-message {
                margin: 10px 0;
                padding: 10px;
                border-radius: 5px;
            }

            .profile-success {
                background-color: #d4edda;
                color: #155724;
            }

            .profile-error {
                background-color: #f8d7da;
                color: #721c24;
            }
            .footer {
                position: fixed;
                bottom: 0;
                width: 100%;
                color: #fff;
                padding: 10px 20px;
                text-align: center;
            }
        </style>
    </head>
    <%@include file="../../Header.jsp" %>

    <body>
        <div class="profile-container">
            <div class="profile-sidebar">
                <a href="<%=request.getContextPath()%>/ProfileController/Profile" class="profile-active">
                    <i class="fas fa-user"></i> Profile
                </a>
                <a href="<%=request.getContextPath()%>/ProfileController/Profile?isChangingPassword=true">
                    <i class="fas fa-lock"></i> Change Password
                </a>
                <a href="<%=request.getContextPath()%>/OrderController/CustomerOrder/${account.id}">
                    <i class="fas fa-receipt"></i> Order
                </a>
                <a href="/LoginController/Logout" class="profile-btn profile-btn-secondary">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>
            <div class="profile-content">
                <h2>User Profile</h2>

                <c:if test="${not empty successMessage}">
                    <div class="profile-message profile-success">${successMessage}</div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="profile-message profile-error">${errorMessage}</div>
                </c:if>

                <c:if test="${account != null}">
                    <c:choose>
                        <c:when test="${param.isEditing == 'true'}">
                            <form action="<%=request.getContextPath()%>/ProfileController/Profile" method="post">
                                <div class="profile-field">
                                    <label>Name:</label>
                                    <input type="text" name="username" value="${account.username}" required>
                                </div>
                                <div class="profile-field">
                                    <label>Email:</label>
                                    <input type="email" name="email" value="${account.email}" required>
                                </div>
                                <div class="profile-field">
                                    <label>Phone:</label>
                                    <input type="text" name="phoneNumber" value="${account.phoneNumber}" required>
                                </div>
                                <div class="profile-field">
                                    <label>Adress:</label>
                                    <input type="text" name="address" value="${account.address}" required>
                                </div>
                                <div class="profile-field">
                                    <input type="submit" class="profile-btn profile-btn-primary" value="Update Profile">
                                </div>
                            </form>
                        </c:when>
                        <c:when test="${param.isChangingPassword == 'true'}">
                            <form action="<%=request.getContextPath()%>/ProfileController/Profile" method="post">
                                <input type="hidden" name="action" value="changePassword">
                                <div class="profile-field">
                                    <label>Old Password:</label>
                                    <input type="password" name="oldPassword" required>
                                </div>
                                <div class="profile-field">
                                    <label>New Password:</label>
                                    <input type="password" name="newPassword" required>
                                </div>
                                <div class="profile-field">
                                    <label>Confirm Password:</label>
                                    <input type="password" name="confirmPassword" required>
                                </div>
                                <div class="profile-field">
                                    <input type="submit" class="profile-btn profile-btn-primary" value="Change Password">
                                </div>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="profile-field">
                                <label>Name:</label>
                                <input type="text" value="${account.username}" readonly>
                            </div>
                            <div class="profile-field">
                                <label>Email:</label>
                                <input type="email" value="${account.email}" readonly>
                            </div>
                            <div class="profile-field">
                                <label>Phone:</label>
                                <input type="text" value="${account.phoneNumber}" readonly>
                            </div>
                            <div class="profile-field">
                                <label>Adress:</label>
                                <input type="text" value="${account.address}" readonly>
                            </div>
                            <div class="profile-field">
                                <a href="<%=request.getContextPath()%>/ProfileController/Profile?isEditing=true" class="profile-btn profile-btn-primary">Edit Profile</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <c:if test="${account == null}">
                    <p>No profile information available.</p>
                </c:if>
            </div>
        </div>
        <div><%@include file="../../Footer.jsp" %></div>
    </body>

</html>