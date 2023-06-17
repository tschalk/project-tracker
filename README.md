## Project Tracker

Der Project Tracker ist eine Anwendung, die es Benutzern ermöglicht, ihre Projektzeiten zu verfolgen und 
diese Informationen dann als CSV-Datei zu exportieren.

## Technologien

- Java OpenJDK 20
- Maven 3.8.2
- MySQL 8.0.33
- JavaFX 11.0.2

Struktur gemäß SRS

Das Projekt ist der Model-View-Controller (MVC) Architektur angelehnt und ist gemäß der Software Requirements Specification (SRS) wie folgt strukturiert:

    _Model:_ Enthält die Klassen, die die Datenstrukturen repräsentieren.
    _View:_ Enthält die Klassen, die für die Darstellung der Benutzeroberfläche verantwortlich sind.
    _Controller:_ Enthält die Klassen, die die Anwendungslogik und die Interaktion zwischen Model und View verwalten.
    _DAO:_ Enthält die Klassen, die für die Kommunikation mit der Datenbank verantwortlich sind.

## Installation

Geben Sie die folgenden Befehle ein, um das Projekt zu klonen und zu bauen:

```bash
git clone https://github.com/tschalk/project-tracker.git
cd project-tracker
mvn clean install
```
## Verwendung

Nachdem Sie das Projekt gebaut haben, können Sie die Anwendung starten, indem Sie den folgenden Befehl eingeben:

```bash
java -jar target/project-tracker.jar
```

## Dokumentation

Weitere Informationen finden Sie in der mitgelieferten Dokumentation:

- `docs/SRS-Project-Tracker.pdf`: Software Requirements Specification
- `docs/ClassDiagram.png`: Klassendiagramm
