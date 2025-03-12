<%-- 
    Document   : cart
    Created on : 25 Feb 2025, 09:51:38
    Author     : hnpt6504
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="Model.Cart" %>
<%@ page import="Model.CartItem" %>

<jsp:useBean id="cart" class="Model.Cart" scope="session"/>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Giỏ hàng</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            /* Giữ nguyên phần CSS của bạn */
            body {
                background-color: #f5f5f5;
                font-family: 'Arial', sans-serif;
            }

            .container {
                max-width: 1200px;
                margin-top: 40px;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            }

            h2 {
                color: #333;
                font-weight: 700;
                margin-bottom: 30px;
                text-align: center;
            }

            .table {
                border-radius: 8px;
                overflow: hidden;
            }

            .table thead th {
                background-color: #007bff;
                color: white;
                border: none;
                padding: 15px;
                text-align: center;
                white-space: nowrap;
            }
            .table th:nth-child(3) {
                width: 250px;
            } /* Thu nhỏ cột hình ảnh */
            .table th:nth-child(2) {
                width: 150px;
            } /* Điều chỉnh tên sản phẩm */
            .table tbody tr {
                transition: background-color 0.3s;
            }

            .table tbody tr:hover {
                background-color: #f8f9fa;
            }

            .table td {
                vertical-align: middle;
                padding: 15px;
                text-align: center;
            }

            .quantity-input {
                width: 80px !important;
                margin: 0 auto;
                border-radius: 5px;
                border: 1px solid #ced4da;
                padding: 5px;
                text-align: center;
            }

            .item-price, .item-total {
                color: #e74c3c;
                font-weight: 600;
            }

            .delete-btn {
                padding: 6px 20px;
                border-radius: 20px;
                font-size: 14px;
                transition: all 0.3s;
            }

            .delete-btn:hover {
                background-color: #dc3545;
                transform: scale(1.05);
            }

            .text-end {
                margin-top: 30px;
                padding: 20px;
                background-color: #f8f9fa;
                border-radius: 8px;
            }

            #cartTotal {
                color: #28a745;
                font-size: 1.5rem;
            }

            .btn-success {
                padding: 12px 30px;
                font-size: 16px;
                border-radius: 25px;
                transition: all 0.3s;
            }

            .btn-success:hover {
                background-color: #218838;
                transform: translateY(-2px);
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
            }

            .alert-warning {
                margin-top: 20px;
                padding: 20px;
                border-radius: 8px;
                font-size: 1.1rem;
                text-align: center;
                background-color: #fff3cd;
                color: #856404;

            }

            img {
                border-radius: 5px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s;
            }
            /* Responsive adjustments */
            @media (max-width: 768px) {
                .table td, .table th {
                    font-size: 14px;
                    padding: 10px;
                }

                .quantity-input {
                    width: 60px !important;
                }

                .btn-success {
                    width: 100%;
                    padding: 10px;
                }
            }
            /* Tùy chỉnh checkbox */
            .custom-checkbox {
                display: none; /* Ẩn checkbox mặc định */
            }

            .custom-checkbox-label {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                width: 24px;
                height: 24px;
                border: 2px solid #007bff;
                border-radius: 6px;
                cursor: pointer;
                transition: all 0.3s ease;
                background-color: white;
            }

            .custom-checkbox:checked + .custom-checkbox-label {
                background-color: #007bff;
                border-color: #0056b3;
            }

            .custom-checkbox-label::after {
                content: "✔";
                color: white;
                font-size: 16px;
                display: none;
            }

            .custom-checkbox:checked + .custom-checkbox-label::after {
                display: block;
            }

            /* Điều chỉnh khoảng cách giữa checkbox và văn bản */
            .table td:first-child,
            .table th:first-child {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../../Header.jsp" />
        <div class="container mt-4" id="cartContainer">
            <h2>Giỏ hàng của bạn</h2>

            <c:choose>
                <c:when test="${not empty cart.cartItems}">
                    <table class="table table-striped">
                        <thead class="table-dark">
                            <tr>
                                <th>Select</th>
                                <th>Tên sản phẩm</th>
                                <th>Hình ảnh</th>
                                <th>Số lượng</th>
                                <th>Giá</th>
                                <th>Tổng</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody id="cartItems">

                            <c:forEach var="item" items="${cart.cartItems}">
                                <tr data-product-id="${item.product.productID}">

                                    <td>
                                        <input type="checkbox" class="custom-checkbox select-item" id="select-${item.product.productID}" data-product-id="${item.product.productID}">
                                        <label for="select-${item.product.productID}" class="custom-checkbox-label"></label>
                                    </td>
                                    <td>${item.product.productName}</td>
                                    <td><img src="/link/img/${item.product.proImg}" alt="${item.product.productName}" width="100px" height="100px"></td>
                                    <td>
                                        <input type="number" class="form-control quantity-input" 
                                               name="quantity" value="${item.quantity}" min="1" 
                                               data-product-id="${item.product.productID}">
                                    </td>
                                    <td class="item-price"><fmt:formatNumber value="${item.product.proPrice}" type="number" groupingUsed="true"/> đ</td>
                                    <td class="item-total"><fmt:formatNumber value="${item.totalPrice}" type="number" groupingUsed="true"/> đ</td>
                                    <td>
                                        <button class="btn btn-danger delete-btn" 
                                                data-product-id="${item.product.productID}">Xóa</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div >

                        <div class="text-end ">
                            <div class="text-start"><input type="checkbox" id="selectAll" class="custom-checkbox">
                                <label for="selectAll" class="custom-checkbox-label"></label>  Chọn tất cả
                            </div>
                            <h4>Tổng tiền: 
                                <strong id="cartTotal"><fmt:formatNumber value="0" type="number" groupingUsed="true"/> đ</strong>
                            </h4>
                            <a href="checkout.jsp" class="btn btn-success" id="checkoutBtn">Thanh toán</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="alert alert-warning">Giỏ hàng của bạn đang trống!</p>
                </c:otherwise>
            </c:choose>
        </div>
        

        <!-- JavaScript xử lý cập nhật số lượng, xóa sản phẩm và tính tổng tiền -->
        <script>
            // Hàm định dạng số với dấu phân cách
            function formatNumber(number) {
                return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + " đ";
            }

            // Cập nhật số lượng
            document.querySelectorAll('.quantity-input').forEach(input => {
                input.addEventListener('change', function () {
                    const productId = this.getAttribute('data-product-id');
                    const quantity = this.value;

                    fetch('${pageContext.request.contextPath}/CartController', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: 'action=update&productId=' + encodeURIComponent(productId) + '&quantity=' + encodeURIComponent(quantity)
                    })
                            .then(response => response.json())
                            .then(result => {
                                if (result.status === 'success') {
                                    const row = this.closest('tr');
                                    const priceText = row.querySelector('.item-price').textContent.replace(/[^0-9]/g, '');
                                    const price = parseFloat(priceText);
                                    const newTotal = price * quantity;
                                    row.querySelector('.item-total').textContent = formatNumber(newTotal);
                                    updateCartTotal();
                                }
                            })
                            .catch(error => console.error('Lỗi:', error));
                });
            });

            // Xóa sản phẩm
            document.querySelectorAll('.delete-btn').forEach(button => {
                button.addEventListener('click', function () {
                    const productId = this.getAttribute('data-product-id');

                    fetch('${pageContext.request.contextPath}/CartController', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: 'action=delete&productId=' + encodeURIComponent(productId)
                    })
                            .then(response => response.json())
                            .then(result => {
                                if (result.status === 'success') {
                                    const row = this.closest('tr');
                                    row.remove();
                                    const remainingItems = document.querySelectorAll('#cartItems tr');
                                    if (remainingItems.length === 0) {
                                        document.getElementById('cartContainer').innerHTML = `
                                    <h2>Giỏ hàng của bạn</h2>
                                    <p class="alert alert-warning">Giỏ hàng của bạn đang trống!</p>
                                `;
                                    } else {
                                        updateCartTotal();
                                    }
                                }
                            })
                            .catch(error => console.error('Lỗi:', error));
                });
            });

            // Hàm cập nhật tổng tiền dựa trên các sản phẩm được chọn
            function updateCartTotal() {
                let total = 0;
                document.querySelectorAll('#cartItems tr').forEach(row => {
                    const checkbox = row.querySelector('.select-item');
                    if (checkbox.checked) {
                        const itemTotal = parseFloat(row.querySelector('.item-total').textContent.replace(/[^0-9]/g, ''));
                        total += itemTotal;
                    }
                });
                document.getElementById('cartTotal').textContent = formatNumber(total);
            }

            // Xử lý checkbox "Chọn tất cả"
            document.getElementById('selectAll').addEventListener('change', function () {
                const isChecked = this.checked;
                document.querySelectorAll('.select-item').forEach(checkbox => {
                    checkbox.checked = isChecked;
                });
                updateCartTotal();
            });

            // Xử lý checkbox từng sản phẩm
            document.querySelectorAll('.select-item').forEach(checkbox => {
                checkbox.addEventListener('change', function () {
                    updateCartTotal();
                    // Kiểm tra nếu tất cả checkbox đều được chọn
                    const allChecked = Array.from(document.querySelectorAll('.select-item')).every(cb => cb.checked);
                    document.getElementById('selectAll').checked = allChecked;
                });
            });

            // Xử lý nút thanh toán
            document.getElementById('checkoutBtn').addEventListener('click', function (e) {
                const selectedItems = document.querySelectorAll('.select-item:checked');
                if (selectedItems.length === 0) {
                    e.preventDefault();
                    alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán!');
                }
            });
        </script>
    </body>
    <div style="background-color: rgb(31, 41, 55)">
    <jsp:include page="../../Footer.jsp" />
    </div>
</html>