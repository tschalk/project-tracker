package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProjectDAO {

    private final DatabaseConnectionManager databaseConnectionManager;
    private final Connection connection;
    private final CostCenterDAO costCenterDAO;
    private final ResponsibleDAO responsibleDAO;
    private final TimesheetEntryDAO timesheetEntryDAO;

    public ProjectDAO(DatabaseConnectionManager databaseConnectionManager, CostCenterDAO costCenterDAO, ResponsibleDAO responsibleDAO, TimesheetEntryDAO timesheetEntryDAO) {
        this.databaseConnectionManager = databaseConnectionManager;
        this.connection = databaseConnectionManager.getConnection();
        this.costCenterDAO = costCenterDAO;
        this.responsibleDAO = responsibleDAO;
        this.timesheetEntryDAO = timesheetEntryDAO;
    }

//    public Project addProject(String description, CostCenter costCenter, Responsible responsible, User user) {
//        String query = "INSERT INTO Project (description, cost_center_id, responsible_id, user_id) VALUES (?, ?, ?, ?)";
//
//        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            pstmt.setString(1, description);
//            pstmt.setInt(2, costCenter.getId());
//            pstmt.setInt(3, responsible.getId());
//            pstmt.setInt(4, user.getId());
//
//            pstmt.executeUpdate();
//
//            ResultSet rs = pstmt.getGeneratedKeys();
//            if (rs.next()) {
//                int projectId = rs.getInt(1);
//
//                Project project = new Project();
//                project.setId(projectId);
//                project.setDescription(description);
//                project.setCostCenter(costCenter.getName());
//                project.setResponsible(responsible.getName());
//
//                return project;
//            }
//        } catch (SQLException e) {
//            System.err.println("Error adding project: " + e.getMessage());
//        }
//
//        return null;
//    }


    public void addProject(String description, CostCenter costCenter, Responsible responsible, User user) {
        String query = "INSERT INTO Project (description, cost_center_id, responsible_id, user_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, description);
            pstmt.setInt(2, costCenter.getId());
            pstmt.setInt(3, responsible.getId());
            pstmt.setInt(4, user.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ObservableList<Project> readAllProjectsByUserID(int userID) {
        ObservableList<Project> projectList = FXCollections.observableArrayList();
        String query = "SELECT * FROM project WHERE user_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setId(rs.getInt("id"));
                project.setDescription(rs.getString("description"));

                int costCenterId = rs.getInt("cost_center_id");
                CostCenter costCenter = costCenterDAO.getCostCenterById(costCenterId);
                project.setCostCenter(costCenter.getName());

                int responsibleId = rs.getInt("responsible_id");
                Responsible responsible = responsibleDAO.getResponsibleById(responsibleId);
                project.setResponsible(responsible.getName());

                projectList.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
    }

    public int getProjectDuration(int projectId) {
        int duration = 0;
        String query = "SELECT SUM(duration) as total FROM TimesheetEntry WHERE project_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                duration = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return duration;
    }

    public TimesheetEntryDAO getTimesheetEntryDAO() {
        return timesheetEntryDAO;
    }

    public List<TimesheetEntry> readAllTimesheetEntriesForProject(int projectId) {
        List<TimesheetEntry> timesheetEntries = new ArrayList<>();
            String query = "SELECT * FROM timesheetentry WHERE project_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TimesheetEntry entry = new TimesheetEntry();
                entry.setId(rs.getInt("id"));
                entry.setStartDateTime(rs.getTimestamp("start_time").toLocalDateTime());
                entry.setDuration(rs.getInt("duration"));
                timesheetEntries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timesheetEntries;
    }

    public void deleteProject(int projectId) {
        // zuerst alle TimesheetEntries für dieses Projekt löschen
        timesheetEntryDAO.deleteTimesheetEntriesForProject(projectId);

        String query = "DELETE FROM Project WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return databaseConnectionManager;
    }
}
