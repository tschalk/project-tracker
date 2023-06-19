package com.github.tschalk.project_tracker.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Diese Klasse liest die SQL-Befehle aus der Datei database.sql und führt sie aus.
 * Die Datei muss im Verzeichnis src/main/resources/config liegen.
 */

public class DatabaseInitializer {
    public static void initialize(String host, int port, String username, String password) {
        try {
            Path sqlScriptPath = getConfigFilePath("database.sql");
            String sqlCommands = new String(Files.readAllBytes(sqlScriptPath));
            String url = "jdbc:mysql://" + host + ":" + port + "/";

            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String[] individualCommands = sqlCommands.split(";");
                for (String command : individualCommands) {
                    if (!command.trim().isEmpty()) {
                        executeSQLCommand(connection, command);
                    }
                }
            }
        } catch (IOException e) {
            handleIOException("database.sql");
        } catch (SQLException e) {
            handleSQLException(e.getMessage());
        }
    }

    private static Path getConfigFilePath(String fileName) throws IOException {
        String dir = System.getProperty("user.dir");
        return Paths.get(dir, fileName);
    }


    private static void executeSQLCommand(Connection connection, String command) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(command);
        }
    }

    private static void handleIOException(String fileName) {
        System.out.println("Error while reading the SQL script: " + fileName);
    }

    private static void handleSQLException(String errorMessage) {
        System.out.println("Error while executing the SQL script: " + errorMessage);
    }
}
