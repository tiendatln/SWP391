<%-- 
    Document   : Voucher
    Created on : Feb 13, 2025, 3:28:50 PM
    Author     : ADMIN
--%>
<%@page import="Model.Voucher"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Voucher Management</title>
        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
        <style>
            body {
                display: flex;
                background-color: #f8f9fa;
            }
            .content {
                margin-left: 260px;
                padding: 20px;
                width: 85%;
            }
            .container-fluid {
                min-height: 100vh;
            }
            .table th, .table td {
                vertical-align: middle;
            }
            .price {
                font-size: 18px;
                font-weight: bold;
                color: red;
            }
            /* Đổi màu header bảng thành trắng và chữ đen */
            .table-light th {
                background-color: #ffffff;
                color: #000000;
                border: 1px solid #dee2e6;
            }
            /* Căn chỉnh thanh tìm kiếm và nút Add */
            .header-actions {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .header-actions .search-form {
                flex: 0 0 50%; /* Tăng chiều rộng thanh tìm kiếm lên 50% */
            }
            /* Viền đỏ cho trường lỗi */
            .is-invalid {
                border-color: #dc3545 !important;
            }
            /* Thông báo lỗi trong modal */
            .modal-error {
                color: #dc3545;
                font-size: 14px;
                margin-bottom: 15px;
            }
        </style>
    </head>
    <body class="bg-light sb-nav-fixed">
        <%@ include file="../../AdminLayout.jsp" %>
        <div class="content container-fluid px-4">
            <h2 class="mb-4">Voucher Management</h2>

            <!-- Hiển thị thông báo thành công hoặc lỗi -->
            <% if (session.getAttribute("message") != null) { %>
                <div class="alert alert-success" id="success-message"><%= session.getAttribute("message") %></div>
                <% session.removeAttribute("message"); %>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" id="error-message"><%= request.getAttribute("error") %></div>
            <% } %>

            <!-- Thanh tìm kiếm bên trái và nút Add bên phải -->
            <div class="header-actions">
                <form action="VoucherController" method="get" class="search-form d-flex">
                    <input type="text" name="search" class="form-control me-2" placeholder="Input voucher code..." value="<%= request.getParameter("search") != null ? request.getParameter("search") : ""%>">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addVoucherModal">Add New Voucher</button>
            </div>

            <table class="table table-bordered">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Voucher Code</th>
                        <th>Discount (%)</th>
                        <th>Start Day</th>
                        <th>End Day</th>
                        <th>Total</th>
                        <th>Used</th>
                        <th>Actions</th>
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
                        <td class="price"><%= voucher.getPercentDiscount() %>%</td>
                        <td><%= voucher.getStartDate() %></td>
                        <td><%= voucher.getEndDate() %></td>
                        <td><%= voucher.getQuantity() %></td>
                        <td><%= voucher.getUsedTime() %></td>
                        <td>
                            <button class="btn btn-primary" style="color: black;" data-bs-toggle="modal" data-bs-target="#editVoucherModal<%= voucher.getVoucherID() %>">Edit</button>
                            <form action="VoucherController" method="POST" style="display:inline;">
                                <input type="hidden" name="deleteVoucherCode" value="<%= voucher.getVoucherCode() %>">
                                <button type="submit" class="btn btn-outline-danger" style="color: black" onclick="return confirm('Are you sure to delete this voucher?')"><i class="fa fa-trash"></i></button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="8">No voucher data.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <!-- Modal Thêm Voucher -->
        <div class="modal fade" id="addVoucherModal" tabindex="-1" aria-labelledby="addVoucherModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addVoucherModalLabel">Add New Voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <% 
                            String modalError = (String) request.getAttribute("modalError");
                            String errorFields = (String) request.getAttribute("errorFields");
                            String addVoucherCode = (String) request.getAttribute("voucherCode");
                            String addStartDate = (String) request.getAttribute("startDate");
                            String addEndDate = (String) request.getAttribute("endDate");
                            String addPercentDiscount = (String) request.getAttribute("percentDiscount");
                            String addQuantity = (String) request.getAttribute("quantity");
                            String id = (String) request.getAttribute("id");
                            boolean showAddModal = modalError != null && (id == null || id.isEmpty());
                        %>
                        <% if (showAddModal && modalError != null) { %>
                            <div class="modal-error"><%= modalError %></div>
                        <% } %>
                        <form action="VoucherController" method="post">
                            <div class="mb-3">
                                <label for="add-voucherCode" class="form-label">Voucher Code</label>
                                <input type="text" class="form-control <%= errorFields != null && errorFields.contains("voucherCode") ? "is-invalid" : "" %>" 
                                       id="add-voucherCode" name="voucherCode" 
                                       value="<%= addVoucherCode != null ? addVoucherCode : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="add-discount" class="form-label">Discount (%)</label>
                                <input type="number" class="form-control <%= errorFields != null && errorFields.contains("percentDiscount") ? "is-invalid" : "" %>" 
                                       id="add-discount" name="percentDiscount" min="0" max="100" 
                                       value="<%= addPercentDiscount != null ? addPercentDiscount : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="add-startDate" class="form-label">Start Day</label>
                                <input type="date" class="form-control <%= errorFields != null && errorFields.contains("startDate") ? "is-invalid" : "" %>" 
                                       id="add-startDate" name="startDate" 
                                       value="<%= addStartDate != null ? addStartDate : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="add-endDate" class="form-label">End Day</label>
                                <input type="date" class="form-control <%= errorFields != null && errorFields.contains("endDate") ? "is-invalid" : "" %>" 
                                       id="add-endDate" name="endDate" 
                                       value="<%= addEndDate != null ? addEndDate : "" %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="add-quantity" class="form-label">Total</label>
                                <input type="number" class="form-control <%= errorFields != null && errorFields.contains("quantity") ? "is-invalid" : "" %>" 
                                       id="add-quantity" name="quantity" min="0" 
                                       value="<%= addQuantity != null ? addQuantity : "" %>" required>
                            </div>
                            <div class="d-flex justify-content-end">
                                <button type="submit" class="btn btn-primary">Add</button>
                                <button type="button" class="btn btn-danger ms-2" data-bs-dismiss="modal">Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Chỉnh Sửa Voucher -->
        <%
            if (voucherList != null && !voucherList.isEmpty()) {
                for (Voucher voucher : voucherList) {
                    boolean showEditModal = modalError != null && id != null && !id.isEmpty() && id.equals(String.valueOf(voucher.getVoucherID()));
        %>
        <div class="modal fade" id="editVoucherModal<%= voucher.getVoucherID() %>" tabindex="-1" aria-labelledby="editVoucherModalLabel<%= voucher.getVoucherID() %>" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editVoucherModalLabel<%= voucher.getVoucherID() %>">Edit Voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <% if (showEditModal && modalError != null) { %>
                            <div class="modal-error"><%= modalError %></div>
                        <% } %>
                        <form action="VoucherController" method="post">
                            <input type="hidden" name="id" value="<%= voucher.getVoucherID() %>">
                            <div class="mb-3">
                                <label for="edit-voucherCode-<%= voucher.getVoucherID() %>" class="form-label">Voucher Code</label>
                                <input type="text" class="form-control <%= showEditModal && errorFields != null && errorFields.contains("voucherCode") ? "is-invalid" : "" %>" 
                                       id="edit-voucherCode-<%= voucher.getVoucherID() %>" name="voucherCode" 
                                       value="<%= showEditModal && addVoucherCode != null ? addVoucherCode : voucher.getVoucherCode() %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="edit-discount-<%= voucher.getVoucherID() %>" class="form-label">Discount (%)</label>
                                <input type="number" class="form-control <%= showEditModal && errorFields != null && errorFields.contains("percentDiscount") ? "is-invalid" : "" %>" 
                                       id="edit-discount-<%= voucher.getVoucherID() %>" name="percentDiscount" min="0" max="100" 
                                       value="<%= showEditModal && addPercentDiscount != null ? addPercentDiscount : voucher.getPercentDiscount() %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="edit-startDate-<%= voucher.getVoucherID() %>" class="form-label">Start Day</label>
                                <input type="date" class="form-control <%= showEditModal && errorFields != null && errorFields.contains("startDate") ? "is-invalid" : "" %>" 
                                       id="edit-startDate-<%= voucher.getVoucherID() %>" name="startDate" 
                                       value="<%= showEditModal && addStartDate != null ? addStartDate : voucher.getStartDate() %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="edit-endDate-<%= voucher.getVoucherID() %>" class="form-label">End Day</label>
                                <input type="date" class="form-control <%= showEditModal && errorFields != null && errorFields.contains("endDate") ? "is-invalid" : "" %>" 
                                       id="edit-endDate-<%= voucher.getVoucherID() %>" name="endDate" 
                                       value="<%= showEditModal && addEndDate != null ? addEndDate : voucher.getEndDate() %>" required>
                            </div>
                            <div class="mb-3">
                                <label for="edit-quantity-<%= voucher.getVoucherID() %>" class="form-label">Total</label>
                                <input type="number" class="form-control <%= showEditModal && errorFields != null && errorFields.contains("quantity") ? "is-invalid" : "" %>" 
                                       id="edit-quantity-<%= voucher.getVoucherID() %>" name="quantity" min="0" 
                                       value="<%= showEditModal && addQuantity != null ? addQuantity : voucher.getQuantity() %>" required>
                            </div>
                            
                            <div class="d-flex justify-content-end">
                                <button type="submit" class="btn btn-primary">Save Change</button>
                                <button type="button" class="btn btn-danger ms-2" data-bs-dismiss="modal">Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <%
                }
            }
        %>

        <!-- Bootstrap 5 JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <!-- Script để ẩn thông báo sau 3 giây và tự động mở modal khi có lỗi -->
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Ẩn thông báo sau 3 giây
                const successMessage = document.getElementById('success-message');
                const errorMessage = document.getElementById('error-message');

                if (successMessage) {
                    setTimeout(() => {
                        successMessage.style.display = 'none';
                    }, 3000); // 3000ms = 3 giây
                }

                if (errorMessage) {
                    setTimeout(() => {
                        errorMessage.style.display = 'none';
                    }, 3000); // 3000ms = 3 giây
                }

                // Tự động mở modal khi có lỗi
                <% if (request.getAttribute("modalError") != null) { %>
                    <% if (id != null && !id.isEmpty()) { %>
                        // Mở modal chỉnh sửa
                        const editModal = new bootstrap.Modal(document.getElementById('editVoucherModal<%= id %>'));
                        editModal.show();
                    <% } else { %>
                        // Mở modal thêm mới
                        const addModal = new bootstrap.Modal(document.getElementById('addVoucherModal'));
                        addModal.show();
                    <% } %>
                <% } %>
            });
        </script>
    </body>
</html>