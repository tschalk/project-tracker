package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DatabaseLoginView extends VBox {

    private final Stage stage;
    private final TextField hostField;
    private final TextField portField;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final DatabaseLoginController databaseLoginController;


    public DatabaseLoginView(DatabaseLoginController databaseLoginController, Stage stage) {

        this.databaseLoginController = databaseLoginController;
        this.stage = stage;

        hostField = new TextField();
        portField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();

        loadDatabaseProperties(databaseLoginController);
        initUI();
    }

    // Hier werden die Daten aus der Datenbank.properties Datei geladen und in die Textfelder gesetzt
    private void loadDatabaseProperties(DatabaseLoginController controller) {

        String host = controller.getDatabaseProperty("host");
        String port = controller.getDatabaseProperty("port");
        String user = controller.getDatabaseProperty("user");
        String password = controller.getDatabaseProperty("password");
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
    }

    // Hier werden die Textfelder und der Connect Button erstellt
    private void initUI() {

        hostField.setPromptText("Hostname");
        portField.setPromptText("Port");
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connect());

        this.getChildren().addAll(hostField, portField, usernameField, passwordField, connectButton);
    }

    // Hier wird die Verbindung zur Datenbank hergestellt
    private void connect() {

        String host = hostField.getText();
        String port = portField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean connectionSuccessful = databaseLoginController.createConnection(host, Integer.parseInt(port), username, password);
        if (connectionSuccessful) {
            System.out.println("Connection successful!");

//            switchToLoginView();

        } else {
            System.out.println("Connection failed!");
        }
    }

    // Hier wird eine neue LoginView erstellt und die Szene gewechselt
//    private void switchToLoginView() {
//
//        UserDAO userDAO = new UserDAO(databaseLoginController.getDatabaseManager()); // Weiterleitung der Datenbankverbindung an den UserDAO
//        UserController userController = new UserController(userDAO);
//        LoginView loginView = new LoginView(userController, stage);
//
//        Scene scene = new Scene(loginView, 800, 600);
//        stage.setScene(scene);
//    }
}

