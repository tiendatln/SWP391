<%-- 
    Document   : product
    Created on : Feb 12, 2025, 12:39:16 AM
    Author     : Nguyễn Trường Vinh _ vinhntca181278
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script>
            function validateForm() {
                let username = document.getElementById("username").value;
                let email = document.getElementById("email").value;
                let password = document.getElementById("password").value;
                let confirmPassword = document.getElementById("confirmPassword").value;
                let phone = document.getElementById("phone").value;
                let address = document.getElementById("address").value;

                let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                let phonePattern = /^[0-9]{10}$/;

                if (username.length < 5) {
                    alert("Username must contain at least 5 characters!");
                    return false;
                }
                if (!emailPattern.test(email)) {
                    alert("Invalid email format!");
                    return false;
                }
                if (password.length < 6) {
                    alert("Password must have at least 6 characters!");
                    return false;
                }
                if (password !== confirmPassword) {
                    alert("Passwords do not match!");
                    return false;
                }
                if (!phonePattern.test(phone)) {
                    alert("Phone number must have 10 digits!");
                    return false;
                }
                if (address.trim() === "") {
                    alert("Address cannot be empty!");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body class="bg-light">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card mt-5">
                        <div class="card-header text-center">
                            <h2>Register</h2>
                        </div>
                        <div class="card-body">
                            <form action="/LoginController/Register" method="post" onsubmit="return validateForm()">
                                <div class="form-group">
                                    <label>Username</label>
                                    <input type="text" id="username" name="username" class="form-control" required>
                                    <small class="text-danger">
                                        <% if (request.getParameter("error") != null) { %>
                                            <%= request.getParameter("error") %>
                                        <% } %>
                                    </small>
                                </div>
                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" id="email" name="email" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Password</label>
                                    <input type="password" id="password" name="password" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Confirm Password</label>
                                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Phone Number</label>
                                    <input type="text" id="phone" name="phone" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input type="text" id="address" name="address" class="form-control" required>
                                </div>
                                <button type="submit" class="btn btn-primary btn-block">Register</button>
                            </form>
                            <div class="text-center mt-3">
                                <p>Already have account? <a href="/web/login.jsp">Login</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
