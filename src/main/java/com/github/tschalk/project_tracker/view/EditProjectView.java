package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.EditProjectController;
import com.github.tschalk.project_tracker.model.Project;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class EditProjectView extends Parent {

    EditProjectController editProjectController;
    Stage stage;
    Project selectedProject;

    public EditProjectView(EditProjectController editProjectController, Stage stage) {
        this.editProjectController = editProjectController;
        this.stage = stage;

//        System.out.println("EditProjectView: " +   selectedProject.getDescription());
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
        System.out.println("selectedProject = " + selectedProject);
    }
}
