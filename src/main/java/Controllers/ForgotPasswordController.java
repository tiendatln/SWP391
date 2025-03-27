package Controllers;

import DAOs.AccountDAO;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/ForgotPasswordController", "/ForgotPasswordController/verify", "/ForgotPasswordController/reset", "/ForgotPasswordController/resend"})
public class ForgotPasswordController extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
        if (accountDAO == null) {
            throw new ServletException("Failed to initialize AccountDAO.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/ForgotPasswordController")) {
            request.getRequestDispatcher("/web/forgot-password.jsp").forward(request, response);
        } else if (path.equals("/ForgotPasswordController/verify") || path.equals("/ForgotPasswordController/resend")) {
            request.getRequestDispatcher("/web/forgot-password.jsp").forward(request, response);
        } else if (path.equals("/ForgotPasswordController/reset")) {
            request.getRequestDispatcher("/web/reset-password.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid URL");
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String path = request.getServletPath();
    HttpSession session = request.getSession();
    response.setContentType("text/html;charset=UTF-8");

    if (path.equals("/ForgotPasswordController")) {
        // Bước 1: Kiểm tra email và gửi OTP
        String email = request.getParameter("email");

        if (accountDAO.checkEmailExists(email)) {
            String otp = generateOTP();
            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            session.setAttribute("otpTimestamp", System.currentTimeMillis());

            String emailResult = sendOTPEmail(email, otp);

            if (emailResult.equals("success")) {
                request.setAttribute("message", "An OTP has been sent to your email.");
            } else {
                request.setAttribute("error", emailResult);
            }
        } else {
            request.setAttribute("error", "Email not found.");
        }
        request.getRequestDispatcher("/web/forgot-password.jsp").forward(request, response);

    } else if (path.equals("/ForgotPasswordController/verify")) {
        // Bước 2: Xác thực OTP
        String storedOtp = (String) session.getAttribute("otp");
        Long otpTimestamp = (Long) session.getAttribute("otpTimestamp");
        String inputOtp = request.getParameter("otp");

        long currentTime = System.currentTimeMillis();
        long OTP_EXPIRY_TIME = 10 * 60 * 1000; // 10 phút
        if (otpTimestamp == null || (currentTime - otpTimestamp) > OTP_EXPIRY_TIME) {
            session.removeAttribute("otp");
            session.removeAttribute("otpTimestamp");
            response.getWriter().write("OTP has expired. Please request a new one.");
            return;
        }

        if (inputOtp != null && inputOtp.equals(storedOtp)) {
            response.getWriter().write("OTP verified successfully.");
        } else {
            response.getWriter().write("Invalid OTP. Please try again.");
        }

    } else if (path.equals("/ForgotPasswordController/resend")) {
        // Bước gửi lại OTP
        String email = (String) session.getAttribute("email");

        if (email == null) {
            request.setAttribute("error", "Session expired. Please start over.");
            request.getRequestDispatcher("/web/forgot-password.jsp").forward(request, response);
            return;
        }

        String newOtp = generateOTP();
        session.setAttribute("otp", newOtp);
        session.setAttribute("otpTimestamp", System.currentTimeMillis());

        String emailResult = sendOTPEmail(email, newOtp);

        if (emailResult.equals("success")) {
            request.setAttribute("message", "A new OTP has been sent to your email.");
        } else {
            request.setAttribute("error", emailResult);
        }
        request.getRequestDispatcher("/web/forgot-password.jsp").forward(request, response);

    } else if (path.equals("/ForgotPasswordController/reset")) {
        // Bước 3: Cập nhật mật khẩu mới
        String email = (String) session.getAttribute("email");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (email == null) {
            request.setAttribute("error", "Session expired. Please start over.");
            request.getRequestDispatcher("/web/forgot-password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/web/reset-password.jsp").forward(request, response);
            return;
        }

        boolean updated = accountDAO.updatePassword(email, newPassword);
        if (updated) {
            session.removeAttribute("otp");
            session.removeAttribute("email");
            session.removeAttribute("otpTimestamp");
            request.setAttribute("message", "Password reset successfully. Please login.");
            request.getRequestDispatcher("/web/reset-password.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to reset password. Please try again.");
            request.getRequestDispatcher("/web/reset-password.jsp").forward(request, response);
        }
    }
}

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo OTP 6 số
        return String.valueOf(otp);
    }

    private String sendOTPEmail(String toEmail, String otp) {
        final String fromEmail = "hnpt3531@gmail.com";
        final String password = "jiuh sape mftd outq"; // App Password đã hoạt động

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Smart Devices Selling Website - Reset Password");

            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<h2 style='color: #007bff;'>Password Reset Request</h2>" +
                    "<p>Dear user,</p>" +
                    "<p>We received a request to reset your password for your account associated with <strong>" + toEmail + "</strong>.</p>" +
                    "<p>Your OTP is: <strong style='font-size: 18px; color: #28a745;'>" + otp + "</strong></p>" +
                    "<p>Please enter this OTP in the application to reset your password. This OTP will expire in <strong>10 minutes</strong>.</p>" +
                    "<p>If you did not request a password reset, please ignore this email or contact our support team.</p>" +
                    "<p style='font-size: 12px; color: #666;'>If you found this email in your Spam/Junk folder, please mark it as 'Not Spam' to ensure future emails reach your inbox.</p>" +
                    "<p>Best regards,<br>Your App Name Team<br>" +
                    "<a href='mailto:support@yourapp.com' style='color: #007bff;'>support@yourapp.com</a></p>" +
                    "</body>" +
                    "</html>";

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Your OTP is: " + otp + "\nPlease use this to reset your password. This OTP will expire in 10 minutes.");

            Multipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("OTP email sent to: " + toEmail);
            return "success";
        } catch (MessagingException e) {
            String errorMessage;
            if (e.getMessage().contains("Authentication failed")) {
                errorMessage = "Failed to send OTP: Invalid email credentials. Please check your App Password.";
            } else if (e.getMessage().contains("Connection timed out")) {
                errorMessage = "Failed to send OTP: Cannot connect to email server. Please check your internet connection.";
            } else {
                errorMessage = "Failed to send OTP: " + e.getMessage();
            }
            System.out.println("Failed to send OTP email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return errorMessage;
        }
    }
}