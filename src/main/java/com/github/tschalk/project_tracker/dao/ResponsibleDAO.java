package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

public class ResponsibleDAO {

    DatabaseConnectionManager databaseConnectionManager;

    public ResponsibleDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }
}
