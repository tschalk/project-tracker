```plantuml
@startuml

left to right direction


actor Anwender

package "Project Tracker" {
usecase "Datenbank Login" #lightblue
usecase "Anwendungs-Login" #lightblue
usecase "Projekte anzeigen" #lightblue
usecase "Neues Projekt hinzufügen"
usecase "Projekt bearbeiten"
usecase "Verantwortliche verwalten" 
usecase "Kontierung verwalten"
usecase "Daten exportieren"
usecase "Zeiterfassung" 
}

Anwender -- "Datenbank Login"
Anwender -- "Anwendungs-Login"
Anwender -- "Projekte anzeigen"

"Projekte anzeigen" <. "Neues Projekt hinzufügen" : <<extends>>
"Projekte anzeigen" <. "Projekt bearbeiten" : <<extends>>
"Projekt bearbeiten" <. "Verantwortliche verwalten" : <<extends>>
"Projekt bearbeiten" <. "Kontierung verwalten" : <<extends>>
"Projekte anzeigen" <. "Daten exportieren" : <<extends>>
"Projekte anzeigen" <. "Zeiterfassung" : <<extends>>

@enduml

