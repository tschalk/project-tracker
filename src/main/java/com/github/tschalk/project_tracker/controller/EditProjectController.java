package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.view.MainWindowView;

import java.util.List;

public class EditProjectController {

    private ProjectDAO projectDAO;
    private Project project;
    private UserLoginController userLoginController;
    private MainWindowView mainWindowView;
    private TimesheetEntryDAO timesheetEntryDAO;
    public EditProjectController(ProjectDAO projectDAO, UserLoginController userLoginController, MainWindowView mainWindowView) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
        this.mainWindowView = mainWindowView;
        this.timesheetEntryDAO = projectDAO.getTimesheetEntryDAO();
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void changeProjectDescription(String newDescription) {
        this.project.setDescription(newDescription);
//        projectDAO.updateProject(project); // updateProject(project); muss erst implementiert werden
    }

    public MainWindowView getMainWindowView() {
        return mainWindowView;
    }

    public List<TimesheetEntry> getTimesheetEntriesForProject(Project project) {
        return projectDAO.readAllTimesheetEntriesForProject(project.getId());
    }

    public void removeTimesheetEntry(TimesheetEntry entry) {
        timesheetEntryDAO.removeEntry(entry);
    }

    public void deleteProject(Project project) {
        projectDAO.deleteProject(project.getId());
        // aktualisiere alle notwendigen Listen oder Views
    }


    public void refreshProjectTable() {
        mainWindowView.updateProjectTableView();
    }


    public TimesheetEntryDAO getTimesheetEntryDAO() {
        return timesheetEntryDAO;
    }
}
