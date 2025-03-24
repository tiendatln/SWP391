<%-- 
    Document   : product
    Created on : Feb 12, 2025, 12:39:16 AM
    Author     : Nguyễn Trường Vinh _ vinhntca181278
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shop Điện Thoại</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
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
    </style>
</head>
<body class="bg-light">
    <div><%@include file="../Header.jsp" %></div>
        <div class="container mt-4">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#filterModal">Lọc</button>
        <div class="modal fade" id="filterModal" tabindex="-1" aria-labelledby="filterModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="filterModalLabel">Bộ lọc sản phẩm</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <h6>Hãng</h6>
                        <div class="d-flex flex-wrap gap-2">
                            <button class="btn btn-outline-secondary">Samsung</button>
                            <button class="btn btn-outline-secondary">iPhone</button>
                            <button class="btn btn-outline-secondary">Oppo</button>
                        </div>
                        <h6 class="mt-3">Giá</h6>
                        <div class="d-flex flex-wrap gap-2">
                            <button class="btn btn-outline-secondary">Dưới 2 triệu</button>
                            <button class="btn btn-outline-secondary">Từ 2 - 4 triệu</button>
                        </div>
                        <h6 class="mt-3">Nhu cầu</h6>
                        <div class="d-flex flex-wrap gap-2">
                            <button class="btn btn-outline-secondary">Chơi game</button>
                            <button class="btn btn-outline-secondary">Pin trâu</button>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                        <button type="button" class="btn btn-primary">Áp dụng</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-3">
            <div class="col-md-3">
                <div class="product-card">
                    <img src="samsung-s25.jpg" alt="Samsung Galaxy S25 5G">
                    <h6>Samsung Galaxy S25 5G</h6>
                    <p>Full HD+ | 6.2"</p>
                    <p class="price">18.990.000đ <span class="old-price">22.990.000đ</span> <span class="discount">-17%</span></p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="product-card">
                    <img src="samsung-s25-ultra.jpg" alt="Samsung Galaxy S25 Ultra">
                    <h6>Samsung Galaxy S25 Ultra 5G</h6>
                    <p>Quad HD+ (2K+) | 6.9"</p>
                    <p class="price">29.990.000đ <span class="old-price">33.990.000đ</span> <span class="discount">-11%</span></p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="product-card">
                    <img src="iphone-16-pro-max.jpg" alt="iPhone 16 Pro Max">
                    <h6>iPhone 16 Pro Max</h6>
                    <p>Super Retina XDR | 6.9"</p>
                    <p class="price">32.890.000đ <span class="old-price">34.990.000đ</span> <span class="discount">-6%</span></p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="product-card">
                    <img src="samsung-s24-ultra.jpg" alt="Samsung Galaxy S24 Ultra">
                    <h6>Samsung Galaxy S24 Ultra 5G</h6>
                    <p>Quad HD+ (2K+) | 6.8"</p>
                    <p class="price">25.990.000đ <span class="old-price">29.990.000đ</span> <span class="discount">-13%</span></p>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <div><%@include file="../Footer.jsp" %></div>
</body>
</html>
