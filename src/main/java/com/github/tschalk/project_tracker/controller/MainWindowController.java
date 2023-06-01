package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.model.User;
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

    public User getCurrentUser() {
        return userLoginController.getCurrentUser();
    }

    public StopwatchState getStopwatchState() {
        return this.stopwatchState;
    }

    public String getWelcomeMessage() {
        User currentUser = getCurrentUser();
        return currentUser != null ? "Welcome " + currentUser.getName() : "Welcome";
    }
}
