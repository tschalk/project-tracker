package com.github.tschalk.project_tracker.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;
    private String host;
    private String port;
    private String username;
    private String password;

    public boolean connect() {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/ProjectTracker";
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createConnection(String host, int port, String username, String password) {
        this.host = host;
        this.port = String.valueOf(port);
        this.username = username;
        this.password = password;
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/ProjectTracker";
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "DatabaseManager{" +
                "connection=" + connection +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

