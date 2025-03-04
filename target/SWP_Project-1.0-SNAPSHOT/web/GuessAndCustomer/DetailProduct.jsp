<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Product Page</title>
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
        <style>
            body{
                background-color: #edf1f5;
                margin-top:20px;
            }
            .main{
                margin-top: 30px;
            }
            
            .card {
                margin-bottom: 30px;
                background-color: #fff;
                border: none;
                border-radius: 5px;
                padding: 20px;
                box-shadow: 0px 0px 10px rgba(0,0,0,0.1);
            }
            .card .card-subtitle {
                font-weight: 300;
                margin-bottom: 10px;
                color: #8898aa;
            }
            .table-product.table-striped tbody tr:nth-of-type(odd) {
                background-color: #f3f8fa!important;
            }
            .table-product td{
                border-top: 0px solid #dee2e6 !important;
                color: #728299!important;
            }
            .img-responsive {
                width: 100%;
                height: auto;
            }
            .product-img{
                justify-self: center;
            }
            .product-img img{
                height: 30%;
                width: 100%;
            }
            .user-info {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }
        .user-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            border: 2px solid #007BFF;
        }
        .user-name {
            margin-left: 10px;
            font-size: 18px;
            font-weight: bold;
        }
        .rating {
            display: flex;
        }
        .star {
            font-size: 20px;
            color: #ccc;
            cursor: pointer;
        }
        .star.selected {
            color: gold;
        }
        .comment-box {
            background: #f8f9fa;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 10px;
        }
        .reply-box {
            margin-left: 40px;
        }
        .border-comment{
            margin-top: 30px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 5px 15px rgba(0, 0, 0, 0.2);
            width: 80%;
            max-width: 800px;
        }
        </style>
    </head>
    <body>
        <%@include file="../../Header.jsp" %>

        <div class="container main">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title">Rounded Chair</h3>
                    <h6 class="card-subtitle">globe type chair for rest</h6>
                    <div class="row">
                        <div class="col-lg-5 col-md-5 col-sm-6 product-img">
                            <div class="white-box text-center"><img src="/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg" class="img-responsive"></div>
                        </div>
                        <div class="col-lg-7 col-md-7 col-sm-6">
                            <h4 class="box-title mt-5">Product description</h4>
                            <p>Lorem Ipsum available,but the majority have suffered alteration in some form,by injected humour,or randomised words which don't look even slightly believable.but the majority have suffered alteration in some form,by injected humour</p>
                            <h2 class="mt-5">
                                $153<small class="text-success">(36%off)</small>
                            </h2>
                            <button class="btn btn-dark btn-rounded mr-1" data-toggle="tooltip" title="" data-original-title="Add to cart">
                                <i class="fa fa-shopping-cart"></i>
                            </button>
                            <button class="btn btn-primary btn-rounded">Buy Now</button>
                            <h3 class="box-title mt-5">Key Highlights</h3>
                            <ul class="list-unstyled">
                                <li><i class="fa fa-check text-success"></i>Sturdy structure</li>
                                <li><i class="fa fa-check text-success"></i>Designed to foster easy portability</li>
                                <li><i class="fa fa-check text-success"></i>Perfect furniture to flaunt your wonderful collectibles</li>
                            </ul>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-12">
                            <h3 class="box-title mt-5">General Info</h3>
                            <div class="table-responsive">
                                <table class="table table-striped table-product">
                                    <tbody>
                                        <tr>
                                            <td width="390">Brand</td>
                                            <td>Stellar</td>
                                        </tr>
                                        <tr>
                                            <td>Delivery Condition</td>
                                            <td>Knock Down</td>
                                        </tr>
                                        <tr>
                                            <td>Seat Lock Included</td>
                                            <td>Yes</td>
                                        </tr>
                                        <tr>
                                            <td>Type</td>
                                            <td>Office Chair</td>
                                        </tr>
                                        <tr>
                                            <td>Style</td>
                                            <td>Contemporary&amp;Modern</td>
                                        </tr>
                                        <tr>
                                            <td>Wheels Included</td>
                                            <td>Yes</td>
                                        </tr>
                                        <tr>
                                            <td>Upholstery Included</td>
                                            <td>Yes</td>
                                        </tr>
                                        <tr>
                                            <td>Upholstery Type</td>
                                            <td>Cushion</td>
                                        </tr>
                                        <tr>
                                            <td>Head Support</td>
                                            <td>No</td>
                                        </tr>
                                        <tr>
                                            <td>Suitable For</td>
                                            <td>Study&amp;Home Office</td>
                                        </tr>
                                        <tr>
                                            <td>Adjustable Height</td>
                                            <td>Yes</td>
                                        </tr>
                                        <tr>
                                            <td>Model Number</td>
                                            <td>F01020701-00HT744A06</td>
                                        </tr>
                                        <tr>
                                            <td>Armrest Included</td>
                                            <td>Yes</td>
                                        </tr>
                                        <tr>
                                            <td>Care Instructions</td>
                                            <td>Handle With Care,Keep In Dry Place,Do Not Apply Any Chemical For Cleaning.</td>
                                        </tr>
                                        <tr>
                                            <td>Finish Type</td>
                                            <td>Matte</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="container border-comment">
                        <h2 class="text-center">Quản lý Bình luận & Đánh giá</h2>

                        <!-- Form thêm bình luận -->
                        <form action="CommentServlet" method="post">
                            <div class="user-info">
                                <img src="/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg" alt="User Avatar" class="user-avatar">
                                <span class="user-name">Người dùng</span>
                            </div>
                            <div class="rating">
                                <span class="star" data-value="1">&#9733;</span>
                                <span class="star" data-value="2">&#9733;</span>
                                <span class="star" data-value="3">&#9733;</span>
                                <span class="star" data-value="4">&#9733;</span>
                                <span class="star" data-value="5">&#9733;</span>
                            </div>
                            <input type="hidden" name="rating" id="ratingValue">
                            <textarea name="comment" class="form-control" placeholder="Viết bình luận..." required></textarea>
                            <button type="submit" class="btn btn-primary mt-2">Gửi bình luận</button>
                        </form>

                        <!-- Danh sách bình luận -->
                        <div class="mt-4">
                            <h5>Bình luận:</h5>
                            <div class="comment-box">
                                <div class="user-info">
                                    <img src="/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg" alt="User Avatar" class="user-avatar">
                                    <span class="user-name">John Doe</span>
                                </div>
                                <div class="rating">
                                    <span class="star selected">&#9733;</span>
                                    <span class="star selected">&#9733;</span>
                                    <span class="star selected">&#9733;</span>
                                    <span class="star">&#9733;</span>
                                    <span class="star">&#9733;</span>
                                </div>
                                <p>Bình luận mẫu...</p>
                                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editCommentModal">Sửa</button>
                                <a href="CommentServlet?action=delete&id=1" class="btn btn-sm btn-danger">Xóa</a>
                                <button class="btn btn-sm btn-secondary" data-bs-toggle="modal" data-bs-target="#replyModal">Trả lời</button>

                                <!-- Trả lời bình luận -->
                                <div class="reply-box mt-2">
                                    <div class="user-info">
                                        <img src="/link/img/918316ff74d2ab3a59bc3ec31dae287e.jpg" alt="User Avatar" class="user-avatar">
                                        <span class="user-name">Admin</span>
                                    </div>
                                    <p>Trả lời mẫu...</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="../../Footer.jsp" %>
    </body>
</html>
