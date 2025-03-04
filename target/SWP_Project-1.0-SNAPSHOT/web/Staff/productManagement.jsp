<%-- 
    Document   : product
    Created on : Feb 12, 2025, 12:39:16 AM
    Author     : Nguyễn Trường Vinh _ vinhntca181278
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
            <a href="/ProductController/UpdateQuantity" class="btn btn-success mb-3" >Thêm Sản Phẩm</a>
            <input type="text" class="form-control mb-3" placeholder="Tìm kiếm sản phẩm...">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Hình ảnh</th>
                        <th>Tên Sản Phẩm</th>
                        <th>Giá</th>
                        <th>Giá Cũ</th>
                        <th>Giảm Giá</th>
                        <th>Số Lượng</th>
                        <th>Trạng Thái</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><img src="samsung-s25.jpg" alt="Samsung Galaxy S25 5G" width="50"></td>
                        <td>Samsung Galaxy S25 5G</td>
                        <td class="price">18.990.000đ</td>
                        <td class="old-price">22.990.000đ</td>
                        <td class="discount">-17%</td>
                        <td>25</td>
                        <td>Đang Bán</td>
                        <td>
                            <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#editProductModal">Sửa</button>
                            <button class="btn btn-danger btn-sm">Xóa</button>

                        </td>
                    </tr>
                    <tr>
                        <td><img src="iphone-16-pro-max.jpg" alt="iPhone 16 Pro Max" width="50"></td>
                        <td>iPhone 16 Pro Max</td>
                        <td class="price">32.890.000đ</td>
                        <td class="old-price">34.990.000đ</td>
                        <td class="discount">-6%</td>
                        <td>15</td>
                        <td>Ngừng Kinh Doanh</td>
                        <td>
                            <button class="btn btn-warning btn-sm" data-bs-toggle="modal" data-bs-target="#editProductModal">Sửa</button>
                            <button class="btn btn-danger btn-sm">Xóa</button>                        
                        </td>
                    </tr>
                </tbody>
            </table>

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
                                    <input type="text" class="form-control">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Giá</label>
                                    <input type="number" class="form-control">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Giá cũ</label>
                                    <input type="number" class="form-control">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Giảm giá (%)</label>
                                    <input type="number" class="form-control">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Số lượng</label>
                                    <input type="number" class="form-control">
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

