## Project Tracker

Der Project Tracker ist eine Anwendung, die es Benutzern ermöglicht, ihre Projektzeiten zu verfolgen und 
diese Informationen dann als CSV-Datei zu exportieren.

## Technologien

- Java OpenJDK 20
- Maven 4.0
- MySQL 8.0.33
- JavaFX 20

Struktur gemäß SRS

Das Projekt ist der Model-View-Controller (MVC) Architektur angelehnt und ist gemäß der Software Requirements Specification (SRS) wie folgt strukturiert:

    model Enthält die Klassen, die die Datenstrukturen repräsentieren.
    view: Enthält die Klassen, die für die Darstellung der Benutzeroberfläche verantwortlich sind.
    controller: Enthält die Klassen, die die Anwendungslogik und die Interaktion zwischen Model und View verwalten.
    dao: Enthält die Klassen, die für die Kommunikation mit der Datenbank verantwortlich sind.
    util: Enthält Hilfsklassen
    database: Enthält die Klassen, die für die Datenbank verantwortlich sind.

## Installation

1. Geben Sie die folgenden Befehle ein, um das Projekt zu klonen:
    ```bash
    git clone https://github.com/tschalk/project-tracker.git
    cd project-tracker
    ```
2. Compilieren Sie das Projekt.

3. Fügen Sie die beiden benötigten Dateien in das Arbeitsverzeichnis hinzu:
   - database.properties
   - database.sql
    
## Verwendung

Nachdem Sie das Projekt gebaut haben, können Sie die Anwendung starten, indem Sie den folgenden Befehl eingeben:

```bash
java -jar target/project-tracker.jar
```

## Dokumentation

Weitere Informationen finden Sie in der mitgelieferten Dokumentation:

- `docs/SRS-Project-Tracker.pdf`: Software Requirements Specification
- `docs/ClassDiagram.png`: Klassendiagramm
