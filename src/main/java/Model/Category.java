/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author tiend
 */
public class Category {
   private int categoryID;
   private String type;
   private MainCategory mainCategory;

    public Category(int categoryID, String type, MainCategory mainCategory) {
        this.categoryID = categoryID;
        this.type = type;
        this.mainCategory = mainCategory;
    }

    public Category() {
    }
    public Category(int aInt, String string) {
        }

    public Category(int categoryID) {
        this.categoryID = categoryID;
    }

 
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }
   
}
