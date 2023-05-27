package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class MainWindowController {
    private final ProjectDAO projectDAO;
    private final UserLoginController userLoginController;
    private final TimesheetEntryDAO timesheetEntryDAO;
    private Project selectedProject;
    private StopwatchState stopwatchState;

    public MainWindowController(ProjectDAO projectDAO, UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
        this.timesheetEntryDAO = projectDAO.getTimesheetEntryDAO();
        this.stopwatchState = StopwatchState.STOPPED;
    }

    public void startStopwatch(Project selectedProject) {
        System.out.println("Start Stopwatch");
        this.selectedProject = selectedProject;
        this.stopwatchState = StopwatchState.RUNNING;
    }

    public void stopStopwatch() {
        this.stopwatchState = StopwatchState.STOPPED;
    }

    public void updateProjectDuration(long secondsElapsed) {
        if (selectedProject != null) {
            TimesheetEntry timesheetEntry = new TimesheetEntry();
            timesheetEntry.setStartDateTime(LocalDateTime.now().minusSeconds(secondsElapsed));
            timesheetEntry.setDuration((int)secondsElapsed);
            timesheetEntryDAO.addTimesheetEntry(timesheetEntry, selectedProject.getId());

            int totalDuration = projectDAO.getProjectDuration(selectedProject.getId());
            selectedProject.setDuration(totalDuration);
        }
    }

    public int getProjectDuration(Project project) {
        return projectDAO.getProjectDuration(project.getId());
    }

    public ObservableList<Project> getProjectList() {
        User currentUser = userLoginController.getCurrentUser();
        return currentUser != null ?
                FXCollections.observableArrayList(projectDAO.readAllProjectsByUserID(currentUser.getId())) :
                FXCollections.observableArrayList();
    }

//    public ObservableList<Project> getProjectList() {
//        User currentUser = userLoginController.getCurrentUser();
//        if (currentUser != null) {
//            List<Project> projects = projectDAO.readAllProjectsByUserID(currentUser.getId());
//            return FXCollections.observableArrayList(projects);
//        }
//        return FXCollections.observableArrayList();
//    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public User getCurrentUser() {
        return userLoginController.getCurrentUser();
    }

    public UserLoginController getUserLoginController() {
        return userLoginController;
    }

    public StopwatchState getStopwatchState() {
        return this.stopwatchState;
    }

    public String getWelcomeMessage() {
        User currentUser = getCurrentUser();
        return currentUser != null ? "Welcome " + currentUser.getName() : "Welcome";
    }


//    public void importCsv(String filePath) {
//        User currentUser = getCurrentUser();
//        if (currentUser != null) {
//            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
//                lines.skip(1)  // Skip the header line
//                        .forEach(line -> {
//                            String[] values = line.split(";");
//
//                            if (values.length >= 5) {  // Ensure that there are at least 5 values per line
//                                String description = values[2];
//                                CostCenter costCenter = new CostCenter();
//                                costCenter.setName(values[3]);
//                                Responsible responsible = new Responsible();
//                                responsible.setName(values[4]);
//
//                                LocalDate date;
//                                try {
//                                    date = LocalDate.parse(values[0]);
//                                } catch (DateTimeParseException e) {
//                                    e.printStackTrace();
//                                    return;
//                                }
//
//                                int duration;
//                                try {
//                                    duration = Integer.parseInt(values[1]);
//                                } catch (NumberFormatException e) {
//                                    e.printStackTrace();
//                                    return;
//                                }
//
//                                // Create and save Project
//                                Project project = projectDAO.addProject(description, costCenter, responsible, currentUser);
//
//                                // Create and save TimesheetEntry
//                                if (project != null) {
//                                    TimesheetEntry timesheetEntry = new TimesheetEntry();
//                                    timesheetEntry.setStartDateTime(LocalDateTime.of(date, LocalTime.MIDNIGHT));
//                                    timesheetEntry.setDuration(duration);
//                                    timesheetEntry.setProjectId(project.getId());
//                                    timesheetEntryDAO.addTimesheetEntry(timesheetEntry, project.getId());
//                                }
//                            }
//                        });
//            } catch (IOException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }





}
