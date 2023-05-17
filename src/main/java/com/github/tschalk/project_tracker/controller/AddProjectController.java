package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.CostCenterDAO;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.ResponsibleDAO;
import com.github.tschalk.project_tracker.model.CostCenter;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AddProjectController {

    private ProjectDAO projectDAO;
    private CostCenterDAO costCenterDAO;
    private ResponsibleDAO responsibleDAO;
    private User currentUser;

    public AddProjectController(ProjectDAO projectDAO, CostCenterDAO costCenterDAO, User currentUser) {
        this.projectDAO = projectDAO;
        this.costCenterDAO = costCenterDAO;
        this.currentUser = currentUser;
    }

    public void addCostCenter(String name) {
        CostCenter newCostCenter = new CostCenter();
        newCostCenter.setName(name);
        costCenterDAO.add(newCostCenter);
    }

    public ObservableList<CostCenter> getCostCenters() {
        List<CostCenter> costCenterList = costCenterDAO.getAll();
        return FXCollections.observableArrayList(costCenterList);
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }


//    public ObservableList<CostCenter> getCostCenters() {
//        return costCenterDAO.getAll();
//    }
//
//    public ObservableList<Responsible> getResponsibles() {
//        return responsibleDAO.getAll();
//    }
//
//    public void addCostCenter(String name) {
//        costCenterDAO.add(name);
//    }
//
//    public void addResponsible(String name) {
//        responsibleDAO.add(name);
//    }
}
