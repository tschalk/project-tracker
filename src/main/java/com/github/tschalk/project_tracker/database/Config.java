package com.github.tschalk.project_tracker.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Diese Klasse liest die Konfiguration aus der Datei database.properties.
 * Die Datei muss im Verzeichnis src/main/resources/config liegen.
 *
 */

public class Config {
    private final Properties configProps = new Properties();
    private final Path configPath;


    public Config() {
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


