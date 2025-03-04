
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User Profile</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            .sidebar {
                width: 200px;
            }
            .content {
                flex-grow: 1;
                padding: 20px;
                background: white;
                border-radius: 10px;
            }
        </style>
    </head>
    <body class="bg-light">
        <div>
            <%@include file="../../Header.jsp" %>
        </div>

        <div class="container mt-4 d-flex">
            <div class="sidebar me-3">
                <button class="btn btn-dark w-100 mb-2" onclick="showSection('profile')">&#128100; Profile</button>
                <button class="btn btn-secondary w-100 mb-2" onclick="showSection('change-password')">&#128274; Change Password</button>
            </div>
            <div class="content w-100" id="profile-section">
                <h2>User Profile</h2>
                <form>
                    <div class="row mb-3">
                        <div class="col">
                            <label class="form-label">Name:</label>
                            <input type="text" class="form-control" value="John Doe">
                        </div>
                        <div class="col">
                            <label class="form-label">Email:</label>
                            <input type="email" class="form-control" value="johndoe@example.com">
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col">
                            <label class="form-label">Age:</label>
                            <input type="number" class="form-control" value="30">
                        </div>
                        <div class="col">
                            <label class="form-label">Phone:</label>
                            <input type="text" class="form-control" value="0123456789">
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">Update Profile</button>
                </form>
            </div>
            <div class="content w-100 d-none" id="change-password-section">
                <h2>Change Password</h2>
                <form>
                    <div class="mb-3">
                        <label class="form-label">Current Password</label>
                        <input type="password" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">New Password</label>
                        <input type="password" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Confirm New Password</label>
                        <input type="password" class="form-control">
                    </div>
                    <button type="submit" class="btn btn-primary">Change Password</button>
                </form>
            </div>
        </div>
        <script>
            function showSection(section) {
                document.getElementById('profile-section').classList.toggle('d-none', section !== 'profile');
                document.getElementById('change-password-section').classList.toggle('d-none', section !== 'change-password');
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <div><%@include file="../../Footer.jsp" %></div>
    </body>
</html>
