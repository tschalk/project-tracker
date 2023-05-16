package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.UserLoginController;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
