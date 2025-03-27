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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <style>
        body { display: flex; background-color: #f8f9fa; }
        .content { margin-left: 260px; padding: 20px; width: 85%; }
        .container-fluid { min-height: 100vh; }
        .table th, .table td { vertical-align: middle; }
        .btn-primary, .btn-outline-danger { color: black; }
        .table thead th { background-color: #e9ecef; color: #000; }
    </style>
</head>
<body class="bg-light sb-nav-fixed">
    <%@ include file="../../AdminLayout.jsp" %>
    <div class="content container-fluid px-4">
        <h2 class="mb-4">Voucher Management</h2>

        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success" id="success-message">${sessionScope.message}</div>
            <% session.removeAttribute("message"); %>
        </c:if>
        <c:if test="${not empty requestScope.error}">
            <div class="alert alert-danger" id="error-message">${requestScope.error}</div>
        </c:if>

        <div class="row mb-3">
            <div class="col-md-9">
                <form action="VoucherController" method="get" class="d-flex">
                    <input type="text" name="search" class="form-control me-2" placeholder="Search by voucher code..." value="${requestScope.searchKeyword}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>
            <div class="col-md-3 text-end">
                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addVoucherModal">Add New Voucher</button>
            </div>
        </div>

        <table class="table table-bordered table-hover" id="voucherTable">
            <thead>
                <tr>
                    <th>Voucher ID</th>
                    <th>Voucher Code</th>
                    <th>Discount (%)</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Total</th>
                    <th>Used</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty voucherList}">
                        <c:forEach var="voucher" items="${voucherList}">
                            <tr>
                                <td>${voucher.voucherID}</td>
                                <td>${voucher.voucherCode}</td>
                                <td>${voucher.percentDiscount}%</td>
                                <td>${voucher.startDate}</td>
                                <td>${voucher.endDate}</td>
                                <td>${voucher.quantity}</td>
                                <td>${voucher.usedTime}</td>
                                <td>
                                    <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#editVoucherModal${voucher.voucherID}">Edit</button>
                                    <button class="btn btn-outline-danger btn-sm" onclick="confirmDelete(${voucher.voucherID})"><i class="fa fa-trash"></i></button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="text-center">No voucher data available.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <!-- Add Voucher Modal -->
        <div class="modal fade" id="addVoucherModal" tabindex="-1" aria-labelledby="addVoucherLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addVoucherLabel">Add New Voucher</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="VoucherController" method="post">
                            <div class="mb-3">
                                <label class="form-label">Voucher Code</label>
                                <input type="text" class="form-control" name="voucherCode" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Discount (%)</label>
                                <input type="number" class="form-control" name="percentDiscount" min="0" max="100" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Start Date</label>
                                <input type="date" class="form-control" name="startDate" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">End Date</label>
                                <input type="date" class="form-control" name="endDate" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Total</label>
                                <input type="number" class="form-control" name="quantity" min="0" required>
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-primary">Add</button>
                                <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Cancel</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Voucher Modal -->
        <c:forEach var="voucher" items="${voucherList}">
            <div class="modal fade" id="editVoucherModal${voucher.voucherID}" tabindex="-1" aria-labelledby="editVoucherLabel${voucher.voucherID}" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editVoucherLabel${voucher.voucherID}">Edit Voucher</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="VoucherController" method="post">
                                <input type="hidden" name="id" value="${voucher.voucherID}">
                                <div class="mb-3">
                                    <label class="form-label">Voucher Code</label>
                                    <input type="text" class="form-control" name="voucherCode" value="${voucher.voucherCode}" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Discount (%)</label>
                                    <input type="number" class="form-control" name="percentDiscount" min="0" max="100" value="${voucher.percentDiscount}" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Start Date</label>
                                    <input type="date" class="form-control" name="startDate" value="${voucher.startDate}" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">End Date</label>
                                    <input type="date" class="form-control" name="endDate" value="${voucher.endDate}" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Total</label>
                                    <input type="number" class="form-control" name="quantity" min="0" value="${voucher.quantity}" required>
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script>
        function confirmDelete(voucherID) {
            if (confirm("Are you sure you want to delete this voucher?")) {
                window.location.href = "VoucherController?deleteVoucherID=" + voucherID;
            }
        }

        document.addEventListener("DOMContentLoaded", function() {
            const successMessage = document.querySelector("#success-message");
            const errorMessage = document.querySelector("#error-message");

            if (successMessage) {
                setTimeout(function() { successMessage.style.display = "none"; }, 3000);
            }
            if (errorMessage) {
                setTimeout(function() { errorMessage.style.display = "none"; }, 3000);
            }
        });
    </script>
</body>
</html>