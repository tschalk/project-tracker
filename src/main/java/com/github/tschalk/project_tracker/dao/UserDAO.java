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

    public User readUserByUsername(String username) {
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

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DatabaseConnectionManager getDatabaseManager() {
        return databaseConnectionManager;
    }
}
