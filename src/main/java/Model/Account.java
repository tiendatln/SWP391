/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author tiend
 */
public class Account {
    private int id;
    private String username;
    private String email;
    private String Password;
    private String phoneNumber;
    private String address;
    private String role;

    public Account(int id, String username, String email, String Password, String phoneNumber, String address, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.Password = Password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public Account(String username, String email, String Password, String phoneNumber, String address, String role) {
        this.username = username;
        this.email = email;
        this.Password = Password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public Account() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
