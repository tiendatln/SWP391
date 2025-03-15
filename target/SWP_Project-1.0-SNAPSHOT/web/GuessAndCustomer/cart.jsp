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
            margin: 0;
            padding: 0;
        }

        .cart-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 30px;
            border-radius: 10px;
            background-color: #fff;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .cart-title {
            color: #333;
            font-weight: 700;
            margin-bottom: 30px;
            text-align: center;
        }

        .cart-table {
            width: 100%;
            border-radius: 8px;
            overflow: hidden;
            border-collapse: collapse;
        }

        .cart-table thead th {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 15px;
            text-align: center;
            white-space: nowrap;
        }

        .cart-table th:nth-child(2) {
            width: 150px;
        }

        .cart-table th:nth-child(3) {
            width: 250px;
        }

        .cart-table tbody tr {
            transition: background-color 0.3s ease;
        }

        .cart-table tbody tr:hover {
            background-color: #f8f9fa;
        }

        .cart-table td {
            vertical-align: middle;
            padding: 15px;
            text-align: center;
        }

        .cart-quantity-input {
            width: 80px;
            margin: 0 auto;
            border-radius: 5px;
            border: 1px solid #ced4da;
            padding: 5px;
            text-align: center;
            box-sizing: border-box;
        }

        .cart-item-price,
        .cart-item-total {
            color: #e74c3c;
            font-weight: 600;
        }

        .cart-delete-btn {
            padding: 6px 20px;
            border-radius: 20px;
            font-size: 14px;
            background-color: #dc3545;
            color: #fff;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .cart-delete-btn:hover {
            background-color: #c82333;
            transform: scale(1.05);
        }

        .cart-text-end {
            margin-top: 30px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 8px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        #cartTotal {
            color: #28a745;
            font-size: 1.5rem;
            margin: 0;
        }

        .cart-btn-success {
            padding: 12px 30px;
            font-size: 16px;
            border-radius: 25px;
            background-color: #28a745;
            color: #fff;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .cart-btn-success:hover {
            background-color: #218838;
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        .cart-alert-warning {
            margin-top: 20px;
            padding: 20px;
            border-radius: 8px;
            font-size: 1.1rem;
            text-align: center;
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }

        .cart-custom-checkbox {
            margin-right: 5px;
        }

        .cart-text-start {
            display: flex;
            align-items: center;
        }

        .user-id-display {
            font-size: 1.2rem;
            color: #007bff;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <jsp:include page="../../Header.jsp" />
    <div class="cart-container" id="cartContainer">
        <h2 class="cart-title">
            Your shopping cart
        </h2>

        <c:set var="cart" value="${sessionScope.cart}" />
        <c:choose>
            <c:when test="${not empty cart and not empty cart.cartItems}">
                <form id="checkoutForm" method="post" action="/OrderController/PrepareOrder">
                    <input type="text" name="userID" value="${sessionScope.userId}" hidden="true" >
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Select</th>
                                <th>Product name</th>
                                <th>Image</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Total</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="cartItems">
                            <c:forEach var="item" items="${cart.cartItems}">
                                <tr data-product-id="${item.product.productID}">
                                    <td>
                                        <input type="checkbox" class="cart-custom-checkbox cart-select-item" 
                                               id="select-${item.product.productID}" 
                                               name="productId/Quantity" value="${item.product.productID}/${item.quantity}"
                                               data-product-id="${item.product.productID}">
                                        <label for="select-${item.product.productID}" class="cart-custom-checkbox-label"></label>
                                    </td>
                                    <td>${item.product.productName}</td>
                                    <td><img src="${pageContext.request.contextPath}/link/img/${item.product.proImg}" alt="${item.product.productName}" width="100px" height="100px"></td>
                                    <td>
                                        <input type="number" class="cart-quantity-input" 
                                               name="quantityShow" value="${item.quantity}" min="1" 
                                               data-product-id="${item.product.productID}">
                                    </td>
                                    <td class="cart-item-price"><fmt:formatNumber value="${item.product.proPrice}" type="number" groupingUsed="true"/> đ</td>
                                    <td class="cart-item-total"><fmt:formatNumber value="${item.totalPrice}" type="number" groupingUsed="true"/> đ</td>
                                    <td>
                                        <button type="button" class="cart-delete-btn" 
                                                data-product-id="${item.product.productID}">Delete</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="cart-text-end">
                        <div class="cart-text-start">
                            <input type="checkbox" id="selectAll" class="cart-custom-checkbox">
                            <label for="selectAll" class="cart-custom-checkbox-label"></label> Select All
                        </div>
                        <h4>Total amount: 
                            <strong id="cartTotal"><fmt:formatNumber value="0" type="number" groupingUsed="true"/> đ</strong>
                        </h4>
                        <button type="submit" class="cart-btn-success" id="checkoutBtn">Payment</button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <p class="cart-alert-warning">Your cart is empty!</p>
            </c:otherwise>
        </c:choose>
    </div>
    <div style="background-color: rgb(31, 41, 55)">
        <jsp:include page="../../Footer.jsp" />
    </div>

    <!-- JavaScript -->
    <script>
        // Hàm định dạng số với dấu phân cách
        function formatNumber(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + " đ";
        }

        // Cập nhật số lượng
        document.querySelectorAll('.cart-quantity-input').forEach(input => {
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
                        const priceText = row.querySelector('.cart-item-price').textContent.replace(/[^0-9]/g, '');
                        const price = parseFloat(priceText);
                        const newTotal = price * quantity;
                        row.querySelector('.cart-item-total').textContent = formatNumber(newTotal);
                        updateCartTotal();
                    }
                })
                .catch(error => console.error('Error:', error));
            });
        });

        // Xóa sản phẩm
        document.querySelectorAll('.cart-delete-btn').forEach(button => {
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
                                <h2 class="cart-title">Your shopping cart<span class="user-id-display">(User ID: ${sessionScope.userId})</span></h2>
                                <p class="cart-alert-warning">Your cart is empty!</p>
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
                const checkbox = row.querySelector('.cart-select-item');
                if (checkbox && checkbox.checked) {
                    const itemTotal = parseFloat(row.querySelector('.cart-item-total').textContent.replace(/[^0-9]/g, ''));
                    total += itemTotal;
                }
            });
            document.getElementById('cartTotal').textContent = formatNumber(total);
        }

        // Xử lý checkbox "Chọn tất cả"
        document.getElementById('selectAll')?.addEventListener('change', function () {
            const isChecked = this.checked;
            document.querySelectorAll('.cart-select-item').forEach(checkbox => {
                checkbox.checked = isChecked;
            });
            updateCartTotal();
        });

        // Xử lý checkbox từng sản phẩm
        document.querySelectorAll('.cart-select-item').forEach(checkbox => {
            checkbox.addEventListener('change', function () {
                updateCartTotal();
                const allChecked = Array.from(document.querySelectorAll('.cart-select-item')).every(cb => cb.checked);
                document.getElementById('selectAll').checked = allChecked;
            });
        });

        // Xử lý nút thanh toán
        document.getElementById('checkoutBtn')?.addEventListener('click', function (e) {
            const selectedItems = document.querySelectorAll('.cart-select-item:checked');
            if (selectedItems.length === 0) {
                e.preventDefault();
                alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán!');
            } else {
                document.getElementById('checkoutForm').submit();
            }
        });
    </script>
</body>
</html>