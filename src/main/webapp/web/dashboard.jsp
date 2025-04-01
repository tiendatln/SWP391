<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <%@ include file="../AdminLayout.jsp" %> <!-- Sidebar Integration -->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(fetchAndDrawCharts);

        function fetchAndDrawCharts() {
            drawOrderChart();
            drawProductChart();
            drawAccountChart();
        }

        // Draw Order Chart
        function drawOrderChart() {
            var orderData = google.visualization.arrayToDataTable([
                ['Order State', 'Count'],
                <%  
                    String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=SWP391;encrypt=true;trustServerCertificate=true;";
                    String dbUser = "sa";
                    String dbPassword = "123";
                    Connection conn = null;

                    try {
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);

                        String orderQuery = "SELECT orderState, COUNT(orderID) AS orderCount " +
                                            "FROM orderTotal WHERE YEAR([date]) = ? " +
                                            "GROUP BY orderState";

                        PreparedStatement orderStmt = conn.prepareStatement(orderQuery);
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        String selectedYearParam = request.getParameter("selectedYear");
                        int selectedYear = (selectedYearParam != null) ? Integer.parseInt(selectedYearParam) : currentYear;
                        orderStmt.setInt(1, selectedYear);

                        ResultSet orderRs = orderStmt.executeQuery();

                        while (orderRs.next()) {
                            int orderState = orderRs.getInt("orderState");
                            int orderCount = orderRs.getInt("orderCount");

                            String stateLabel;
                            switch (orderState) {
                                case 0: stateLabel = "Pending"; break;
                                case 1: stateLabel = "Confirmed"; break;
                                case 2: stateLabel = "Canceled"; break;
                                default: stateLabel = "Unknown";
                            }
                %>
                ['<%= stateLabel %>', <%= orderCount %>],
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

            var orderOptions = {
                title: `Order States Distribution`,
                pieHole: 0.4,
                colors: ['#FFD700', '#FF5733', '#2980B9']
            };

            var orderChart = new google.visualization.PieChart(document.getElementById('DashboardOrderChartDiv'));
            orderChart.draw(orderData, orderOptions);
        }

        // Draw Product Chart
        function drawProductChart() {
            var productData = google.visualization.arrayToDataTable([
                ['Category', 'Total Quantity'],
                <%  
                    try {
                        conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);

                        String productQuery = "SELECT type, SUM(proQuantity) AS totalQuantity FROM category " +
                                              "INNER JOIN product ON category.categoryID = product.categoryID GROUP BY type";

                        Statement productStmt = conn.createStatement();
                        ResultSet productRs = productStmt.executeQuery(productQuery);

                        while (productRs.next()) {
                            String type = productRs.getString("type");
                            int totalQuantity = productRs.getInt("totalQuantity");
                %>
                ['<%= type %>', <%= totalQuantity %>],
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

            var productOptions = {
                title: 'Product Quantities by Category',
                hAxis: {title: 'Category'},
                vAxis: {title: 'Quantity'},
                legend: 'none',
                colors: ['#4CAF50']
            };

            var productChart = new google.visualization.ColumnChart(document.getElementById('DashboardProductChartDiv'));
            productChart.draw(productData, productOptions);
        }

        // Draw Account Chart
        function drawAccountChart() {
            var accountData = google.visualization.arrayToDataTable([
                ['Role', 'Count'],
                <%  
                    try {
                        conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);

                        String accountQuery = "SELECT role, COUNT(id) AS accountCount FROM account GROUP BY role";

                        Statement accountStmt = conn.createStatement();
                        ResultSet accountRs = accountStmt.executeQuery(accountQuery);

                        while (accountRs.next()) {
                            String role = accountRs.getString("role");
                            int count = accountRs.getInt("accountCount");
                %>
                ['<%= role %>', <%= count %>],
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
                hAxis: {title: 'Role'},
                vAxis: {title: 'Count'},
                legend: 'none',
                colors: ['#E91E63']
            };

            var accountChart = new google.visualization.ColumnChart(document.getElementById('DashboardAccountChartDiv'));
            accountChart.draw(accountData, accountOptions);
        }
    </script>
    <style>
        /* CSS for Dashboard */
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            background-color: #f4f4f9;
        }
        .DashboardContent {
            margin-left: 20%; /* For sidebar */
            padding: 20px;
        }
        .DashboardContent h1 {
            color: #333333;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .DashboardFilters {
            margin-bottom: 20px;
        }
        #DashboardYearDropdown {
            padding: 5px;
            font-size: 16px;
        }
        #DashboardOrderChartDiv,
        #DashboardProductChartDiv,
        #DashboardAccountChartDiv {
            margin-left: 20px; /* Align charts to the right */
            width: 800px;
            height: 500px;
        }
    </style>
</head>
<body>
    <div class="DashboardContent">
        <h1>Dashboard</h1>
        <form method="GET" action="dashboard.jsp">
            <div class="DashboardFilters">
                <label for="DashboardYearDropdown"><strong>Select Year:</strong></label>
                <select id="DashboardYearDropdown" name="selectedYear" onchange="this.form.submit()">
                    <%
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        String selectedYearParam = request.getParameter("selectedYear");
                        int selectedYear = (selectedYearParam != null) ? Integer.parseInt(selectedYearParam) : currentYear;

                        for (int year = currentYear; year >= 2000; year--) {
                    %>
                    <option value="<%= year %>" <%= (year == selectedYear) ? "selected" : "" %>><%= year %></option>
                    <% } %>
                </select>
            </div>
        </form>

        <div class="DashboardChartContainer">
            <h3>Order States Distribution</h3>
            <div id="DashboardOrderChartDiv"></div>
        </div>
        <div class="DashboardChartContainer">
            <h3>Product Quantities by Category</h3>
            <div id="DashboardProductChartDiv"></div>
        </div>
        <div class="DashboardChartContainer">
            <h3>Account Roles Distribution</h3>
            <div id="DashboardAccountChartDiv"></div>
        </div>
    </div>
</body>
</html>