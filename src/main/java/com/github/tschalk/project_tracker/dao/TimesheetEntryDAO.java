package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

public class TimesheetEntryDAO {

    DatabaseConnectionManager databaseConnectionManager;

    public TimesheetEntryDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }
}
