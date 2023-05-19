package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.CostCenter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CostCenterDAO {

    private final Connection connection;

    public CostCenterDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.connection = databaseConnectionManager.getConnection();
    }

    public void add(CostCenter costCenter) {
        String query = "INSERT INTO costcenter (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, costCenter.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CostCenter> getAll() {
        List<CostCenter> costCenterList = new ArrayList<>();
        String query = "SELECT * FROM costcenter";

        try (PreparedStatement stmt = connection.prepareStatement(query);
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

    public CostCenter getCostCenterById(int costCenterId) {
        String query = "SELECT * FROM CostCenter WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, costCenterId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                CostCenter costCenter = new CostCenter();
                costCenter.setId(rs.getInt("id"));
                costCenter.setName(rs.getString("name"));
                return costCenter;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    public int getCostCenterId(String costCenterName) {
        String query = "SELECT id FROM CostCenter WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, costCenterName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public void remove(CostCenter costCenter) {

        // TODO: Prüfe ob CostCenter in Verwendung ist.

        String sql = "DELETE FROM costcenter WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, costCenter.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
