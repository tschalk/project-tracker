module com.github.tschalk.projecttracker {
    requires javafx.controls;
    requires java.sql;
    requires javafx.base;
    opens com.github.tschalk.project_tracker.model to javafx.base;
    opens com.github.tschalk.project_tracker to javafx.graphics;
}