package com.github.tschalk.project_tracker.utils;

import com.github.tschalk.project_tracker.model.Project;
import com.github.tschalk.project_tracker.view.EditProjectView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.tschalk.project_tracker.Main.STYLESHEET_PATH;
public class SceneManager {

    public static final String DATABASE_LOGIN_SCENE = "Database Login";
    public static final String USER_LOGIN_SCENE = "User Login";
    public static final String MAIN_WINDOW_SCENE = "Main Window";
    public static final String ADD_PROJECT_SCENE = "Add Project";
    public static final String EDIT_PROJECT_SCENE = "Edit Project";
    public static final String EXPORT_SCENE = "Export";
    public static final String CHANGE_PASSWORD_SCENE = "Admin Change Password";
    public static final String USER_MANAGEMENT_SCENE = "User Management";

    public static final int USER_LOGIN_VIEW_WIDTH = 250;
    public static final int USER_LOGIN_VIEW_HEIGHT = 190;
    public static final int DATABASE_LOGIN_VIEW_WIDTH = 250;
    public static final int DATABASE_LOGIN_VIEW_HEIGHT = 260;
    public static final int MAIN_WINDOW_VIEW_WIDTH = 350;
    public static final int MAIN_WINDOW_VIEW_HEIGHT = 480;
    public static final int ADD_PROJECT_VIEW_WIDTH = 350;
    public static final int ADD_PROJECT_VIEW_HEIGHT = 300;
    public static final int EDIT_PROJECT_VIEW_WIDTH = 300;
    public static final int EDIT_PROJECT_VIEW_HEIGHT = 300;
    public static final int EXPORT_VIEW_WIDTH = 300;
    public static final int EXPORT_VIEW_HEIGHT = 200;
    public static final int CHANGE_PASSWORD_VIEW_WIDTH = 300;
    public static final int CHANGE_PASSWORD_VIEW_HEIGHT = 200;
    public static final int USER_MANAGEMENT_VIEW_WIDTH = 300;
    public static final int USER_MANAGEMENT_VIEW_HEIGHT = 350;

    private static SceneManager sceneManager = null;

    private final Map<String, Scene> scenes = new HashMap<>();
//    private final Map<String, Stage> stages = new HashMap<>();

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

    public void showCustomScene(String name, @NotNull Stage stage) {
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
//        newStage.initStyle(StageStyle.UNDECORATED);

//        stages.put(sceneName, newStage); // Damit man die Stage später abrufen kann
        System.out.println("newStage = " + newStage);
        System.out.println("scene = " + scene);
        newStage.show();
    }

    public void showNewWindowWithCustomScene(String sceneName, Project selectedProject) {
        // Hole die Szene aus der Map
        Scene scene = this.scenes.get(sceneName);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());
        // Überprüfen, ob die Szene eine Instanz von EditProjectView ist
        if(scene.getRoot() instanceof EditProjectView editProjectView) {
            editProjectView.setSelectedProject(selectedProject);
        }
        Stage newStage = new Stage();
        newStage.setScene(scene);

        newStage.show();
    }
}