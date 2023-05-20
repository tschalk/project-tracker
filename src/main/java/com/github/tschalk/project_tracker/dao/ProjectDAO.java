package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

}
