module com.github.tschalk.projecttracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.github.tschalk.project_tracker to javafx.graphics;


}