package com.github.tschalk.project_tracker.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.tschalk.project_tracker.Main.MAIN_WINDOW_SCENE;
import static com.github.tschalk.project_tracker.Main.STYLESHEET_PATH;

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

    public void loadAndShowCustomScene(String name, Stage stage) {

        Scene scene = scenes.get(name);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        stage.setScene(scene);
        stage.setTitle(MAIN_WINDOW_SCENE);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}