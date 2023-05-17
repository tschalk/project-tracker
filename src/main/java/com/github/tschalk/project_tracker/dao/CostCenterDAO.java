package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

public class CostCenterDAO {

    DatabaseConnectionManager databaseConnectionManager;

    public CostCenterDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }



}
