package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseManager;

public class ProjectDAO {

    DatabaseManager databaseManager;

    public ProjectDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
