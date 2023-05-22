package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.ExportController;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExportView extends VBox {

    ExportController exportController;
    Stage stage;
    public ExportView(ExportController exportController, Stage stage) {
        this.exportController = exportController;
        this.stage = stage;
    }
}
