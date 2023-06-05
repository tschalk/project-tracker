package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProjectDAO {
    private final Connection connection;
    private final CostCenterDAO costCenterDAO;
    private final ResponsibleDAO responsibleDAO;
    private final TimesheetEntryDAO timesheetEntryDAO;

    public ProjectDAO(DatabaseConnectionManager databaseConnectionManager, CostCenterDAO costCenterDAO, ResponsibleDAO responsibleDAO, TimesheetEntryDAO timesheetEntryDAO) {
        this.connection = databaseConnectionManager.getConnection();
        this.costCenterDAO = costCenterDAO;
        this.responsibleDAO = responsibleDAO;
        this.timesheetEntryDAO = timesheetEntryDAO;
    }

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

    public List<Project> readAllProjectsByUserID(int userID) {
    List<Project> projectList = new ArrayList<>();
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

    public void updateProject(Project project) {
        String query = "UPDATE project SET description = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, project.getDescription());
            pstmt.setInt(2, project.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProjectCostCenter(Project project, CostCenter costCenter) {
        String query = "UPDATE project SET cost_center_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, costCenter.getId());
            pstmt.setInt(2, project.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateProjectResponsible(Project project, Responsible responsible) {
        String query = "UPDATE project SET responsible_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, responsible.getId());
            pstmt.setInt(2, project.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    public CostCenterDAO getCostCenterDAO() {
        return costCenterDAO;
    }

    public ResponsibleDAO getResponsibleDAO() {
        return responsibleDAO;
    }

    public TimesheetEntryDAO getTimesheetEntryDAO() {
        return timesheetEntryDAO;
    }
}
