package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.model.TimesheetEntry;
import com.github.tschalk.project_tracker.view.MainWindowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class EditProjectController {

    private final ProjectDAO projectDAO;
    private final MainWindowView mainWindowView;
    private final TimesheetEntryDAO timesheetEntryDAO;

    public EditProjectController(ProjectDAO projectDAO, MainWindowView mainWindowView) {
        this.projectDAO = projectDAO;
        this.mainWindowView = mainWindowView;
        this.timesheetEntryDAO = projectDAO.getTimesheetEntryDAO();
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
    }

    public ObservableList<CostCenter> getCostCenters() {
        List<CostCenter> costCenterList = projectDAO.getCostCenterDAO().getAll();
        return FXCollections.observableArrayList(costCenterList);
    }

    public void updateCostCenter(Project project, CostCenter newCostCenter) {
        CostCenter costCenter = projectDAO.getCostCenterDAO().getCostCenterByName(newCostCenter.getName());
        if (costCenter != null) {
            projectDAO.updateProjectCostCenter(project, costCenter);
        }
    }

    public ObservableList<Responsible> getResponsibles() {
        List<Responsible> responsibleList = projectDAO.getResponsibleDAO().getAll();
        return FXCollections.observableArrayList(responsibleList);
    }

    public void updateResponsible(Project project, Responsible newResponsible) {
        Responsible responsible = projectDAO.getResponsibleDAO().getResponsibleByName(newResponsible.getName());
        if (responsible != null) {
            projectDAO.updateProjectResponsible(project, responsible);
        }
    }

    public void updateProject(Project project) {
        projectDAO.updateProject(project);
    }

    public TimesheetEntryDAO getTimesheetEntryDAO() {
        return timesheetEntryDAO;
    }
}
