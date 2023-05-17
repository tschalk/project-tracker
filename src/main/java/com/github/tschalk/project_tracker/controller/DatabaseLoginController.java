package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.database.DatabaseConfig;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

/**
 * Diese Klasse stellt eine verbindung zwischen DatabaseLoginView und DatabaseManager her.
 */
public class DatabaseLoginController {
    private final DatabaseConnectionManager databaseConnectionManager;

    public DatabaseLoginController(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }

    public String getDatabaseProperty(String propertyName) {
        String databaseName = databaseConnectionManager.getDatabaseName();
        DatabaseConfig config = databaseConnectionManager.getDatabaseConfig();
        return config.getProperty(databaseName + '.' + propertyName);
    }

    public boolean createConnectionFromUI(String host, int port, String username, String password, String databaseName) {
        boolean isConnected = databaseConnectionManager.createConnection(host, port, username, password, databaseName);
        if (isConnected) {
            updateConfig(host, port, username, password, databaseName);
        }
        return isConnected;
    }

    private void updateConfig(String host, int port, String username, String password, String databaseName) {
        databaseConnectionManager.updateConfig(host, port, username, password, databaseName);
    }

    public DatabaseConnectionManager getDatabaseManager() {
        return databaseConnectionManager;
    }
}
