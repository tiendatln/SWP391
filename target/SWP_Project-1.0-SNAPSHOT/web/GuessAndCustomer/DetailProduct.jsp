<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết sản phẩm</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 1200px;
            margin-top: 30px;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        .product-img {
            max-width: 100%;
            height: auto;
            border-radius: 15px;
            transition: transform 0.3s ease;
        }
        .product-img:hover {
            transform: scale(1.05);
        }
        .product-details {
            padding: 20px;
        }
        .price {
            color: #e74c3c;
            font-size: 2rem;
            font-weight: bold;
            margin: 10px 0;
        }
        .btn-custom {
            border-radius: 25px;
            padding: 10px 25px;
            transition: all 0.3s ease;
        }
        .btn-custom:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        .alert-box {
            display: none;
            border-radius: 10px;
        }
        .comment-section {
            margin-top: 40px;
            padding: 20px;
            background: #f1f3f5;
            border-radius: 10px;
        }
        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
        }
        .rating .star {
            font-size: 1.5rem;
            color: #ddd;
            cursor: pointer;
            transition: color 0.2s;
        }
        .rating .star.selected, .rating .star:hover {
            color: #f1c40f;
        }
        .comment-box {
            padding: 15px;
            background: #fff;
            border-radius: 10px;
            margin-bottom: 15px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        }
        .edit-comment-form {
            display: none;
            margin-top: 10px;
        }
        textarea.form-control {
            border-radius: 10px;
            resize: none;
        }
        .edit-comment-btn {
            pointer-events: auto !important; /* Đảm bảo nút có thể bấm */
            cursor: pointer !important; /* Hiển thị con trỏ tay */
        }
    </style>
</head>
<jsp:include page="/Header.jsp" />
<body>
    <div class="container">
        <h2 class="text-center mb-4" style="font-weight: 600; color: #2c3e50;">Product Detail</h2>
        <c:choose>
            <c:when test="${not empty product}">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <img src="../../link/img/${product.proImg}" class="product-img" alt="${product.productName}">
                    </div>
                    <div class="col-md-6 product-details">
                        <h3 style="color: #34495e;">${product.productName}</h3>
                        <p class="price">
                            <c:choose>
                                <c:when test="${not empty product.proPrice}">
                                    <fmt:formatNumber value="${product.proPrice}" type="number" groupingUsed="true" /> VNĐ
                                </c:when>                                
                            </c:choose>
                        </p>
                        <p><strong>Product code:</strong> ${product.productID}</p>
                        <p><strong>Status:</strong> 
                            <span class="${product.proState == 1 ? 'text-success' : 'text-danger'}">
                                <c:out value="${product.proState == 1 ? 'Còn hàng' : 'Hết hàng'}" />
                            </span>
                        </p>
                        <p><strong>Quantity:</strong> ${product.proQuantity}</p>
                        <div id="alertBox" class="alert alert-success alert-box"></div>

                        <c:if test="${product.proState == 1 && product.proQuantity > 0}">
                            <form id="addToCartForm" class="align-items-center gap-3">
                                <input type="hidden" name="productID" id="productID" value="${product.productID}">
                                <div class="d-flex">
                                    <label for="quantity" class="fw-bold">Số lượng:</label>
                                    <input type="number" name="quantity" id="quantity" min="1" max="${product.proQuantity}" value="1" class="form-control w-25">
                                </div>
                                <div style="margin-top: 5px;">
                                    <button type="button" id="addToCartBtn" class="btn btn-success ">
                                        <i class="fas fa-cart-plus"></i> Add to cart
                                    </button>
                                    <button class="btn btn-primary btn-rounded">Buy Now</button>
                                </div>
                            </form>
                        </c:if>

                        <c:if test="${product.proState == 0 || product.proQuantity == 0}">
                            <button class="btn btn-secondary btn-custom mt-3" disabled>Out of stock</button>
                        </c:if>
                    </div>
                </div>
            </c:when>
        </c:choose>
        <div class="col-lg-12 col-md-12 col-sm-12">
            <h3 class="box-title mt-5">General Info</h3>
            <div class="table-responsive">
                <table class="table table-striped table-product">
                    <tbody>
                        <c:if test="${not empty productDetail.operatingSystem}">
                            <tr><td width="390">Operating System</td><td>${productDetail.operatingSystem}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.cpuTechnology}">
                            <tr><td>CPU Technology</td><td>${productDetail.cpuTechnology}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.coreCount}">
                            <tr><td>Core Count</td><td>${productDetail.coreCount}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.threadCount}">
                            <tr><td>Thread Count</td><td>${productDetail.threadCount}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.cpuSpeed}">
                            <tr><td>CPU Speed</td><td>${productDetail.cpuSpeed}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.gpu}">
                            <tr><td>GPU</td><td>${productDetail.gpu}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.ram}">
                            <tr><td>RAM</td><td>${productDetail.ram} GB</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.ramType}">
                            <tr><td>RAM Type</td><td>${productDetail.ramType}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.ramBusSpeed}">
                            <tr><td>RAM Bus Speed</td><td>${productDetail.ramBusSpeed}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.maxRam}">
                            <tr><td>Max RAM</td><td>${productDetail.maxRam} GB</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.storage}">
                            <tr><td>Storage</td><td>${productDetail.storage}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.memoryCard}">
                            <tr><td>Memory Card Support</td><td>${productDetail.memoryCard}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.screen}">
                            <tr><td>Screen</td><td>${productDetail.screen}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.resolution}">
                            <tr><td>Resolution</td><td>${productDetail.resolution}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.refreshRate}">
                            <tr><td>Refresh Rate</td><td>${productDetail.refreshRate}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.batteryCapacity}">
                            <tr><td>Battery Capacity</td><td>${productDetail.batteryCapacity}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.batteryType}">
                            <tr><td>Battery Type</td><td>${productDetail.batteryType}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.maxChargingSupport}">
                            <tr><td>Max Charging Support</td><td>${productDetail.maxChargingSupport}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.releaseDate}">
                            <tr><td>Release Date</td><td>${productDetail.releaseDate}</td></tr>
                        </c:if>
                        <c:if test="${not empty productDetail.origin}">
                            <tr><td>Origin</td><td>${productDetail.origin}</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
<!-- Comment Section -->
        <div class="comment-section">
            <h3 class="text-center mb-4" style="color: #2c3e50;">Bình luận & Đánh giá</h3>

            <!-- Form thêm bình luận -->
            <form action="${pageContext.request.contextPath}/ProductController" method="post">
                <div class="user-info mb-3">
                    <img src="/link/img/cat.jpg" alt="User Avatar" class="user-avatar">
                    <span class="user-name fw-bold">${sessionScope.user != null ? sessionScope.user.username : 'Người dùng'}</span>
                </div>
                <div class="rating mb-3">
                    <span class="star" data-value="1">★</span>
                    <span class="star" data-value="2">★</span>
                    <span class="star" data-value="3">★</span>
                    <span class="star" data-value="4">★</span>
                    <span class="star" data-value="5">★</span>
                </div>
                <input type="hidden" name="rate" id="ratingValue" value="0">
                <textarea name="comment" class="form-control mb-3" placeholder="Viết bình luận của bạn..." rows="3" required></textarea>
                <input type="hidden" name="productId" value="${productId != null ? productId : param.productId}" />
                <input type="hidden" name="id" value="${sessionScope.user.id}" />
                <button type="submit" class="btn btn-primary btn-custom" ${sessionScope.user == null ? 'disabled' : ''}>
                    <i class="fas fa-paper-plane"></i> Gửi bình luận
                </button>
                <c:if test="${sessionScope.user == null}">
                    <p class="text-danger mt-2">Vui lòng <a href="${pageContext.request.contextPath}/LoginController/Login">đăng nhập</a> để bình luận.</p>
                </c:if>
            </form>

            <!-- Danh sách bình luận -->
            <div class="mt-4 comment-container">
                <h5 class="fw-bold">Bình luận:</h5>
                <c:choose>
                    <c:when test="${empty comments || comments == null}">
                        <p>Chưa có bình luận nào.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="comment" items="${comments}" varStatus="loop">
                            <div class="comment-box" id="comment-${comment.commentID}">
                                <div class="user-info">
                                    <img src="/link/img/cat.jpg" alt="User Avatar" class="user-avatar">
                                    <span class="user-name fw-bold">${sessionScope.user != null && sessionScope.user.id == comment.id ? sessionScope.user.username : 'Người dùng'}</span>
                                </div>
                                <div class="rating">
                                    <c:forEach var="i" begin="1" end="5">
                                        <span class="star ${i <= comment.rate ? 'selected' : ''}">★</span>
                                    </c:forEach>
                                </div>
                                <p class="mt-2">${comment.comment}</p>
                                <c:if test="${sessionScope.user != null && sessionScope.user.id == comment.id}">
                                    <button class="btn btn-sm btn-warning edit-comment-btn" data-bs-toggle="modal" data-bs-target="#editCommentModal" 
                                            data-comment-id="${comment.commentID}" data-rate="${comment.rate}" data-content="${comment.comment}">Sửa</button>
                                    <form action="${pageContext.request.contextPath}/ProductController" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="deleteComment">
                                        <input type="hidden" name="commentID" value="${comment.commentID}">
                                        <input type="hidden" name="productId" value="${productId}">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa bình luận này?')">Xóa</button>
                                    </form>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Modal chỉnh sửa bình luận -->
    <div class="modal fade" id="editCommentModal" tabindex="-1" aria-labelledby="editCommentModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editCommentModalLabel">Chỉnh sửa bình luận</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editCommentForm" action="${pageContext.request.contextPath}/ProductController" method="post">
                        <input type="hidden" name="action" value="updateComment">
                        <input type="hidden" name="commentID" id="editCommentID">
                        <input type="hidden" name="productId" value="${productId}">
                        <div class="mb-3">
                            <label class="fw-bold">Đánh giá:</label>
                            <div class="rating" id="editRating">
                                <span class="star" data-value="1">★</span>
                                <span class="star" data-value="2">★</span>
                                <span class="star" data-value="3">★</span>
                                <span class="star" data-value="4">★</span>
                                <span class="star" data-value="5">★</span>
                            </div>
                            <input type="hidden" name="rate" id="editRatingValue" value="0">
                        </div>
                        <div class="mb-3">
                            <label for="editCommentContent" class="fw-bold">Nội dung bình luận:</label>
                            <textarea name="comment" id="editCommentContent" class="form-control" rows="3" required></textarea>
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
                    
                    
                    

  <!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function () {
    // Xử lý nút sửa bình luận để điền dữ liệu vào modal
    document.querySelectorAll('.edit-comment-btn').forEach(button => {
        button.addEventListener('click', function () {
            const commentId = this.getAttribute('data-comment-id');
            const rate = this.getAttribute('data-rate');
            const content = this.getAttribute('data-content');

            console.log('Đã bấm nút Sửa cho commentID: ' + commentId);

            // Điền dữ liệu vào modal
            document.getElementById('editCommentID').value = commentId;
            document.getElementById('editRatingValue').value = rate;
            document.getElementById('editCommentContent').value = content;

            // Cập nhật sao đánh giá trong modal
            const ratingStars = document.querySelectorAll('#editRating .star');
            ratingStars.forEach(star => {
                star.classList.remove('selected');
                if (star.getAttribute('data-value') <= rate) {
                    star.classList.add('selected');
                }
            });
        });
    });

    // Xử lý rating sao trong modal
    document.querySelectorAll('#editRating .star').forEach(star => {
        star.addEventListener('click', function () {
            const value = this.getAttribute('data-value');
            document.getElementById('editRatingValue').value = value;
            this.parentElement.querySelectorAll('.star').forEach(s => {
                s.classList.remove('selected');
                if (s.getAttribute('data-value') <= value) {
                    s.classList.add('selected');
                }
            });
        });
    });

    // Xử lý AJAX thêm vào giỏ hàng
    document.getElementById("addToCartBtn")?.addEventListener("click", function () {
        let productID = document.getElementById("productID").value;
        let quantity = document.getElementById("quantity").value;

        fetch("${pageContext.request.contextPath}/CartController", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: "action=add&productId=" + encodeURIComponent(productID) + "&quantity=" + encodeURIComponent(quantity)
        })
        .then(response => response.json())
        .then(result => {
            let alertBox = document.getElementById("alertBox");
            alertBox.style.display = "block";
            if (result.status === "success") {
                alertBox.className = "alert alert-success alert-box";
                alertBox.innerText = result.message;
            } else {
                alertBox.className = "alert alert-danger alert-box";
                alertBox.innerText = result.message;
                if (result.message.includes("đăng nhập")) {
                    setTimeout(() => window.location.href = "${pageContext.request.contextPath}/LoginController/Login", 1000);
                }
            }
            setTimeout(() => alertBox.style.display = "none", 2000);
        })
        .catch(error => {
            console.error("Lỗi:", error);
            let alertBox = document.getElementById("alertBox");
            alertBox.className = "alert alert-danger alert-box";
            alertBox.innerText = "Có lỗi xảy ra khi thêm vào giỏ hàng!";
            alertBox.style.display = "block";
            setTimeout(() => alertBox.style.display = "none", 3000);
        });
    });

    // Xử lý rating sao cho thêm bình luận
    document.querySelectorAll('.comment-section > form .rating .star').forEach(star => {
        star.addEventListener('click', function () {
            const value = this.getAttribute('data-value');
            document.getElementById('ratingValue').value = value;
            this.parentElement.querySelectorAll('.star').forEach(s => {
                s.classList.remove('selected');
                if (s.getAttribute('data-value') <= value) {
                    s.classList.add('selected');
                }
            });
        });
    });
});
</script>
</body>
</html>