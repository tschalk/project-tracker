package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.CostCenterDAO;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.ResponsibleDAO;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.model.User;
import com.github.tschalk.project_tracker.view.MainWindowView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AddProjectController {

    private ProjectDAO projectDAO;
    private CostCenterDAO costCenterDAO;
    private ResponsibleDAO responsibleDAO;
    private UserLoginController userLoginController;
    private MainWindowView mainWindowView;

    public AddProjectController(ProjectDAO projectDAO, CostCenterDAO costCenterDAO, ResponsibleDAO responsibleDAO, UserLoginController userLoginController, MainWindowView mainWindowView) {
        this.projectDAO = projectDAO;
        this.costCenterDAO = costCenterDAO;
        this.responsibleDAO = responsibleDAO;
        this.userLoginController = userLoginController;
        this.mainWindowView = mainWindowView;
    }

    /**
     * Cost Center:
     */

    public void addCostCenter(String name) {
        CostCenter newCostCenter = new CostCenter();
        newCostCenter.setName(name);
        costCenterDAO.add(newCostCenter);
    }

    public ObservableList<CostCenter> getCostCenters() {
        List<CostCenter> costCenterList = costCenterDAO.getAll();
        return FXCollections.observableArrayList(costCenterList);
    }

    public void removeCostCenter(CostCenter costCenter) {
        costCenterDAO.remove(costCenter);
    }

    /**
     * Responsible:
     */

    public void addResponsible(String name) {
        Responsible newResponsible = new Responsible();
        newResponsible.setName(name);
        responsibleDAO.add(newResponsible);
    }

    public ObservableList<Responsible> getResponsible() {
        List<Responsible> responsibleList = responsibleDAO.getAll();
        return FXCollections.observableArrayList(responsibleList);
    }

    public void removeResponsible(Responsible responsible) {
        responsibleDAO.remove(responsible);
    }

    /**
     * Getter & Setter:
     */

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public User getCurrentUser() {
        return userLoginController.getCurrentUser();
    }

    public MainWindowView getMainWindowView() {
        return mainWindowView;
    }
}
