package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.database.DatabaseConfig;
import com.github.tschalk.project_tracker.database.DatabaseManager;

/**
 * Diese Klasse stellt eine verbindung zwischen DatabaseLoginView und DatabaseManager her.
 */
public class DatabaseLoginController {
    private final DatabaseManager databaseManager;

    public DatabaseLoginController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public String getDatabaseProperty(String propertyName) {
        String databaseName = databaseManager.getDatabaseName();
        DatabaseConfig config = databaseManager.getDatabaseConfig();
        return config.getProperty(databaseName + '.' + propertyName);
    }

    public boolean createConnectionFromUI(String host, int port, String username, String password, String databaseName) {
        boolean isConnected = databaseManager.createConnection(host, port, username, password, databaseName);
        if (isConnected) {
            updateConfig(host, port, username, password, databaseName);
        }
        return isConnected;
    }

    private void updateConfig(String host, int port, String username, String password, String databaseName) {
        databaseManager.updateConfig(host, port, username, password, databaseName);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
