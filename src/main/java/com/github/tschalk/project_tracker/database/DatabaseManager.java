package com.github.tschalk.project_tracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Diese Klasse ist verantwortlich für die direkte Interaktion mit der Datenbank.
 * Sie stellt die Verbindung zur Datenbank her.
 */

public class DatabaseManager {
    private String host;
    private String port;
    private String username;
    private String password;
    private String databaseName;
    private final DatabaseConfig config;
    private Connection connection;

    public DatabaseManager(DatabaseConfig config) {
        this.config = config;
        this.host = config.getProperty("database.host");
        this.port = config.getProperty("database.port");
        this.username = config.getProperty("database.user");
        this.password = config.getProperty("database.password");
        this.databaseName = config.getProperty("database.databaseName");
    }

    public boolean connect() {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (SQLException e) {
            System.err.println("Error while connecting to the database." + e.getMessage());
            return false;
        }
    }

    public boolean createConnection(String host, int port, String username, String password, String databaseName) {
        this.host = host;
        this.port = String.valueOf(port);
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;

        try {
            String url = "jdbc:mysql://" + host + ":" + port;
            connection = DriverManager.getConnection(url, username, password);
            DatabaseInitializer.initialize(host, port, username, password);
            connection.setCatalog(databaseName);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateConfig(String host, int port, String username, String password, String databaseName) {
        config.setProperty("database.host", host);
        config.setProperty("database.port", String.valueOf(port));
        config.setProperty("database.user", username);
        config.setProperty("database.password", password);
        config.setProperty("database.databaseName", databaseName);
    }


    public DatabaseConfig getDatabaseConfig() {
        return config;
    }

    public String getDatabaseName() {
        return databaseName;
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

