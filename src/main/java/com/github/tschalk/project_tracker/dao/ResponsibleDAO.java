package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseManager;

public class ResponsibleDAO {

    DatabaseManager databaseManager;

    public ResponsibleDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
