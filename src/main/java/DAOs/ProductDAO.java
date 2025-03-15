/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Product;
import Model.Category;
import DAOs.CategoryDAO;
import Model.ProductDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyễn Trường Vinh _ vinhntca181278
 */
public class ProductDAO {

    Connection conn;

    public ProductDAO() {
        try {
            conn = DBConnection.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Add product
    public boolean addProduct(String name, int quantity, double price, boolean state, String img, String description, int categoryID) {
        String sql = "INSERT INTO product (productName, proQuantity, proPrice, proState, proImg, proDes, categoryID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, state);
            stmt.setString(5, img);
            stmt.setString(6, description);
            stmt.setInt(7, categoryID);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try ( ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int productID = generatedKeys.getInt(1);

                        // Gọi phương thức addProductDetail ngay sau khi thêm product
                        return addProductDetail(productID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update product
    public boolean updateProduct(int id, String name, int quantity, double price, boolean state, String img, String description, int categoryID) {
        String sql = "UPDATE product SET productName=?, proQuantity=?, proPrice=?, proState=?, proImg=?, proDes=?, categoryID=? WHERE productID=?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setBoolean(4, state);
            stmt.setString(5, img);
            stmt.setString(6, description);
            stmt.setInt(7, categoryID);
            stmt.setInt(8, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete product
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE productID=?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Get all products

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        CategoryDAO categoryDAO = new CategoryDAO();

        String sql = "SELECT * FROM product";
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category category = categoryDAO.getNormalCategoryByID(rs.getInt("categoryID"));
                ProductDetail productDetail = getProductDetailById(rs.getInt("productID"));
                Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getInt("proQuantity"),
                        rs.getLong("proPrice"),
                        rs.getByte("proState"),
                        rs.getString("proImg"),
                        rs.getString("proDes"),
                        category,
                        productDetail
                );

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getAllActiveProducts() {
        List<Product> products = new ArrayList<>();
        CategoryDAO categoryDAO = new CategoryDAO();

        String sql = "SELECT * FROM product where proState=1";
        try ( Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                
                Category category = categoryDAO.getNormalCategoryByID(rs.getInt("categoryID"));
                ProductDetail productDetail = getProductDetailById(rs.getInt("productID"));
                Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("productName"),
                        rs.getInt("proQuantity"),
                        rs.getLong("proPrice"),
                        rs.getByte("proState"),
                        rs.getString("proImg"),
                        rs.getString("proDes"),
                        category,
                        productDetail
                );

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product getProductById(int id) {
        Product product = null;
        String sql = "SELECT * FROM product WHERE productID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category category = new CategoryDAO().getNormalCategoryByID(rs.getInt("categoryID"));

                    product = new Product(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getInt("proQuantity"),
                            rs.getLong("proPrice"),
                            rs.getByte("proState"),
                            rs.getString("proImg"),
                            rs.getString("proDes"),
                            category
                    );

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    //add product detail
    public boolean addProductDetail(int productID) {
        String sql = "INSERT INTO productDetail (productID) VALUES (?)";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ProductDetail getProductDetailById(int productID) {
        String sql = "SELECT * FROM productDetail WHERE productID = ?";
        ProductDetail productDetail = null;
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productID);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProductDAO productDAO = new ProductDAO();
                    Product product = productDAO.getProductById(productID);
                    productDetail
                            = new ProductDetail(
                                    rs.getInt("productDetailID"),
                                    product,
                                    rs.getString("operatingSystem"),
                                    rs.getString("cpuTechnology"),
                                    rs.getInt("coreCount"),
                                    rs.getInt("threadCount"),
                                    rs.getString("cpuSpeed"),
                                    rs.getString("gpu"),
                                    rs.getInt("ram"),
                                    rs.getString("ramType"),
                                    rs.getString("ramBusSpeed"),
                                    rs.getInt("maxRam"),
                                    rs.getString("storage"),
                                    rs.getString("memoryCard"),
                                    rs.getString("screen"),
                                    rs.getString("resolution"),
                                    rs.getString("refreshRate"),
                                    rs.getString("batteryCapacity"),
                                    rs.getString("batteryType"),
                                    rs.getString("maxChargingSupport"),
                                    rs.getString("releaseDate"),
                                    rs.getString("origin")
                            );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetail;
    }

    public boolean updateProductDetail(ProductDetail productDetail) {
        String sql = "UPDATE productDetail SET operatingSystem=?, cpuTechnology=?, coreCount=?, threadCount=?, cpuSpeed=?, gpu=?, "
                + "ram=?, ramType=?, ramBusSpeed=?, maxRam=?, storage=?, memoryCard=?, screen=?, resolution=?, refreshRate=?, "
                + "batteryCapacity=?, batteryType=?, maxChargingSupport=?, releaseDate=?, origin=? WHERE productID=?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productDetail.getOperatingSystem());
            ps.setString(2, productDetail.getCpuTechnology());
            ps.setObject(3, productDetail.getCoreCount(), java.sql.Types.INTEGER);
            ps.setObject(4, productDetail.getThreadCount(), java.sql.Types.INTEGER);
            ps.setString(5, productDetail.getCpuSpeed());
            ps.setString(6, productDetail.getGpu());
            ps.setObject(7, productDetail.getRam(), java.sql.Types.INTEGER);
            ps.setString(8, productDetail.getRamType());
            ps.setString(9, productDetail.getRamBusSpeed());
            ps.setObject(10, productDetail.getMaxRam(), java.sql.Types.INTEGER);
            ps.setString(11, productDetail.getStorage());
            ps.setString(12, productDetail.getMemoryCard());
            ps.setString(13, productDetail.getScreen());
            ps.setString(14, productDetail.getResolution());
            ps.setString(15, productDetail.getRefreshRate());
            ps.setString(16, productDetail.getBatteryCapacity());
            ps.setString(17, productDetail.getBatteryType());
            ps.setString(18, productDetail.getMaxChargingSupport());
            ps.setString(19, productDetail.getReleaseDate());  // **Lưu dưới dạng String**
            ps.setString(20, productDetail.getOrigin());
            ps.setInt(21, productDetail.getProductID().getProductID()); // **Lấy ID từ đối tượng Product**

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProductDetail> getAllProductDetails() {
        List<ProductDetail> productDetails = new ArrayList<>();
        String sql = "SELECT * FROM productDetail";

        try ( PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            ProductDAO productDAO = new ProductDAO();

            while (rs.next()) {
                int productID = rs.getInt("productID");
                Product product = productDAO.getProductById(productID);

                ProductDetail productDetail = new ProductDetail(
                        rs.getInt("productDetailID"),
                        product,
                        rs.getString("operatingSystem"),
                        rs.getString("cpuTechnology"),
                        rs.getInt("coreCount"),
                        rs.getInt("threadCount"),
                        rs.getString("cpuSpeed"),
                        rs.getString("gpu"),
                        rs.getInt("ram"),
                        rs.getString("ramType"),
                        rs.getString("ramBusSpeed"),
                        rs.getInt("maxRam"),
                        rs.getString("storage"),
                        rs.getString("memoryCard"),
                        rs.getString("screen"),
                        rs.getString("resolution"),
                        rs.getString("refreshRate"),
                        rs.getString("batteryCapacity"),
                        rs.getString("batteryType"),
                        rs.getString("maxChargingSupport"),
                        rs.getString("releaseDate"),
                        rs.getString("origin")
                );

                productDetails.add(productDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails;
    }

    public boolean deleteProductDetail(int productID) {
        String sql = "DELETE FROM productDetail WHERE productID = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getProductImgById(int productID) {
        String productImg = null;
        String sql = "SELECT proImg FROM Product WHERE productID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    productImg = rs.getString("proImg");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productImg;
    }

}
