/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author tiend
 */
public class CartForOrder {
    private Account account;
    private Product product;
    private int quantity;

    public CartForOrder() {
    }

    public CartForOrder(Product product) {
        this.product = product;
    }

    public CartForOrder(Account account, Product product, int quantity) {
        this.account = account;
        this.product = product;
        this.quantity = quantity;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}
