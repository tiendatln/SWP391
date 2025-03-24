<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout - Modern Store</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                line-height: 1.6;
            }

            .checkout-container {
                padding: 40px 0;
                max-width: 1200px;
            }

            .checkout-card {
                border: none;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                background-color: #fff;
                margin-bottom: 30px;
                transition: all 0.3s ease;
            }

            .checkout-card:hover {
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
            }

            .checkout-step {
                display: flex;
                align-items: center;
                padding: 25px;
            }

            .checkout-step-icon {
                width: 60px;
                height: 60px;
                background-color: #e9ecef;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #495057;
                flex-shrink: 0;
            }

            .checkout-form-control {
                border-radius: 8px;
                border: 1px solid #dee2e6;
                padding: 10px 15px;
                transition: border-color 0.3s ease;
            }

            .checkout-form-control:focus {
                border-color: #0d6efd;
                box-shadow: 0 0 5px rgba(13, 110, 253, 0.2);
            }

            .checkout-order-summary {
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                background-color: #fff;
            }

            .checkout-product-img {
                border-radius: 8px;
                object-fit: cover;
                border: 1px solid #eee;
            }

            .checkout-btn {
                padding: 10px 20px;
                border-radius: 8px;
                font-weight: 500;
                transition: all 0.3s ease;
            }

            .checkout-btn-primary {
                background-color: #0d6efd;
                border-color: #0d6efd;
                color: #fff;
            }

            .checkout-btn-primary:hover {
                background-color: #0b5ed7;
                border-color: #0a58ca;
                transform: translateY(-2px);
            }

            .checkout-btn-outline {
                border-color: #6c757d;
                color: #6c757d;
            }

            .checkout-btn-outline:hover {
                background-color: #6c757d;
                color: #fff;
                transform: translateY(-2px);
            }

            .checkout-form-label {
                font-weight: 500;
                color: #495057;
                margin-bottom: 5px;
            }
        </style>
    </head>
    <body>
        <%@include file="../Header.jsp" %>
        <div class="checkout-container container">
            <div class="row g-4">
                <div class="col-lg-8">
                    <div class="card checkout-card checkout-step">
                        <div class="checkout-step-icon">
                            <i class="bx bxs-receipt fs-4"></i>
                        </div>
                        <div class="ms-5 ps-4 w-100">
                            <h4 class="mb-3">Billing Information</h4>
                            <c:set var="account" value="${sessionScope.cartOrder[0].account}" />
                            <c:set var="subtotal" value="0" />
                            <c:forEach items="${sessionScope.cartOrder}" var="cartItem">
                                <c:set var="subtotal" value="${subtotal + (cartItem.product.proPrice * cartItem.quantity)}" />
                            </c:forEach>
                            <c:set var="discount" value="${subtotal * 0.1}" />
                            <c:set var="total" value="${subtotal - discount}" />
                            <form method="post" action="/OrderController/ConfirmOrder">

                                <input name="priceTotal" value="${total}" hidden="">

                                <div class="row g-3">
                                    <div class="col-md-4">
                                        <label class="checkout-form-label">Full Name</label>
                                        <input type="text" class="form-control checkout-form-control" value="${account.username}" disabled="true" placeholder="Enter your name">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="checkout-form-label">Email</label>
                                        <input type="email" name="email" class="form-control checkout-form-control" value="${account.email}" placeholder="Enter email">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="checkout-form-label">Phone</label>
                                        <input type="text" name="phone" class="form-control checkout-form-control" value="${account.phoneNumber}" placeholder="Enter phone">
                                    </div>
                                    <div class="col-12">
                                        <label class="checkout-form-label">Address</label>
                                        <textarea class="form-control checkout-form-control" rows="3" name="address" placeholder="Enter full address">${account.address}</textarea>
                                    </div>
                                    <div class="col-12">
                                        <label class="checkout-form-label">Note</label>
                                        <textarea class="form-control checkout-form-control" name="note" rows="3" ></textarea>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="checkout-form-label">Voucher</label>
                                        <select name="voucher" id="checkout-form-label">
                                            <c:forEach items="${sessionScope.voucher}" var="voucher">
                                                <option value="${voucher.voucherCode}">${voucher.voucherCode}: <span style="color: green">${voucher.percentDiscount}%</span></option>
                                            </c:forEach>
                                        </select>
                                        <div class="d-flex justify-content-between mt-4">
                                            <a href="/web/index.jsp" class="btn checkout-btn checkout-btn-outline btn-outline-danger">
                                                <i class="bx bx-arrow-left me-1"></i> Continue Shopping
                                            </a>
                                            <button type="submit" class="btn checkout-btn checkout-btn-primary btn-outline-primary">
                                                <i class="bx bx-cart me-1"></i> Proceed to Payment
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>


                </div>

                <div class="col-lg-4">
                    <div class="card checkout-order-summary sticky-top" style="top: 20px;">
                        <div class="p-4 border-bottom">
                            <h5 class="mb-0">Order Summary <span class="float-end"></span></h5>
                        </div>
                        <div class="p-4">
                            <c:forEach items="${sessionScope.cartOrder}" var="cartItem">
                                <div class="d-flex align-items-center mb-4">
                                    <img src="../link/img/${cartItem.product.proImg}" class="checkout-product-img me-3" alt="product" width="80">
                                    <div>
                                        <h6 class="mb-1">${cartItem.product.productName}</h6>
                                        <p class="text-muted mb-0">
                                            $<fmt:formatNumber value="${cartItem.product.proPrice}" type="number" groupingUsed="true"/> 
                                            x ${cartItem.quantity} = 
                                            $<fmt:formatNumber value="${cartItem.product.proPrice * cartItem.quantity}" type="number" groupingUsed="true"/>
                                        </p>
                                    </div>
                                </div>
                            </c:forEach>



                            <div class="border-top pt-3">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Subtotal</span>
                                    <span>$<fmt:formatNumber value="${subtotal}" type="number" groupingUsed="true"/></span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Discount</span>
                                    <span>- $<fmt:formatNumber value="${discount}" type="number" groupingUsed="true"/></span>
                                </div>
                                <div class="d-flex justify-content-between pt-3 border-top">
                                    <h5>Total</h5>
                                    <h5>$<fmt:formatNumber value="${total}" type="number" groupingUsed="true"/></h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
                                <!-- Thêm đoạn script này vào trước thẻ đóng </body> trong file JSP -->
<script>
    // Lấy dữ liệu voucher từ sessionScope và chuyển thành JSON
    const vouchers = [
        <c:forEach items="${sessionScope.voucher}" var="voucher" varStatus="loop">
            {
                code: "${voucher.voucherCode}",
                percent: ${voucher.percentDiscount}
            }${!loop.last ? ',' : ''}
        </c:forEach>
    ];

    // Lấy các element cần thao tác
    const voucherSelect = document.getElementById('checkout-form-label');
    const subtotalElement = document.querySelector('.d-flex.mb-2 span:last-child');
    const discountElement = document.querySelector('.d-flex.mb-2:nth-child(2) span:last-child');
    const totalElement = document.querySelector('.d-flex.pt-3.border-top h5:last-child');

    // Lấy giá trị subtotal ban đầu
    const subtotalText = "${subtotal}";
    const subtotal = parseFloat(subtotalText.replace(/[^0-9.-]+/g,""));

    // Hàm định dạng số tiền
    function formatNumber(number) {
        return '$' + number.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    }

    // Hàm cập nhật discount và total
    function updatePrice() {
        const selectedVoucherCode = voucherSelect.value;
        const selectedVoucher = vouchers.find(v => v.code === selectedVoucherCode);
        
        // Tính discount dựa trên percentDiscount của voucher
        const discountPercent = selectedVoucher ? selectedVoucher.percent / 100 : 0.1; // Mặc định 10% nếu không chọn
        const discount = subtotal * discountPercent;
        const total = subtotal - discount;

        // Cập nhật giá trị lên giao diện
        discountElement.textContent = '- ' + formatNumber(discount);
        totalElement.textContent = formatNumber(total);
        
        // Cập nhật giá trị hidden input để gửi form
        document.querySelector('input[name="priceTotal"]').value = total;
    }

    // Thêm event listener cho select
    voucherSelect.addEventListener('change', updatePrice);

    // Gọi hàm lần đầu để hiển thị giá trị ban đầu
    updatePrice();
</script>
                                    <%@include file="../Footer.jsp" %>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>