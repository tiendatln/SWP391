/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.RevenueDAO;
import Model.RevenueReport;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hnpt6504
 */
@WebServlet(name = "RevenueServlet", urlPatterns = {"/RevenueController/Report"})
public class RevenueServlet extends HttpServlet {

    private RevenueDAO revenueDAO;

    public void init() {
        revenueDAO = new RevenueDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String groupBy = request.getParameter("groupBy");

        if (startDate == null || endDate == null || groupBy == null) {
            request.getRequestDispatcher("/web/revenueReport.jsp").forward(request, response);
            return;
        }

        try {
            // Lấy dữ liệu báo cáo doanh thu
            List<RevenueReport> reports = revenueDAO.getRevenueReport(startDate, endDate, groupBy);
            request.setAttribute("reports", reports);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("groupBy", groupBy);

            // Tính tổng doanh thu theo period
            Map<String, Double> periodRevenue = new HashMap<>();
            for (RevenueReport report : reports) {
                String period = report.getPeriod();
                periodRevenue.put(period,
                        periodRevenue.getOrDefault(period, 0.0) + report.getTotalRevenue());
            }
            request.setAttribute("periodRevenueLabels", periodRevenue.keySet());
            request.setAttribute("periodRevenueData", periodRevenue.values());

            // Chuẩn bị chuỗi groupBy viết hoa chữ cái đầu
            String capitalizedGroupBy = groupBy.substring(0, 1).toUpperCase() + groupBy.substring(1);
            request.setAttribute("capitalizedGroupBy", capitalizedGroupBy);

            // Lấy dữ liệu trạng thái đơn hàng
            Map<String, Integer> orderStatusData = revenueDAO.getOrderStatusCount(startDate, endDate);
            request.setAttribute("orderStatusLabels", orderStatusData.keySet());
            request.setAttribute("orderStatusData", orderStatusData.values());
            // Grouping and aggregating the data by productId (using int)
            Map<Integer, RevenueReport> aggregatedReports = new HashMap<>();
            for (RevenueReport report : reports) {
                int productId = report.getProductId();
                if (!aggregatedReports.containsKey(productId)) {
                    aggregatedReports.put(productId, report);
                } else {
                    RevenueReport existingReport = aggregatedReports.get(productId);
                    existingReport.setTotalRevenue(existingReport.getTotalRevenue() + report.getTotalRevenue());
                    existingReport.setTotalQuantity(existingReport.getTotalQuantity() + report.getTotalQuantity());
                }
            }

            // Convert to a list for easier display in JSP
            List<RevenueReport> aggregatedReportList = new ArrayList<>(aggregatedReports.values());
            request.setAttribute("reports", aggregatedReportList);

            request.getRequestDispatcher("/web/revenueReport.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
