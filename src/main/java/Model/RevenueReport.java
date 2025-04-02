/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Model;

/**
 *
 * @author Huynh Nguyen Phu Thanh - CE180094
 */
public class RevenueReport {
private String period; // ngày/tháng/năm
    private int productId;
    private String productName;
    private double totalRevenue;
    private int totalQuantity;

    // Constructor
    public RevenueReport(String period, int productId, String productName, 
                        double totalRevenue, int totalQuantity) {
        this.period = period;
        this.productId = productId;
        this.productName = productName;
        this.totalRevenue = totalRevenue;
        this.totalQuantity = totalQuantity;
    }

    // Getters and Setters
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
}
