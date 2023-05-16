package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;

public class ExportController {
    ProjectDAO projectDAO;

    public ExportController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
}
