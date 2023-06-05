package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.Project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller für den Export von Projekten in eine CSV-Datei.
 */

public class ExportController {
    private final ProjectDAO projectDAO;
    private final UserLoginController userLoginController;
    private final TimesheetEntryDAO timesheetEntryDAO;
    private final String userName;

    public ExportController(ProjectDAO projectDAO,  UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.timesheetEntryDAO = projectDAO.getTimesheetEntryDAO();
        this.userLoginController = userLoginController;
        this.userName = userLoginController.getCurrentUser().getName();
    }

    /**
     * Eerzeugt eine CSV-Datei mit den übergebenen Daten. Die Datei wird im übergebenen Verzeichnis gespeichert.
     * @param projects Liste der Projekte, die exportiert werden sollen
     * @param startDate Startdatum des Zeitraums, der exportiert werden soll
     * @param endDate Enddatum des Zeitraums, der exportiert werden soll
     * @param directory Verzeichnis, in dem die Datei gespeichert werden soll
     * @return true, wenn die Datei erfolgreich erstellt wurde, false sonst
     */

    public boolean generateCSV(List<Project> projects, LocalDate startDate, LocalDate endDate, Path directory) {
        if (projects.isEmpty() || startDate == null || endDate == null) {
            return false;
        }

        String filename = String.format("%s_%s_" + userName + "_project-tracker-export.csv", startDate, endDate);
        Path outputFile = directory.resolve(filename);

        List<String> csvLines = new ArrayList<>();
        csvLines.add("Arbeitstag;Stunden;Beschreibung;Kontierung;Ansprechpartner"); // Header

        for (Project project : projects) {
            Map<LocalDate, Integer> dailySums = timesheetEntryDAO.getDailyDurationSumForProject(
                    project.getId(), startDate, endDate);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (Map.Entry<LocalDate, Integer> entry : dailySums.entrySet()) { // Eintrag für jeden Arbeitstag
                LocalDate date = entry.getKey();
                int dailySumInSeconds = entry.getValue();

                if (dailySumInSeconds < 36) { // Einträge mit weniger als einer 36 Sekunden werden ignoriert
                    continue;
                }

                String formattedOutputDate = date.format(outputFormatter); // Datum im Ausgabeformat umwandeln

                double dailySumInHours = Math.round((double) dailySumInSeconds / 3600 * 100) / 100.0;
                String dailySumInHoursStr = String.format(Locale.GERMANY, "%.2f", dailySumInHours);

                String ssvEntry = String.join(";", formattedOutputDate, dailySumInHoursStr,
                        project.getDescription(), project.getCostCenter(), project.getResponsible());

                csvLines.add(ssvEntry);
            }
        }

        try {
            Files.write(outputFile, csvLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Liefert eine Liste aller Projekte des aktuell angemeldeten Benutzers.
     * @return Liste aller Projekte des aktuell angemeldeten Benutzers
     */

    public List<Project> getProjects() {
        return projectDAO.readAllProjectsByUserID(userLoginController.getCurrentUser().getId());
    }
}
