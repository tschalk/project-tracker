package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.CostCenter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostCenterDAO {

    DatabaseConnectionManager databaseConnectionManager;

    public CostCenterDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
    }

    public void add(CostCenter costCenter) {
        String query = "INSERT INTO CostCenter (name) VALUES (?)";

        try (PreparedStatement stmt = databaseConnectionManager.getConnection().prepareStatement(query)) {
            System.out.println("Adding cost center: " + costCenter.getName());
            stmt.setString(1, costCenter.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CostCenter> getAll() {
        System.out.println("databaseConnectionManager.isConnected() = " + databaseConnectionManager.isConnected());
        List<CostCenter> costCenterList = new ArrayList<>();
        String query = "SELECT * FROM CostCenter";

        try (PreparedStatement stmt = databaseConnectionManager.getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CostCenter costCenter = new CostCenter();
                costCenter.setId(rs.getInt("id"));
                costCenter.setName(rs.getString("name"));
                costCenterList.add(costCenter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return costCenterList;
    }

}
