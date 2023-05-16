package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;

public class ProjectPropertiesController {
    ProjectDAO projectDAO;

    public ProjectPropertiesController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
}
