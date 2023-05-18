package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Responsible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResponsibleDAO {

    private final DatabaseConnectionManager databaseConnectionManager;
    private final Connection connection;

    public ResponsibleDAO(DatabaseConnectionManager databaseConnectionManager) {
        this.databaseConnectionManager = databaseConnectionManager;
        this.connection = databaseConnectionManager.getConnection();
    }

    public void add(Responsible responsible) {
        String query = "INSERT INTO responsible (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, responsible.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Responsible> getAll() {
        List<Responsible> responsibleList = new ArrayList<>();
        String query = "SELECT * FROM responsible";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Responsible responsible = new Responsible();
                responsible.setId(rs.getInt("id"));
                responsible.setName(rs.getString("name"));
                responsibleList.add(responsible);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responsibleList;
    }

    public void remove(Responsible responsible) {

        // TODO: Prüfe ob Responsible in Verwendung ist.

        String sql = "DELETE FROM responsible WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, responsible.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
