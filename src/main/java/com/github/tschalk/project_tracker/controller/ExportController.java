package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExportController {
    ProjectDAO projectDAO;
    UserLoginController userLoginController;
    TimesheetEntryDAO timesheetEntryDAO;

    public ExportController(ProjectDAO projectDAO, TimesheetEntryDAO timesheetEntryDAO, UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
        this.timesheetEntryDAO = timesheetEntryDAO;
    }

    public void generateCSV(List<Project> projects, LocalDate startDate, LocalDate endDate, Path directory) {
        String filename = String.format("%s_%s_project-tracker-export.csv", startDate, endDate);
        Path outputFile = directory.resolve(filename);

        try {
            Files.writeString(outputFile, "Arbeitstag;Stunden;Beschreibung;Kontierung;Ansprechpartner\n"); // Header

            for (Project project : projects) {
                Map<LocalDate, Integer> dailySums = timesheetEntryDAO.getDailyDurationSumForProject(project.getId(), startDate, endDate);

                for (Map.Entry<LocalDate, Integer> entry : dailySums.entrySet()) {
                    LocalDate date = entry.getKey();
                    int dailySum = entry.getValue();

                    String ssvEntry = date + ";" +
                            dailySum + ";" +
                            project.getDescription() + ";" +
                            project.getCostCenter() + ";" +
                            project.getResponsible() + "\n";

                    Files.writeString(outputFile, ssvEntry, StandardOpenOption.APPEND);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Project> getProjects() {
        return projectDAO.readAllProjectsByUserID(userLoginController.getCurrentUser().getId());
    }

}
