package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.UserLoginController;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UserLoginView extends GridPane {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Stage stage;
    private final UserLoginController userLoginController;

    public UserLoginView(UserLoginController userLoginController, Stage stage) {

        this.userLoginController = userLoginController;
        this.stage = stage;

        usernameField = new TextField();
        passwordField = new PasswordField();

        initUI();
    }

    private void initUI() {

        this.setPadding(new Insets(10));
        this.setHgap(5);
        this.setVgap(5);

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> login());

        this.add(usernameLabel, 0, 0);
        this.add(usernameField, 1, 0);
        this.add(passwordLabel, 0, 1);
        this.add(passwordField, 1, 1);
        this.add(loginButton, 0, 2, 2, 1);

        GridPane.setHalignment(loginButton, HPos.CENTER);
    }

    private void login() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username or password cannot be empty.");
            return;
        }

        boolean loginSuccessful = userLoginController.login(username, password);
        if (loginSuccessful) {
            System.out.println("Login successful!");
            switchToTaskView();
            stage.close();
        } else {
            showAlert("Login failed!");
        }
    }

    private void switchToTaskView() {

        System.out.println("now opening main window");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
