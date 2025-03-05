
<%-- 
    Document   : index
    Created on : Feb 12, 2025, 2:18:15 PM
    Author     : tiend
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách Laptop</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <style>
        .item {
            display: block;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .item:hover {
            transform: scale(1.05); /* Phóng to nhẹ khi hover */
            text-decoration: none; /* Loại bỏ gạch chân */
        }

        .item:hover .card {
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2); /* Đổ bóng khi hover */
        }

        .card {
            transition: box-shadow 0.3s ease, transform 0.3s ease;
            border-radius: 10px;
            overflow: hidden;
        }

        .card img {
            transition: transform 0.3s ease;
        }

        .item:hover .card img {
            transform: scale(1.1); /* Phóng to ảnh nhẹ khi hover */
        }

        .img{
            padding: 2%;

        }
    </style>
    <body>
        <div>
            <%@include file="../Header.jsp" %>
        </div>
        <div class="container mt-4" style="min-height: 58.6vh;">
            <h2 class="text-center mb-4">Danh sách Laptop</h2>
        <!-- FORM LỌC -->
        <form method="GET" action="" class="row g-3 mb-4">
            <div class="col-md-3">
                <label for="brand" class="form-label">Chọn thương hiệu:</label>
                <select name="brand" id="brand" class="form-select">
                    <option value="">Tất cả</option>
                    <option value="Asus">Asus</option>
                    <option value="HP">HP</option>
                    <option value="Dell">Dell</option>
                    <option value="Acer">Acer</option>
                    <option value="Lenovo">Lenovo</option>
                    <option value="MacBook">MacBook</option>
                </select>
            </div>
            <div class="col-md-3">
                <label for="minPrice" class="form-label">Giá tối thiểu:</label>
                <input type="number" name="minPrice" id="minPrice" class="form-control" placeholder="VNĐ">
            </div>
            <div class="col-md-3">
                <label for="maxPrice" class="form-label">Giá tối đa:</label>
                <input type="number" name="maxPrice" id="maxPrice" class="form-control" placeholder="VNĐ">
            </div>
            <div class="col-md-3 align-self-end">
                <button type="submit" class="btn btn-primary w-100">Lọc</button>
            </div>
        </form>
            <div class="row">
                <%-- Dữ liệu danh sách laptop --%>
                <%
                    String[][] laptops = {
                        {"Asus Vivobook Go 15", "12.590.000₫", "/link/img/cat.jpg"},
                        {"HP 15s i3", "13.690.000₫", "/link/img/cat.jpg"},
                        {"Dell Inspiron 15", "16.490.000₫", "/link/img/cat.jpg"},
                        {"Acer Aspire 3", "10.590.000₫", "/link/img/cat.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"},
                        {"Lenovo Gaming LQO", "17.690.000₫", "/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg"}
                    };

                    int itemsPerPage = 20; // Số laptop hiển thị trên mỗi trang
                    int totalItems = laptops.length;
                    int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

                    // Lấy trang hiện tại từ request (mặc định là 1)
                    int currentPage = 1;
                    String pageParam = request.getParameter("page");
                    if (pageParam != null) {
                        currentPage = Integer.parseInt(pageParam);
                    }

                    int start = (currentPage - 1) * itemsPerPage;
                    int end = Math.min(start + itemsPerPage, totalItems);

                    for (int i = start; i < end; i++) {

                %>

                <div class="col-md-3 mb-3">
                    <a class="item no-underline" href="/ProductController/DetailProductCustomer">
                        <div class="card">
                            <div class="img">
                                <img src="<%= laptops[i][2]%>" class="card-img img" style=" border-radius: 20px;" alt="<%= laptops[i][0]%>">
                            </div>
                            <div class="card-body">
                                <h5 class="card-title"><%= laptops[i][0]%></h5>
                                <p class="card-text text-danger fw-bold"><%= laptops[i][1]%></p>
                            </div>
                        </div>
                    </a>
                </div>
                <% }%>
            </div>
            
        </div>
        <div>
            <!-- PHÂN TRANG -->
            <nav>
                <ul class="pagination justify-content-center">
                    <% if (currentPage > 1) {%>
                    <li class="page-item">
                        <a class="page-link" href="?page=<%= currentPage - 1%>">Trước</a>
                    </li>
                    <% } %>

                    <% for (int i = 1; i <= totalPages; i++) {%>
                    <li class="page-item <%= (i == currentPage) ? "active" : ""%>">
                        <a class="page-link" href="?page=<%= i%>"><%= i%></a>
                    </li>
                    <% } %>

                    <% if (currentPage < totalPages) {%>
                    <li class="page-item">
                        <a class="page-link" href="?page=<%= currentPage + 1%>">Sau</a>
                    </li>
                    <% }%>
                </ul>
            </nav>
                    <%@include file="../Footer.jsp" %>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
