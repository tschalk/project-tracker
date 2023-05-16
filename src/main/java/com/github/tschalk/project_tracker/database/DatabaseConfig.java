package com.github.tschalk.project_tracker.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *  Diese Klasse ist dafür verantwortlich aus der Datei database.properties zu lesen und Werte zu speichern.
 *  Die Datei liegt im Verzeichnis src/main/resources/config.
 */

public class DatabaseConfig {
    private final Properties configProps;
    private final Path configPath;


    public DatabaseConfig() {
        configProps = new Properties();
        configPath = Paths.get("src/main/resources/config/database.properties");
        try {
            configProps.load(Files.newInputStream(configPath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String property) {
        return configProps.getProperty(property);
    }

    public void setProperty(String key, String value) {
        configProps.setProperty(key, value);
        try {
            configProps.store(Files.newOutputStream(configPath), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


