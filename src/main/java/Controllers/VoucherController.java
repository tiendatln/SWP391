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
            String searchKeyword = request.getParameter("searchKeyword");

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

            if (voucherCode == null || voucherCode.isEmpty() || startDate == null || endDate == null) {
                throw new IllegalArgumentException("Các trường không được để trống.");
            }

            int percentDiscount = Integer.parseInt(request.getParameter("percentDiscount"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int usedTime = 0;
            if (request.getParameter("usedTime") != null) {
                usedTime = Integer.parseInt(request.getParameter("usedTime"));
            }

            VoucherDao voucherDao = new VoucherDao();
            String idParam = request.getParameter("id");
            String deleteVoucherCode = request.getParameter("deleteVoucherCode");
            boolean result;

            if (deleteVoucherCode != null && !deleteVoucherCode.isEmpty()) {
                Voucher voucherToDelete = new Voucher();
                voucherToDelete.setVoucherCode(deleteVoucherCode);
                result = voucherDao.deleteVoucher(voucherToDelete);
            } else if (idParam == null || idParam.isEmpty()) {
                Voucher voucher = new Voucher(voucherCode, LocalDate.parse(startDate),
                        LocalDate.parse(endDate), percentDiscount, quantity, usedTime, 0);
                result = voucherDao.insertVoucher(voucher);
            } else {
                int voucherId = Integer.parseInt(idParam);
                Voucher updatedVoucher = new Voucher(voucherCode, LocalDate.parse(startDate),
                        LocalDate.parse(endDate), percentDiscount, quantity, usedTime, voucherId);
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
