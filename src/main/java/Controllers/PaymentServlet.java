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
        // Lấy dữ liệu từ request hoặc session được truyền từ OrderController
        String account = "070112566455"; // Số tài khoản Sacombank
        String amount = (String) request.getAttribute("amount"); // Số tiền từ OrderController
        String description = (String) request.getAttribute("description"); // Ghi chú từ OrderController

        // Kiểm tra dữ liệu đầu vào
        if (amount == null || amount.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Số tiền không được để trống!");
            return;
        }
        if (description == null || description.isEmpty()) {
            description = "Thanh toán đơn hàng"; // Giá trị mặc định nếu không có ghi chú
        }

        // Tạo chuỗi VietQR theo chuẩn NAPAS
        String vietQRData = buildVietQRData(account, amount, description);

        // Đường dẫn lưu QR
        String filePath = getServletContext().getRealPath("/") + "sacombank_qr.png";
        try {
            generateQRCodeImage(vietQRData, 250, 250, filePath);
        } catch (WriterException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tạo mã QR!");
            return;
        }

        // Truyền đường dẫn QR về JSP để hiển thị
        request.setAttribute("qrPath", "sacombank_qr.png");
        request.getRequestDispatcher("payment.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, 
                           "This endpoint only supports POST requests. Please use the form at /index.jsp.");
    }

    private String buildVietQRData(String account, String amount, String description) {
        // Định dạng chuẩn VietQR theo NAPAS
        StringBuilder qrData = new StringBuilder();
        
        // Header
        qrData.append("000201"); // Version
        qrData.append("010212"); // Method: Dynamic QR

        // Thông tin ngân hàng (Sacombank - BIN 970403)
        qrData.append("38"); // GUID length
        qrData.append(String.format("%02d", 38)); // Độ dài dữ liệu ngân hàng
        qrData.append("00").append("10").append("A000000727"); // Global Unique Identifier
        qrData.append("01"); // Payment info length
        qrData.append(String.format("%02d", 12 + account.length())); // Độ dài thông tin thanh toán
        qrData.append("00").append("06").append("970403"); // BIN Sacombank
        qrData.append("01").append(String.format("%02d", account.length())).append(account); // Số tài khoản

        // Loại giao dịch
        qrData.append("5204"); // Merchant Category Code
        qrData.append("0000"); // Default MCC

        // Tiền tệ (VND = 704)
        qrData.append("5303").append("704");

        // Số tiền
        qrData.append("54"); // Amount field
        qrData.append(String.format("%02d", amount.length())).append(amount); // Số tiền

        // Quốc gia
        qrData.append("5802").append("VN");

        // Nội dung thanh toán
        qrData.append("62"); // Additional data field
        qrData.append(String.format("%02d", 8 + description.length())); // Độ dài nội dung
        qrData.append("08"); // Purpose of transaction
        qrData.append(String.format("%02d", description.length())).append(description);

        // Checksum (CRC16)
        qrData.append("6304"); // CRC field
        String crc = calculateCRC(qrData.toString());
        qrData.append(crc);

        return qrData.toString();
    }

    private String calculateCRC(String data) {
        int crc = 0xFFFF;
        byte[] bytes = data.getBytes();
        for (byte b : bytes) {
            crc ^= (b & 0xFF);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >> 1) ^ 0x8408;
                } else {
                    crc >>= 1;
                }
            }
        }
        return String.format("%04X", crc).toUpperCase();
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