package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import com.github.tschalk.project_tracker.util.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.github.tschalk.project_tracker.util.AlertUtils.showAlert;
import static com.github.tschalk.project_tracker.util.SceneManager.USER_LOGIN_SCENE;

public class DatabaseLoginView extends VBox {
    private final DatabaseLoginController databaseLoginController;
    private final Stage stage;
    private final TextField hostField;
    private final TextField portField;
    private final TextField usernameField;
    private final TextField databaseNameField;
    private final PasswordField passwordField;

    public DatabaseLoginView(DatabaseLoginController databaseLoginController, Stage stage) {
        this.databaseLoginController = databaseLoginController;
        this.stage = stage;
        this.hostField = new TextField();
        this.portField = new TextField();
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        this.databaseNameField = new TextField();
        loadDatabaseProperties(); // database.properties Datei laden
        initializeUI();
    }

    private void loadDatabaseProperties() {
        String host = databaseLoginController.getDatabaseProperty("host");
        String port = databaseLoginController.getDatabaseProperty("port");
        String user = databaseLoginController.getDatabaseProperty("user");
        String password = databaseLoginController.getDatabaseProperty("password");
        String databaseName = databaseLoginController.getDatabaseProperty("database");

        if (host != null && !host.isEmpty()) {
            hostField.setText(host);
        } else {
            hostField.setText("localhost");
        }
        if (port != null && !port.isEmpty()) {
            portField.setText(port);
        } else {
            portField.setText("3306");
        }
        if (user != null && !user.isEmpty()) {
            usernameField.setText(user);
        } else {
            usernameField.setText("root");
        }
        if (password != null && !password.isEmpty()) {
            passwordField.setText(password);
        } else {
            passwordField.setText("root");
        }
        if (databaseName != null && !databaseName.isEmpty()) {
            databaseNameField.setText(databaseName);
        } else {
            databaseNameField.setText("projecttracker");
        }
    }

    // Hier werden die Textfelder und der Connect Button erstellt
    private void initializeUI() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        Label titleLabel = new Label("Database Login");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label hostLabel = new Label("Hostname:");
        gridPane.add(hostLabel, 0, 0);
        gridPane.add(hostField, 1, 0);

        Label portLabel = new Label("Port:");
        gridPane.add(portLabel, 0, 1);
        gridPane.add(portField, 1, 1);

        Label usernameLabel = new Label("Username:");
        gridPane.add(usernameLabel, 0, 2);
        gridPane.add(usernameField, 1, 2);

        Label passwordLabel = new Label("Password:");
        gridPane.add(passwordLabel, 0, 3);
        gridPane.add(passwordField, 1, 3);

        Label databaseNameLabel = new Label("DB-Name:");
        gridPane.add(databaseNameLabel, 0, 4);
        gridPane.add(databaseNameField, 1, 4);

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connect());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(connectButton);
        buttonContainer.getStyleClass().add("button-container");

        this.getChildren().addAll(titleLabel, gridPane, buttonContainer);
    }

    private void connect() {
        String host = hostField.getText();
        String port = portField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String databaseName = databaseNameField.getText();

        boolean connectionSuccessful = databaseLoginController.createConnectionFromUI(host, Integer.parseInt(port), username, password, databaseName);

        if (connectionSuccessful) {
            showAlert(Alert.AlertType.CONFIRMATION, "Connection Status", "Connection Status", "The connection to the database was successful.\nThe database was initialized.");
            System.out.println("Connection successful!");
            switchToLoginView();
        } else {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Connection Failed", "The connection to the database failed.\nPlease check your connection and login credentials.");
            System.out.println("Connection failed!");
        }
    }

    private void switchToLoginView() {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.showCustomScene(USER_LOGIN_SCENE, stage);
    }
}

