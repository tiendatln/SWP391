<%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="Update Order Page" />
                <meta name="author" content="Admin" />
                <title>Update Order</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <style>
                    body {
                        display: flex;
                        min-height: 100vh;
                        margin: 0;
                        font-family: Arial, sans-serif;
                    }

                    .content {
                        margin-left: 250px;
                        padding: 20px;
                        flex-grow: 1;
                        width: calc(100% - 250px);
                    }

                    .filter-bar select,
                    .filter-bar button {
                        margin-right: 10px;
                    }

                    .filter-bar {
                        margin-bottom: 20px;
                    }


                    .table th,
                    .table td {
                        vertical-align: middle;
                    }

                    .input-group {
                        width: 100px;
                    }

                    .img-fluid {
                        width: 80px;
                        height: 80px;
                        border-radius: 50%;
                    }
                </style>
            </head>

            <body class="sb-nav-fixed">
                <%@include file="../../AdminLayout.jsp" %>
                <div class="container-fluid px-4">
                    <h1 class="mt-4">Orders</h1>

                    <div class="mt-5">
                        <div class="row">
                            <div class="col-md-6 col-12 mx-auto">
                                <h3>Update an Order</h3>
                                <hr />
                                <form method="POST" action="#" class="row">
                                    <!-- Hidden ID field -->
                                    <div class="mb-3" style="display: none;">
                                        <label class="form-label">Id:</label>
                                        <input type="text" class="form-control" value="1" />
                                    </div>

                                    <div class="mb-3">
                                        <label>Order id = 1</label>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <label class="form-label">Price: 500.000 đ</label>
                                    </div>

                                    <div class="mb-3 col-12 col-md-6">
                                        <label class="form-label">User:</label>
                                        <input type="text" class="form-control" disabled value="John Doe" />
                                    </div>

                                    <div class="mb-3 col-12 col-md-6">
                                        <label class="form-label">Status:</label>
                                        <select class="form-select" name="status">
                                            <option value="PENDING">PENDING</option>
                                            <option value="CONFIRM">CONFIRM</option>
                                            <option value="SHIPPING">SHIPPING</option>
                                            <option value="COMPLETE">COMPLETE</option>
                                            <option value="CANCEL">CANCEL</option>
                                        </select>
                                    </div>

                                    <div class="col-12 mb-5">
                                        <button type="submit" class="btn btn-warning">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
            </body>

            </html>