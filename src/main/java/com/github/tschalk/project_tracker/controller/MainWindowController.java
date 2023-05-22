package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.model.User;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class MainWindowController {
    private ProjectDAO projectDAO;
    private UserLoginController userLoginController;
    private TimesheetEntryDAO timesheetEntryDAO;
    private Project selectedProject;
    private StopwatchState stopwatchState;
    private SimpleLongProperty secondsElapsed;

    public MainWindowController(ProjectDAO projectDAO, UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
        this.timesheetEntryDAO = projectDAO.getTimesheetEntryDAO();
        this.stopwatchState = StopwatchState.STOPPED;
        this.secondsElapsed = new SimpleLongProperty(0);
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
        if (currentUser != null) {
            List<Project> projects = projectDAO.readAllProjectsByUserID(currentUser.getId());
            return FXCollections.observableArrayList(projects);
        }
        return FXCollections.observableArrayList();
    }

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
}
