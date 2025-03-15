/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.VoucherDao;
import Model.Voucher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author tiend
 */
public class VoucherController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VoucherController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VoucherController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        // Lấy từ khóa tìm kiếm từ yêu cầu
        String searchKeyword = request.getParameter("search");

        VoucherDao voucherDao = new VoucherDao();
        List<Voucher> voucherList;

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            // Nếu có từ khóa tìm kiếm, gọi phương thức tìm kiếm
            voucherList = voucherDao.searchVouchers(searchKeyword);
        } else {
            // Nếu không có từ khóa tìm kiếm, lấy tất cả voucher
            voucherList = voucherDao.getAllVouchers();
        }

        // Đưa danh sách voucher vào request để truyền sang JSP
        request.setAttribute("voucherList", voucherList);

        // Chuyển hướng đến trang hiển thị danh sách voucher
        request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh sách voucher.");
    }
}



    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String voucherCode = request.getParameter("voucherCode");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String percentDiscountStr = request.getParameter("percentDiscount");
            String quantityStr = request.getParameter("quantity");
            String usedTimeStr = request.getParameter("usedTime");
            String deleteVoucherCode = request.getParameter("deleteVoucherCode"); // Lấy mã voucher cần xóa

            // Kiểm tra nếu xóa voucher, chỉ cần mã voucher là đủ
            if (deleteVoucherCode != null && !deleteVoucherCode.isEmpty()) {
                // Nếu có mã voucher cần xóa
                VoucherDao voucherDao = new VoucherDao();
                Voucher voucherToDelete = new Voucher();
                voucherToDelete.setVoucherCode(deleteVoucherCode);  // Gán voucherCode vào đối tượng Voucher

                boolean result = voucherDao.deleteVoucher(voucherToDelete);  // Gọi phương thức xóa voucher

                if (result) {
                    response.sendRedirect(request.getContextPath() + "/VoucherController");
                } else {
                    request.setAttribute("error", "Lỗi khi xóa voucher.");
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                }
                return; // Dừng lại khi thực hiện xóa
            }

            // Kiểm tra các trường không được để trống khi không xóa
            if (voucherCode == null || voucherCode.isEmpty()
                    || startDate == null || startDate.isEmpty()
                    || endDate == null || endDate.isEmpty()
                    || percentDiscountStr == null || percentDiscountStr.isEmpty()
                    || quantityStr == null || quantityStr.isEmpty()) {
                request.setAttribute("error", "Các trường không được để trống.");
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            // Chuyển đổi các giá trị số và kiểm tra hợp lệ
            int percentDiscount = Integer.parseInt(percentDiscountStr);
            int quantity = Integer.parseInt(quantityStr);
            int usedTime = 0;
            if (usedTimeStr != null && !usedTimeStr.isEmpty()) {
                usedTime = Integer.parseInt(usedTimeStr);
            }

            VoucherDao voucherDao = new VoucherDao();
            String idParam = request.getParameter("id");
            boolean result;

            if (idParam == null || idParam.isEmpty()) {
                // Thêm mới voucher
                Voucher voucher = new Voucher(voucherCode, LocalDate.parse(startDate),
                        LocalDate.parse(endDate), percentDiscount, quantity, usedTime);
                result = voucherDao.insertVoucher(voucher);
            } else {
                // Cập nhật voucher
                int voucherId = Integer.parseInt(idParam);
                Voucher updatedVoucher = new Voucher(voucherCode, LocalDate.parse(startDate),
                        LocalDate.parse(endDate), percentDiscount, quantity, usedTime);
                result = voucherDao.updateVoucher(updatedVoucher);
            }

            if (result) {
                response.sendRedirect(request.getContextPath() + "/VoucherController");
            } else {
                request.setAttribute("error", "Lỗi khi xử lý voucher.");
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống.");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
