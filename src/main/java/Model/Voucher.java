/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;

/**
 *
 * @author Kim Chi Khang _ CE180324
 */
public class Voucher {
    private int voucherID;
    private String voucherCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private int percentDiscount;
    private int quantity;
    private int usedTime;
 

    public Voucher() {
    }

    public Voucher(int voucherID, String voucherCode, LocalDate startDate, LocalDate endDate, int percentDiscount, int quantity, int usedTime) {
        this.voucherID = voucherID;
        this.voucherCode = voucherCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentDiscount = percentDiscount;
        this.quantity = quantity;
        this.usedTime = usedTime;
 
    }

    public Voucher(String voucherCode, LocalDate startDate, LocalDate endDate, int percentDiscount, int quantity, int usedTime) {
        this.voucherCode = voucherCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentDiscount = percentDiscount;
        this.quantity = quantity;
        this.usedTime = usedTime;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }
}