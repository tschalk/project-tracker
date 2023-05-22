package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;

public class ExportController {
    ProjectDAO projectDAO;
    UserLoginController userLoginController;

    public ExportController(ProjectDAO projectDAO, UserLoginController userLoginController) {
        this.projectDAO = projectDAO;
        this.userLoginController = userLoginController;
    }
}
