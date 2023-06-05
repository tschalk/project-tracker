package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.util.SimplePasswordEncryption;
import com.github.tschalk.project_tracker.model.User;
import com.github.tschalk.project_tracker.util.Sanitizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final DatabaseConnectionManager databaseConnectionManager;
    private final Sanitizer sanitizer;
    private Connection connection;

    public UserDAO(DatabaseConnectionManager databaseConnectionManager, Sanitizer sanitizer) {
        this.databaseConnectionManager = databaseConnectionManager;
        this.connection = databaseConnectionManager.getConnection();
        this.sanitizer = sanitizer;

    }

    public boolean addUser(String username, String role, String password) {
        String query = "INSERT INTO User (username, password, role) VALUES (?, ?, ?)";

        String[] parameters = {username, role, password};
        String[] sanitizedParameters = {
                sanitizer.sanitize(username),
                sanitizer.sanitize(role),
        };

        for (int i = 0; i < parameters.length; i++) {
            // Nur das Passwort überspringen
            if (i != 2 && parameters[i].length() != sanitizedParameters[i].length()) {
                return false;
            }
        }

        String encryptedPassword = SimplePasswordEncryption.encrypt(password); // Passwort verschlüsseln

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, encryptedPassword);
            pstmt.setString(3, role);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUser(String username) {
        String query = "SELECT * FROM user WHERE username = ?";

        username = sanitizer.sanitize(username);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    String encryptedPassword = resultSet.getString("password");
                    String decryptedPassword = SimplePasswordEncryption.decrypt(encryptedPassword);

                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            decryptedPassword,  // entschlüsseltes Passwort verwenden
                            resultSet.getString("role"),
                            resultSet.getBoolean("is_active")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    String encryptedPassword = resultSet.getString("password");
                    String decryptedPassword = SimplePasswordEncryption.decrypt(encryptedPassword);

                    users.add(new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            decryptedPassword,
                            resultSet.getString("role"),
                            resultSet.getBoolean("is_active")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM User WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE User SET password = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            String encryptedPassword = SimplePasswordEncryption.encrypt(newPassword);
            pstmt.setString(1, encryptedPassword);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DatabaseConnectionManager getDatabaseManager() {
        return databaseConnectionManager;
    }

    public boolean updateUser(String username, String role, boolean isActive) {
        String query = "UPDATE User SET role = ?, is_active = ? WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, role);
            pstmt.setBoolean(2, isActive);
            pstmt.setString(3, username);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
