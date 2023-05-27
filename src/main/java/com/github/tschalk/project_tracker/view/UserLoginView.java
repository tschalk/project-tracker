package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.*;
import com.github.tschalk.project_tracker.dao.CostCenterDAO;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.dao.ResponsibleDAO;
import com.github.tschalk.project_tracker.dao.TimesheetEntryDAO;
import com.github.tschalk.project_tracker.database.DatabaseConnectionManager;
import com.github.tschalk.project_tracker.utils.CustomTitleBar;
import com.github.tschalk.project_tracker.utils.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.github.tschalk.project_tracker.utils.SceneManager.*;


public class UserLoginView extends BorderPane {



    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Stage stage;
    private final UserLoginController userLoginController;

    public UserLoginView(UserLoginController userLoginController, Stage stage)  {
        this.userLoginController = userLoginController;
        this.stage = stage;

        this.usernameField = new TextField();
        this.passwordField = new PasswordField();

        initUI();
    }

    private void initUI() {

        CustomTitleBar titleBar = new CustomTitleBar(this.stage, "Project Tracker");
         this.setTop(titleBar);

        //        this.setPadding(new Insets(0,0, 10, 0));

        Label titleLabel = new Label("User Login");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> login());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().add(loginButton);
        buttonContainer.getStyleClass().add("button-container");

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10, 10, 10, 10));
        contentBox.getChildren().addAll(titleLabel, gridPane, buttonContainer);
        this.setCenter(contentBox);
    }

    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // FIXME: DEBUGGING & TESTING:
        username = "admin";
        password = "123";
//        username = "Max";
//        password = "123";

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username or password cannot be empty.");
            return;
        }

        boolean loginSuccessful = userLoginController.login(username, password);
        if (loginSuccessful) {
            System.out.println("Login successful!");
            stage.close();
            switchToMainWindowView();
        } else {
            showAlert("Login failed!");
        }
    }

    private void switchToMainWindowView() {
        DatabaseConnectionManager databaseConnectionManager = userLoginController.getDatabaseConnectionManager();

        CostCenterDAO costCenterDAO = new CostCenterDAO(databaseConnectionManager);
        ResponsibleDAO responsibleDAO = new ResponsibleDAO(databaseConnectionManager);
        TimesheetEntryDAO timesheetDAO = new TimesheetEntryDAO(databaseConnectionManager);
        ProjectDAO projectDAO = new ProjectDAO(databaseConnectionManager, costCenterDAO, responsibleDAO, timesheetDAO);

        MainWindowController mainWindowController = new MainWindowController(projectDAO, userLoginController);
        MainWindowView mainWindowView = new MainWindowView(mainWindowController, stage);

        AddProjectController addProjectController = new AddProjectController(projectDAO, costCenterDAO, responsibleDAO, userLoginController, mainWindowView); // ProjectDAO
        AddProjectView addProjectView = new AddProjectView(addProjectController);

        EditProjectController editProjectController = new EditProjectController(projectDAO, userLoginController, mainWindowView);
        EditProjectView editProjectView = new EditProjectView(editProjectController, stage);

        ExportController exportController = new ExportController(projectDAO, timesheetDAO, userLoginController);
        ExportView exportView = new ExportView(exportController);

        // Hier werden weitere Views den Szenen hinzugefügt und die Szenen dem SceneManager hinzugefügt.
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.addScene(ADD_PROJECT_SCENE, new Scene(addProjectView, ADD_PROJECT_VIEW_WIDTH, ADD_PROJECT_VIEW_HEIGHT));
        sceneManager.addScene(MAIN_WINDOW_SCENE, new Scene(mainWindowView, MAIN_WINDOW_VIEW_WIDTH, MAIN_WINDOW_VIEW_HEIGHT));
        sceneManager.addScene(EDIT_PROJECT_SCENE, new Scene(editProjectView, EDIT_PROJECT_VIEW_WIDTH, EDIT_PROJECT_VIEW_HEIGHT));
        sceneManager.addScene(EXPORT_SCENE, new Scene(exportView, EXPORT_VIEW_WIDTH, EXPORT_VIEW_HEIGHT));

        if (userLoginController.getCurrentUser().getRole().equals("user")) {
            sceneManager.showCustomScene(MAIN_WINDOW_SCENE, stage);
        }

        if (userLoginController.getCurrentUser().getRole().equals("admin")) {
            AdminChangePasswordController adminChangePasswordController = new AdminChangePasswordController(userLoginController);
            ChangePasswordView changePasswordView = new ChangePasswordView(adminChangePasswordController, stage);

            UserManagementController userManagementController = new UserManagementController(userLoginController);
            UserManagementView userManagementView = new UserManagementView(userManagementController);

            sceneManager.addScene(ADMIN_CHANGE_PASSWORD_SCENE, new Scene(changePasswordView, ADMIN_CHANGE_PASSWORD_VIEW_WIDTH, ADMIN_CHANGE_PASSWORD_VIEW_HEIGHT));
            sceneManager.addScene(USER_MANAGEMENT_SCENE, new Scene(userManagementView, USER_MANAGEMENT_VIEW_WIDTH, USER_MANAGEMENT_VIEW_HEIGHT));

            if (userLoginController.getCurrentUser().getRole().equals("admin") && userLoginController.getCurrentUser().getPassword().equals("admin")) {
                sceneManager.showCustomScene(ADMIN_CHANGE_PASSWORD_SCENE, stage);
            } else {
                sceneManager.showCustomScene(MAIN_WINDOW_SCENE, stage);
            }
        }

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


}
