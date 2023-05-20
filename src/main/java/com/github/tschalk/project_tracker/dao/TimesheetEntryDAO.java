package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.TimesheetEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimesheetEntryDAO {

    Connection connection;

    public TimesheetEntryDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.connection = databaseConnectionManager.getConnection();
    }

    public void addTimesheetEntry(TimesheetEntry entry, int projectId) {
        String query = "INSERT INTO timesheetentry (project_id, start_time, duration) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(entry.getStartDateTime()));
            pstmt.setInt(3, entry.getDuration());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


