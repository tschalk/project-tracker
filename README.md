# _Der Nachfolgende Text dinet als Beispiel_

## Project Tracker - Projektzeiterfassung 

Der Project Tracker ist eine Anwendung, die es Benutzern ermöglicht, ihre Projektzeiten zu verfolgen und 
diese Informationen dann als CSV-Datei zu exportieren.

## Technologien

- Java OpenJDK 20
- Maven 3.8.2
- MySQL 8.0.33
- JavaFX 11.0.2
- MVC-Architektur

## Struktur

Das Projekt folgt der Model-View-Controller (MVC) Architektur und ist wie folgt strukturiert:

- **Model**: Enthält die Klassen, die die Datenstrukturen repräsentieren.
- **View**: Enthält die Klassen, die für die Darstellung der Benutzeroberfläche verantwortlich sind.
- **Controller**: Enthält die Klassen, die die Anwendungslogik und die Interaktion zwischen Model und View verwalten.
- **DAO**: Enthält die Klassen, die für die Kommunikation mit der Datenbank verantwortlich sind.

## Installation

Geben Sie die folgenden Befehle ein, um das Projekt zu klonen und zu bauen:

```bash
git clone https://github.com/tschalk/project_tracker.git
cd project_tracker
mvn clean install
```
## Verwendung

Nachdem Sie das Projekt gebaut haben, können Sie die Anwendung starten, indem Sie den folgenden Befehl eingeben:

```bash
java -jar target/project_tracker-1.0.jar
```

## Dokumentation

Weitere Informationen finden Sie in der mitgelieferten Dokumentation:

- `docs/SRS.pdf`: Software Requirements Specification
- `docs/DesignDocs/ClassDiagram.png`: Klassendiagramm
