/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author tiend
 */
public class OrderTotal {
    private int orderID;
    private String phoneNumber;
    private String address;
    private String note;
    private long totalPrice;
    private Date date;
    private int orderState;
    private int voucherID;
    private Account account;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getVoucherCode() {
        return voucherID;
    }

    public void setVoucherCode(int voucherID) {
        this.voucherID = voucherID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public OrderTotal() {
    }

    public OrderTotal(int orderID, String phoneNumber, String address, String note, long totalPrice, Date date, int orderState, int voucherID, Account account) {
        this.orderID = orderID;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.note = note;
        this.totalPrice = totalPrice;
        this.date = date;
        this.orderState = orderState;
        this.voucherID = voucherID;
        this.account = account;
    }

    public OrderTotal(int orderID, String phoneNumber, String address, String note, long totalPrice, Date date, int orderState, int voucherID) {
        this.orderID = orderID;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.note = note;
        this.totalPrice = totalPrice;
        this.date = date;
        this.orderState = orderState;
        this.voucherID = voucherID;
    }

    public OrderTotal(String phoneNumber, String address, String note, long totalPrice, Date date, int orderState, int voucherID, Account account) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.note = note;
        this.totalPrice = totalPrice;
        this.date = date;
        this.orderState = orderState;
        this.voucherID = voucherID;
        this.account = account;
    }


   
}
