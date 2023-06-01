module com.github.tschalk.projecttracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires org.jetbrains.annotations;
    opens com.github.tschalk.project_tracker.model to javafx.base;
    opens com.github.tschalk.project_tracker to javafx.graphics;
}