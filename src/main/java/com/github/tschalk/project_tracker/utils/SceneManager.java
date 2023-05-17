package com.github.tschalk.project_tracker.utils;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import com.github.tschalk.project_tracker.view.AddProjectView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.tschalk.project_tracker.Main.*;

public class SceneManager {

    private static SceneManager sceneManager = null;

    private Map<String, Scene> scenes = new HashMap<>();

    // Privater Konsruktor damit nur eine Instanz von SceneManager existiert
    private SceneManager() {
    }

    // Get the single instance of SceneManager
    public static SceneManager getInstance() {

        if (sceneManager == null) {
            sceneManager = new SceneManager();
        }
        return sceneManager;
    }

    public void addScene(String name, Scene scene) {

        scenes.put(name, scene);
    }

    public void removeScene(String name) {
        scenes.remove(name);
    }

    public Scene loadScene(String name, Stage stage) {

        Scene scene = scenes.get(name);
        if (scene != null) {
            stage.setScene(scene);
        }
        return scene;
    }

    public void showCustomScene(String name, Stage stage) {

        Scene scene = scenes.get(name);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        stage.setScene(scene);
        stage.setTitle(name);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public void showNewWindowWithCustomScene(String name) {

        Scene scene = scenes.get(name);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle(name);
        newStage.setResizable(false);
        newStage.centerOnScreen();
        newStage.show();
    }

}