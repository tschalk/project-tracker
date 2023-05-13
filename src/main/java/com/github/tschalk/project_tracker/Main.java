package com.github.tschalk.project_tracker;

import com.github.tschalk.project_tracker.controller.DatabaseLoginController;
import com.github.tschalk.project_tracker.database.Config;
import com.github.tschalk.project_tracker.database.DatabaseInitializer;
import com.github.tschalk.project_tracker.database.DatabaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // Erst Teste ich ob die Databse eine verbinugn aufbauen kann und ob die Tabellen existieren.
        // Hier kommt der code hin zum testen der ersten Klassen:

        System.out.println("Starte Test");

        DatabaseInitializer.initialize();
        System.out.println("Database  initialisiert");
        DatabaseManager databaseManager = new DatabaseManager();
        Config config = new Config();
        System.out.println(config.getProperty("database.host"));

        DatabaseLoginController databaseLoginController = new DatabaseLoginController(config, databaseManager);

        stage.setTitle("Project Tracker");
        databaseLoginController.createConnection("localhost", 3306, "root", "root");
        System.out.println(databaseLoginController.getDatabaseManager().toString());


    }
}
