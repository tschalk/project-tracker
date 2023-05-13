package com.github.tschalk.project_tracker;

import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import com.github.tschalk.project_tracker.database.Config;
import com.github.tschalk.project_tracker.database.DatabaseManager;
import com.github.tschalk.project_tracker.view.DatabaseLoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        DatabaseManager databaseManager = new DatabaseManager();
        Config config = new Config();

        DatabaseLoginController databaseLoginController = new DatabaseLoginController(config, databaseManager);

        stage.setTitle("Project Tracker");
        databaseLoginController.createConnection("localhost", 3306, "root", "root");

        DatabaseLoginView databaseLoginView = new DatabaseLoginView(databaseLoginController, stage);
        Scene scene = new Scene(databaseLoginView, 250, 260);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());

        stage.setTitle("Database Login");
        stage.setScene(scene);
        stage.show();

    }
}
