package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseManager;

public class UserDAO {

    DatabaseManager databaseManager;

    public UserDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
