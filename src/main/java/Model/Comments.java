/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Kim Chi Khang _ CE180324
 */
public class Comments {
  private int commentID;
    private int rate;
    private String comment;  
    private int productID;
    private int ID;

    // Constructor
    public Comments(int commentID, int rate, String comment, int productID, int ID) {
        this.commentID = commentID;
        this.rate = rate;
        this.comment= comment;
        this.productID = productID;
        this.ID = ID;
    }

    // Getters and Setters
    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCommentText() {
        return comment;
    }

    public void setCommentText(String commentText) {
        this.comment = comment;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getUserID() {
        return ID;
    }

    public void setUserID(int userID) {
        this.ID = ID;
    }
}