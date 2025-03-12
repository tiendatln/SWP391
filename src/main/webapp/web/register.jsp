<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="Utils.Constants"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign Up Page</title>
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
                flex: 1;  /* Đảm bảo cột trái chiếm không gian còn lại */
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .left-content img {
                width: 100%;  /* Giúp hình ảnh rộng đầy đủ cột trái */
                height: 135vh;  /* Đảm bảo chiều cao ảnh bằng chiều cao cột trái */
                object-fit: cover;  /* Đảm bảo ảnh không bị bóp méo */
            }

            /* Right Column */
            .right-column {
                padding: 20px 40px;
            }

            /* Sign Up Form */
            .account-form {
                background-color: #fff;
                padding: 40px;
                border-radius: 10px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            }

            .account-form h2 {
                font-size: 30px;
                font-weight: bold;
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

            /* Nút Sign Up */
            .account-form .btn {
                background-color: #4e73df;
                border: none;
                padding: 16px;
                font-size: 18px;
                font-weight: 600;
                border-radius: 8px;
                width: 100%;
                transition: background-color 0.3s, transform 0.3s;
            }

            .account-form .btn:hover {
                background-color: #2e59d9;
                transform: translateY(-2px); /* Tạo hiệu ứng nâng khi hover */
            }

            /* Nút Login */
            .account-form .btn-outline-primary {
                border: 2px solid #4e73df;
                background-color: #fff;
                color: #4e73df;
                font-weight: 600;
                padding: 15px;
                border-radius: 8px;
                width: 100%;
                transition: background-color 0.3s, transform 0.3s;
            }

            .account-form .btn-outline-primary:hover {
                background-color: #4e73df;
                color: white;
                transform: translateY(-2px); /* Tạo hiệu ứng nâng khi hover */
            }

            /* Nút Google */
            .account-form .btn-danger {
                background-color: #db4437;
                border: none;
                padding: 16px;
                font-size: 18px;
                font-weight: 600;
                border-radius: 8px;
                width: 100%;
                transition: background-color 0.3s, transform 0.3s;
            }

            .account-form .btn-danger:hover {
                background-color: #c1351d;
                transform: translateY(-2px); /* Tạo hiệu ứng nâng khi hover */
            }

            #error-message {
                position: fixed;
                top: 20px;
                right: 20px;
                background-color: #f44336;
                color: white;
                padding: 10px;
                border-radius: 5px;
                font-size: 16px;
                display: none; /* Hidden by default */
                z-index: 9999;
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
                        <img src="./resource/images/laptop.jpg" alt="Logo or Image" class="img-fluide">
                    </div>
                </div>

                <!-- Right Column - 50% width -->
                <div class="col-lg-6 col-md-12 right-column d-flex align-items-center justify-content-center">
                    <div class="account-form shadow-lg">
                        <h2 class="text-center mb-4">Create Your <span>Account</span></h2>
                        <form action="register" method="post">
                            <!-- Full Name -->
                            <div class="form-group">
                                <label for="fullName">Full Name</label>
                                <input type="text" name="fullName" id="fullName" class="form-control" required autofocus>
                            </div>

                            <!-- Email -->
                            <div class="form-group">
                                <label for="email">Email Address</label>
                                <input type="email" name="email" id="email" class="form-control" required>
                            </div>

                            <!-- Password -->
                            <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" name="password" id="password" class="form-control" required>
                            </div>


                            <!-- Confirm Password -->
                            <div class="form-group">
                                <label for="confirmPassword">Confirm Password</label>
                                <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" required>
                            </div>

                            <!-- Phone Number -->
                            <div class="form-group">
                                <label for="phone">Phone Number</label>
                                <input type="text" name="phone" id="phone" class="form-control" required>
                            </div>

                            <!-- Address -->
                            <div class="form-group">
                                <label for="address">Address</label>
                                <input type="text" name="address" id="address" class="form-control" required>
                            </div>

                            <!-- Submit Button -->
                            <button type="submit" class="btn btn-primary mb-3">Sign Up</button>

                            <div class="row d-flex justify-content-around">
                                <!-- Already have an account link -->
                                <div class="col-md-6 text-center mb-2">
                                    <p>Have an account?</p>
                                    <a href="/LoginController/Login" class="btn btn-outline-primary">
                                        <i class="fas fa-sign-in-alt"></i> Login
                                    </a>
                                </div>

                                <!-- Social Login -->
                                <div class="col-md-6 social-login text-center">
                                    <p>Or sign up with</p>
                                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=<%= Constants.GOOGLE_REDIRECT_URI%>&response_type=code&client_id=<%= Constants.GOOGLE_CLIENT_ID%>&approval_prompt=force" class="btn btn-danger">
                                        <i class="fab fa-google"></i> Google
                                    </a>
                                </div>
                            </div>
                        </form>
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

        </div>

        <!-- Error message div -->
        <div id="error-message">
            <span id="error-text"></span>
        </div>

        <!-- JSTL Error Handling -->
        <c:if test="${not empty message}">
            <script>
                document.getElementById("error-text").innerText = "${message}";
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