/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author tiend
 */
public class OrderProduct {
    private Product product;
    private OrderTotal orderTotal;
    private String productNames;
    private int quantity;
    private long orderPrice; // ✅ Sửa lỗi chính tả

    public OrderProduct(OrderTotal orderTotal, String productNames, int totalQuantity, long totalOrderPrice) {
        this.orderTotal = orderTotal;
        this.productNames = productNames;
        this.quantity = totalQuantity;
        this.orderPrice = totalOrderPrice;
    }
    
    public OrderProduct(Product product, OrderTotal orderTotal, int quantity, long orderPrice) { // ✅ Sửa constructor
        this.product = product;
        this.orderTotal = orderTotal;
        this.quantity = quantity;
        this.orderPrice = orderPrice; // ✅ Sửa biến
    }

    public OrderProduct() {
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderTotal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(OrderTotal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getOrderPrice() { // ✅ Sửa getter
        return orderPrice;
    }

    public void setOrderPrice(long orderPrice) { // ✅ Sửa setter
        this.orderPrice = orderPrice;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }
}
