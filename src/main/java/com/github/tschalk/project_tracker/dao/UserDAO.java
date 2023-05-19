package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    DatabaseConnectionManager databaseConnectionManager;
    Connection connection;

    public UserDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
        this.connection = databaseConnectionManager.getConnection();
    }

    public User getUser(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
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

    public int getUserId(String username) {
        String query = "SELECT id FROM User WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

//    public boolean checkPassword(User user, String password) {
//        String query = "SELECT password FROM User WHERE id = ?";
//
//        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
//
//            pstmt.setInt(1, user.getId());
//
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                String storedPasswordHash = rs.getString("password");
//                return checkPasswordHash(password, storedPasswordHash); // Sie müssen diese Methode implementieren, um den gehashten Passwort-String zu überprüfen
//            } else {
//                return false;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }



    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DatabaseConnectionManager getDatabaseManager() {
        return databaseConnectionManager;
    }
}
