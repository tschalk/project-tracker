package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.TimesheetEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimesheetEntryDAO {
    private final Connection connection;

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

    public Map<LocalDate, Integer> getDailyDurationSumForProject(int projectId, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Integer> dailySums = new LinkedHashMap<>();

        String query = "SELECT DATE(start_time) AS date, SUM(duration) AS sum FROM TimesheetEntry " +
                "WHERE project_id = ? AND DATE(start_time) BETWEEN ? AND ? " +
                "GROUP BY DATE(start_time)" +
                "ORDER BY DATE(start_time)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                int durationSum = rs.getInt("sum");

                dailySums.put(date, durationSum);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dailySums;
    }

    public void updateTimesheetEntry(TimesheetEntry entry) {
        String query = "UPDATE TimesheetEntry SET duration = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, entry.getDuration());
            pstmt.setInt(2, entry.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeEntry(TimesheetEntry entry) {
        String query = "DELETE FROM TimesheetEntry WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, entry.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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


