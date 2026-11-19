# SWP391 - Hệ thống thương mại điện tử

Ứng dụng web thương mại điện tử được xây dựng bằng Java Servlet/JSP, Maven và Microsoft SQL Server. Dự án hỗ trợ quy trình mua sắm cơ bản, quản lý tài khoản, sản phẩm, đơn hàng, voucher và doanh thu.

> Repository: [tiendatln/SWP391](https://github.com/tiendatln/SWP391)

## Tính năng

- Đăng ký, đăng nhập và quản lý tài khoản
- Đăng nhập bằng API
- Quên mật khẩu và đặt lại mật khẩu
- Quản lý hồ sơ người dùng
- Xem danh mục và chi tiết sản phẩm
- Thêm, cập nhật và xóa sản phẩm trong giỏ hàng
- Tạo đơn hàng và xem thông tin thanh toán
- Quản lý voucher
- Quản lý bình luận
- Quản lý danh mục sản phẩm
- Báo cáo doanh thu và thống kê
- Giao diện dành cho khách hàng, nhân viên và quản trị viên

## Công nghệ

- Java
- Java Servlet và JSP
- Apache Maven
- Microsoft SQL Server
- JSTL
- Gson / org.json
- ZXing (mã QR)
- Jakarta Mail
- JDBC
- HTML, CSS và JavaScript

## Cấu trúc thư mục

```text
SWP391/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   ├── Controllers/  # Servlet điều phối request
│       │   ├── DAOs/         # Truy cập dữ liệu SQL Server
│       │   ├── DB/           # Cấu hình kết nối database
│       │   └── Model/        # Các đối tượng dữ liệu
│       └── webapp/
│           ├── WEB-INF/      # Cấu hình web.xml
│           ├── web/          # Trang JSP
│           ├── link/         # Tài nguyên liên quan
│           ├── Header.jsp
│           ├── Footer.jsp
│           └── payment.jsp
└── target/                   # Thư mục build Maven
```

## Yêu cầu

- JDK 8 hoặc cao hơn
- Apache Maven 3.x
- Apache Tomcat hoặc máy chủ Servlet/Jakarta EE tương thích
- Microsoft SQL Server
- NetBeans hoặc IDE Java có hỗ trợ Maven (khuyến nghị)

> Lưu ý: `pom.xml` hiện cấu hình Java 1.7 cho Maven Compiler Plugin và hỗn hợp các API Java EE/Jakarta EE. Nên kiểm tra và thống nhất phiên bản JDK cùng máy chủ servlet trước khi triển khai.

## Cài đặt cơ sở dữ liệu

1. Cài đặt Microsoft SQL Server và SQL Server Configuration Manager.
2. Tạo database tên `SWP391`.
3. Nhập dữ liệu/schema của dự án nếu repository cung cấp.
4. Cập nhật thông tin kết nối trong `src/main/java/DB/DBConnection.java`:

```java
jdbc:sqlserver://localhost:1433;databaseName=SWP391;user=sa;password=123;encrypt=true;trustServerCertificate=true;
```

Khuyến nghị không lưu mật khẩu database trực tiếp trong source code. Có thể chuyển thông tin này sang biến môi trường hoặc file cấu hình không được commit.

## Chạy dự án

### 1. Clone repository

```bash
git clone https://github.com/tiendatln/SWP391.git
cd SWP391
```

### 2. Build bằng Maven

```bash
mvn clean package
```

Nếu build thành công, Maven sẽ tạo file WAR trong thư mục:

```text
target/SWP_Project-1.0-SNAPSHOT.war
```

Tên file thực tế có thể khác tùy cấu hình Maven trong `pom.xml`.

### 3. Triển khai lên Tomcat

1. Sao chép file `.war` vào thư mục `webapps` của Apache Tomcat.
2. Khởi động Tomcat.
3. Mở ứng dụng theo context path tương ứng, ví dụ:

```text
http://localhost:8080/SWP_Project-1.0-SNAPSHOT/
```

Hoặc chạy project bằng NetBeans/Tomcat để IDE tự cấu hình context path.

## Kiểm tra bằng Maven

```bash
mvn test
```

## Vai trò người dùng

Giao diện của project được phân chia theo các khu vực:

- Khách và khách hàng: duyệt sản phẩm, giỏ hàng, đặt hàng và thanh toán
- Nhân viên: truy cập các trang quản lý liên quan trong thư mục `web/Staff`
- Quản trị viên: quản lý tài khoản, danh mục, voucher và doanh thu

## Ghi chú bảo mật

- Không commit mật khẩu database, khóa API, mật khẩu email hoặc token.
- Kiểm tra lại cơ chế xác thực, phân quyền và session trước khi triển khai production.
- Sử dụng tham số hóa truy vấn hoặc lớp DAO nhất quán để giảm nguy cơ SQL Injection.
- Bật HTTPS và cấu hình cookie/session an toàn khi đưa ứng dụng lên môi trường thực tế.

## Đóng góp

1. Fork repository.
2. Tạo branch mới:

```bash
git checkout -b feature/ten-tinh-nang
```

3. Commit thay đổi:

```bash
git commit -m "Thêm tính năng ..."
```

4. Push branch và tạo Pull Request.

## License

Repository hiện chưa cấu hình license. Vui lòng liên hệ chủ dự án trước khi sử dụng hoặc phân phối mã nguồn trong mục đích thương mại.