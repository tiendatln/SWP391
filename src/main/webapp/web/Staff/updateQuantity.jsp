<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cập Nhật Số Lượng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <style>
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
            <h2 class="mt-4">Cập Nhật Số Lượng Sản Phẩm</h2>
            <form id="productForm" class="mb-4">
                <div class="mb-3">
                    <label for="searchProduct" class="form-label">Tìm kiếm sản phẩm</label>
                    <input type="text" class="form-control" id="searchProduct" placeholder="Nhập tên sản phẩm...">
                </div>
                <div class="mb-3">
                    <label for="quantity" class="form-label">Số lượng</label>
                    <input type="number" class="form-control" id="quantity" min="1">
                </div>
                <button type="button" class="btn btn-success" onclick="addProduct()">Thêm</button>
            </form>
            
            <table class="table table-bordered mt-4">
                <thead class="table-dark">
                    <tr>
                        <th>Hình Ảnh</th>
                        <th>Tên Sản Phẩm</th>
                        <th>Số Lượng</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody id="productTable">
                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td><img src='${product.imageUrl}' alt='${product.name}' width='50' class='img-thumbnail'></td>
                            <td>${product.name}</td>
                            <td>
                                <input type='number' class='form-control' value='${product.quantity}' min='1' onchange='updateQuantity(this)'>
                            </td>
                            <td>
                                <button class='btn btn-warning btn-sm' onclick='editQuantity(this)'>Chỉnh sửa</button>
                                <button class='btn btn-danger btn-sm' onclick='removeProduct(this)'>Xóa</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <div class="mt-3">
                <button type="submit" class="btn btn-primary">Cập Nhật</button>
                <a href="productManagement.jsp" class="btn btn-secondary">Quay Lại</a>
            </div>
        </div>

        <script>
            function addProduct() {
                let productName = document.getElementById("searchProduct").value.trim();
                let quantity = parseInt(document.getElementById("quantity").value);
                let imageUrl = "default-image.jpg"; // Đường dẫn hình ảnh mặc định
                
                if (!productName || quantity < 1) {
                    alert("Vui lòng nhập tên sản phẩm và số lượng hợp lệ!");
                    return;
                }

                let table = document.getElementById("productTable");
                let row = table.insertRow();
                row.innerHTML = `
                    <td><img src='${imageUrl}' alt='${productName}' width='50' class='img-thumbnail'></td>
                    <td>${productName}</td>
                    <td>
                        <input type='number' class='form-control' value='${quantity}' min='1' onchange='updateQuantity(this)'>
                    </td>
                    <td>
                        <button class='btn btn-warning btn-sm' onclick='editQuantity(this)'>Chỉnh sửa</button>
                        <button class='btn btn-danger btn-sm' onclick='removeProduct(this)'>Xóa</button>
                    </td>`;
                
                document.getElementById("searchProduct").value = "";
                document.getElementById("quantity").value = "";
            }

            function removeProduct(btn) {
                if (confirm("Bạn có chắc muốn xóa sản phẩm này?")) {
                    btn.closest("tr").remove();
                }
            }

            function updateQuantity(input) {
                if (input.value < 1) {
                    alert("Số lượng không hợp lệ!");
                    input.value = 1;
                }
            }

            function editQuantity(btn) {
                let row = btn.closest("tr");
                row.cells[2].querySelector("input").focus();
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
