package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseManager;

public class TimesheetEntryDAO {

    DatabaseManager databaseManager;

    public TimesheetEntryDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
