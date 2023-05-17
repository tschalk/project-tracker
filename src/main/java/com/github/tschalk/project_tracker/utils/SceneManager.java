package com.github.tschalk.project_tracker.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static SceneManager sceneManager = null;

    private Map<String, Scene> scenes = new HashMap<>();

    // Private constructor to prevent instantiation
    private SceneManager() {}

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

    public void loadScene(String name, Stage stage) {
        stage.setScene(scenes.get(name));
    }
}


// Add scenes
//            sceneManager.addScene("mainWindow", new Scene(new MainWindowView()));
//                    sceneManager.addScene("addProject", new Scene(new AddProjectView()));
//
//                    // Switch to a different scene
//                    sceneManager.loadScene("addProject", stage);