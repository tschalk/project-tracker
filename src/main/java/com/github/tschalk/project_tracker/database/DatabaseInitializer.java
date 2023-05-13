package com.github.tschalk.project_tracker.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        String sqlScriptPath = "src/main/resources/config/database.sql";
        try {
            String sqlCommands = Files.readString(Paths.get(sqlScriptPath));

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "root")) {
                String[] individualCommands = sqlCommands.split(";");
                for (String command : individualCommands) {
                    if (!command.trim().isEmpty()) {
                        executeSQLCommand(connection, command);
                    }
                }
            }
        } catch (IOException e) {
            handleIOException(sqlScriptPath);
        } catch (SQLException e) {
            handleSQLException(e.getMessage());
        }
    }

    private static void executeSQLCommand(Connection connection, String command) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(command);
        }
    }

    private static void handleIOException(String filePath) {
        System.out.println("Error while reading the SQL script: " + filePath);
    }

    private static void handleSQLException(String errorMessage) {
        System.out.println("Error while executing the SQL script: " + errorMessage);
    }

}
