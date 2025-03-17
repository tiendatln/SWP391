/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.VoucherDao;
import Model.Voucher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet xử lý các yêu cầu liên quan đến quản lý voucher: hiển thị, thêm, cập nhật, xóa.
 * @author tiend
 */
public class VoucherController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * Chuyển hướng đến doGet để hiển thị danh sách voucher.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect(request.getContextPath() + "/VoucherController");
    }

    /**
     * Handles the HTTP <code>GET</code> method. Hiển thị danh sách voucher dựa trên từ khóa tìm kiếm.
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
            String searchKeyword = request.getParameter("search");
            VoucherDao voucherDao = new VoucherDao();
            List<Voucher> voucherList;

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                voucherList = voucherDao.searchVouchers(searchKeyword.trim());
            } else {
                voucherList = voucherDao.getAllVouchers();
            }

            request.setAttribute("voucherList", voucherList);
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách voucher: " + e.getMessage());
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method. Xử lý thêm, cập nhật và xóa voucher.
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
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String percentDiscountStr = request.getParameter("percentDiscount");
            String quantityStr = request.getParameter("quantity");
            String deleteVoucherCode = request.getParameter("deleteVoucherCode");
            String idParam = request.getParameter("id");

            VoucherDao voucherDao = new VoucherDao();

            // Xử lý xóa voucher
            if (deleteVoucherCode != null && !deleteVoucherCode.isEmpty()) {
                Voucher voucherToDelete = voucherDao.getVoucherByCode(deleteVoucherCode);
                if (voucherToDelete != null) {
                    boolean result = voucherDao.deleteVoucher(voucherToDelete);
                    if (result) {
                        request.getSession().setAttribute("message", "Xóa voucher thành công!");
                    } else {
                        request.setAttribute("error", "Lỗi khi xóa voucher.");
                    }
                } else {
                    request.setAttribute("error", "Không tìm thấy voucher để xóa.");
                }
                response.sendRedirect(request.getContextPath() + "/VoucherController");
                return;
            }

            // Kiểm tra dữ liệu đầu vào
            if (voucherCode == null || voucherCode.trim().isEmpty() || 
                startDateStr == null || startDateStr.isEmpty() || 
                endDateStr == null || endDateStr.isEmpty() || 
                percentDiscountStr == null || percentDiscountStr.isEmpty() || 
                quantityStr == null || quantityStr.isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ các trường.");
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            // Chuyển đổi dữ liệu
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            int percentDiscount = Integer.parseInt(percentDiscountStr);
            int quantity = Integer.parseInt(quantityStr);

            // Kiểm tra logic nghiệp vụ
            if (startDate.isAfter(endDate)) {
                request.setAttribute("error", "Ngày bắt đầu phải trước ngày hết hạn.");
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }
            if (percentDiscount < 0 || percentDiscount > 100) {
                request.setAttribute("error", "Phần trăm giảm giá phải từ 0 đến 100.");
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            Voucher voucher = new Voucher(voucherCode, startDate, endDate, percentDiscount, quantity, 0); // Mặc định usedTime là 0 khi thêm mới
            boolean result;

            if (idParam != null && !idParam.isEmpty()) {
                // Cập nhật voucher
                int voucherId = Integer.parseInt(idParam);
                Voucher existingVoucher = voucherDao.getVoucherByCode(voucherCode); // Lấy thông tin hiện tại từ DB
                if (existingVoucher != null && existingVoucher.getVoucherID() == voucherId) {
                    voucher.setVoucherID(voucherId);
                    voucher.setUsedTime(existingVoucher.getUsedTime()); // Giữ nguyên usedTime từ DB
                    if (quantity < existingVoucher.getUsedTime()) {
                        request.setAttribute("error", "Số lượng phải lớn hơn hoặc bằng số lần đã dùng (" + existingVoucher.getUsedTime() + ").");
                        request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                        return;
                    }
                    result = voucherDao.updateVoucher(voucher);
                    if (result) {
                        request.getSession().setAttribute("message", "Cập nhật voucher thành công!");
                    } else {
                        request.setAttribute("error", "Lỗi khi cập nhật voucher.");
                    }
                } else {
                    request.setAttribute("error", "Voucher không tồn tại hoặc mã không khớp.");
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
            } else {
                // Thêm voucher mới
                if (voucherDao.getVoucherByCode(voucherCode) != null) {
                    request.setAttribute("error", "Mã voucher đã tồn tại.");
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
                result = voucherDao.insertVoucher(voucher);
                if (result) {
                    request.getSession().setAttribute("message", "Thêm voucher thành công!");
                } else {
                    request.setAttribute("error", "Lỗi khi thêm voucher.");
                }
            }

            response.sendRedirect(request.getContextPath() + "/VoucherController");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Dữ liệu số không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet quản lý voucher cho nhân viên.";
    }
}