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

    public void removeEntry(TimesheetEntry entry) {
        String query = "DELETE FROM timesheetentry WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, entry.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Appropriate error handling here
        }
    }

    public void deleteTimesheetEntriesForProject(int projectId) {
        String query = "DELETE FROM TimesheetEntry WHERE project_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}


