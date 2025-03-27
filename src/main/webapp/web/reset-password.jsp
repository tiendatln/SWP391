<%-- 
    Document   : reset-password
    Created on : Mar 21, 2025, 4:13:40 PM
    Author     : Kim Chi Khang _ CE180324
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f0f0f0;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 400px;
            text-align: center;
        }
        h2 {
            color: #333;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
        .message {
            color: green;
            margin-bottom: 15px;
        }
        label {
            display: block;
            text-align: left;
            margin: 10px 0 5px;
            color: #555;
        }
        input[type="password"], input[type="text"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            /* Tắt biểu tượng con mắt mặc định của trình duyệt */
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
        }
        /* Đảm bảo không hiển thị nút reveal password của trình duyệt */
        input[type="password"]::-ms-reveal,
        input[type="password"]::-ms-clear,
        input[type="password"]::-webkit-textfield-decoration-container {
            display: none;
        }
        .form-check {
            text-align: left;
            margin-bottom: 15px;
        }
        button {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }
        button:hover {
            background-color: #0056b3;
        }
        a {
            color: #007bff;
            text-decoration: none;
            display: inline-block;
            margin-top: 10px;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Reset Password</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
        <form action="/ForgotPasswordController/reset" method="POST">
            <label for="newPassword">New Password:</label>
            <input type="password" id="newPassword" name="newPassword" required>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="showNewPassword">
                <label class="form-check-label" for="showNewPassword">Show password</label>
            </div>
            
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required>
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="showConfirmPassword">
                <label class="form-check-label" for="showConfirmPassword">Show password</label>
            </div>
            
            <button type="submit">Reset Password</button>
        </form>
        <a href="/web/login.jsp">Back to Login</a>
    </div>

    <!-- Modal thông báo thành công -->
    <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="successModalLabel">Success</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p class="text-success">Your password has been reset successfully!</p>
                </div>
                <div class="modal-footer">
                    <a href="/web/login.jsp" class="btn btn-primary">Go back to Login</a>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript xử lý hiển thị/ẩn mật khẩu và modal -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // Xử lý hiển thị/ẩn mật khẩu mới
            const newPasswordInput = document.getElementById('newPassword');
            const showNewPasswordCheckbox = document.getElementById('showNewPassword');
            
            showNewPasswordCheckbox.addEventListener('change', function() {
                newPasswordInput.type = this.checked ? 'text' : 'password';
            });

            // Xử lý hiển thị/ẩn xác nhận mật khẩu
            const confirmPasswordInput = document.getElementById('confirmPassword');
            const showConfirmPasswordCheckbox = document.getElementById('showConfirmPassword');
            
            showConfirmPasswordCheckbox.addEventListener('change', function() {
                confirmPasswordInput.type = this.checked ? 'text' : 'password';
            });

            // Xử lý modal thành công
            <% if (request.getAttribute("message") != null && request.getAttribute("message").equals("Password reset successfully. Please login.")) { %>
                var successModal = new bootstrap.Modal(document.getElementById('successModal'));
                successModal.show();
            <% } %>
        });
    </script>
</body>
</html>