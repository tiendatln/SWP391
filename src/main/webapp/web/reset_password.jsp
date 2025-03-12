<%-- 
    Document   : resit
    Created on : Dec 1, 2024, 4:22:11 PM
    Author     : HAO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Password</title>
        <!-- Bootstrap CSS -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <!-- FontAwesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <style>
            /* Body Styling */
            body {
                background-color: #f4f7fc;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            /* Left Column */
            .left-column {
                background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
                height: 100vh;
                display: flex;
                flex-direction: column;
                justify-content: center;
                text-align: center;
            }

            .left-content img {
                max-width: 100%;
                height: auto;
            }

            /* Right Column */
            .right-column {
                padding: 20px;
            }

            /* Form Container */
            .account-form {
                background-color: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            }

            .account-form h2 {
                font-size: 30px;
                font-weight: bold;
                text-align: center;
            }

            .account-form .form-group label {
                font-size: 14px;
                font-weight: 600;
            }

            .account-form .form-control {
                border-radius: 5px;
                padding: 15px;
                font-size: 16px;
                border: 1px solid #ddd;
            }

            .account-form .form-control:focus {
                border-color: #4e73df;
                box-shadow: 0 0 0 0.25rem rgba(78, 115, 223, 0.25);
            }

            .account-form .btn {
                background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
                border: none;
                padding: 12px;
                font-size: 18px;
                border-radius: 5px;
                transition: all 0.3s ease;
            }

            .account-form .btn:hover {
                background: linear-gradient(#fff, #C0C0C0, #A9A9A9);
            }

            /* Responsive Styling */
            @media (max-width: 768px) {
                .left-column {
                    display: none;
                }

                .right-column {
                    padding: 20px;
                }

                .account-form {
                    padding: 30px;
                }
            }
        </style>
    </head>

    <body class="bg-light">
        <div class="container-fluid">
            <div class="row">
                <!-- Left Column - 50% width with background image -->
                <div class="col-lg-6 d-none d-lg-block left-column">
                    <div class="left-content">
                        <img src="./resource/images/posterWatch.webp" alt="Logo or Image" class="img-fluide">
                    </div>
                </div>

                <!-- Right Column - 50% width -->
                <div class="col-lg-6 col-md-12 right-column d-flex align-items-center justify-content-center">
                    <div class="account-form shadow-lg">
                        <h2 class="text-center mb-4">Reset Your Password</h2>
                        <form action="reset-password" method="post">
                            <!-- Hidden field for the token received in the reset link -->
                            <input type="hidden" name="token" value="${param.token}" />
                            <div class="form-group">
                                <label for="newPassword">New Password</label>
                                <input type="password" name="newPassword" id="newPassword" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword">Confirm Password</label>
                                <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" required>
                            </div>
                            <button type="submit" class="btn btn-primary btn-block mb-3">Reset Password</button>

                            <!-- Back to Login Button -->
                            <div class="text-center mb-3">
                                <p>Remember your password?</p>
                                <a href="/LoginController/Login" class="btn btn-success btn-block">
                                    <i class="fas fa-sign-in-alt"></i> Back to Login
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Error message div -->
        <div id="error-message">
            <span id="error-text"></span>
        </div>

        <!-- JSTL Error Handling -->
        <c:if test="${not empty errorMessage}">
            <script>
                document.getElementById("error-text").innerText = "${errorMessage}";
                document.getElementById("error-message").style.display = "block";
                setTimeout(function () {
                    document.getElementById("error-message").style.display = "none";
                }, 5000);
            </script>
        </c:if>
        <!-- Bootstrap JS and jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>

</html>
