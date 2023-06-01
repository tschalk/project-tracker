package com.github.tschalk.project_tracker.database;//package com.github.tschalk.project_tracker.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Diese Klasse ist für das Erstellen und Wiederherstellen von Datenbank-Backups zuständig.
 * Die Klasse verwendet die Klasse DatabaseConfig, um die Datenbank-Verbindungsdaten aus der Datei database.properties herzustellen.
 * Die Klasse verwendet die MySQL-Tools mysqldump.exe.
 * Dieses Tool müssen auf dem System installiert sein und im PATH vorhanden sein.
 * Die mysqldump-Utility, führt standardmäßig eine Sperre auf den Tabellen aus,
 * sodass keine Schreiboperationen durchgeführt werden können, während das Backup läuft.
 */

public class DatabaseBackupManager {

    private final DatabaseConfig config;
    private final Path backupFilePath;

    public DatabaseBackupManager(DatabaseConfig config) {
        this.config = config;
        this.backupFilePath = generateBackupFilePathForDate(LocalDate.now());
    }

    public void performDatabaseBackup() {
        // Hier wird sichergestellt, dass das Backup nur einmal pro Tag ausgeführt wird, um unnötige Backups zu vermeiden.
        // Und Konflikte zu vermeiden, wenn mehrere Instanzen der Anwendung gleichzeitig laufen.
        if (!Files.exists(backupFilePath)) {
            backupDatabase();
            deleteOlderBackups(2);
        }
    }

    private void backupDatabase() {
        String executeCmd = getBackupCommand();
        executeBackupCommand(executeCmd, "Backup created successfully!", "Could not create the backup.");
    }

    private String getBackupCommand() {
        String host = config.getProperty("database.host");
        int port = Integer.parseInt(config.getProperty("database.port"));
        String username = config.getProperty("database.user");
        String password = config.getProperty("database.password");
        String databaseName = config.getProperty("database.databaseName");

        return String.format(
                // -h% ... Host -P% ... Port -u% ... Username -p% ... Password -B% ... Database -r% ... Output file
                "mysqldump -h%s -P%d -u%s -p%s --add-drop-database -B %s -r %s",
                host, port, username, password, databaseName, backupFilePath);
    }

    private void executeBackupCommand(String executeCmd, String successMessage, String errorMessage) {
        try {
            Process process = new ProcessBuilder(executeCmd.split(" ")).start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println(successMessage);
            } else {
                System.err.println(errorMessage);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error while executing backup command: " + e.getMessage());
        }
    }

    private void deleteOlderBackups(int weeks) {
        // Aktuelles Datum
        LocalDate currentDate = LocalDate.now();

        // Backups löschen, die älter als die angegebene Anzahl von Wochen sind
        LocalDate thresholdDate = currentDate.minusWeeks(weeks);

        try {
            if (Files.exists(backupFilePath)) {
                // Überprüfen, ob das Backup älter ist als die angegebene Anzahl von Wochen
                LocalDateTime lastModified = Files.getLastModifiedTime(backupFilePath).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (lastModified.toLocalDate().isBefore(thresholdDate)) {
                    Files.delete(backupFilePath);
                    System.out.println("Backup deleted: " + backupFilePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while deleting backup: " + e.getMessage());
        }
    }

    private void restoreDatabase(LocalDate date) {
        Path backupFileForGivenDate = generateBackupFilePathForDate(date);
        if (Files.exists(backupFileForGivenDate)) {
            String executeCmd = getRestoreCommand(backupFileForGivenDate);

            executeBackupCommand(executeCmd, "Backup restored successfully!", "Could not restore the backup.");
        } else {
            System.out.println("No backup found for the given date: " + date);
        }
    }

    private String getRestoreCommand(Path backupFilePath) {
        String host = config.getProperty("database.host");
        int port = Integer.parseInt(config.getProperty("database.port"));
        String username = config.getProperty("database.user");
        String password = config.getProperty("database.password");

        return String.format(
                // -h% ... Host -P% ... Port -u% ... Username -p% ... Password -e ... execute command
                "mysql --host=%s --port=%d --user=%s --password=%s -e \"source %s\"",
                host, port, username, password, backupFilePath);
    }

    private Path generateBackupFilePathForDate(LocalDate date) {
        String backupFileName = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date) + "-ProjectTracker-MySQLdump.sql";
        // Erstelle das Verzeichnis, falls es nicht existiert
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.home"), "MySQL-Backups"));
        } catch (IOException e) {
            System.err.println("Error while creating backup directory: " + e.getMessage());
        }
        return Paths.get(System.getProperty("user.home"), "MySQL-Backups", backupFileName);
    }
}
