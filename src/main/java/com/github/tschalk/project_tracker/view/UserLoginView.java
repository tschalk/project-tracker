package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.*;
import com.github.tschalk.project_tracker.dao.CostCenterDAO;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.ResponsibleDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.database.DatabaseBackupManager;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.util.CustomTitleBar;
import com.github.tschalk.project_tracker.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static com.github.tschalk.project_tracker.util.AlertUtils.showAlert;
import static com.github.tschalk.project_tracker.util.SceneManager.*;

public class UserLoginView extends BorderPane {
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Stage stage;
    private final UserLoginController userLoginController;

    public UserLoginView(UserLoginController userLoginController, Stage stage) {
        this.userLoginController = userLoginController;
        this.stage = stage;
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        initializeUI();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void initializeUI() {
        // This
        this.setPadding(new Insets(0, 0, 10, 0));

        // Top
        CustomTitleBar titleBar = new CustomTitleBar(this.stage, "Project Tracker");
        this.setTop(titleBar);

        // Center
        Label titleLabel = new Label("User Login");

        GridPane gridPane = getGridPane();

        Button loginButton = getloginButton();

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().add(loginButton);
        buttonContainer.getStyleClass().add("button-container");

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10, 10, 10, 10));
        contentBox.getChildren().addAll(titleLabel, gridPane, buttonContainer);

        this.setCenter(contentBox);
    }

    @NotNull
    private Button getloginButton() {
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> login());
        return loginButton;
    }

    @NotNull
    private GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);

        return gridPane;
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // FIXME: DEBUGGING & TESTING:
//        username = "admin";
//        password = "123";

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Username or password cannot be empty.");
            return;
        }

        boolean loginSuccessful = userLoginController.login(username, password);
        if (loginSuccessful) {
            System.out.println("Login successful!");
            stage.close();
            switchToMainWindowView(username, password);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Login failed!");
        }
    }

    private void switchToMainWindowView(String username, String password) {
        DatabaseConnectionManager databaseConnectionManager = userLoginController.getDatabaseConnectionManager();

        CostCenterDAO costCenterDAO = new CostCenterDAO(databaseConnectionManager);
        ResponsibleDAO responsibleDAO = new ResponsibleDAO(databaseConnectionManager);
        TimesheetEntryDAO timesheetDAO = new TimesheetEntryDAO(databaseConnectionManager);
        ProjectDAO projectDAO = new ProjectDAO(
                databaseConnectionManager, costCenterDAO, responsibleDAO, timesheetDAO);

        MainWindowController mainWindowController = new MainWindowController(projectDAO, userLoginController);
        MainWindowView mainWindowView = new MainWindowView(mainWindowController, stage, new DatabaseBackupManager(databaseConnectionManager.getDatabaseConfig()));

        AddProjectController addProjectController = new AddProjectController(
                projectDAO, costCenterDAO, responsibleDAO, userLoginController, mainWindowView); // ProjectDAO
        AddProjectView addProjectView = new AddProjectView(addProjectController);

        EditProjectController editProjectController = new EditProjectController(
                projectDAO, userLoginController, mainWindowView);
        EditProjectView editProjectView = new EditProjectView(editProjectController);

        ExportController exportController = new ExportController(
                projectDAO, timesheetDAO, userLoginController);
        ExportView exportView = new ExportView(exportController);

        ChangePasswordController changePasswordController = new ChangePasswordController(userLoginController);
        ChangePasswordView changePasswordView = new ChangePasswordView(changePasswordController, stage);
        ChangePasswordView changePasswordLoggedUserView = new ChangePasswordView(changePasswordController, stage, true);

        // Admin only:
        UserManagementController userManagementController = new UserManagementController(userLoginController);
        UserManagementView userManagementView = new UserManagementView(userManagementController);

        // Hier werden weitere Views den Szenen hinzugefügt und die Szenen dem SceneManager hinzugefügt.
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.addScene(ADD_PROJECT_SCENE,
                new Scene(addProjectView, ADD_PROJECT_VIEW_WIDTH, ADD_PROJECT_VIEW_HEIGHT));
        sceneManager.addScene(MAIN_WINDOW_SCENE,
                new Scene(mainWindowView, MAIN_WINDOW_VIEW_WIDTH, MAIN_WINDOW_VIEW_HEIGHT));
        sceneManager.addScene(EDIT_PROJECT_SCENE,
                new Scene(editProjectView, EDIT_PROJECT_VIEW_WIDTH, EDIT_PROJECT_VIEW_HEIGHT));
        sceneManager.addScene(EXPORT_SCENE,
                new Scene(exportView, EXPORT_VIEW_WIDTH, EXPORT_VIEW_HEIGHT));
        sceneManager.addScene(CHANGE_PASSWORD_SCENE,
                new Scene(changePasswordView, CHANGE_PASSWORD_VIEW_WIDTH, CHANGE_PASSWORD_VIEW_HEIGHT));
        sceneManager.addScene(CHANGE_PASSWORD_LOGGED_USER_SCENE,
                new Scene(changePasswordLoggedUserView, CHANGE_PASSWORD_LOGGED_USER_VIEW_WIDTH, CHANGE_PASSWORD_LOGGED_USER_VIEW_HEIGHT));

        // Admin only:
        sceneManager.addScene(USER_MANAGEMENT_SCENE,
                new Scene(userManagementView, USER_MANAGEMENT_VIEW_WIDTH, USER_MANAGEMENT_VIEW_HEIGHT));

        // Hier wird die erste Szene gesetzt.
        if (password.startsWith(username) &&
                isNumeric(password.substring(username.length()))) {
            sceneManager.showCustomScene(CHANGE_PASSWORD_SCENE, stage);
        } else {
            sceneManager.showCustomScene(MAIN_WINDOW_SCENE, stage);
        }
    }
}
