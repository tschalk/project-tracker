package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ProjectDAO {

    DatabaseConnectionManager databaseConnectionManager;

    public ProjectDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }

    public void addProject(String description, String costCenter, String responsible) {
        String sql = "INSERT INTO Project (description, cost_center_id, responsible_id, user_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, description);
//            pstmt.setInt(2, getCostCenterId(costCenter));
//            pstmt.setInt(3, getResponsibleId(responsible));
//            pstmt.setInt(4, getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public DatabaseConnectionManager getDatabaseConnectionManager() {
        return databaseConnectionManager;
    }

//    private int getCostCenterId(String costCenter) {
//        ID des Cost Centers basierend auf dem Namen zu erhalten
//
//        return id;
//    }
//
//    private int getResponsibleId(String responsible) {
//        ID des Responsible basierend auf dem Namen zu erhalten
//
//        return id;
//    }





}
