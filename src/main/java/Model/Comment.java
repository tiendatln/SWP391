package Model;

public class Comment {

    private int commentID;
    private int rate;
    private String comment;
    private int productID;
    private int id;

    // Constructors, Getters, Setters
    public Comment(int commentID, int rate, String comment, int productID, int id) {
        this.commentID = commentID;
        this.rate = rate;
        this.comment = comment;
        this.productID = productID;
        this.id = id;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Comment{" + "commentID=" + commentID + ", rate=" + rate + ", comment=" + comment + ", productID=" + productID + ", id=" + id + '}';
    }

}
