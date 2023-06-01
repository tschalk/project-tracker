package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.database.DatabaseBackupManager;
import com.github.tschalk.project_tracker.database.DatabaseConfig;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

/**
 * Diese Klasse stellt eine verbindung zwischen DatabaseLoginView und DatabaseManager her.
 */
public class DatabaseLoginController {
    private final DatabaseConnectionManager databaseConnectionManager;
//    private final DatabaseBackupManager databaseBackupManager;

    public DatabaseLoginController(DatabaseConnectionManager databaseConnectionManager/*, DatabaseBackupManager databaseBackupManager*/) {
        this.databaseConnectionManager = databaseConnectionManager;
//        this.databaseBackupManager = databaseBackupManager;
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
            System.out.println("performing database backup");
//            databaseBackupManager.performDatabaseBackup();
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
