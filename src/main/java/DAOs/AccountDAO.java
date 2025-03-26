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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountDAO {

    private Connection conn;
    private final Map<String, String> resetTokens = new HashMap<>(); // Lưu cặp <token, email>
    private final Map<String, Long> tokenTimestamps = new HashMap<>(); // Lưu cặp <token, timestamp>
    private static final long TOKEN_EXPIRY_TIME = 30 * 60 * 1000; // 30 phút

    public AccountDAO() {
        try {
            conn = DBConnection.connect();
            if (conn == null) {
                throw new SQLException("Failed to connect to the database.");
            }
            System.out.println("Database connected successfully.");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Database connection failed.", ex);
            throw new RuntimeException("Cannot initialize AccountDAO due to database connection failure.", ex);
        }
    }

    public Account validateUser(String username, String password) {
        String hashedPassword = hashMD5(password);

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Account user = new Account();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error validating user.", e);
        }
        return null;
    }

    private String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 hashing failed.", e);
        }
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[account]";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("role")
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error retrieving accounts.", e);
        }
        return accounts;
    }

    public void updateRole(int accountId, String newRole) {
        String sql = "UPDATE account SET role = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newRole);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error updating role.", e);
        }
    }

    public List<Account> searchAccounts(String keyword) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM account WHERE username LIKE ? OR email LIKE ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setEmail(rs.getString("email"));
                account.setPhoneNumber(rs.getString("phone_number"));
                account.setRole(rs.getString("role"));
                account.setAddress(rs.getString("address"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error searching accounts.", e);
        }
        return accounts;
    }

    public Account getAccountById(int id) {
        Account account = null;
        String sql = "SELECT * FROM [dbo].[account] WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    account = new Account(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error retrieving account by ID.", e);
        }
        return account;
    }

    public Account getAccountByUsername(String username) {
        String query = "SELECT * FROM account WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error retrieving account by username.", e);
        }
        return null;
    }

    public boolean addNewAccount(Account account) {
        String query = "INSERT INTO account (username, email, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getEmail());
            ps.setString(3, hashMD5(account.getPassword()));
            ps.setString(4, account.getPhoneNumber());
            ps.setString(5, account.getAddress());
            ps.setString(6, account.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error adding new account.", e);
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String query = "SELECT * FROM account WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu email tồn tại
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error checking email existence.", e);
        }
        return false;
    }

    public boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE account SET password = ? WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, hashMD5(newPassword));
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error updating password.", e);
        }
        return false;
    }

    public String generateResetToken(String email) {
        String checkQuery = "SELECT * FROM account WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkQuery)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String token = UUID.randomUUID().toString();
                long currentTime = System.currentTimeMillis();

                // Lưu token và timestamp vào HashMap
                resetTokens.put(token, email);
                tokenTimestamps.put(token, currentTime);

                System.out.println("Reset token generated for " + email + ": " + token);
                return token;
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error generating reset token.", e);
        }
        return null;
    }

    public Account getAccountByResetToken(String token) {
        // Kiểm tra token có tồn tại trong HashMap không
        if (!resetTokens.containsKey(token)) {
            System.out.println("Token not found in HashMap: " + token);
            return null;
        }

        // Kiểm tra thời gian hết hạn của token
        Long timestamp = tokenTimestamps.get(token);
        if (timestamp == null || (System.currentTimeMillis() - timestamp) > TOKEN_EXPIRY_TIME) {
            System.out.println("Token expired: " + token);
            resetTokens.remove(token);
            tokenTimestamps.remove(token);
            return null;
        }

        // Lấy email từ HashMap và truy vấn tài khoản từ cơ sở dữ liệu
        String email = resetTokens.get(token);
        String query = "SELECT * FROM account WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Account found for token: " + token + ", email: " + email);
                return new Account(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error retrieving account by reset token.", e);
        }
        return null;
    }

    public boolean resetPassword(String token, String newPassword) {
        // Kiểm tra token có tồn tại trong HashMap không
        if (!resetTokens.containsKey(token)) {
            System.out.println("Token not found in HashMap: " + token);
            return false;
        }

        // Kiểm tra thời gian hết hạn của token
        Long timestamp = tokenTimestamps.get(token);
        if (timestamp == null || (System.currentTimeMillis() - timestamp) > TOKEN_EXPIRY_TIME) {
            System.out.println("Token expired: " + token);
            resetTokens.remove(token);
            tokenTimestamps.remove(token);
            return false;
        }

        // Lấy email từ HashMap và cập nhật mật khẩu
        String email = resetTokens.get(token);
        String query = "UPDATE account SET password = ? WHERE email = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, hashMD5(newPassword));
            ps.setString(2, email);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Xóa token khỏi HashMap sau khi reset mật khẩu thành công
                resetTokens.remove(token);
                tokenTimestamps.remove(token);
                System.out.println("Password reset successfully for email: " + email);
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error resetting password.", e);
        }
        return false;
    }
    public int countAdmins() {
        String sql = "SELECT COUNT(*) FROM account WHERE role = 'admin'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error counting admin accounts.", e);
        }
        return 0;
    }
}