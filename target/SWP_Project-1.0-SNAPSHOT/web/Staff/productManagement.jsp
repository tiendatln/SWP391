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

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Sản Phẩm</title>
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">       
        <!-- CSS JS bootstrap 5.0-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

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
            <h2>Product Management</h2>
            <button class="btn btn-success mb-3" data-bs-toggle="modal" data-bs-target="#addProductModal">Add New Product</button>

            <input type="text" id="searchInput" placeholder="Search product..." class="form-control mb-3" onkeyup="searchProducts()">

            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Image</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td><img src="/link/img/${product.proImg}" alt="${product.productName}" width="50"></td>
                            <td>${product.productName}</td>
                            <td class="price"><fmt:formatNumber value="${product.proPrice}" type="currency" currencySymbol="đ"/></td>
                            <td>${product.proQuantity}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.proState == 1}">Active</c:when>
                                    <c:otherwise>Out Of Business</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn btn-primary"type="button" data-bs-toggle="modal" data-bs-target="#editProductModal${product.productID}" style="color: black;" >Edit</button>
                                <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#editProductDetailModal${product.productID}">
                                    Edit Product Detail
                                </button>

                                <button class="btn btn-outline-danger"" type="button" style="color: black" onclick="confirmDelete('${product.productID}')"><i class="fa fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <!--             add new product-->
            <div class="modal fade" role="dialog" tabindex="-1" id="addProductModal" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Add Product</h5>
                        </div>
                        <div class="modal-body">
                            <div class="py-1">
                                <form class="form" action="/ProductController?action=addProduct" method="post" enctype="multipart/form-data">



                                    <div class="row">
                                        <div class="col">
                                            <div class="row">
                                                <div class="form-group">
                                                    <label>Product Name</label>
                                                    <input class="form-control" type="text" name="productName" placeholder="Enter product name" required>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group">
                                                    <label>Quantity</label>
                                                    <input class="form-control" type="number" name="proQuantity" min="0" placeholder="Enter quantity" required>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group">
                                                    <label>Price</label>
                                                    <input class="form-control" type="number" step="0.01" name="proPrice" min="0" placeholder="Enter price" required>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col mb-3">
                                                    <div class="form-group">
                                                        <label>Description</label>
                                                        <textarea class="form-control" rows="4" name="proDescription" placeholder="Enter product description"></textarea>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group">
                                                    <label>Category</label>
                                                    <select class="form-control" name="proCategory" required>
                                                        <option value="">Select category</option>
                                                        <c:forEach var="category" items="${category}">
                                                            <option value="${category.categoryID}">${category.type}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="form-group">
                                                    <label>State</label>
                                                    <select class="form-control" name="proState" required>
                                                        <option value="1">Active</option>
                                                        <option value="0">Inactive</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-12 col-sm-6 mb-3">
                                                    <div class="mb-2"><b>Upload Product Image</b></div>
                                                    <div id="myfileupload">
                                                        <input type="file" id="proImg" name="proImg" accept="image/*" >
                                                        <img id="imagePreview" src="#" alt="Image Preview" style="display: none; width: 200px; margin-top: 10px;">

                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col d-flex justify-content-end">
                                                    <button class="btn btn-primary" type="submit">Add Product</button>
                                                    <a class="btn btn-danger" data-bs-dismiss="modal" href="#" style="margin-left: 15px;">Cancel</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>    
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- jQuery and Bootstrap JS -->
            <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

            <!-- Edit product -->
            <c:forEach items="${productList}" var="product">
                <div class="modal fade" role="dialog" tabindex="-1" id="editProductModal${product.productID}" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
                    <div class="modal-dialog modal-lg" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Edit Product</h5>
                            </div>
                            <div class="modal-body">
                                <div class="py-1">
                                    <form class="form" action="/ProductController?action=editProduct" method="post" enctype="multipart/form-data">
                                        <div class="row">
                                            <div class="col">
                                                <div class="row">

                                                    <div class="form-group">
                                                        <label>Product ID</label>
                                                        <input class="form-control" type="text" name="productID" value="${product.productID}" readonly style="background: #f1f1f1">
                                                    </div>

                                                </div>
                                                <div class="row">
                                                    <div class="form-group">
                                                        <label>Product Name</label>
                                                        <input class="form-control" type="text" name="productName" placeholder="Enter product name" value="${product.productName}" required>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="form-group">
                                                        <label>Quantity</label>
                                                        <input class="form-control" type="number" name="proQuantity" min="0" placeholder="Enter quantity" value="${product.proQuantity}" required>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="form-group">
                                                        <label>Price</label>
                                                        <input class="form-control" type="number" step="0.01" name="proPrice" min="0" placeholder="Enter price" value="${product.proPrice}" required>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col mb-3">
                                                        <div class="form-group">
                                                            <label>Description</label>
                                                            <textarea class="form-control" rows="4" name="proDescription" placeholder="Enter product description" >${product.proDes}</textarea>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="form-group">
                                                        <label>Category</label>
                                                        <select class="form-control" name="proCategory" required>
                                                            <option value="">Select category</option>
                                                            <c:forEach var="category" items="${category}">
                                                                <option value="${category.categoryID}" 
                                                                        ${category.categoryID == product.category.categoryID ? "selected" : ""}>
                                                                    ${category.type}
                                                                </option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>


                                                <div class="row">
                                                    <div class="form-group">
                                                        <label>State</label>
                                                        <select class="form-control" name="proState" value="${product.proState}" required>
                                                            <option value="1">Active</option>
                                                            <option value="0">Inactive</option>
                                                        </select>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-12 col-sm-6 mb-3">
                                                        <div class="mb-2"><b>Upload Product Image</b></div>
                                                        <div id="myfileupload">
                                                            <input type="file" id="proImg${product.productID}" name="proImg" accept="image/*" onchange="previewImage(event, 'imagePreview${product.productID}')">
                                                            <img id="imagePreview${product.productID}" src="#" alt="Image Preview" style="display: none; width: 200px; margin-top: 10px;">

                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col d-flex justify-content-end">
                                                        <button class="btn btn-primary" type="submit">Save Change</button>
                                                        <a class="btn btn-danger" data-bs-dismiss="modal" href="#" style="margin-left: 15px;">Cancel</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>    
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <!-- edit product detail -->
            <c:forEach items="${productList}" var="product">
                <div class="modal fade" id="editProductDetailModal${product.productID}" tabindex="-1" aria-labelledby="editProductDetailLabel${product.productID}" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="editProductDetailLabel${product.productID}">Edit Product Detail</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="/ProductController?action=updateProductDetail" method="post">
                                    <input type="hidden" name="productID" value="${product.productID}">

                                    <div class="mb-3">
                                        <label class="form-label">Operating System:</label>
                                        <input type="text" class="form-control" name="operatingSystem" value="${product.productDetail.operatingSystem}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">CPU Technology:</label>
                                        <input type="text" class="form-control" name="cpuTechnology" value="${product.productDetail.cpuTechnology}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Core Count:</label>
                                        <input type="number" class="form-control" name="coreCount" value="${product.productDetail.coreCount}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Thread Count:</label>
                                        <input type="number" class="form-control" name="threadCount" value="${product.productDetail.threadCount}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">CPU Speed (GHz):</label>
                                        <input type="text" class="form-control" name="cpuSpeed" value="${product.productDetail.cpuSpeed}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">GPU:</label>
                                        <input type="text" class="form-control" name="gpu" value="${product.productDetail.gpu}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">RAM (GB):</label>
                                        <input type="number" class="form-control" name="ram" value="${product.productDetail.ram}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">RAM Type:</label>
                                        <input type="text" class="form-control" name="ramType" value="${product.productDetail.ramType}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">RAM Bus Speed (MHz):</label>
                                        <input type="text" class="form-control" name="ramBusSpeed" value="${product.productDetail.ramBusSpeed}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Max RAM (GB):</label>
                                        <input type="number" class="form-control" name="maxRam" value="${product.productDetail.maxRam}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Storage:</label>
                                        <input type="text" class="form-control" name="storage" value="${product.productDetail.storage}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Memory Card:</label>
                                        <input type="text" class="form-control" name="memoryCard" value="${product.productDetail.memoryCard}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Screen Size:</label>
                                        <input type="text" class="form-control" name="screen" value="${product.productDetail.screen}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Resolution:</label>
                                        <input type="text" class="form-control" name="resolution" value="${product.productDetail.resolution}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Refresh Rate (Hz):</label>
                                        <input type="text" class="form-control" name="refreshRate" value="${product.productDetail.refreshRate}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Battery Capacity:</label>
                                        <input type="text" class="form-control" name="batteryCapacity" value="${product.productDetail.batteryCapacity}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Battery Type:</label>
                                        <input type="text" class="form-control" name="batteryType" value="${product.productDetail.batteryType}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Max Charging Support (W):</label>
                                        <input type="text" class="form-control" name="maxChargingSupport" value="${product.productDetail.maxChargingSupport}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Release Date:</label>
                                        <input type="date" class="form-control" name="releaseDate" value="${product.productDetail.releaseDate}">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Origin:</label>
                                        <input type="text" class="form-control" name="origin" value="${product.productDetail.origin}">
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-primary">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>          
        </div>
        <script>
            function confirmDelete(productID) {
                if (confirm("Are you sure you want to delete this product?")) {
                    window.location.href = "/ProductController?action=deleteProduct&productID=" + productID;
                }
            }


            function searchProducts() {
                let input = document.getElementById("searchInput").value.toLowerCase();
                let table = document.querySelector("table tbody");
                let rows = table.getElementsByTagName("tr");

                for (let i = 0; i < rows.length; i++) {
                    let cols = rows[i].getElementsByTagName("td");
                    let match = false;

                    for (let j = 0; j < cols.length; j++) {
                        if (cols[j].textContent.toLowerCase().includes(input)) {
                            match = true;
                            break;
                        }
                    }

                    rows[i].style.display = match ? "" : "none";
                }
            }

            function previewImage(event, previewId) {
                const file = event.target.files[0];
                if (file) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const previewImage = document.getElementById(previewId);
                        previewImage.src = e.target.result;
                        previewImage.style.display = 'block';
                    };
                    reader.readAsDataURL(file);
                }
            }

            document.getElementById('proImg').addEventListener('change', function (event) {
                let file = event.target.files[0];
                if (file) {
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        let img = document.getElementById('imagePreview');
                        img.src = e.target.result;
                        img.style.display = 'block';
                    };
                    reader.readAsDataURL(file);
                } else {
                    document.getElementById('imagePreview').style.display = 'none';
                }
            });
        </script>
        <script src = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" ></script>

    </body>
</html>

