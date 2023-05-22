package com.github.tschalk.project_tracker.utils;

import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.view.EditProjectView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.tschalk.project_tracker.Main.STYLESHEET_PATH;
public class SceneManager {

    private static SceneManager sceneManager = null;

    private final Map<String, Scene> scenes = new HashMap<>();

    // Privater Konsruktor damit nur eine Instanz von SceneManager existiert
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

    public void showNewWindowWithCustomScene(String sceneName) {
        Scene scene = scenes.get(sceneName);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle(sceneName);
        newStage.setResizable(false);
        newStage.centerOnScreen();

        newStage.show();
    }

    public void showNewWindowWithCustomScene(String sceneName, Project selectedProject) {
        // Hole die Szene aus der Map
        Scene scene = this.scenes.get(sceneName);
        // Überprüfen, ob die Szene eine Instanz von EditProjectView ist
        if(scene.getRoot() instanceof EditProjectView editProjectView) {
            editProjectView.setSelectedProject(selectedProject);
        }
        Stage newStage = new Stage();
        newStage.setScene(scene);

        newStage.show();
    }


//    public void showNewWindowWithCustomSceneAndProject(String sceneName, Project project) {
//        Scene scene = scenes.get(sceneName);
//        if (scene == null) {
//            System.out.println("Scene \"" + sceneName + "\" not found!");
//            return;
//        }
//
//        // Setzen des Projekts im EditProjectController bevor die Szene gezeigt wird
//        if (sceneName.equals(UserLoginView.EDIT_PROJECT_SCENE)) {
//            // Hier die Instanz des EditProjectController erhalten
//            EditProjectController editProjectController = (EditProjectController) scene.getUserData();
//            editProjectController.setProject(project);
//        }
//
//        Stage newStage = new Stage();
//        newStage.setScene(scene);
//        newStage.show();
//    }



}