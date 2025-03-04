<%-- 
    Document   : Voucher
    Created on : Feb 13, 2025, 3:28:50 PM
    Author     : ADMIN
--%>
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>

<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Voucher</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <style>
            body {
                font-family: 'Poppins', sans-serif;
                background: linear-gradient(to right, #74ebd5, #acb6e5);
                margin: 0;
                padding: 0;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }

            .main {
                flex: 1;
                background: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
                margin-top: 20px;
            }
            .modal-content {
                padding: 20px;
            }
            .content {
                
                padding: 20px;
                width: 70%;
                
            }
        </style>
    </head>
    <body>

        <%@include file="../../AdminLayout.jsp" %>


        <div class="content container-fluid main">
            <h2 class="text-center">Quản lý Voucher</h2>

            <!-- Form tìm kiếm -->
            <form action="VoucherServlet" method="get" class="d-flex mb-3">
                <input type="text" name="search" class="form-control me-2" placeholder="Nhập mã voucher...">
                <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            </form>

            <!-- Nút thêm voucher -->
            <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#voucherModal">Thêm Voucher</button>

            <!-- Bảng danh sách voucher -->
            <table class="table table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Mã Voucher</th>
                        <th>Giảm Giá (%)</th>
                        <th>Ngày Hết Hạn</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>SALE50</td>
                        <td>50%</td>
                        <td>2025-12-31</td>
                        <td>
                            <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#voucherModal">Sửa</button>
                            <a href="VoucherServlet?action=delete&id=1" class="btn btn-danger btn-sm">Xóa</a>
                        </td>
                    </tr>
                    <!-- Dữ liệu lấy từ database sẽ hiển thị ở đây -->
                </tbody>
            </table>
        </div>

        <!-- Modal Thêm/Sửa Voucher -->
        <div class="modal fade" id="voucherModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <h4 class="modal-title text-center">Thêm/Sửa Voucher</h4>
                    <form action="VoucherServlet" method="post">
                        <input type="hidden" name="id" id="voucherId">
                        <div class="mb-3">
                            <label class="form-label">Mã Voucher:</label>
                            <input type="text" name="code" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Giảm Giá (%):</label>
                            <input type="number" name="discount" class="form-control" required min="1" max="100">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Ngày Hết Hạn:</label>
                            <input type="date" name="expiry" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
