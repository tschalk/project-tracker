package com.github.tschalk.project_tracker.controller;

import com.github.tschalk.project_tracker.dao.ProjectDAO;

public class MainWindowController {
    ProjectDAO projectDAO;

    public MainWindowController(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }
}
