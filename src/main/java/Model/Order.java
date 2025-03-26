package Model;

import java.util.List;

public class Order {
    private Product product;
    private OrderTotal orderTotal;
   
    private int quantity;
    private long orderPrice; // ✅ Sửa lỗi chính tả


    
    public Order(Product product, OrderTotal orderTotal, int quantity, long orderPrice) { // ✅ Sửa constructor
        this.product = product;
        this.orderTotal = orderTotal;
        this.quantity = quantity;
        this.orderPrice = orderPrice; // ✅ Sửa biến
    }

    public Order() {
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

}
