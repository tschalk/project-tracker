package com.github.tschalk.project_tracker;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.controller.UserLoginController;
import com.github.tschalk.project_tracker.dao.CostCenterDAO;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.ResponsibleDAO;
import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.database.DatabaseBackupManager;
import com.github.tschalk.project_tracker.database.DatabaseConfig;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.utils.CustomTitleBar;
import com.github.tschalk.project_tracker.utils.Sanitizer;
import com.github.tschalk.project_tracker.utils.SceneManager;
import com.github.tschalk.project_tracker.view.AddProjectView;
import com.github.tschalk.project_tracker.view.DatabaseLoginView;
import com.github.tschalk.project_tracker.view.MainWindowView;
import com.github.tschalk.project_tracker.view.UserLoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.github.tschalk.project_tracker.utils.SceneManager.*;

public class Main extends Application {



    public static final String STYLESHEET_PATH = "/css/styles.css";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(event -> Platform.exit());

        // Hier wird die Verbindung zur Datenbank hergestellt.
        DatabaseConfig config = new DatabaseConfig();
        DatabaseBackupManager databaseBackupManager = new DatabaseBackupManager(config);
        DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager(config, databaseBackupManager);
        databaseConnectionManager.connect();
        Sanitizer sanitizer = Sanitizer.getDefault();
        UserDAO userDAO = new UserDAO(databaseConnectionManager, sanitizer);
        DatabaseLoginController databaseLoginController = new DatabaseLoginController(databaseConnectionManager);
        DatabaseLoginView databaseLoginView = new DatabaseLoginView(databaseLoginController, stage);

        UserLoginController userLoginController = new UserLoginController(userDAO);
        UserLoginView userLoginView = new UserLoginView(userLoginController, stage);

        // Hier werden Views den Szenen hinzugefügt und die Szenen dem SceneManager hinzugefügt.
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.addScene(USER_LOGIN_SCENE, new Scene(userLoginView, USER_LOGIN_VIEW_WIDTH, USER_LOGIN_VIEW_HEIGHT));
        sceneManager.addScene(DATABASE_LOGIN_SCENE, new Scene(databaseLoginView, DATABASE_LOGIN_VIEW_WIDTH, DATABASE_LOGIN_VIEW_HEIGHT));

        // Hier werden die Entry-points der Anwendung definiert.
        if (databaseConnectionManager.isConnected()) {
            System.out.println("Connected to database");
            sceneManager.showCustomScene(USER_LOGIN_SCENE, stage);

        } else {
            System.err.println("Connection to database failed!");
            sceneManager.showCustomScene(DATABASE_LOGIN_SCENE, stage);
        }
    }

}
