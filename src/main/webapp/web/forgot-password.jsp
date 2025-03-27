<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- Thêm jQuery -->
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
        .btn-resend {
            background-color: #007bff;
        }
        .btn-resend:hover {
            background-color: #0056b3;
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
                    <form id="otpForm" method="post">
                        <div class="mb-3">
                            <label for="otp" class="form-label">OTP:</label>
                            <input type="text" id="otp" name="otp" class="form-control" required>
                        </div>
                        <button type="submit" class="btn">Verify OTP</button>
                    </form>
                    <!-- Thêm nút gửi lại OTP -->
                    <form action="/ForgotPasswordController/resend" method="post" class="mt-3">
                        <button type="submit" class="btn btn-resend">Resend OTP</button>
                    </form>
                    <div id="otpFeedback" class="mt-3"></div> <!-- Phản hồi từ server -->
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript để xử lý modal và AJAX -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            <% if (request.getAttribute("message") != null && request.getAttribute("error") == null) { %>
                var otpModal = new bootstrap.Modal(document.getElementById('otpModal'));
                otpModal.show();
            <% } %>

            // Xử lý gửi form OTP qua AJAX
            $('#otpForm').on('submit', function (e) {
                e.preventDefault(); // Ngăn gửi form mặc định

                var otpValue = $('#otp').val();
                $.ajax({
                    url: '/ForgotPasswordController/verify',
                    type: 'POST',
                    data: { otp: otpValue },
                    success: function (response) {
                        if (response.includes('OTP verified successfully')) {
                            // Nếu OTP đúng, chuyển hướng đến trang reset password
                            window.location.href = '/ForgotPasswordController/reset';
                        } else {
                            // Nếu OTP sai, hiển thị thông báo lỗi trong modal
                            $('#otpFeedback').html('<p class="text-danger">Invalid OTP. Please try again.</p>');
                        }
                    },
                    error: function () {
                        $('#otpFeedback').html('<p class="text-danger">An error occurred. Please try again.</p>');
                    }
                });
            });
        });
    </script>
</body>
</html>