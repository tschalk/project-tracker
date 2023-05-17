package com.github.tschalk.project_tracker;

import com.github.tschalk.project_tracker.controller.AddProjectController;
import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.controller.UserLoginController;
import com.github.tschalk.project_tracker.dao.CostCenterDAO;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.ResponsibleDAO;
import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.database.DatabaseConfig;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.model.Responsible;
import com.github.tschalk.project_tracker.utils.SceneManager;
import com.github.tschalk.project_tracker.view.AddProjectView;
import com.github.tschalk.project_tracker.view.DatabaseLoginView;
import com.github.tschalk.project_tracker.view.MainWindowView;
import com.github.tschalk.project_tracker.view.UserLoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String MAIN_WINDOW_SCENE = "Main Window";
    public static final String USER_LOGIN_SCENE = "User Login";
    public static final String DATABASE_LOGIN_SCENE = "Database Login";
    public static final String ADD_PROJECT_SCENE = "Add Project";

    public static final int USER_LOGIN_VIEW_WIDTH = 250;
    public static final int USER_LOGIN_VIEW_HEIGHT = 150;
    private static final int DATABASE_LOGIN_VIEW_WIDTH = 250;
    private static final int DATABASE_LOGIN_VIEW_HEIGHT = 260;
    private static final int MAIN_WINDOW_VIEW_WIDTH = 800;
    private static final int MAIN_WINDOW_VIEW_HEIGHT = 480;
    private static final int ADD_PROJECT_VIEW_WIDTH = 300;
    private static final int ADD_PROJECT_VIEW_HEIGHT = 210;

    public static final String STYLESHEET_PATH = "/css/styles.css";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // Hier wird die Verbindung zur Datenbank hergestellt.
        DatabaseConfig config = new DatabaseConfig();
        DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager(config);

        // Hier werden alle Controller und Views erstellt.
        DatabaseLoginController databaseLoginController = new DatabaseLoginController(databaseConnectionManager);
        DatabaseLoginView databaseLoginView = new DatabaseLoginView(databaseLoginController, stage);

        UserDAO userDAO = new UserDAO(databaseConnectionManager);
        UserLoginController userLoginController = new UserLoginController(userDAO);
        UserLoginView userLoginView = new UserLoginView(userLoginController, stage);

        ProjectDAO projectDAO = new ProjectDAO(databaseConnectionManager);
        MainWindowController mainWindowController = new MainWindowController(projectDAO, userLoginController.getCurrentUser());
        MainWindowView mainWindowView = new MainWindowView(mainWindowController, stage);

        CostCenterDAO costCenterDAO = new CostCenterDAO(databaseConnectionManager);
//        ResponsibleDAO responsibleDAO = new ResponsibleDAO();
        AddProjectController addProjectController = new AddProjectController(projectDAO, costCenterDAO, userLoginController.getCurrentUser()); // ProjectDAO
        AddProjectView addProjectView = new AddProjectView(addProjectController, stage);

        // Hier werden die Views den Szenen hinzugefügt und die Szenen dem SceneManager hinzugefügt.
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.addScene(USER_LOGIN_SCENE, new Scene(userLoginView, USER_LOGIN_VIEW_WIDTH, USER_LOGIN_VIEW_HEIGHT));
        sceneManager.addScene(DATABASE_LOGIN_SCENE, new Scene(databaseLoginView, DATABASE_LOGIN_VIEW_WIDTH, DATABASE_LOGIN_VIEW_HEIGHT));
        sceneManager.addScene(MAIN_WINDOW_SCENE, new Scene(mainWindowView, MAIN_WINDOW_VIEW_WIDTH,MAIN_WINDOW_VIEW_HEIGHT));
        sceneManager.addScene(ADD_PROJECT_SCENE, new Scene(addProjectView, ADD_PROJECT_VIEW_WIDTH,ADD_PROJECT_VIEW_HEIGHT));

        // Hier werden die Entry-points der Anwendung definiert.
        if (databaseConnectionManager.connect()) {
            System.out.println("Connected to database");
            sceneManager.showCustomScene(USER_LOGIN_SCENE, stage);

        } else {
            System.err.println("Connection to database failed!");
            sceneManager.showCustomScene(DATABASE_LOGIN_SCENE, stage);
        }
    }

}
