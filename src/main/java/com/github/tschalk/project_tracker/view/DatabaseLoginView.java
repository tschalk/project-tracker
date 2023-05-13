package com.github.tschalk.project_tracker.view;

import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
        setSpacing(10);
        setPadding(new Insets(10));

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

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connect());

        Button initializeDatabaseButton = new Button("Initialize Database");
        initializeDatabaseButton.setOnAction(e -> databaseLoginController.initializeDatabase());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(connectButton, initializeDatabaseButton);
        buttonContainer.getStyleClass().add("button-container");

        getChildren().addAll(titleLabel, gridPane, buttonContainer);

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

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Connection Failed");
            alert.setContentText("The connection to the database failed.\nPlease check your connection and login credentials.");

            alert.showAndWait();
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

