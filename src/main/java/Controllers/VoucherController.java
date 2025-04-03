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
 * Servlet to handle voucher management requests: display, add, update, and delete.
 * @author tiend
 */
public class VoucherController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.sendRedirect(request.getContextPath() + "/VoucherController");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String searchKeyword = request.getParameter("search");
            String deleteVoucherCode = request.getParameter("deleteVoucherCode");
            VoucherDao voucherDao = new VoucherDao();

            // Handle voucher deletion
            if (deleteVoucherCode != null && !deleteVoucherCode.isEmpty()) {
                Voucher voucherToDelete = voucherDao.getVoucherByCode(deleteVoucherCode);
                if (voucherToDelete != null) {
                    boolean result = voucherDao.deleteVoucher(voucherToDelete);
                    if (result) {
                        request.getSession().setAttribute("message", "Voucher deleted successfully!");
                    } else {
                        request.setAttribute("error", "Error deleting voucher.");
                    }
                } else {
                    request.setAttribute("error", "Voucher not found for deletion.");
                }
                response.sendRedirect(request.getContextPath() + "/VoucherController");
                return;
            }

            // Load voucher list for display
            loadVoucherList(request, searchKeyword, voucherDao);
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading voucher list: " + e.getMessage());
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String voucherCode = request.getParameter("voucherCode");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String percentDiscountStr = request.getParameter("percentDiscount");
            String quantityStr = request.getParameter("quantity");
            String idParam = request.getParameter("id");

            VoucherDao voucherDao = new VoucherDao();

            // Validate input data and set error fields for modal
            if (voucherCode == null || voucherCode.trim().isEmpty() || 
                startDateStr == null || startDateStr.isEmpty() || 
                endDateStr == null || endDateStr.isEmpty() || 
                percentDiscountStr == null || percentDiscountStr.isEmpty() || 
                quantityStr == null || quantityStr.isEmpty()) {
                request.setAttribute("modalError", "Please fill in all fields.");
                setInvalidFields(request, voucherCode, startDateStr, endDateStr, percentDiscountStr, quantityStr);
                loadVoucherList(request, null, voucherDao);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            // Convert data
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            int percentDiscount = Integer.parseInt(percentDiscountStr);
            int quantity = Integer.parseInt(quantityStr);
            LocalDate currentDate = LocalDate.now();

            // Business logic validation with modal errors
            if (startDate.isAfter(endDate)) {
                request.setAttribute("modalError", "Start date must be before end date.");
                request.setAttribute("invalidStartDate", true);
                request.setAttribute("invalidEndDate", true);
                loadVoucherList(request, null, voucherDao);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }
            if (percentDiscount < 0 || percentDiscount > 100) {
                request.setAttribute("modalError", "Discount percentage must be between 0 and 100.");
                request.setAttribute("invalidPercentDiscount", true);
                loadVoucherList(request, null, voucherDao);
                request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                return;
            }

            Voucher voucher = new Voucher(voucherCode, startDate, endDate, percentDiscount, quantity, 0);
            boolean result;

            if (idParam != null && !idParam.isEmpty()) {
                // Update voucher (no past date restriction)
                int voucherId = Integer.parseInt(idParam);
                Voucher existingVoucher = voucherDao.getVoucherByCode(voucherCode);
                if (existingVoucher != null && existingVoucher.getVoucherID() == voucherId) {
                    voucher.setVoucherID(voucherId);
                    voucher.setUsedTime(existingVoucher.getUsedTime());
                    if (quantity < existingVoucher.getUsedTime()) {
                        request.setAttribute("modalError", "Quantity must be greater than or equal to the number of times used (" + existingVoucher.getUsedTime() + ").");
                        request.setAttribute("invalidQuantity", true);
                        loadVoucherList(request, null, voucherDao);
                        request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                        return;
                    }
                    result = voucherDao.updateVoucher(voucher);
                    if (result) {
                        request.getSession().setAttribute("message", "Voucher updated successfully!");
                    } else {
                        request.setAttribute("modalError", "Error updating voucher.");
                        loadVoucherList(request, null, voucherDao);
                    }
                } else {
                    // Allow changing voucherCode
                    Voucher voucherById = voucherDao.getVoucherById(voucherId);
                    if (voucherById != null) {
                        Voucher duplicateVoucher = voucherDao.getVoucherByCode(voucherCode);
                        if (duplicateVoucher != null && duplicateVoucher.getVoucherID() != voucherId) {
                            request.setAttribute("modalError", "Voucher code already exists.");
                            request.setAttribute("invalidVoucherCode", true);
                            loadVoucherList(request, null, voucherDao);
                            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                            return;
                        }
                        voucher.setVoucherID(voucherId);
                        voucher.setUsedTime(voucherById.getUsedTime());
                        if (quantity < voucherById.getUsedTime()) {
                            request.setAttribute("modalError", "Quantity must be greater than or equal to the number of times used (" + voucherById.getUsedTime() + ").");
                            request.setAttribute("invalidQuantity", true);
                            loadVoucherList(request, null, voucherDao);
                            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                            return;
                        }
                        result = voucherDao.updateVoucher(voucher);
                        if (result) {
                            request.getSession().setAttribute("message", "Voucher updated successfully!");
                        } else {
                            request.setAttribute("modalError", "Error updating voucher.");
                            loadVoucherList(request, null, voucherDao);
                        }
                    } else {
                        request.setAttribute("modalError", "Voucher does not exist.");
                        request.setAttribute("invalidVoucherCode", true);
                        loadVoucherList(request, null, voucherDao);
                        request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                        return;
                    }
                }
            } else {
                // Add new voucher (keep past date restriction)
                if (startDate.isBefore(currentDate)) {
                    request.setAttribute("modalError", "Start date cannot be in the past.");
                    request.setAttribute("invalidStartDate", true);
                    loadVoucherList(request, null, voucherDao);
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
                if (voucherDao.getVoucherByCode(voucherCode) != null) {
                    request.setAttribute("modalError", "Voucher code already exists.");
                    request.setAttribute("invalidVoucherCode", true);
                    loadVoucherList(request, null, voucherDao);
                    request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
                    return;
                }
                result = voucherDao.insertVoucher(voucher);
                if (result) {
                    request.getSession().setAttribute("message", "Voucher added successfully!");
                } else {
                    request.setAttribute("modalError", "Error adding voucher.");
                    loadVoucherList(request, null, voucherDao);
                }
            }

            response.sendRedirect(request.getContextPath() + "/VoucherController");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("modalError", "Invalid numeric data: " + e.getMessage());
            setInvalidNumericFields(request, request.getParameter("percentDiscount"), request.getParameter("quantity"));
            VoucherDao voucherDao = new VoucherDao();
            loadVoucherList(request, null, voucherDao);
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            VoucherDao voucherDao = new VoucherDao();
            loadVoucherList(request, null, voucherDao);
            request.getRequestDispatcher("/web/Staff/Voucher.jsp").forward(request, response);
        }
    }

    // Helper method to load voucher list
    private void loadVoucherList(HttpServletRequest request, String searchKeyword, VoucherDao voucherDao) {
        try {
            List<Voucher> voucherList;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                voucherList = voucherDao.searchVouchers(searchKeyword.trim());
            } else {
                voucherList = voucherDao.getAllVouchers();
            }
            request.setAttribute("voucherList", voucherList);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading voucher list: " + e.getMessage());
        }
    }

    // Helper method to set invalid fields for empty inputs
    private void setInvalidFields(HttpServletRequest request, String voucherCode, String startDate, String endDate, 
                                  String percentDiscount, String quantity) {
        if (voucherCode == null || voucherCode.trim().isEmpty()) request.setAttribute("invalidVoucherCode", true);
        if (startDate == null || startDate.isEmpty()) request.setAttribute("invalidStartDate", true);
        if (endDate == null || endDate.isEmpty()) request.setAttribute("invalidEndDate", true);
        if (percentDiscount == null || percentDiscount.isEmpty()) request.setAttribute("invalidPercentDiscount", true);
        if (quantity == null || quantity.isEmpty()) request.setAttribute("invalidQuantity", true);
    }

    // Helper method to set invalid fields for numeric errors
    private void setInvalidNumericFields(HttpServletRequest request, String percentDiscount, String quantity) {
        try {
            Integer.parseInt(percentDiscount);
        } catch (NumberFormatException e) {
            request.setAttribute("invalidPercentDiscount", true);
        }
        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            request.setAttribute("invalidQuantity", true);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing vouchers for admin.";
    }
}