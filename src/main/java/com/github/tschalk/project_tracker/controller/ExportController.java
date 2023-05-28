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

public class ExportController {
    ProjectDAO projectDAO;
    UserLoginController userLoginController;
    TimesheetEntryDAO timesheetEntryDAO;
    String userName;

    public ExportController(ProjectDAO projectDAO, TimesheetEntryDAO timesheetEntryDAO, UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
        this.timesheetEntryDAO = timesheetEntryDAO;
        this.userName = userLoginController.getCurrentUser().getName();
    }

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



    public List<Project> getProjects() {
        return projectDAO.readAllProjectsByUserID(userLoginController.getCurrentUser().getId());
    }

}
