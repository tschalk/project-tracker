package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

import java.sql.Connection;

public class TimesheetEntryDAO {
    Connection connection;

    public TimesheetEntryDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.connection = databaseConnectionManager.getConnection();
    }
}
