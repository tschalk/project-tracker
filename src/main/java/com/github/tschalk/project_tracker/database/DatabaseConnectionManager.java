package com.github.tschalk.project_tracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Diese Klasse ist verantwortlich für die direkte Interaktion mit der Datenbank.
 * Sie stellt die Verbindung zur Datenbank her.
 */

public class DatabaseConnectionManager {
    private String host;
    private String port;
    private String username;
    private String password;
    private String databaseName;
    private final DatabaseConfig config;
    private Connection connection;
    private final DatabaseBackupManager databaseBackupManager;

    public DatabaseConnectionManager(DatabaseConfig config, DatabaseBackupManager databaseBackupManager) {
        this.config = config;
        this.host = config.getProperty("database.host");
        this.port = config.getProperty("database.port");
        this.username = config.getProperty("database.user");
        this.password = config.getProperty("database.password");
        this.databaseName = config.getProperty("database.databaseName");
        this.databaseBackupManager = databaseBackupManager;
    }

    public boolean isConnected() {
        try {
            databaseBackupManager.performDatabaseBackup();
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean connect() {

        if (host == null || port == null || username == null || password == null || databaseName == null) {
            System.out.println("properties are null");
            return false;
        }
        if (host.isEmpty() || port.isEmpty() || username.isEmpty() || password.isEmpty() || databaseName.isEmpty()) {
            System.out.println("properties are empty");
            return false;
        }

        System.out.println("Try connecting to database...");
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

            databaseBackupManager.performDatabaseBackup();
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

    public Connection getConnection() {
        return connection;
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

