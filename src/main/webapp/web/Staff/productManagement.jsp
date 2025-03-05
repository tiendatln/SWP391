<%-- 
    Document   : product
    Created on : Feb 12, 2025, 12:39:16 AM
    Author     : Nguyễn Trường Vinh _ vinhntca181278
--%>

<%@page import="Model.Category"%>
<%@page import="Model.MainCategory"%>
<%@page import="DAOs.CategoryDAO"%>
<%@page import="DAOs.MainCategoryDAO"%>
<%@page import="java.util.List"%>
<%@page import="Model.Product"%>
<%@page import="DAOs.ProductDAO"%>
<%@page import="DB.DBConnection"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    Connection conn = DBConnection.connect();

    ProductDAO productDAO = new ProductDAO();
    CategoryDAO categoryDAO = new CategoryDAO();

    List<Product> productList = productDAO.getAllProducts();
    List<Category> category = categoryDAO.getAllCategories(); // Lấy tất cả danh mục

    request.setAttribute("category", category);
    request.setAttribute("productList", productList);
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Sản Phẩm</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            .container{
                min-height: 100vh;
            }
            .product-card {
                border: 1px solid #ddd;
                border-radius: 10px;
                padding: 15px;
                text-align: center;
                background: #fff;
            }
            .product-card img {
                width: 100%;
                border-radius: 10px;
            }
            .price {
                font-size: 18px;
                font-weight: bold;
                color: red;
            }
            .old-price {
                text-decoration: line-through;
                color: gray;
            }
            .discount {
                color: #f00;
                font-size: 14px;
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
        </style>
    </head>
    <body class="bg-light sb-nav-fixed">
        <%@include file="../../AdminLayout.jsp" %>
        <div class="content container-fluid px-4">
            <h2>Quản Lý Sản Phẩm</h2>
            <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#addProductModal">Thêm Sản Phẩm</button>

            <input type="text" class="form-control mb-3" placeholder="Tìm kiếm sản phẩm...">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Hình ảnh</th>
                        <th>Tên Sản Phẩm</th>
                        <th>Giá</th>
                        <th>Số Lượng</th>
                        <th>Trạng Thái</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td><img src="${product.proImg}" alt="${product.productName}" width="50"></td>
                            <td>${product.productName}</td>
                            <td class="price"><fmt:formatNumber value="${product.proPrice}" type="currency" currencySymbol="đ"/></td>
                            <td>${product.proQuantity}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.proState == 1}">Đang Bán</c:when>
                                    <c:otherwise>Ngừng Kinh Doanh</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="editProduct.jsp?id=${product.productID}" class="btn btn-warning btn-sm">Sửa</a>
                                <a href="deleteProduct?id=${product.productID}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa?')">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addProductModalLabel">Thêm Sản Phẩm</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="/ProductController/ProductManagement" method="POST" enctype="multipart/form-data">
                                <input type="hidden" name="action" value="addProduct">

                                <div class="mb-3">
                                    <label class="form-label">Tên sản phẩm</label>
                                    <input type="text" class="form-control" name="productName" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Giá</label>
                                    <input type="number" class="form-control" name="proPrice" step="0.01" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Mô tả</label>
                                    <textarea class="form-control" name="proDescription" rows="3"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Danh mục</label>
                                    <select class="form-control" name="proCategory" id="categorySelect" required>
                                        <option value="">-- Chọn danh mục --</option>
                                        <c:forEach var="category" items="${category}">
                                            <option value="${category.categoryID}">${category.type}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Số lượng</label>
                                    <input type="number" class="form-control" name="proQuantity" min="0" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Trạng thái</label>
                                    <select class="form-control" name="proState">
                                        <option value="1">Đang Bán</option>
                                        <option value="0">Ngừng Kinh Doanh</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Hình ảnh</label>
                                    <input type="file" class="form-control" name="proImg" accept="image/*" required>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                    <button type="submit" class="btn btn-primary">Lưu</button>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>


            <div class="modal fade" id="editProductModal" tabindex="-1" aria-labelledby="editProductModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editProductModalLabel">Chỉnh Sửa Sản Phẩm</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form>
                                <div class="mb-3">
                                    <label class="form-label">Tên sản phẩm</label>
                                    <input type="text" class="form-control" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Giá</label>
                                    <input type="number" class="form-control" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Mô tả</label>
                                    <input type="number" class="form-control">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Danh mục</label>
                                    <input type="number" class="form-control" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Trạng thái</label>
                                    <select class="form-control">
                                        <option>Đang Bán</option>
                                        <option>Ngừng Kinh Doanh</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Hình ảnh</label>
                                    <input type="file" class="form-control">
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="button" class="btn btn-primary">Lưu Thay Đổi</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>

