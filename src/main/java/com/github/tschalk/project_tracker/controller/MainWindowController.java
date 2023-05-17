package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.model.User;
import javafx.stage.Stage;

import java.util.List;

public class MainWindowController {
    ProjectDAO projectDAO;
    User currentUser;
//    Stage stage;

    public MainWindowController(ProjectDAO projectDAO, User currentUser/*, Stage stage*/) {
        this.projectDAO = projectDAO;
        this.currentUser = currentUser;
//        this.stage = stage;
    }

    public List<Project> getProjectList() {
        return projectDAO.readAllProjectsByUserID(currentUser.getId());
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }
}
