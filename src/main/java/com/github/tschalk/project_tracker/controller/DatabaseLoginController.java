package com.github.tschalk.project_tracker.controller;


import com.github.tschalk.project_tracker.database.DatabaseInitializer;
import com.github.tschalk.project_tracker.database.DatabaseManager;
import com.github.tschalk.project_tracker.database.Config;


public class DatabaseLoginController {
    private final String DATABASE;
    private final Config config;
    private final DatabaseManager databaseManager;

    public DatabaseLoginController(Config config, DatabaseManager databaseManager) {
        this.config = config;
        this.databaseManager = databaseManager;
        DATABASE = "database";
    }

    public String getDatabaseProperty(String property) {
        System.out.println("DatabaseLoginController.getDatabaseProperty: " + DATABASE + '.' + property);
        return config.getProperty(DATABASE + '.' + property);
    }

//    public boolean createConnection(String host, int port, String username, String password) {
//        return databaseManager.createConnection(host, port, username, password);
//    }

    public boolean createConnection(String host, int port, String username, String password, String databaseName) {
        boolean isConnected = databaseManager.createConnection(host, port, username, password, databaseName);
        if (isConnected) {
            updateConfig(host, port, username, password, databaseName);
        }
        return isConnected;
    }


    private void updateConfig(String host, int port, String username, String password, String databaseName) {
        config.setProperty(DATABASE + ".host", host);
        config.setProperty(DATABASE + ".port", String.valueOf(port));
        config.setProperty(DATABASE + ".user", username);
        config.setProperty(DATABASE + ".password", password);
        config.setProperty(DATABASE + ".databaseName", databaseName);
    }

//    public void initializeDatabase() {
//        String host = config.getProperty(DATABASE + ".host");
//        int port;
//        try {
//            port = Integer.parseInt(config.getProperty(DATABASE + ".port"));
//        } catch (NumberFormatException e) {
//            System.err.println("Invalid port number format: " + e.getMessage());
//            port = 0;
//            return;
//        }
//        String username = config.getProperty(DATABASE + ".user");
//        String password = config.getProperty(DATABASE + ".password");
//
//        DatabaseInitializer.initialize(host, port, username, password);
//    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
