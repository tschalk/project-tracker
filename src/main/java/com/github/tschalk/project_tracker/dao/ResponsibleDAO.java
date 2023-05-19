package com.github.tschalk.project_tracker.dao;

import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.Responsible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResponsibleDAO {

    private final Connection connection;

    public ResponsibleDAO(DatabaseConnectionManager databaseConnectionManager) {
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

    public Responsible getResponsibleById(int responsibleId) {
        String query = "SELECT * FROM Responsible WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, responsibleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Responsible responsible = new Responsible();
                responsible.setId(rs.getInt("id"));
                responsible.setName(rs.getString("name"));
                return responsible;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    public int getResponsibleId(String responsibleName) {
        String query = "SELECT id FROM Responsible WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, responsibleName);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
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
