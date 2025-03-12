/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author tiend
 */
public class Product {
    private int productID;
    private String productName;
    private int proQuantity;
    private long proPrice;
    private byte proState;
    private String proImg;
    private String proDes;
    private Category category;

    public Product(int productID, String productName, int proQuantity, long proPrice, byte proState, String proImg, String proDes, Category category) {
        this.productID = productID;
        this.productName = productName;
        this.proQuantity = proQuantity;
        this.proPrice = proPrice;
        this.proState = proState;
        this.proImg = proImg;
        this.proDes = proDes;
        this.category = category;
    }

    

    public Product() {
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }

    public long getProPrice() {
        return proPrice;
    }

    public void setProPrice(long proPrice) {
        this.proPrice = proPrice;
    }

    public int getProState() {
        return proState;
    }

    public void setProState(byte proState) {
        this.proState = proState;
    }

    public String getProImg() {
        return proImg;
    }

    public void setProImg(String proImg) {
        this.proImg = proImg;
    }

    public String getProDes() {
        return proDes;
    }

    public void setProDes(String proDes) {
        this.proDes = proDes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
}
