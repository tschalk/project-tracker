package com.github.tschalk.project_tracker;

import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import com.github.tschalk.project_tracker.controller.UserLoginController;
import com.github.tschalk.project_tracker.dao.UserDAO;
import com.github.tschalk.project_tracker.database.DatabaseConfig;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.view.DatabaseLoginView;
import com.github.tschalk.project_tracker.view.UserLoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static final int USER_LOGIN_VIEW_WIDTH = 250;
    public static final int USER_LOGIN_VIEW_HEIGHT = 150;
    private static final int DATABASE_LOGIN_VIEW_WIDTH = 250;
    private static final int DATABASE_LOGIN_VIEW_HEIGHT = 260;
    private static final String STYLESHEET_PATH = "/css/styles.css";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        DatabaseConfig config = new DatabaseConfig();
        DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager(config);

        /*
        SceneManager sceneManager = new SceneManager();

            // Add scenes
            sceneManager.addScene("mainWindow", new Scene(new MainWindowView()));
            sceneManager.addScene("addProject", new Scene(new AddProjectView()));

            // Switch to a different scene
            sceneManager.loadScene("addProject", stage);
         */

        if (databaseConnectionManager.connect()) {
            System.out.println("Connected successfully");
            showUserLoginView(stage, databaseConnectionManager);
        } else {
            System.err.println("Connection failed");
            showDatabaseLoginView(stage, databaseConnectionManager);
        }
    }

    private void showUserLoginView(Stage stage, DatabaseConnectionManager databaseConnectionManager) {

        UserDAO userDAO = new UserDAO(databaseConnectionManager);
        UserLoginController userLoginController = new UserLoginController(userDAO);
        UserLoginView userLoginView = new UserLoginView(userLoginController, stage);

        displayScene(new Scene(userLoginView, USER_LOGIN_VIEW_WIDTH, USER_LOGIN_VIEW_HEIGHT), stage, "User Login");
    }

    private void showDatabaseLoginView(Stage stage, DatabaseConnectionManager databaseConnectionManager) {

        DatabaseLoginController databaseLoginController = new DatabaseLoginController(databaseConnectionManager);
        DatabaseLoginView databaseLoginView = new DatabaseLoginView(databaseLoginController, stage);

        displayScene(new Scene(databaseLoginView, DATABASE_LOGIN_VIEW_WIDTH, DATABASE_LOGIN_VIEW_HEIGHT), stage, "Database Login");
    }

    private void displayScene(Scene userLoginView, Stage stage, String User_Login) {

        userLoginView.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        stage.setTitle(User_Login);
        stage.setScene(userLoginView);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}
