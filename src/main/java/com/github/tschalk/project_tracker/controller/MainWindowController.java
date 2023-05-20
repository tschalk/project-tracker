package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.List;

public class MainWindowController {

    ProjectDAO projectDAO;
    UserLoginController userLoginController;

    public MainWindowController(ProjectDAO projectDAO, UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
    }

//    public List<Project> getProjectList() {
//        return projectDAO.readAllProjectsByUserID(currentUser.getId());
//    }

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


}
