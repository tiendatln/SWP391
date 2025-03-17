<%-- 
    Document   : Voucher
    Created on : Feb 13, 2025, 3:28:50 PM
    Author     : ADMIN
--%>
<%@page import="Model.Voucher"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Voucher</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: Arial, sans-serif;
            }
            body {
                display: flex;
                background-color: #f8f9fa;
            }
            .main {
                flex-grow: 1;
                padding: 20px;
                margin-left: 250px;
            }
            .content {
                background: white;
                padding: 20px;
                border-radius: 10px;
                width: 100%;
            }
            h2 {
                text-align: center;
                margin-bottom: 20px;
            }
            .search-box {
                display: flex;
                margin-bottom: 20px;
            }
            .search-box input {
                flex: 1;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .search-box button {
                padding: 10px;
                margin-left: 10px;
                border: none;
                background-color: #007bff;
                color: white;
                border-radius: 5px;
                cursor: pointer;
            }
            .btn-add {
                padding: 10px 15px;
                background: green;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                margin-bottom: 20px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 10px;
                text-align: center;
            }
            th {
                background: #333;
                color: white;
            }
            .btn-edit {
                background: #ffc107;
                color: black;
                padding: 5px 10px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .btn-delete {
                background: #dc3545;
                color: white;
                padding: 5px 10px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .modal {
                display: none;
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.4);
                padding-top: 60px;
            }
            .modal-content {
                background-color: #fefefe;
                margin: 5% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 400px;
                border-radius: 10px;
            }
            .close {
                color: #aaa;
                font-size: 28px;
                font-weight: bold;
                position: absolute;
                right: 10px;
                top: 10px;
            }
            .close:hover,
            .close:focus {
                color: black;
                text-decoration: none;
                cursor: pointer;
            }
            .btn-cancel {
                background: #ccc;
                color: white;
                padding: 5px 10px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .btn-cancel:hover {
                background-color: #aaa;
            }
            .message, .error-message {
                padding: 10px;
                margin-bottom: 20px;
                border-radius: 5px;
            }
            .message {
                background-color: #d4edda;
                color: #155724;
            }
            .error-message {
                background-color: #f8d7da;
                color: #721c24;
            }
        </style>
    </head>
    <body>
        <%@ include file="../../AdminLayout.jsp" %>
        <div class="main">
            <div class="content">
                <h2>Quản lý Voucher</h2>

                <!-- Hiển thị thông báo thành công hoặc lỗi -->
                <% if (session.getAttribute("message") != null) { %>
                    <div class="message"><%= session.getAttribute("message") %></div>
                    <% session.removeAttribute("message"); %>
                <% } %>
                <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message"><%= request.getAttribute("error") %></div>
                <% } %>

                <form action="VoucherController" method="get" class="search-box">
                    <input type="text" name="search" placeholder="Nhập mã voucher..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : ""%>">
                    <button type="submit">Tìm kiếm</button>
                </form>
                <button class="btn-add" onclick="openAddModal()">Thêm Voucher</button>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Mã Voucher</th>
                            <th>Giảm Giá (%)</th>
                            <th>Ngày Bắt Đầu</th>
                            <th>Ngày Hết Hạn</th>
                            <th>Số Lượng</th>
                            <th>Đã Dùng</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Voucher> voucherList = (List<Voucher>) request.getAttribute("voucherList");
                            if (voucherList != null && !voucherList.isEmpty()) {
                                for (Voucher voucher : voucherList) {
                        %>
                        <tr>
                            <td><%= voucher.getVoucherID() %></td>
                            <td><%= voucher.getVoucherCode() %></td>
                            <td><%= voucher.getPercentDiscount() %>%</td>
                            <td><%= voucher.getStartDate() %></td>
                            <td><%= voucher.getEndDate() %></td>
                            <td><%= voucher.getQuantity() %></td>
                            <td><%= voucher.getUsedTime() %></td>
                            <td>
                                <button class="btn-edit" onclick="openEditModal('<%= voucher.getVoucherID() %>', '<%= voucher.getVoucherCode() %>', '<%= voucher.getPercentDiscount() %>', '<%= voucher.getStartDate() %>', '<%= voucher.getEndDate() %>', '<%= voucher.getQuantity() %>', '<%= voucher.getUsedTime() %>')">Sửa</button>
                                <form action="VoucherController" method="POST" style="display:inline;">
                                    <input type="hidden" name="deleteVoucherCode" value="<%= voucher.getVoucherCode() %>">
                                    <button type="submit" class="btn-delete" onclick="return confirm('Bạn có chắc chắn muốn xóa voucher này không?')">Xóa</button>
                                </form>
                            </td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="8">Không có dữ liệu voucher.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Modal Thêm Voucher -->
        <div id="modal-add-voucher" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modal-add-voucher')">×</span>
                <h3>Thêm Voucher Mới</h3>
                <form action="VoucherController" method="post">
                    <label for="add-voucherCode">Mã Voucher:</label>
                    <input type="text" id="add-voucherCode" name="voucherCode" required><br>

                    <label for="add-discount">Giảm Giá (%):</label>
                    <input type="number" id="add-discount" name="percentDiscount" min="0" max="100" required><br>

                    <label for="add-startDate">Ngày Bắt Đầu:</label>
                    <input type="date" id="add-startDate" name="startDate" required><br>

                    <label for="add-endDate">Ngày Hết Hạn:</label>
                    <input type="date" id="add-endDate" name="endDate" required><br>

                    <label for="add-quantity">Số Lượng:</label>
                    <input type="number" id="add-quantity" name="quantity" min="0" required><br>

                    <button type="submit" class="btn-add">Lưu</button>
                    <button type="button" class="btn-cancel" onclick="closeModal('modal-add-voucher')">Hủy</button>
                </form>
            </div>
        </div>

        <!-- Modal Chỉnh Sửa Voucher -->
        <div id="modal-edit-voucher" class="modal">
            <div class="modal-content">
                <span class="close" onclick="closeModal('modal-edit-voucher')">×</span>
                <h3>Chỉnh Sửa Voucher</h3>
                <form action="VoucherController" method="post">
                    <input type="hidden" id="edit-id" name="id">
                    <label for="edit-voucherCode">Mã Voucher:</label>
                    <input type="text" id="edit-voucherCode" name="voucherCode" required><br>

                    <label for="edit-discount">Giảm Giá (%):</label>
                    <input type="number" id="edit-discount" name="percentDiscount" min="0" max="100" required><br>

                    <label for="edit-startDate">Ngày Bắt Đầu:</label>
                    <input type="date" id="edit-startDate" name="startDate" required><br>

                    <label for="edit-endDate">Ngày Hết Hạn:</label>
                    <input type="date" id="edit-endDate" name="endDate" required><br>

                    <label for="edit-quantity">Số Lượng:</label>
                    <input type="number" id="edit-quantity" name="quantity" min="0" required><br>

                    <!-- Trường "Đã Dùng" chỉ hiển thị, không cho sửa -->
                    <label for="edit-usedTime">Đã Dùng:</label>
                    <input type="number" id="edit-usedTime" name="usedTime" readonly><br>

                    <button type="submit" class="btn-add">Cập Nhật</button>
                    <button type="button" class="btn-cancel" onclick="closeModal('modal-edit-voucher')">Hủy</button>
                </form>
            </div>
        </div>

        <script>
            function openAddModal() {
                document.getElementById('modal-add-voucher').style.display = 'block';
            }

            function openEditModal(id, voucherCode, percentDiscount, startDate, endDate, quantity, usedTime) {
                document.getElementById('edit-id').value = id;
                document.getElementById('edit-voucherCode').value = voucherCode;
                document.getElementById('edit-discount').value = percentDiscount;
                document.getElementById('edit-startDate').value = startDate;
                document.getElementById('edit-endDate').value = endDate;
                document.getElementById('edit-quantity').value = quantity;
                document.getElementById('edit-usedTime').value = usedTime;
                document.getElementById('modal-edit-voucher').style.display = 'block';
            }

            function closeModal(modalId) {
                document.getElementById(modalId).style.display = 'none';
            }
        </script>
    </body>
</html>