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

    public Config() {
        Path configPath = Paths.get("src/main/resources/config/database.properties");
        try {
            configProps.load(Files.newInputStream(configPath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String property) {
        return configProps.getProperty(property);
    }
}


