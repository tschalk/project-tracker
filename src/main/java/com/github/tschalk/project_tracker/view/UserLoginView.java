package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.Main;
import com.github.tschalk.project_tracker.controller.MainWindowController;
import com.github.tschalk.project_tracker.controller.UserLoginController;
import com.github.tschalk.project_tracker.dao.ProjectDAO;
import com.github.tschalk.project_tracker.utils.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

import static com.github.tschalk.project_tracker.Main.MAIN_WINDOW_SCENE;
import static com.github.tschalk.project_tracker.Main.STYLESHEET_PATH;

public class UserLoginView extends VBox {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Stage stage;
    private final UserLoginController userLoginController;

    public UserLoginView(UserLoginController userLoginController, Stage stage) {

        this.userLoginController = userLoginController;
        this.stage = stage;

        this.usernameField = new TextField();
        this.passwordField = new PasswordField();

        initUI();
    }

    private void initUI() {

        this.setSpacing(10);
        this.setPadding(new Insets(10));

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

        this.getChildren().addAll(titleLabel, gridPane, buttonContainer);
    }

    private void login() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        // FIXME: DEBUGGING & TESTING:
        username = "Max";
        password = "123";
        //

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
        SceneManager sceneManager = SceneManager.getInstance();
        Scene scene = sceneManager.loadScene(MAIN_WINDOW_SCENE, stage);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());
        stage.setTitle(MAIN_WINDOW_SCENE);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
