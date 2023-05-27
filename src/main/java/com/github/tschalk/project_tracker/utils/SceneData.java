package com.github.tschalk.project_tracker.utils;

import javafx.scene.Parent;
import javafx.stage.Stage;

public class SceneData {
    private Stage stage;
    private Parent view;
    private Object controller;

    public SceneData(Stage stage, Parent view, Object controller) {
        this.stage = stage;
        this.view = view;
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Parent getView() {
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    @Override
    public String toString() {
        return "SceneData{" +
                "stage=" + stage +
                ", view=" + view +
                ", controller=" + controller +
                '}';
    }
}

