<%-- 
    Document   : Voucher
    Created on : Feb 13, 2025, 3:28:50 PM
    Author     : ADMIN
--%>
<%@page import="Model.Voucher"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Voucher Management</title>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">       
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        .container {
            min-height: 100vh;
        }
        body {
            display: flex;
        }
        .sidebar {
            width: 250px;
            background-color: #FFCC00;
            height: 100vh;
            position: fixed;
            padding-top: 20px;
        }
        .content {
            margin-left: 260px;
            padding: 20px;
            width: 85%;
        }
        .discount {
            font-size: 18px;
            font-weight: bold;
            color: red;
        }
        .expired {
            color: gray;
            font-style: italic;
        }
        .alert {
            transition: opacity 0.5s ease-out;
        }
    </style>
</head>
<body class="bg-light sb-nav-fixed">
    <%@ include file="../../AdminLayout.jsp" %>
    <div class="content container-fluid px-4">
        <h2>Voucher Management</h2>

        <!-- Hiển thị thông báo thành công hoặc lỗi chung -->
        <% if (session.getAttribute("message") != null) { %>
            <div class="alert alert-success alert-temp" id="successAlert"><%= session.getAttribute("message") %></div>
            <% session.removeAttribute("message"); %>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger alert-temp" id="errorAlert"><%= request.getAttribute("error") %></div>
        <% } %>

        <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#addVoucherModal">Add New Voucher</button>
        <input type="text" id="searchInput" placeholder="Search voucher..." class="form-control mb-3" onkeyup="searchVouchers()">

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Voucher Code</th>
                    <th>Discount (%)</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Quantity</th>
                    <th>Used</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Voucher> voucherList = (List<Voucher>) request.getAttribute("voucherList");
                    java.time.LocalDate currentDate = java.time.LocalDate.now();
                    if (voucherList != null && !voucherList.isEmpty()) {
                        for (Voucher voucher : voucherList) {
                            boolean isExpired = voucher.getEndDate().isBefore(currentDate);
                %>
                <tr class="<%= isExpired ? "expired" : "" %>">
                    <td><%= voucher.getVoucherID() %></td>
                    <td><%= voucher.getVoucherCode() %></td>
                    <td class="discount"><%= voucher.getPercentDiscount() %>%</td>
                    <td><%= voucher.getStartDate() %></td>
                    <td><%= voucher.getEndDate() %></td>
                    <td><%= voucher.getQuantity() %></td>
                    <td><%= voucher.getUsedTime() %></td>
                    <td><%= isExpired ? "Expired" : "Active" %></td>
                    <td>
                        <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editVoucherModal<%= voucher.getVoucherID() %>">Edit</button>
                        <button class="btn btn-outline-danger btn-sm" onclick="confirmDelete('<%= voucher.getVoucherCode() %>')"><i class="fa fa-trash"></i></button>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="9">No voucher data.</td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <!-- Modal Thêm Voucher -->
        <div class="modal fade" id="addVoucherModal" tabindex="-1" aria-labelledby="addVoucherModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addVoucherModalLabel">Add New Voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <% if (request.getAttribute("modalError") != null) { %>
                            <div class="alert alert-danger alert-temp" id="addVoucherAlert"><%= request.getAttribute("modalError") %></div>
                        <% } %>
                        <form action="VoucherController" method="post">
                            <div class="row">
                                <div class="col">
                                    <div class="form-group mb-3">
                                        <label>Voucher Code</label>
                                        <input class="form-control <%= request.getAttribute("invalidVoucherCode") != null ? "is-invalid" : "" %>" 
                                               type="text" name="voucherCode" 
                                               value="<%= request.getAttribute("invalidVoucherCode") == null && request.getParameter("voucherCode") != null ? request.getParameter("voucherCode") : "" %>" 
                                               placeholder="Enter voucher code" required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>Discount (%)</label>
                                        <input class="form-control <%= request.getAttribute("invalidPercentDiscount") != null ? "is-invalid" : "" %>" 
                                               type="number" name="percentDiscount" min="0" max="100" 
                                               value="<%= request.getAttribute("invalidPercentDiscount") == null && request.getParameter("percentDiscount") != null ? request.getParameter("percentDiscount") : "" %>" 
                                               placeholder="Enter discount percentage" required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>Start Date</label>
                                        <input class="form-control <%= request.getAttribute("invalidStartDate") != null ? "is-invalid" : "" %>" 
                                               type="date" name="startDate" 
                                               value="<%= request.getAttribute("invalidStartDate") == null && request.getParameter("startDate") != null ? request.getParameter("startDate") : "" %>" 
                                               required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>End Date</label>
                                        <input class="form-control <%= request.getAttribute("invalidEndDate") != null ? "is-invalid" : "" %>" 
                                               type="date" name="endDate" 
                                               value="<%= request.getAttribute("invalidEndDate") == null && request.getParameter("endDate") != null ? request.getParameter("endDate") : "" %>" 
                                               required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>Quantity</label>
                                        <input class="form-control <%= request.getAttribute("invalidQuantity") != null ? "is-invalid" : "" %>" 
                                               type="number" name="quantity" min="0" 
                                               value="<%= request.getAttribute("invalidQuantity") == null && request.getParameter("quantity") != null ? request.getParameter("quantity") : "" %>" 
                                               placeholder="Enter total quantity" required>
                                    </div>
                                    <div class="row">
                                        <div class="col d-flex justify-content-end">
                                            <button class="btn btn-primary" type="submit">Add Voucher</button>
                                            <a class="btn btn-danger" data-bs-dismiss="modal" href="#" style="margin-left: 15px;">Cancel</a>
                                        </div>
                                    </div>
                                </div>
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
                    String submittedId = request.getParameter("id");
                    boolean isCurrentVoucher = submittedId != null && submittedId.equals(String.valueOf(voucher.getVoucherID()));
        %>
        <div class="modal fade" id="editVoucherModal<%= voucher.getVoucherID() %>" tabindex="-1" aria-labelledby="editVoucherModalLabel<%= voucher.getVoucherID() %>" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editVoucherModalLabel<%= voucher.getVoucherID() %>">Edit Voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <% if (isCurrentVoucher && request.getAttribute("modalError") != null) { %>
                            <div class="alert alert-danger alert-temp" id="editVoucherAlert<%= voucher.getVoucherID() %>"><%= request.getAttribute("modalError") %></div>
                        <% } %>
                        <form action="VoucherController" method="post">
                            <input type="hidden" name="id" value="<%= voucher.getVoucherID() %>">
                            <div class="row">
                                <div class="col">
                                    <div class="form-group mb-3">
                                        <label>Voucher Code</label>
                                        <input class="form-control <%= isCurrentVoucher && request.getAttribute("invalidVoucherCode") != null ? "is-invalid" : "" %>" 
                                               type="text" name="voucherCode" 
                                               value="<%= isCurrentVoucher && request.getAttribute("invalidVoucherCode") == null && request.getParameter("voucherCode") != null ? request.getParameter("voucherCode") : voucher.getVoucherCode() %>" 
                                               required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>Discount (%)</label>
                                        <input class="form-control <%= isCurrentVoucher && request.getAttribute("invalidPercentDiscount") != null ? "is-invalid" : "" %>" 
                                               type="number" name="percentDiscount" min="0" max="100" 
                                               value="<%= isCurrentVoucher && request.getAttribute("invalidPercentDiscount") == null && request.getParameter("percentDiscount") != null ? request.getParameter("percentDiscount") : voucher.getPercentDiscount() %>" 
                                               required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>Start Date</label>
                                        <input class="form-control <%= isCurrentVoucher && request.getAttribute("invalidStartDate") != null ? "is-invalid" : "" %>" 
                                               type="date" name="startDate" 
                                               value="<%= isCurrentVoucher && request.getAttribute("invalidStartDate") == null && request.getParameter("startDate") != null ? request.getParameter("startDate") : voucher.getStartDate() %>" 
                                               required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>End Date</label>
                                        <input class="form-control <%= isCurrentVoucher && request.getAttribute("invalidEndDate") != null ? "is-invalid" : "" %>" 
                                               type="date" name="endDate" 
                                               value="<%= isCurrentVoucher && request.getAttribute("invalidEndDate") == null && request.getParameter("endDate") != null ? request.getParameter("endDate") : voucher.getEndDate() %>" 
                                               required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label>Quantity</label>
                                        <input class="form-control <%= isCurrentVoucher && request.getAttribute("invalidQuantity") != null ? "is-invalid" : "" %>" 
                                               type="number" name="quantity" min="0" 
                                               value="<%= isCurrentVoucher && request.getAttribute("invalidQuantity") == null && request.getParameter("quantity") != null ? request.getParameter("quantity") : voucher.getQuantity() %>" 
                                               required>
                                    </div>
                                    <div class="row">
                                        <div class="col d-flex justify-content-end">
                                            <button class="btn btn-primary" type="submit">Save Changes</button>
                                            <a class="btn btn-danger" data-bs-dismiss="modal" href="#" style="margin-left: 15px;">Cancel</a>
                                        </div>
                                    </div>
                                </div>
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
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script>
        function confirmDelete(voucherCode) {
            if (confirm("Are you sure you want to delete this voucher?")) {
                window.location.href = "VoucherController?deleteVoucherCode=" + voucherCode;
            }
        }

        function searchVouchers() {
            let input = document.getElementById("searchInput").value.toLowerCase();
            let table = document.querySelector("table tbody");
            let rows = table.getElementsByTagName("tr");

            for (let i = 0; i < rows.length; i++) {
                let cols = rows[i].getElementsByTagName("td");
                let match = false;

                for (let j = 0; j < cols.length; j++) {
                    if (cols[j].textContent.toLowerCase().includes(input)) {
                        match = true;
                        break;
                    }
                }

                rows[i].style.display = match ? "" : "none";
            }
        }

        // Function to hide alerts after a timeout
        function hideAlert(alertElement) {
            if (alertElement) {
                setTimeout(() => {
                    alertElement.style.opacity = '0';
                    setTimeout(() => alertElement.style.display = 'none', 500); // Match transition duration
                }, 5000); // Hide after 5 seconds
            }
        }

        // Function to hide alert and remove red borders when typing in modal
        function setupInputListeners(modalId) {
            const modal = document.getElementById(modalId);
            const alert = modal.querySelector('.alert-temp');
            const inputs = modal.querySelectorAll('input');

            if (alert) {
                hideAlert(alert); // Start the timeout for auto-hiding
            }

            inputs.forEach(input => {
                input.addEventListener('input', () => {
                    // Hide alert
                    if (alert) {
                        alert.style.display = 'none';
                    }
                    // Remove red border (is-invalid class)
                    if (input.classList.contains('is-invalid')) {
                        input.classList.remove('is-invalid');
                    }
                });
            });
        }

        // Show modals and set up listeners if there’s an error
        document.addEventListener('DOMContentLoaded', () => {
            // Hide success/error alerts on main page
            hideAlert(document.getElementById('successAlert'));
            hideAlert(document.getElementById('errorAlert'));

            <% if (request.getAttribute("modalError") != null) { %>
                <% if (request.getParameter("id") != null) { %>
                    const editModal = new bootstrap.Modal(document.getElementById('editVoucherModal<%= request.getParameter("id") %>'));
                    editModal.show();
                    setupInputListeners('editVoucherModal<%= request.getParameter("id") %>');
                <% } else { %>
                    const addModal = new bootstrap.Modal(document.getElementById('addVoucherModal'));
                    addModal.show();
                    setupInputListeners('addVoucherModal');
                <% } %>
            <% } %>
        });
    </script>
</body>
</html>