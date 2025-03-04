import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/generateSacombankQR")
public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String account = "070112566455";
        String amount = "10000";
        String description = "description";

        // Chuỗi VietQR cho Sacombank
        String paymentData = "00020101021238570010A00000072701270006970403" + // BIN Sacombank
                            "0112" + account + // Số tài khoản
                            "520400005303704" +
                            (amount != null && !amount.isEmpty() ? "5406" + String.format("%06d", Integer.parseInt(amount)) : "") + // Số tiền (6 chữ số, thêm 0 nếu cần)
                            "5802VN" + 
                            "62" + (description != null ? String.format("%02d", description.length() + 4) + "0802" + description : "0408") + // Nội dung
                            "6304A1B2"; // Checksum (giả định, cần tính chính xác nếu dùng thực tế)

        // Đường dẫn lưu QR
        String filePath = getServletContext().getRealPath("/") + "sacombank_qr.png";
        try {
            generateQRCodeImage(paymentData, 250, 250, filePath);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        request.setAttribute("qrPath", "sacombank_qr.png");
        request.getRequestDispatcher("payment.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, 
                           "This endpoint only supports POST requests. Please use the form at /index.jsp.");
    }

    private void generateQRCodeImage(String text, int width, int height, String filePath) 
            throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        File qrFile = new File(filePath);
        ImageIO.write(image, "PNG", qrFile);
    }
}
