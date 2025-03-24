/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hnpt6504
 */



public class Cart {
    private int userId;
    private Map<Integer, CartItem> items;

    public Cart(int userId) {
        this.userId = userId;
        this.items = new HashMap<>();
    }

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (items.containsKey(product.getProductID())) {
            CartItem item = items.get(product.getProductID());
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(product.getProductID(), new CartItem(product, quantity));
        }
    }

    public void removeItem(int productId) {
        items.remove(productId);
    }

    public void updateQuantity(int productId, int quantity) {
        if (items.containsKey(productId)) {
            if (quantity > 0) {
                items.get(productId).setQuantity(quantity);
            } else {
                removeItem(productId);
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(items.values());
    }

    public double getTotalAmount() {
        double total = 0;
        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

