package dao;

import models.User;
import utils.DatabaseConnector;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public int addUser(User user) throws SQLException {
        String sql = "INSERT INTO User (username, email, UserPassword, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.setString(4, user.getRole() != null ? user.getRole() : "user"); // Default role if not set

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                user.setUserId(userId); // Optional
                return userId;
            }
        }

        return -1;
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM User WHERE userId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("UserPassword"));
            }
        }

        return null;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User authenticateUser(String email, String password) {
        return null;
    }

    public User login(String email, String plainPassword) throws SQLException {
        // ✅ First, hash the plain password
        String hashedPassword = hashPassword(plainPassword);

        String sql = "SELECT * FROM User WHERE email = ? AND UserPassword = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // ✅ Then set the parameters
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("UserPassword"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }
        return null; // login failed
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection conn = DatabaseConnector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("UserPassword"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM User WHERE userId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

}
