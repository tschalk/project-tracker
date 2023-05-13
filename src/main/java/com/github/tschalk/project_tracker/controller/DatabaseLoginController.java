package com.github.tschalk.project_tracker.controller;


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

    public boolean createConnection(String host, int port, String username, String password) {
        return databaseManager.createConnection(host, port, username, password);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
