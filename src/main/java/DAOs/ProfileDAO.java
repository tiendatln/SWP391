/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Model.Account;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Huynh Nguyen Phu Thanh - CE180094
 */
public class ProfileDAO {

   public static Account getAccountById(int id) { // Thêm từ khóa static
        Account account = null;
        String sql = "SELECT * FROM account WHERE id = ?";
        
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setEmail(rs.getString("email"));
                // Không trả về password để bảo mật
                // account.setPassword(rs.getString("password"));
                account.setPhoneNumber(rs.getString("phone_number"));
                account.setAddress(rs.getString("address"));
                account.setRole(rs.getString("role"));
            }
            
            rs.close();
            ps.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
        return account;
    }
   
   public boolean updateAccount(Account account) {
        String sql = "UPDATE account SET username = ?, email = ?, phone_number = ?, address = ? WHERE id = ?";
        
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhoneNumber());
            ps.setString(4, account.getAddress());
            ps.setInt(5, account.getId());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            conn.close();
            
            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   
   public boolean checkOldPassword(int userId, String oldPassword) {
        String sql = "SELECT password FROM account WHERE id = ?";
        
        try {
            Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // Mã hóa mật khẩu cũ nhập vào bằng MD5
                String encryptedOldPassword = hashMD5(oldPassword);
                boolean isMatch = storedPassword.equals(encryptedOldPassword);
                System.out.println("Stored password (MD5): " + storedPassword);
                System.out.println("Provided old password (MD5): " + encryptedOldPassword);
                System.out.println("Password match: " + isMatch);
                rs.close();
                ps.close();
                conn.close();
                return isMatch;
            } else {
                System.out.println("No user found with ID: " + userId);
                rs.close();
                ps.close();
                conn.close();
                return false;
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in checkOldPassword: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE account SET password = ? WHERE id = ?";
        
        try {
            Connection conn = DBConnection.connect();
            // Mã hóa mật khẩu mới bằng MD5 trước khi lưu
            String encryptedNewPassword =hashMD5(newPassword);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, encryptedNewPassword);
            ps.setInt(2, userId);
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected in updatePassword: " + rowsAffected);
            System.out.println("New password (MD5): " + encryptedNewPassword);
            ps.close();
            conn.close();
            
            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in updatePassword: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static String hashMD5(String input) {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
