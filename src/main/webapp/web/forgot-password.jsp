<%-- 
    Document   : forgot-password
    Created on : Mar 20, 2025, 5:12:43 PM
    Author     : Kim Chi Khang _ CE180324
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f2f2f2;
        }
        .forgot-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 350px;
            text-align: center;
        }
        .btn {
            width: 100%;
            padding: 10px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
        }
        .btn:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <div class="forgot-container">
        <h2>Forgot Password</h2>
        <form action="/ForgotPasswordController" method="post">
            <div class="mb-3">
                <label for="email" class="form-label">Email:</label>
                <input type="email" id="email" name="email" class="form-control" required>
            </div>
            <button type="submit" class="btn">Send OTP</button>
        </form>
        <% if (request.getAttribute("error") != null) { %>
            <p class="text-danger mt-3"><%= request.getAttribute("error") %></p>
        <% } %>
        <% if (request.getAttribute("message") != null) { %>
            <p class="text-success mt-3"><%= request.getAttribute("message") %></p>
        <% } %>
        <div class="mt-3">
            <a href="/web/login.jsp">Back to Login</a>
        </div>
    </div>

    <!-- Modal để nhập OTP -->
    <div class="modal fade" id="otpModal" tabindex="-1" aria-labelledby="otpModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="otpModalLabel">Enter OTP</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="/ForgotPasswordController/verify" method="post">
                        <div class="mb-3">
                            <label for="otp" class="form-label">OTP:</label>
                            <input type="text" id="otp" name="otp" class="form-control" required>
                        </div>
                        <button type="submit" class="btn">Verify OTP</button>
                    </form>
                    <% if (request.getAttribute("error") != null) { %>
                        <p class="text-danger mt-3"><%= request.getAttribute("error") %></p>
                    <% } %>
                    <% if (request.getAttribute("message") != null) { %>
                        <p class="text-success mt-3"><%= request.getAttribute("message") %></p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript để tự động mở modal khi OTP được gửi thành công hoặc có lỗi -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            <% if (request.getAttribute("message") != null || request.getAttribute("error") != null) { %>
                var otpModal = new bootstrap.Modal(document.getElementById('otpModal'));
                otpModal.show();
            <% } %>
        });
    </script>
</body>
</html>