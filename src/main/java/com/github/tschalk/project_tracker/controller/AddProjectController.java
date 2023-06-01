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
    private final ProjectDAO projectDAO;
    private final CostCenterDAO costCenterDAO;
    private final ResponsibleDAO responsibleDAO;
    private final UserLoginController userLoginController;
    private final MainWindowView mainWindowView;

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

    public CostCenter addCostCenter(String name) {
        CostCenter newCostCenter = new CostCenter();
        newCostCenter.setName(name);
        costCenterDAO.add(newCostCenter);

        return newCostCenter;
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
     *
     * @return
     */

    public Responsible addResponsible(String name) {
        Responsible newResponsible = new Responsible();
        newResponsible.setName(name);
        responsibleDAO.add(newResponsible);

        return newResponsible;
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
