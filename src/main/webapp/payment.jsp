<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mã QR Thanh toán Sacombank</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            text-align: center; 
            background-color: #f4f4f4; 
            margin: 0; 
            padding: 0; 
        }
        .container {
            background: white;
            width: 50%;
            margin: 50px auto;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
        }
        h2 { 
            color: #0044cc; 
        }
        p {
            font-size: 16px;
            color: #333;
        }
        .error {
            color: #dc3545;
            font-weight: bold;
        }
        img {
            width: 250px;
            height: 250px;
            margin-top: 10px;
            border: 2px solid #ddd;
            border-radius: 10px;
            padding: 10px;
            background: white;
        }
        .btn {
            display: inline-block;
            margin: 15px 10px;
            padding: 10px 20px;
            font-size: 16px;
            color: white;
            background: #007bff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            transition: background 0.3s ease;
        }
        .btn:hover {
            background: #0056b3;
        }
        .success {
            background: #28a745;
        }
        .success:hover {
            background: #218838;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Mã QR Thanh toán Sacombank</h2>
        
        <%-- Hiển thị thông tin thanh toán và mã QR --%>
        <c:if test="${not empty qrPath}">
            <p>Vui lòng quét mã QR bên dưới bằng ứng dụng Sacombank hoặc app hỗ trợ VietQR:</p>
            <p>${sessionScope.message1}</p>
            <p>${sessionScope.message2}</p>
            <img src="${pageContext.request.contextPath}/${qrPath}" alt="Sacombank QR Code">
            <br><br>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn success">Xác nhận thanh toán thành công</a>
        </c:if>
    </div>
</body>
</html>