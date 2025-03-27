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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet handling requests related to voucher management: display, add, update, delete.
 *
 * @author tiend
 */
public class VoucherController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Redirects to doGet to display the voucher list.
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
     * Handles the HTTP <code>GET</code> method. Displays the voucher list based
     * on the search keyword.
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
            request.setAttribute("error", "Error loading voucher list: " + e.getMessage());
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method. Handles adding, updating, and deleting
     * vouchers.
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

            // Handle voucher deletion
            if (deleteVoucherCode != null && !deleteVoucherCode.isEmpty()) {
                Voucher voucherToDelete = voucherDao.getVoucherByCode(deleteVoucherCode);
                if (voucherToDelete != null) {
                    boolean result = voucherDao.deleteVoucher(voucherToDelete);
                    if (result) {
                        request.getSession().setAttribute("message", "Voucher has been successfully deleted!");
                    } else {
                        request.getSession().setAttribute("error", "Error deleting voucher.");
                    }
                } else {
                    request.getSession().setAttribute("error", "Voucher not found for deletion.");
                }
                response.sendRedirect(request.getContextPath() + "/VoucherController");
                return;
            }

            // Validate input data
            if (voucherCode == null || voucherCode.trim().isEmpty() || 
                startDateStr == null || startDateStr.isEmpty() || 
                endDateStr == null || endDateStr.isEmpty() || 
                percentDiscountStr == null || percentDiscountStr.isEmpty() || 
                quantityStr == null || quantityStr.isEmpty()) {
                request.setAttribute("modalError", "Please fill in all required fields.");
                request.setAttribute("voucherCode", voucherCode);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("percentDiscount", percentDiscountStr);
                request.setAttribute("quantity", quantityStr);
                request.setAttribute("id", idParam);
                request.setAttribute("errorFields", determineErrorFields(voucherCode, startDateStr, endDateStr, percentDiscountStr, quantityStr));
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            // Convert data
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            int percentDiscount = Integer.parseInt(percentDiscountStr);
            int quantity = Integer.parseInt(quantityStr);

            // Business logic validation
            LocalDate currentDate = LocalDate.now(); // Get current date
            if (startDate.isBefore(currentDate)) {
                request.setAttribute("modalError", "Start date cannot be in the past.");
                request.setAttribute("voucherCode", voucherCode);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("percentDiscount", percentDiscountStr);
                request.setAttribute("quantity", quantityStr);
                request.setAttribute("id", idParam);
                request.setAttribute("errorFields", "startDate");
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }
            if (endDate.isBefore(currentDate)) {
                request.setAttribute("modalError", "End date cannot be in the past.");
                request.setAttribute("voucherCode", voucherCode);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("percentDiscount", percentDiscountStr);
                request.setAttribute("quantity", quantityStr);
                request.setAttribute("id", idParam);
                request.setAttribute("errorFields", "endDate");
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }
            if (startDate.isAfter(endDate)) {
                request.setAttribute("modalError", "Start date must be before end date.");
                request.setAttribute("voucherCode", voucherCode);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("percentDiscount", percentDiscountStr);
                request.setAttribute("quantity", quantityStr);
                request.setAttribute("id", idParam);
                request.setAttribute("errorFields", "startDate,endDate");
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }
            if (percentDiscount < 0 || percentDiscount > 100) {
                request.setAttribute("modalError", "Discount percentage must be between 0 and 100.");
                request.setAttribute("voucherCode", voucherCode);
                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("percentDiscount", percentDiscountStr);
                request.setAttribute("quantity", quantityStr);
                request.setAttribute("id", idParam);
                request.setAttribute("errorFields", "percentDiscount");
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            Voucher voucher = new Voucher(voucherCode, startDate, endDate, percentDiscount, quantity, 0); // Default usedTime is 0 for new voucher
            boolean result;

            if (idParam != null && !idParam.isEmpty()) {
                // Update voucher
                int voucherId = Integer.parseInt(idParam);
                Voucher existingVoucher = voucherDao.getVoucherByCode(voucherCode); // Get current info from DB
                if (existingVoucher != null && existingVoucher.getVoucherID() == voucherId) {
                    voucher.setVoucherID(voucherId);
                    voucher.setUsedTime(existingVoucher.getUsedTime()); // Retain usedTime from DB
                    if (quantity < existingVoucher.getUsedTime()) {
                        request.setAttribute("modalError", "Quantity must be greater than or equal to the number of times used (" + existingVoucher.getUsedTime() + ").");
                        request.setAttribute("voucherCode", voucherCode);
                        request.setAttribute("startDate", startDateStr);
                        request.setAttribute("endDate", endDateStr);
                        request.setAttribute("percentDiscount", percentDiscountStr);
                        request.setAttribute("quantity", quantityStr);
                        request.setAttribute("id", idParam);
                        request.setAttribute("errorFields", "quantity");
                        List<Voucher> voucherList = voucherDao.getAllVouchers();
                        request.setAttribute("voucherList", voucherList);
                        request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                        return;
                    }
                    result = voucherDao.updateVoucher(voucher);
                    if (result) {
                        request.getSession().setAttribute("message", "Voucher has been successfully updated!");
                    } else {
                        request.setAttribute("modalError", "Error updating voucher.");
                        request.setAttribute("voucherCode", voucherCode);
                        request.setAttribute("startDate", startDateStr);
                        request.setAttribute("endDate", endDateStr);
                        request.setAttribute("percentDiscount", percentDiscountStr);
                        request.setAttribute("quantity", quantityStr);
                        request.setAttribute("id", idParam);
                        List<Voucher> voucherList = voucherDao.getAllVouchers();
                        request.setAttribute("voucherList", voucherList);
                        request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                        return;
                    }
                } else {
                    request.setAttribute("modalError", "Voucher code must not be duplicated.");
                    request.setAttribute("voucherCode", voucherCode);
                    request.setAttribute("startDate", startDateStr);
                    request.setAttribute("endDate", endDateStr);
                    request.setAttribute("percentDiscount", percentDiscountStr);
                    request.setAttribute("quantity", quantityStr);
                    request.setAttribute("id", idParam);
                    request.setAttribute("errorFields", "voucherCode");
                    List<Voucher> voucherList = voucherDao.getAllVouchers();
                    request.setAttribute("voucherList", voucherList);
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
            } else {
                // Add new voucher
                if (voucherDao.getVoucherByCode(voucherCode) != null) {
                    request.setAttribute("modalError", "Voucher code already exists.");
                    request.setAttribute("voucherCode", voucherCode);
                    request.setAttribute("startDate", startDateStr);
                    request.setAttribute("endDate", endDateStr);
                    request.setAttribute("percentDiscount", percentDiscountStr);
                    request.setAttribute("quantity", quantityStr);
                    request.setAttribute("id", idParam);
                    request.setAttribute("errorFields", "voucherCode");
                    List<Voucher> voucherList = voucherDao.getAllVouchers();
                    request.setAttribute("voucherList", voucherList);
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
                result = voucherDao.insertVoucher(voucher);
                if (result) {
                    request.getSession().setAttribute("message", "Voucher has been successfully added!");
                } else {
                    request.setAttribute("modalError", "Error adding voucher.");
                    request.setAttribute("voucherCode", voucherCode);
                    request.setAttribute("startDate", startDateStr);
                    request.setAttribute("endDate", endDateStr);
                    request.setAttribute("percentDiscount", percentDiscountStr);
                    request.setAttribute("quantity", quantityStr);
                    request.setAttribute("id", idParam);
                    List<Voucher> voucherList = voucherDao.getAllVouchers();
                    request.setAttribute("voucherList", voucherList);
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
            }

            response.sendRedirect(request.getContextPath() + "/VoucherController");
        } catch (NumberFormatException e) {
            try {
                e.printStackTrace();
                request.setAttribute("modalError", "Invalid number format: " + e.getMessage());
                request.setAttribute("voucherCode", request.getParameter("voucherCode"));
                request.setAttribute("startDate", request.getParameter("startDate"));
                request.setAttribute("endDate", request.getParameter("endDate"));
                request.setAttribute("percentDiscount", request.getParameter("percentDiscount"));
                request.setAttribute("quantity", request.getParameter("quantity"));
                request.setAttribute("id", request.getParameter("id"));
                request.setAttribute("errorFields", "percentDiscount,quantity");
                VoucherDao voucherDao = new VoucherDao();
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VoucherController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                request.setAttribute("modalError", "System error: " + e.getMessage());
                request.setAttribute("voucherCode", request.getParameter("voucherCode"));
                request.setAttribute("startDate", request.getParameter("startDate"));
                request.setAttribute("endDate", request.getParameter("endDate"));
                request.setAttribute("percentDiscount", request.getParameter("percentDiscount"));
                request.setAttribute("quantity", request.getParameter("quantity"));
                request.setAttribute("id", request.getParameter("id"));
                VoucherDao voucherDao = new VoucherDao();
                List<Voucher> voucherList = voucherDao.getAllVouchers();
                request.setAttribute("voucherList", voucherList);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(VoucherController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Determines the fields with errors to mark with a red border.
     */
    private String determineErrorFields(String voucherCode, String startDate, String endDate, String percentDiscount, String quantity) {
        StringBuilder errorFields = new StringBuilder();
        if (voucherCode == null || voucherCode.trim().isEmpty()) {
            errorFields.append("voucherCode,");
        }
        if (startDate == null || startDate.isEmpty()) {
            errorFields.append("startDate,");
        }
        if (endDate == null || endDate.isEmpty()) {
            errorFields.append("endDate,");
        }
        if (percentDiscount == null || percentDiscount.isEmpty()) {
            errorFields.append("percentDiscount,");
        }
        if (quantity == null || quantity.isEmpty()) {
            errorFields.append("quantity,");
        }
        if (errorFields.length() > 0) {
            errorFields.setLength(errorFields.length() - 1); // Remove the last comma
        }
        return errorFields.toString();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for managing vouchers for staff.";
    }
}