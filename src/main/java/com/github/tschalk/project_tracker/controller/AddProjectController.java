package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.model.User;

public class AddProjectController {

    ProjectDAO projectDAO;
    User currentUser;

    public AddProjectController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
}
