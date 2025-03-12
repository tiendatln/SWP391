/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package Controllers;

import DAOs.CommentsDAO;
import Model.Comments;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Kim Chi Khang _ CE180324
 */
public class CommentController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CommentController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CommentController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy ID sản phẩm từ yêu cầu
            int productID = Integer.parseInt(request.getParameter("productID"));

            CommentsDAO commentsDAO = new CommentsDAO();
            List<Comments> commentsList = commentsDAO.getCommentsByProduct(productID);

            // Đưa danh sách comment vào request để truyền sang JSP
            request.setAttribute("commentsList", commentsList);

            // Chuyển hướng đến trang hiển thị danh sách comment
            request.getRequestDispatcher("/web/Staff/Comments.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh sách comment.");
        }
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy các tham số từ yêu cầu
            int rate = Integer.parseInt(request.getParameter("rate"));
            String commentText = request.getParameter("commentText");
            int productID = Integer.parseInt(request.getParameter("productID"));
            int userID = Integer.parseInt(request.getParameter("userID"));
            String deleteCommentID = request.getParameter("deleteCommentID");

            // Kiểm tra nếu có mã comment cần xóa
            if (deleteCommentID != null && !deleteCommentID.isEmpty()) {
                // Xóa comment
                CommentsDAO commentsDAO = new CommentsDAO();
                boolean result = commentsDAO.deleteComment(Integer.parseInt(deleteCommentID), userID);
                if (result) {
                    response.sendRedirect(request.getContextPath() + "/CommentsController?productID=" + productID);
                } else {
                    request.setAttribute("error", "Lỗi khi xóa comment.");
                    request.getRequestDispatcher("/web/Staff/Comments.jsp").forward(request, response);
                }
                return;
            }

            // Thêm mới comment
            CommentsDAO commentsDAO = new CommentsDAO();
            boolean result = commentsDAO.addComment(rate, commentText, productID, userID);

            if (result) {
                response.sendRedirect(request.getContextPath() + "/CommentsController?productID=" + productID);
            } else {
                request.setAttribute("error", "Lỗi khi thêm comment.");
                request.getRequestDispatcher("/web/Staff/Comments.jsp").forward(request, response);
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
