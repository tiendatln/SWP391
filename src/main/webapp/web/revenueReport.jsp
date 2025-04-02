<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.sql.*, java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Report</title>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
            google.charts.load('current', {'packages': ['corechart', 'piechart']});
            google.charts.setOnLoadCallback(drawAccountChart);

            function drawAccountChart() {
                var accountData = google.visualization.arrayToDataTable([
                    ['Role', 'Count'],
            <%
                String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=SWP391;encrypt=true;trustServerCertificate=true;";
                String dbUser = "sa";
                String dbPassword = "123";
                Connection conn = null;
                try {
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);

                    Statement accountStmt = conn.createStatement();
                    String accountQuery = "SELECT role, COUNT(id) AS accountCount FROM account GROUP BY role";
                    ResultSet accountRs = accountStmt.executeQuery(accountQuery);
                    while (accountRs.next()) {
                        String role = accountRs.getString("role");
                        int accountCount = accountRs.getInt("accountCount");
            %>
                    ['<%= role%>', <%= accountCount%>],
            <%
                    }
                } catch (Exception e) {
                    out.println("<p>Error: " + e.getMessage() + "</p>");
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            out.println("<p>Error closing connection: " + e.getMessage() + "</p>");
                        }
                    }
                }
            %>
                ]);

                var accountOptions = {
                    title: 'Account Roles Distribution',
                    pieSliceText: 'percentage', // Hiển thị phần trăm khi hover
                    slices: {
                        0: {offset: 0.1, color: '#FF5733'}, // Màu cho 'admin'
                        1: {offset: 0.1, color: '#2980B9'}  // Màu cho 'customer'
                    },
                    tooltip: {isHtml: true}, // Cho phép hiển thị HTML trong tooltip
                };

                var accountChart = new google.visualization.PieChart(document.getElementById('DashboardAccountChartDiv'));
                accountChart.draw(accountData, accountOptions);

                // Hiển thị tổng số tài khoản
                var totalAccounts = 0;
                for (var i = 0; i < accountData.getNumberOfRows(); i++) {
                    totalAccounts += accountData.getValue(i, 1);  // Cộng dồn số lượng tài khoản
                }

                document.getElementById('totalAccounts').innerHTML = "Total Accounts: " + totalAccounts;

                // Lắng nghe sự kiện hover trên pie chart để hiển thị thông tin thành phần
                google.visualization.events.addListener(accountChart, 'onmouseover', function (e) {
                    var role = accountData.getValue(e.row, 0);  // Lấy tên role
                    var count = accountData.getValue(e.row, 1);  // Lấy số lượng
                    var percentage = (count / totalAccounts * 100).toFixed(2);  // Tính tỷ lệ phần trăm

                    var tooltipText = role + ": " + count + " accounts (" + percentage + "%)";
                    document.getElementById('tooltipInfo').innerHTML = tooltipText;  // Hiển thị thông tin trong tooltip
                });
            }
        </script>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                margin-top: 0;
                margin-left: 0;
                background-color: #f8f9fa;
            }
            h2 {
                text-align: center;
                color: #333;
            }

            select, input[type="date"], input[type="submit"] {
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            input[type="submit"] {
                background-color: #28a745;
                color: white;
                border: none;
                cursor: pointer;
            }
            input[type="submit"]:hover {
                background-color: #218838;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                overflow: hidden;
            }
            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            th {
                background-color: #007bff;
                color: white;
            }
            tr:hover {
                background-color: #f1f1f1;
            }
            form {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 10px;
                background: #fff;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
            }
            #revenueChart {
                max-width: 800px;
                height: 400px;
                margin: 20px auto;
                background: #fff;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            #orderStatusChart {
                max-width: 400px;
                height: 400px;
                margin: 20px auto;
                background: #fff;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            .container {
                margin-left: 300px; /* 🔹 Dịch bảng sang phải để tránh sidebar */
                padding: 20px;
                position: relative;
            }

            .table-container {
                overflow-x: auto; /* 🔹 Cho phép cuộn ngang nếu cần */
                max-width: 100%;
            }

            th, td {
                white-space: nowrap; /* 🔹 Ngăn nội dung xuống dòng */
            }
            .DashboardContent {
                /*                margin-left: 270px;  For sidebar */
                padding: 20px;
            }
            .DashboardContent h1 {
                color: #333333;
                font-size: 24px;
                margin-bottom: 20px;
            }
            #DashboardAccountChartDiv {
                margin-bottom: 40px;
                /*                margin-left: 20px;  Shift charts slightly to the right */
            }
            .DashboardChartTitle {
                font-weight: bold;
                color: #666666;
                margin-bottom: 10px;
            }

        </style>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>

    <body>
        <jsp:include page="/AdminLayout.jsp" />
        <div class="container">
            <h2>Report</h2>
            <div class="DashboardContent">
                <div class="DashboardChartContainer">
                    <h3 class="DashboardChartTitle">Account Roles Distribution</h3>
                    <!-- Div để hiển thị Pie Chart -->
                    <div id="DashboardAccountChartDiv" style="width: 900px; height: 500px;"></div>

                    <!-- Div hiển thị tổng số tài khoản -->
                    <div id="totalAccounts" style="font-size: 18px; margin-top: 20px;"></div>

                    <!-- Div hiển thị thông tin thành phần khi hover -->
                    <div id="tooltipInfo" style="font-size: 16px; color: #555; margin-top: 10px;"></div>

                </div>
            </div>

            <form action="/RevenueController/Report" method="get">
                <label>Start Date:</label>
                <input type="date" name="startDate" id="startDate" value="${startDate}" required oninput="validateStartDate()">

                <label>End Date:</label>
                <input type="date" name="endDate" id="endDate" value="${endDate}" required min="" oninput="validateEndDate()">

                <label>Group By:</label>
                <select name="groupBy" required>
                    <option value="day" ${groupBy == 'day' ? 'selected' : ''}>Day</option>
                    <option value="month" ${groupBy == 'month' ? 'selected' : ''}>Month</option>
                    <option value="year" ${groupBy == 'year' ? 'selected' : ''}>Year</option>
                </select>

                <input type="submit" value="Generate Report">
            </form>
            <script>
            function validateStartDate() {
                let today = new Date().toISOString().split('T')[0]; // Lấy ngày hôm nay (YYYY-MM-DD)
                let startDateInput = document.getElementById("startDate");
                let endDateInput = document.getElementById("endDate");

                if (startDateInput.value >= today) {
                    alert("Start Date phải trước hôm nay!");
                    startDateInput.value = "";
                    return;
                }

                // Cập nhật min cho End Date
                endDateInput.min = startDateInput.value;
            }

            function validateEndDate() {
                let startDate = document.getElementById("startDate").value;
                let endDate = document.getElementById("endDate").value;

                if (startDate && endDate < startDate) {
                    alert("End Date phải sau hoặc bằng Start Date!");
                    document.getElementById("endDate").value = "";
                }
            }
            </script>





            <c:if test="${not empty reports}">
                <h3 style="text-align: center; color: #555;">Report from ${startDate} to ${endDate} (Grouped by ${groupBy})</h3>

                <!-- Biểu đồ cột hiện tại -->
                <canvas id="revenueChart"></canvas>
                <script>
                    const revenueLabels = [
                    <c:forEach items="${periodRevenueLabels}" var="label" varStatus="loop">
                    "${label}"${loop.last ? '' : ','}
                    </c:forEach>
                    ];

                    const revenueData = [
                    <c:forEach items="${periodRevenueData}" var="data" varStatus="loop">
                        ${data}${loop.last ? '' : ','}
                    </c:forEach>
                    ];

                    // Tính tổng doanh thu bằng JSP
                    <c:set var="totalRevenue" value="0" />
                    <c:forEach items="${periodRevenueData}" var="data">
                        <c:set var="totalRevenue" value="${totalRevenue + data}" />
                    </c:forEach>

                    const totalRevenue = ${totalRevenue}; // Truyền từ JSP vào JS

                    const revenueCtx = document.getElementById('revenueChart').getContext('2d');
                    const revenueChart = new Chart(revenueCtx, {
                        type: 'bar',
                        data: {
                            labels: revenueLabels,
                            datasets: [{
                                    label: 'Revenue by ${groupBy} (Total: ' + totalRevenue.toLocaleString() + ' VND)', // Thêm tổng doanh thu
                                    data: revenueData,
                                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                                    borderColor: 'rgba(75, 192, 192, 1)',
                                    borderWidth: 1
                                }]
                        },
                        options: {
                            responsive: true,
                            scales: {
                                y: {beginAtZero: true, title: {display: true, text: 'Revenue (VND)'}},
                                x: {title: {display: true, text: '${capitalizedGroupBy}'}}
                            }
                        }
                    });
                </script>


                <!-- Biểu đồ tròn mới cho trạng thái đơn hàng -->
                <canvas id="orderStatusChart"></canvas>
                <script>
                    const statusLabels = [
                    <c:forEach items="${orderStatusLabels}" var="label" varStatus="loop">
                    "${label}"${loop.last ? '' : ','}
                    </c:forEach>
                    ];

                    const statusData = [
                    <c:forEach items="${orderStatusData}" var="data" varStatus="loop">
                        ${data}${loop.last ? '' : ','}
                    </c:forEach>
                    ];

                    // Tính tổng số đơn hàng trong JSP
                    <c:set var="totalOrders" value="0" />
                    <c:forEach items="${orderStatusData}" var="data">
                        <c:set var="totalOrders" value="${totalOrders + data}" />
                    </c:forEach>

                    const totalOrders = ${totalOrders}; // Truyền từ JSP vào JavaScript

                    // Màu sắc theo trạng thái đơn hàng
                    const statusColors = {
                        'Cancel': {backgroundColor: 'rgba(255, 35, 35, 0.6)', borderColor: 'rgba(255, 35, 35, 1)'}, // Đỏ
                        'Complete': {backgroundColor: 'rgba(35, 255, 35, 0.6)', borderColor: 'rgba(35, 255, 35, 1)'}, // Xanh lá
                        'Pending': {backgroundColor: 'rgba(255, 255, 35, 0.6)', borderColor: 'rgba(255, 255, 35, 1)'} // Vàng
                    };

                    // Ánh xạ màu sắc cho từng trạng thái
                    const backgroundColor = statusLabels.map(label => statusColors[label]?.backgroundColor || 'rgba(0, 0, 0, 0.1)');
                            const borderColor = statusLabels.map(label => statusColors[label]?.borderColor || 'rgba(0, 0, 0, 0.5)');

                    const statusCtx = document.getElementById('orderStatusChart').getContext('2d');
                    const statusChart = new Chart(statusCtx, {
                        type: 'pie',
                        data: {
                            labels: statusLabels,
                            datasets: [{
                                    label: 'Order Status',
                                    data: statusData,
                                    backgroundColor: backgroundColor,
                                    borderColor: borderColor,
                                    borderWidth: 1
                                }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                legend: {position: 'top'},
                                title: {
                                    display: true,
                                    text: ['Order Status Distribution', 'Total Orders: ' + totalOrders.toLocaleString()] // Thêm tổng đơn hàng ngay dưới tiêu đề
                                }
                            }
                        }
                    });
                </script>




                <!-- Bảng dữ liệu hiện tại -->
                <table>
                    <tr>

                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Total Revenue</th>
                        <th>Total Quantity</th>
                    </tr>
                    <c:forEach items="${reports}" var="report">
                        <tr>

                            <td>${report.productId}</td>  <!-- Đây sẽ hiển thị productId dưới dạng số nguyên -->
                            <td>${report.productName}</td>
                            <td class="price">
                                <fmt:formatNumber value="${report.totalRevenue}" type="currency" currencySymbol=""/>
                                <span>VND</span>
                            </td>
                            <td>${report.totalQuantity}</td>
                        </tr>
                    </c:forEach>
                </table>

            </c:if>
        </div>
    </body>
</html>
